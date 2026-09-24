export default async function handler(req, res) {
  if (req.method !== "POST") {
    return res.status(405).json({ error: "POST kisimi atorneqarsinnaavoq." });
  }

  const { text, tool } = req.body || {};

  if (!text || !text.trim()) {
    return res.status(400).json({ error: "Oqaasertat amigaataapput." });
  }

  try {
    const response = await fetch("https://api.openai.com/v1/responses", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${process.env.OPENAI_API_KEY}`
      },
      body: JSON.stringify({
        model: "gpt-5-mini",
        instructions:
          "Illit OQAASERISSOQ AI-vutit. Kalaallit oqaasii pingaarnertut atukkit. " +
          "Kalaallisut erseqqissumik, természetimik eqqortumillu akissuteqarit.",
        input: `${tool || "Allanneq"}: ${text}`
      })
    });

    const data = await response.json();

    if (!response.ok) {
      return res.status(response.status).json({
        error: data?.error?.message || "AI-mut attaveqarneq iluatsinngilaq."
      });
    }

    return res.status(200).json({
      result: data.output_text || "Akissut pissarsiarineqanngilaq."
    });

  } catch (error) {
    return res.status(500).json({
      error: "Serverimi kukkusoqarpoq."
    });
  }
}
