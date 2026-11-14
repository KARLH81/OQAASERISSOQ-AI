const TOOLS = new Set(["Allanneq", "Nutserineq", "Nipilersuut", "Pitsanngorsaruk", "Meeqqat Create"]);
const LANGUAGES = new Set(["Kalaallisut", "Danskisut", "Tuluttut"]);
const OPENAI_MODEL = process.env.OPENAI_MODEL || "gpt-5-mini";

function responseError(res, status, error) {
  return res.status(status).json({ error });
}

function validateRequest(body) {
  if (!body || typeof body !== "object" || Array.isArray(body)) {
    return { error: "Oqaasertat amigaataapput." };
  }

  const { text, tool = "Allanneq", sourceLanguage, targetLanguage } = body;
  if (typeof text !== "string" || !text.trim() || text.length > 12000) {
    return { error: "Oqaasertat amigaataapput." };
  }
  if (!TOOLS.has(tool) || (tool === "Nutserineq" &&
      (!LANGUAGES.has(sourceLanguage) || !LANGUAGES.has(targetLanguage)))) {
    return { error: "Oqaatsit suliassarlu eqqortumik toqqakkit." };
  }
  return { text: text.trim(), tool, sourceLanguage, targetLanguage };
}

function instructionsFor({ tool, sourceLanguage, targetLanguage }) {
  return "Illit OQAASERISSOQ AI-vutit. Kalaallit oqaasii pingaarnertut atukkit. " +
    "Keep every response suitable for children and families using this app. Do not generate sexual, violent, hateful, self-harm, dangerous, illegal, deceptive, or abusive content. Briefly refuse unsafe requests and offer a safe alternative. Never request or include a child's name, school, address, contact details, or other identifying personal information. " +
    "Kalaallisut erseqqissumik akissuteqarit. Nalorniguit oqaatigissavat. " +
    (tool === "Nutserineq" ? `Translate from ${sourceLanguage} to ${targetLanguage}. Preserve meaning; return only the translation.` : "") +
    (tool === "Meeqqat Create" ? "Create an original, short, joyful, child-friendly song in clear Kalaallisut from the child’s supplied word or short phrase. Use simple language, a title, one short verse and a memorable chorus. Keep it age-appropriate; do not include frightening, violent, sexual, or adult themes, and do not ask for personal information." : "");
}

function configuredProvider() {
  // Provider boundary: add a separately reviewed adapter here. Never silently
  // fail over to another provider because that changes where user text goes.
  return (process.env.AI_PROVIDER || "openai").trim().toLowerCase();
}

async function generateWithOpenAI(request) {
  if (!process.env.OPENAI_API_KEY) {
    const error = new Error("provider_not_configured");
    error.code = "provider_not_configured";
    throw error;
  }

  const response = await fetch("https://api.openai.com/v1/responses", {
    method: "POST",
    signal: AbortSignal.timeout(45000),
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${process.env.OPENAI_API_KEY}`
    },
    body: JSON.stringify({
      model: OPENAI_MODEL,
      store: false,
      max_output_tokens: 2500,
      instructions: instructionsFor(request),
      input: `${request.tool}: ${request.text}`
    })
  });

  const data = await response.json();
  if (!response.ok) {
    const error = new Error("provider_request_failed");
    error.code = "provider_request_failed";
    error.status = response.status;
    error.providerCode = data.error?.code;
    error.providerType = data.error?.type;
    throw error;
  }

  const result = (data.output || [])
    .filter(item => item.type === "message")
    .flatMap(item => item.content || [])
    .filter(part => part.type === "output_text" && typeof part.text === "string")
    .map(part => part.text).join("\n").trim();
  if (!result || data.status === "incomplete") {
    const error = new Error("incomplete_response");
    error.code = "incomplete_response";
    throw error;
  }
  return result;
}

export default async function handler(req, res) {
  if (req.method !== "POST") {
    return responseError(res, 405, "POST kisimi atorneqarsinnaavoq.");
  }

  const request = validateRequest(req.body);
  if (request.error) return responseError(res, 400, request.error);

  const provider = configuredProvider();
  if (provider !== "openai") {
    // Fail closed: no request text is sent until a provider adapter is reviewed.
    return responseError(res, 503, "AI provider suli aaqqissorneqanngilaq.");
  }

  try {
    const result = await generateWithOpenAI(request);
    return res.status(200).json({ result });
  } catch (error) {
    if (error.code === "provider_not_configured") {
      return responseError(res, 503, "AI suli piareersarneqanngilaq.");
    }
    if (error.code === "incomplete_response") {
      return responseError(res, 502, "Akissut tamakkiisoq pissarsiarineqanngilaq. Misileqqiguk.");
    }
    if (error.code === "provider_request_failed") {
      const message = error.status === 401 || error.status === 403
        ? "OpenAI API-mut akuersissutit misissukkit. Vercel-imi OPENAI_API_KEY aamma OpenAI project-imi pisinnaatitsissutit misissorneqassapput."
        : error.status === 429
          ? "OpenAI API-p killissaa aporpoq. OpenAI account-imi usage, limit aamma saldo misissukkit."
          : "OpenAI-mut attavearnermi teknikkikkut ajornartorsiuteqarpoq. Kingusinnerusukkut misileqqiguk.";
      console.error("AI provider request failed", {
        provider,
        status: error.status,
        code: error.providerCode,
        type: error.providerType
      });
      return responseError(res, error.status >= 400 && error.status < 500 ? error.status : 502, message);
    }
    if (error.name === "TimeoutError" || error.name === "AbortError") {
      return responseError(res, 504, "AI-p akissutaa sivisuallaamik utaqqivoq. Misileqqiguk.");
    }
    console.error("AI provider connection failed", { provider, name: error.name });
    return responseError(res, 503, "AI-mut attaveqarneq iluatsinngilaq. Kingusinnerusukkut misileqqiguk.");
  }
}
