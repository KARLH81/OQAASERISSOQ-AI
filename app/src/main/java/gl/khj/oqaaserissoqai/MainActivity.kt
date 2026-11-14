package gl.khj.oqaaserissoqai

import android.content.ClipData
import android.content.ClipboardManager
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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private val tools = listOf("Allanneq", "Nutserineq", "Nipilersuut", "Pitsanngorsaruk", "Meeqqat Create")
    private val languages = listOf("Kalaallisut", "Danskisut", "Tuluttut")
    private lateinit var toolPicker: Spinner
    private lateinit var languagePanel: LinearLayout
    private lateinit var sourcePicker: Spinner
    private lateinit var targetPicker: Spinner
    private lateinit var textInput: EditText
    private lateinit var runButton: Button
    private lateinit var clearButton: Button
    private lateinit var statusText: TextView
    private lateinit var privacyNotice: TextView
    private lateinit var answerText: TextView
    private lateinit var resultActions: LinearLayout

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
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(scroll) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                maxOf(systemBars.bottom, ime.bottom)
            )
            insets
        }

        page.addView(textView("OQAASERISSOQ AI", 28f, Color.rgb(7, 59, 92), true))
        page.addView(textView("Kalaallisut allanneq, nutserineq pitsanngorsaanerlu.", 16f, Color.rgb(57, 79, 93)))
        page.addView(textView("Suliassaq", 15f, Color.rgb(7, 59, 92), true).withTopMargin(22))
        toolPicker = spinner(tools)
        page.addView(toolPicker, matchWidth())
        toolPicker.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                languagePanel.visibility = if (tools[position] == "Nutserineq") View.VISIBLE else View.GONE
                if (::textInput.isInitialized) {
                    textInput.hint = if (tools[position] == "Meeqqat Create") "Meeqqap oqaasia ataatsimik allagit..." else "Uani allagit..."
                }
                if (::privacyNotice.isInitialized) {
                    privacyNotice.text = if (tools[position] == "Meeqqat Create") {
                        "Meeqqap aqqa, atuarfia, najugaqarfia, peqqinnissaa attaveqarfialuunniit allassanngilat. Meeqqat angajoqqaamik/akisussaasumik ikiorneqarlutik atussapput. Allatat AI-mut serverikkut nassiunneqassapput."
                    } else {
                        "Allatat serverikkut AI-mut nassiunneqassapput. Inuttut paasissutissat pingaarutillit ilanngunnagit."
                    }
                }
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

        clearButton = Button(this).apply {
            text = "Allatat piiarlugit"
            setOnClickListener {
                androidx.appcompat.app.AlertDialog.Builder(this@MainActivity)
                    .setTitle("Allatat piiarlugit?")
                    .setMessage("Oqaasertat akissullu app-imi isikkivimmiittut piiarneqassapput.")
                    .setNegativeButton("Taamaatitsi", null)
                    .setPositiveButton("Piiaruk") { _, _ ->
                        textInput.text?.clear()
                        answerText.text = ""
                        answerText.visibility = View.GONE
                        resultActions.visibility = View.GONE
                        statusText.text = ""
                    }
                    .show()
            }
        }
        page.addView(clearButton, matchWidth().apply { topMargin = dp(6) })

        privacyNotice = textView(
            "Allatat serverikkut AI-mut nassiunneqassapput. Inuttut paasissutissat pingaarutillit ilanngunnagit.",
            13f,
            Color.rgb(82, 103, 116)
        ).withTopMargin(8)
        page.addView(privacyNotice)

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
        answerText.setTextIsSelectable(true)
        page.addView(answerText, matchWidth().apply { topMargin = dp(8) })

        resultActions = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            visibility = View.GONE
        }
        val copyButton = Button(this).apply {
            text = "Kopiér akissut"
            setOnClickListener {
                val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("OQAASERISSOQ AI", answerText.text))
                Toast.makeText(this@MainActivity, "Akissut kopierineqarpoq.", Toast.LENGTH_SHORT).show()
            }
        }
        val shareButton = Button(this).apply {
            text = "Avitseqatigiuk"
            setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, answerText.text.toString())
                }
                startActivity(Intent.createChooser(shareIntent, "Akissut avitseqatigiuk"))
            }
        }
        resultActions.addView(copyButton, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        resultActions.addView(shareButton, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        page.addView(resultActions, matchWidth().apply { topMargin = dp(4) })

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
        clearButton.isEnabled = false
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
                    resultActions.visibility = if (code in 200..299) View.VISIBLE else View.GONE
                    runButton.isEnabled = true
                    clearButton.isEnabled = true
                }
            } catch (_: Exception) {
                runOnUiThread {
                    statusText.text = "Serverimut attaveqarneq iluatsinngilaq. Interneti misissorlugu misileqqiguk."
                    answerText.visibility = View.GONE
                    resultActions.visibility = View.GONE
                    runButton.isEnabled = true
                    clearButton.isEnabled = true
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
