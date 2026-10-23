import { test } from 'node:test';
import assert from 'node:assert/strict';
import { readFile } from 'node:fs/promises';
const code = await readFile(new URL('../api/chat.js', import.meta.url), 'utf8');
const { default: handler } = await import('data:text/javascript;base64,' + Buffer.from(code).toString('base64'));
async function call(body, method = 'POST') {
  const res = { statusCode: 200, status(n) { this.statusCode = n; return this; }, json(x) { this.body = x; return this; } };
  await handler({method, body}, res); return res;
}
test('validation, translation and nested REST output', async () => {
  const oldKey = process.env.OPENAI_API_KEY;
  const oldFetch = globalThis.fetch;
  try {
    delete process.env.OPENAI_API_KEY;
    assert.equal((await call({text: 'aluu'})).statusCode, 503);
    assert.equal((await call({text: 123})).statusCode, 400);
    assert.equal((await call({}, 'GET')).statusCode, 405);
    process.env.OPENAI_API_KEY = 'test-only';
    let request;
    globalThis.fetch = async (_, options) => {
      request = JSON.parse(options.body);
      return {ok:true, json:async()=>({status:'completed',output:[{type:'reasoning'},{type:'message',content:[{type:'output_text',text:'Hello'}]}]})};
    };
    const res = await call({text:'Aluu',tool:'Nutserineq',sourceLanguage:'Kalaallisut',targetLanguage:'Tuluttut'});
    assert.equal(res.body.result,'Hello');
    assert.match(request.instructions,/Kalaallisut to Tuluttut/);
    assert.equal(request.store,false);
    assert.equal((await call({text:'aluu',tool:'Nutserineq',targetLanguage:'invalid'})).statusCode,400);
    globalThis.fetch = async()=>({ok:true,json:async()=>({status:'incomplete',output:[]})});
    assert.equal((await call({text:'aluu'})).statusCode,502);
    globalThis.fetch = async()=>({ok:false,status:429,json:async()=>({error:{message:'secret internal details'}})});
    const limited = await call({text:'aluu'});
    assert.equal(limited.statusCode,429);
    assert.match(limited.body.error,/usage, limit aamma saldo/);
    assert.doesNotMatch(JSON.stringify(limited.body),/secret/);
    globalThis.fetch = async()=>({ok:false,status:401,json:async()=>({error:{message:'secret invalid key'}})});
    const unauthorized = await call({text:'aluu'});
    assert.equal(unauthorized.statusCode,401);
    assert.match(unauthorized.body.error,/OPENAI_API_KEY/);
    assert.doesNotMatch(JSON.stringify(unauthorized.body),/secret/);
  } finally { globalThis.fetch = oldFetch; if(oldKey === undefined) delete process.env.OPENAI_API_KEY; else process.env.OPENAI_API_KEY = oldKey; }
});


test('child mode makes an original age-appropriate Kalaallisut song', async () => {
  const oldKey = process.env.OPENAI_API_KEY;
  const oldFetch = globalThis.fetch;
  try {
    process.env.OPENAI_API_KEY = 'test-only';
    let request;
    globalThis.fetch = async (_, options) => {
      request = JSON.parse(options.body);
      return {ok:true, json:async()=>({status:'completed',output:[{type:'message',content:[{type:'output_text',text:'Seqernup erinaq'}]}]})};
    };
    const res = await call({text:'seqernup',tool:'Meeqqat Create'});
    assert.equal(res.statusCode,200);
    assert.match(request.instructions,/child-friendly song/);
    assert.match(request.instructions,/suitable for children and families/);
    assert.match(request.instructions,/Never request or include a child's name/);
    assert.match(request.instructions,/do not ask for personal information/);
    assert.match(request.input,/Meeqqat Create: seqernup/);
  } finally {
    globalThis.fetch = oldFetch;
    if(oldKey === undefined) delete process.env.OPENAI_API_KEY; else process.env.OPENAI_API_KEY = oldKey;
  }
});
