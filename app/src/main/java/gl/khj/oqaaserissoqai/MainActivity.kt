package gl.khj.oqaaserissoqai

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.net.Uri

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 64, 48, 48)
        }
        layout.addView(TextView(this).apply {
            text = "OQAASERISSOQ AI"
            textSize = 30f
        })
        layout.addView(TextView(this).apply {
            text = "Kalaallit oqaasiinut naleqqussagaq AI-p pilersitsiviusoq."
            textSize = 18f
        })
        listOf("Allanneq", "Nutserineq", "Nipilersuut", "Pitsanngorsaruk").forEach { name ->
            layout.addView(Button(this).apply {
                text = name
                setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://oqaaserissoq-ai.vercel.app/#tool=$name")))
                }
            })
        }
        setContentView(layout)
    }
}
