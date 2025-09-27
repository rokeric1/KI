package com.example.ki

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.ki.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

class Sehara : AppCompatActivity() {

    private lateinit var namesLayout: LinearLayout
    private var namesList: List<String>? = null
    private lateinit var zao: TextView
    private lateinit var searchBar: EditText

    private val since = "2024-05-10"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sehara)

        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.systemUiVisibility = uiOptions

        zao = findViewById(R.id.zaostatak)
        searchBar = findViewById(R.id.search_bar)
        zaostali()

        namesLayout = findViewById(R.id.rijec)

        // Get the list of names passed via the Intent
        namesList = intent.getStringArrayListExtra("NAMES_LIST")

        val p = ContextCompat.getDrawable(this, R.drawable.dodaj)
        val k = ResourcesCompat.getFont(this, R.font.kulen)

        val textViewList = mutableListOf<TextView>()

        namesList?.let {
            val reversedNamesList = it.reversed()
            for (name in reversedNamesList) {
                val nameTextView = TextView(this)
                nameTextView.text = name.uppercase()
                nameTextView.textSize = 16f
                nameTextView.background = p
                nameTextView.typeface = k
                nameTextView.setTextColor(Color.parseColor("#42eba7"))
                nameTextView.setPadding(20, 16, 20, 16)
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 0, 0, 16)
                nameTextView.layoutParams = params
                namesLayout.addView(nameTextView)
                textViewList.add(nameTextView)
            }
        }

        // Implement search filter
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim().uppercase()
                for (textView in textViewList) {
                    if (textView.text.toString().contains(query)) {
                        textView.visibility = View.VISIBLE
                    } else {
                        textView.visibility = View.GONE
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    fun daysSince(date: String, dateFormat: String = "yyyy-MM-dd"): Int {
        val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
        return try {
            val parsedDate = sdf.parse(date)
            val today = Calendar.getInstance().time
            val diffInMillis = abs(today.time - (parsedDate?.time ?: 0))
            (diffInMillis / (1000 * 60 * 60 * 24)).toInt()
        } catch (e: Exception) {
            println("Error parsing date: ${e.message}")
            -1
        }
    }

    private fun zaostali() {
        zao.text = "${daysSince(since) + 24}"
    }
}
