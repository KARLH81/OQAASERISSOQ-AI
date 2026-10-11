export default async function handler(req, res) {
  if (req.method !== "POST") {
    return res.status(405).json({ error: "POST kisimi atorneqarsinnaavoq." });
  }

  const { text, tool = "Allanneq", sourceLanguage, targetLanguage } = req.body || {};

  if (typeof text !== "string" || !text.trim() || text.length > 12000) {
    return res.status(400).json({ error: "Oqaasertat amigaataapput." });
  }

  const languages = ["Kalaallisut", "Danskisut", "Tuluttut"];
  if (!["Allanneq", "Nutserineq", "Nipilersuut", "Pitsanngorsaruk", "Meeqqat Create"].includes(tool) ||
      (tool === "Nutserineq" && (!languages.includes(sourceLanguage) || !languages.includes(targetLanguage)))) {
    return res.status(400).json({ error: "Oqaatsit suliassarlu eqqortumik toqqakkit." });
  }
  if (!process.env.OPENAI_API_KEY) {
    return res.status(503).json({ error: "AI suli piareersarneqanngilaq." });
  }

  try {
    const response = await fetch("https://api.openai.com/v1/responses", {
      method: "POST",
      signal: AbortSignal.timeout(45000),
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${process.env.OPENAI_API_KEY}`
      },
      body: JSON.stringify({
        model: "gpt-5-mini",
        store: false,
        max_output_tokens: 2500,
        instructions:
          "Illit OQAASERISSOQ AI-vutit. Kalaallit oqaasii pingaarnertut atukkit. " +
          "Kalaallisut erseqqissumik akissuteqarit. Nalorniguit oqaatigissavat. " +
          (tool === "Nutserineq" ? `Translate from ${sourceLanguage} to ${targetLanguage}. Preserve meaning; return only the translation.` : "") +
          (tool === "Meeqqat Create" ? "Create an original, short, joyful, child-friendly song in clear Kalaallisut from the child’s supplied word or short phrase. Use simple language, a title, one short verse and a memorable chorus. Keep it age-appropriate; do not include frightening, violent, sexual, or adult themes, and do not ask for personal information." : ""),
        input: `${tool}: ${text}`
      })
    });

    const data = await response.json();

    if (!response.ok) {
      const errorMessage = response.status === 401 || response.status === 403
        ? "OpenAI API-mut akuersissutit misissukkit. Vercel-imi OPENAI_API_KEY aamma OpenAI project-imi pisinnaatitsissutit misissorneqassapput."
        : response.status === 429
          ? "OpenAI API-p killissaa aporpoq. OpenAI account-imi usage, limit aamma saldo misissukkit."
          : "OpenAI-mut attavearnermi teknikkikkut ajornartorsiuteqarpoq. Kingusinnerusukkut misileqqiguk.";
      console.error("OpenAI API request failed", {
        status: response.status,
        code: data.error?.code,
        type: data.error?.type
      });
      return res.status(response.status).json({ error: errorMessage });
    }

    const result = (data.output || [])
      .filter(item => item.type === "message")
      .flatMap(item => item.content || [])
      .filter(part => part.type === "output_text" && typeof part.text === "string")
      .map(part => part.text).join("\n").trim();
    if (!result || data.status === "incomplete") {
      return res.status(502).json({ error: "Akissut tamakkiisoq pissarsiarineqanngilaq. Misileqqiguk." });
    }
    return res.status(200).json({ result });

  } catch (error) {
    return res.status(500).json({
      error: "Serverimi kukkusoqarpoq."
    });
  }
}
