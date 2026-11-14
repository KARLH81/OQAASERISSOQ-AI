# OQAASERISSOQ AI — architecture decisions

Updated: 14 November 2026

## Goals

- Keep the AI provider replaceable without rewriting the Android app or web client.
- Keep user text, provider credentials and access decisions on the server.
- Keep costs bounded and do not silently send text to a second provider.
- Keep user-controlled data portable and restorable.
- Let Karl use the creator account at no cost; let the creator decide who else receives free access.

## AI provider boundary

The `/api/chat` endpoint owns request validation and the app's response contract. A provider adapter receives only the validated tool, text and optional translation languages, then returns a result string. `AI_PROVIDER` selects an adapter; it defaults to `openai`. `OPENAI_MODEL` controls the OpenAI model and defaults to `gpt-5-mini`.

Only the OpenAI adapter is implemented today. An unknown provider fails closed. There is no automatic provider failover: changing provider can send user text to a different company and can change retention, location, quality and cost. Before adding or enabling another adapter, review its data retention, subprocessors, region, security, output format and pricing, then update the privacy policy and Play Data safety draft.

All requests set `store: false` and cap output at 2,500 tokens. Provider secrets stay in server environment variables. Logs may contain status and error codes, never prompts or generated text.

## Accounts and storage boundary

Accounts and saved creations are not implemented. Do not promise either until a server-side identity check and durable storage exist. The planned data layer should use standard PostgreSQL and a small repository interface, so data can be exported and restored without app-specific database features. Keep the Android client away from direct database credentials.

The access model must support:

1. A creator account that is always free.
2. A server-side allowlist or invite decision controlled by the creator for other free accounts.
3. Per-account abuse and usage limits; creator-free access does not mean unlimited upstream spend.
4. User export and deletion, documented retention, encrypted backups and a tested restore path.

Do not add paid subscriptions unless the creator changes this product rule.

## Resilience and deployment

The current Vercel API remains a small HTTP function with no provider-specific client code in Android. Environment variables hold deployment-specific credentials. Keep production and test secrets separate. Back up durable user data daily once storage is introduced, test restoring it before beta expansion, and avoid storing generated text by default unless the user explicitly saves it.

## Release gates

- Review the active provider and its retention before each release.
- Verify server-side authorization and rate limits before opening account creation.
- Verify backup restoration and deletion before storing creations.
- Reconcile privacy and Play declarations with deployed settings, not just repository code.
- Keep external Kalaallisut datasets blocked until item-level rights and human review are recorded.
