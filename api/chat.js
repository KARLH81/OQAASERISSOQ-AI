export default async function handler(req, res) {
  if (req.method !== "POST") {
    return res.status(405).json({ error: "POST kisimi atorneqarsinnaavoq." });
  }

  const { text, tool = "Allanneq", sourceLanguage, targetLanguage } = req.body || {};

  if (typeof text !== "string" || !text.trim() || text.length > 12000) {
    return res.status(400).json({ error: "Oqaasertat amigaataapput." });
  }

  const languages = ["Kalaallisut", "Danskisut", "Tuluttut"];
  if (!["Allanneq", "Nutserineq", "Nipilersuut", "Pitsanngorsaruk"].includes(tool) ||
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
          (tool === "Nutserineq" ? `Translate from ${sourceLanguage} to ${targetLanguage}. Preserve meaning; return only the translation.` : ""),
        input: `${tool}: ${text}`
      })
    });

    const data = await response.json();

    if (!response.ok) {
      return res.status(response.status).json({
        error: "AI-mut attaveqarneq iluatsinngilaq. Kingusinnerusukkut misileqqiguk."
      });
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
