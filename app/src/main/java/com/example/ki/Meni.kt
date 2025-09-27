package com.example.ki

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

class Meni : AppCompatActivity() {


    private lateinit var rules: Button
    private lateinit var sehara: Button
    private var namesList: List<String>? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meni)

        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.systemUiVisibility = uiOptions

        namesList = intent.getStringArrayListExtra("NAMES_LIST")
        val novaLista = namesList?.toMutableSet() ?: mutableSetOf()


        sehara = findViewById(R.id.button1)

        sehara.setOnClickListener {
            val intent = Intent(this, Sehara::class.java)
            intent.putStringArrayListExtra("NAMES_LIST", ArrayList(novaLista))
            startActivity(intent)
        }

        rules = findViewById(R.id.button2)

        rules.setOnClickListener {
            val intent = Intent(this, Rules::class.java)
            startActivity(intent)
        }


    }

}
