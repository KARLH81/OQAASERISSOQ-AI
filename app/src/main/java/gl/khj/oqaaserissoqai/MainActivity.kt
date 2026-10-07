package gl.khj.oqaaserissoqai

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private val tools = listOf("Allanneq", "Nutserineq", "Nipilersuut", "Pitsanngorsaruk")
    private val languages = listOf("Kalaallisut", "Danskisut", "Tuluttut")
    private lateinit var toolPicker: Spinner
    private lateinit var languagePanel: LinearLayout
    private lateinit var sourcePicker: Spinner
    private lateinit var targetPicker: Spinner
    private lateinit var textInput: EditText
    private lateinit var runButton: Button
    private lateinit var statusText: TextView
    private lateinit var answerText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val page = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(24), dp(20), dp(28))
            setBackgroundColor(Color.rgb(242, 247, 250))
        }
        val scroll = ScrollView(this).apply {
            isFillViewport = true
            addView(page)
        }

        page.addView(textView("OQAASERISSOQ AI", 28f, Color.rgb(7, 59, 92), true))
        page.addView(textView("Kalaallisut allanneq, nutserineq pitsanngorsaanerlu.", 16f, Color.rgb(57, 79, 93)))
        page.addView(textView("Suliassaq", 15f, Color.rgb(7, 59, 92), true).withTopMargin(22))
        toolPicker = spinner(tools)
        page.addView(toolPicker, matchWidth())
        toolPicker.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                languagePanel.visibility = if (tools[position] == "Nutserineq") View.VISIBLE else View.GONE
            }
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

        languagePanel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            visibility = View.GONE
        }
        languagePanel.addView(textView("Oqaatsit aallaaviusut", 14f, Color.rgb(7, 59, 92), true).withTopMargin(16))
        sourcePicker = spinner(languages)
        languagePanel.addView(sourcePicker, matchWidth())
        languagePanel.addView(textView("Oqaatsit nutseriffigisassat", 14f, Color.rgb(7, 59, 92), true).withTopMargin(10))
        targetPicker = spinner(listOf("Danskisut", "Kalaallisut", "Tuluttut"))
        languagePanel.addView(targetPicker, matchWidth())
        page.addView(languagePanel)

        page.addView(textView("Oqaasertat", 15f, Color.rgb(7, 59, 92), true).withTopMargin(18))
        textInput = EditText(this).apply {
            hint = "Uani allagit..."
            textSize = 16f
            minLines = 5
            maxLines = 12
            gravity = android.view.Gravity.TOP
            setPadding(dp(14), dp(12), dp(14), dp(12))
            setTextColor(Color.rgb(20, 39, 51))
            setHintTextColor(Color.rgb(115, 133, 145))
            setBackgroundColor(Color.WHITE)
        }
        page.addView(textInput, matchWidth().apply { topMargin = dp(6) })

        page.addView(textView(
            "Allatat serverikkut AI-mut nassiunneqassapput. API key app-imik ilaanngilaq.",
            13f,
            Color.rgb(82, 103, 116)
        ).withTopMargin(8))

        runButton = Button(this).apply {
            text = "AI aallartiguk"
            setTextColor(Color.WHITE)
            backgroundTintList = android.content.res.ColorStateList.valueOf(Color.rgb(0, 137, 156))
            setOnClickListener { submit() }
        }
        page.addView(runButton, matchWidth().apply { topMargin = dp(14) })

        statusText = textView("", 14f, Color.rgb(57, 79, 93))
        page.addView(statusText.withTopMargin(8))

        answerText = textView("", 17f, Color.rgb(20, 39, 51))
        answerText.setPadding(dp(14), dp(14), dp(14), dp(14))
        answerText.setBackgroundColor(Color.WHITE)
        answerText.visibility = View.GONE
        page.addView(answerText, matchWidth().apply { topMargin = dp(8) })

        val webButton = Button(this).apply {
            text = "Web version ammaruk"
            setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://oqaaserissoq-ai.vercel.app/")))
            }
        }
        page.addView(webButton, matchWidth().apply { topMargin = dp(12) })
        setContentView(scroll)
    }

    private fun submit() {
        val prompt = textInput.text.toString().trim()
        if (prompt.isEmpty()) {
            statusText.text = "Siullermik oqaasertat allakkit."
            answerText.visibility = View.GONE
            return
        }
        if (prompt.length > 12000) {
            statusText.text = "Oqaasertat 12.000-it sinnerlugit takissuseqarpallaartut."
            answerText.visibility = View.GONE
            return
        }

        val selectedTool = toolPicker.selectedItem.toString()
        val body = JSONObject()
            .put("text", prompt)
            .put("tool", selectedTool)
        if (selectedTool == "Nutserineq") {
            body.put("sourceLanguage", sourcePicker.selectedItem.toString())
            body.put("targetLanguage", targetPicker.selectedItem.toString())
        }

        runButton.isEnabled = false
        statusText.text = "OQAASERISSOQ AI eqqarsarpoq..."
        answerText.visibility = View.GONE

        Thread {
            var connection: HttpURLConnection? = null
            try {
                connection = (URL("$API_BASE/api/chat").openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    connectTimeout = 15_000
                    readTimeout = 50_000
                    doOutput = true
                    setRequestProperty("Content-Type", "application/json; charset=utf-8")
                    setRequestProperty("Accept", "application/json")
                }
                connection.outputStream.use { stream ->
                    stream.write(body.toString().toByteArray(Charsets.UTF_8))
                }
                val code = connection.responseCode
                val stream = if (code in 200..299) connection.inputStream else connection.errorStream
                val payload = stream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }.orEmpty()
                val json = if (payload.isNotBlank()) JSONObject(payload) else JSONObject()
                val result = if (code in 200..299) {
                    json.optString("result").takeIf { it.isNotBlank() }
                        ?: "Akissut pissarsiarineqanngilaq. Misileqqiguk."
                } else {
                    json.optString("error").ifBlank { "AI-mut attaveqarneq iluatsinngilaq. Kingusinnerusukkut misileqqiguk." }
                }
                runOnUiThread {
                    statusText.text = if (code in 200..299) "Akissut piareerpoq." else "AI-mut attaveqarneq iluatsinngilaq."
                    answerText.text = result
                    answerText.visibility = View.VISIBLE
                    runButton.isEnabled = true
                }
            } catch (_: Exception) {
                runOnUiThread {
                    statusText.text = "Serverimut attaveqarneq iluatsinngilaq. Interneti misissorlugu misileqqiguk."
                    runButton.isEnabled = true
                }
            } finally {
                connection?.disconnect()
            }
        }.start()
    }

    private fun spinner(values: List<String>): Spinner {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, values).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        return Spinner(this).apply { this.adapter = adapter }
    }

    private fun textView(value: String, size: Float, color: Int, bold: Boolean = false) =
        TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            if (bold) setTypeface(typeface, android.graphics.Typeface.BOLD)
        }

    private fun matchWidth() = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    private fun TextView.withTopMargin(margin: Int): TextView {
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply { topMargin = dp(margin) }
        return this
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    companion object {
        private const val API_BASE = "https://oqaaserissoq-ai.vercel.app"
    }
}
