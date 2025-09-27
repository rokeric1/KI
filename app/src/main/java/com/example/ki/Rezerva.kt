package com.example.ki

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

class Rezerva : AppCompatActivity() {

    private val sharedPreferencesName = "rezerve"
    private val namesKey = "names"

    private lateinit var nameInput: EditText
    private lateinit var addButton: ImageButton
    private lateinit var namesLayout: LinearLayout
    private val rezerve = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rezerva)

        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.systemUiVisibility = uiOptions

        nameInput = findViewById(R.id.nameInput)
        addButton = findViewById(R.id.addButton)
        namesLayout = findViewById(R.id.rijec)

        // Load names from SharedPreferences
        val sharedPreferences = getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
        rezerve.addAll(sharedPreferences.getStringSet(namesKey, emptySet()) ?: emptySet())

        // Populate UI with saved names
        updateScrollView()

        addButton.setOnClickListener {
            val name = nameInput.text.toString().trim()

            if (name.isEmpty() || name.length <= 2) {
                Toast.makeText(this, "To ja glup jel", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (rezerve.contains(name)) {
                Toast.makeText(this, "Name already added", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            rezerve.add(name)
            sharedPreferences.edit().putStringSet(namesKey, rezerve).apply()

            updateScrollView()
            nameInput.text.clear()
        }
    }

    private fun updateScrollView() {
        namesLayout.removeAllViews() // Clear previous views
        val sharedPreferences = getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)

        val p = ContextCompat.getDrawable(this, R.drawable.dodaj)
        val k = ResourcesCompat.getFont(this, R.font.kulen)


        rezerve.forEach { name ->
            val nameView = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                background=p
                gravity= Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    120
                ).apply {
                    setMargins(0, 0, 0, 16) // 16dp bottom margin
                }
            }

            // Create the name TextView
            val nameTextView = TextView(this).apply {
                text = name.uppercase()
                textSize = 16f
                typeface= k
                setTextColor(Color.parseColor("#42eba7"))
                setPadding(30, 16, 20, 16) // Padding around the text
                layoutParams = LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f // Make name take most of the space
                )
            }

            // Create the remove ImageButton
            val removeImageButton = ImageButton(this).apply {
                setImageResource(R.drawable.minus) // Set your image resource here
                setBackgroundColor(Color.TRANSPARENT)

                (this as ImageView).scaleType = ImageView.ScaleType.CENTER_CROP
                val params = LinearLayout.LayoutParams(70, 70).apply {
                    marginStart = 10  // Sets the left margin (for LTR layouts) or right margin (for RTL layouts)
                    marginEnd = 20    // Sets the right margin (for LTR layouts) or left margin (for RTL layouts)
                }

                layoutParams = params


                setOnClickListener {
                    rezerve.remove(name)
                    sharedPreferences.edit().putStringSet(namesKey, rezerve).apply()
                    updateScrollView() // Update the layout
                }
            }

            // Add both the name and the remove button to the same layout (horizontal)
            nameView.addView(nameTextView)
            nameView.addView(removeImageButton)

            // Add the nameView to the main namesLayout
            namesLayout.addView(nameView)
        }
    }

}
