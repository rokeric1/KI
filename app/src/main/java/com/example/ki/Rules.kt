package com.example.ki

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class Rules : AppCompatActivity() {



    private lateinit var database: FirebaseDatabase
    private lateinit var namesRef: DatabaseReference
    private lateinit var namesRef2: DatabaseReference
    private lateinit var namesRef3: DatabaseReference
    private lateinit var namesRef4: DatabaseReference

    private lateinit var nameInput: EditText
    private lateinit var nameInput2: EditText
    private lateinit var addButton: ImageButton
    private lateinit var namesLayout: LinearLayout
    private lateinit var namesLayout2: LinearLayout
    private lateinit var zelje: Button
    private lateinit var pravila: Button
    private lateinit var zeljeScroll: ScrollView
    private lateinit var pravilaScroll: ScrollView
    private val rules = mutableListOf<String>()
    private val approving = mutableListOf<String>()
    private val rdesc = mutableListOf<String>()
    private val adesc = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rules)

        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.systemUiVisibility = uiOptions

        nameInput = findViewById(R.id.nameInput)
        nameInput2 = findViewById(R.id.nameInput2)
        addButton = findViewById(R.id.addButton)
        namesLayout = findViewById(R.id.rijec)
        namesLayout2 = findViewById(R.id.rijec2)



        database= Firebase.database("https://kiii-9113e-default-rtdb.europe-west1.firebasedatabase.app")
        namesRef = database.getReference("rules")
        namesRef2 = database.getReference("wish")
        namesRef3 = database.getReference("ropis")
        namesRef4 = database.getReference("wopis")


        namesRef.get().addOnSuccessListener { snapshot ->
            snapshot.children.forEach { child ->
                val name1 = child.getValue(String::class.java)
                if (name1 != null) {
                    rules.add(name1)
                }
            }
            namesRef3.get().addOnSuccessListener { snapshot2 ->
                snapshot2.children.forEach { child ->
                    val name2 = child.getValue(String::class.java)
                    if (name2 != null) {
                        rdesc.add(name2)
                    }
                }
                updateScrollView()


            }.addOnFailureListener {
                Toast.makeText(this, "Failed to sync with Firebase", Toast.LENGTH_SHORT).show()
            }


        }.addOnFailureListener {
            Toast.makeText(this, "Failed to sync with Firebase", Toast.LENGTH_SHORT).show()
        }







        namesRef2.get().addOnSuccessListener { snapshot ->
            snapshot.children.forEach { child ->
                val name1 = child.getValue(String::class.java)
                if (name1 != null) {
                    approving.add(name1)
                }
            }
            namesRef4.get().addOnSuccessListener { snapshot2 ->
                snapshot2.children.forEach { child ->
                    val name2 = child.getValue(String::class.java)
                    if (name2 != null) {
                        adesc.add(name2)
                    }
                }
                updateScrollView2()


            }.addOnFailureListener {
                Toast.makeText(this, "Failed to sync with Firebase", Toast.LENGTH_SHORT).show()
            }



        }.addOnFailureListener {
            Toast.makeText(this, "Failed to sync with Firebase", Toast.LENGTH_SHORT).show()
        }





        updateScrollView()
        updateScrollView2()




        zelje= findViewById(R.id.zelje)
        pravila= findViewById(R.id.zakon)
        zeljeScroll = findViewById(R.id.zeljeScr)
        pravilaScroll = findViewById(R.id.pravilaScr)

        zelje.setOnClickListener {
                zeljeScroll.visibility = View.GONE
                zelje.visibility = View.GONE
                pravilaScroll.visibility = View.VISIBLE
                pravila.visibility = View.VISIBLE
        }

        pravila.setOnClickListener {
                zeljeScroll.visibility = View.VISIBLE
                zelje.visibility = View.VISIBLE
                pravilaScroll.visibility = View.GONE
                pravila.visibility = View.GONE

        }




        addButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val opis = nameInput2.text.toString().trim()


            if (name.isEmpty() || name.length <= 2 || opis.isEmpty()) {
                Toast.makeText(this, "To ja glup jel", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (rules.contains(name)) {
                Toast.makeText(this, "Name already added", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            rules.add(name)
            rdesc.add(opis)
            namesRef.push().setValue(name).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        Toast.makeText(this, "Pravilo dodano", Toast.LENGTH_SHORT)
                            .show()
                        nameInput.text.clear()
                    }, 1250)
                } else {
                    Toast.makeText(
                        this,
                        "Failed to add name to Firebase",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            namesRef3.push().setValue(opis).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        Toast.makeText(this, "Opis dodan", Toast.LENGTH_SHORT)
                            .show()
                        nameInput2.text.clear()
                    }, 1250)
                } else {
                    Toast.makeText(
                        this,
                        "Failed to add name to Firebase",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            updateScrollView()
            nameInput.text.clear()
            nameInput2.text.clear()
        }
    }


    var pomocna =1


    private fun updateScrollView() {
        namesLayout.removeAllViews()



        val p = ContextCompat.getDrawable(this, R.drawable.dodaj)
        val k = ResourcesCompat.getFont(this, R.font.kulen)





        rules.forEach { name ->
            val nameView = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                background=p
                gravity= Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 0, 16) // 16dp bottom margin
                }
            }

            val br = rules.indexOf(name)+1
            val opisi = rdesc[br-1]
            val redni = br.toString()
            val praviloo = redni + ".  " + name.uppercase()
            val opis = redni + ".  " + opisi

            // Create the name TextView
            val nameTextView = TextView(this).apply {
                if(pomocna == 1 ){ text = praviloo } else if(pomocna == 0) {text= opis}
                textSize = 16f
                typeface= k
                setTextColor(Color.parseColor("#42eba7"))
                setPadding(30, 16, 20, 16) // Padding around the text
                layoutParams = LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f // Make name take most of the space
                )


                setOnClickListener {
                    if(pomocna == 1){
                        pomocna = 0
                    } else if(pomocna == 0){
                        pomocna = 1
                    }
                    updateScrollView()
                }

            }


            // Create the remove ImageButton
            val removeImageButton = ImageButton(this).apply {
                setImageResource(R.drawable.check) // Set your image resource here
                setBackgroundColor(Color.TRANSPARENT)

                (this as ImageView).scaleType = ImageView.ScaleType.CENTER_CROP
                val params = LinearLayout.LayoutParams(70, 70).apply {
                    marginStart = 10  // Sets the left margin (for LTR layouts) or right margin (for RTL layouts)
                    marginEnd = 20    // Sets the right margin (for LTR layouts) or left margin (for RTL layouts)
                }

                layoutParams = params


                setOnClickListener {
                    rules.remove(name)
                    rdesc.remove(opisi)
                    var ckey : String = ""
                    var okey : String = ""
                    namesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (child in snapshot.children) {
                                if(child.value == name){
                                    ckey = child.key.toString()
                                    namesRef.child(ckey).removeValue()
                                    break
                                }
                            }
                            namesRef3.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot1: DataSnapshot) {
                                    for (child in snapshot1.children) {
                                        if(child.value == opisi){
                                            okey = child.key.toString()
                                            namesRef3.child(okey).removeValue()
                                            break
                                        }
                                    }
                                    updateScrollView()
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    println("Database error: ${error.message}")
                                }
                            })

                        }

                        override fun onCancelled(error: DatabaseError) {
                            println("Database error: ${error.message}")
                        }
                    })

                    approving.add(name)
                    adesc.add(opisi)
                    namesRef2.push().setValue(name)
                    namesRef4.push().setValue(opisi)
                    updateScrollView()
                    updateScrollView2()

                }
            }

            // Add both the name and the remove button to the same layout (horizontal)
            nameView.addView(nameTextView)
            nameView.addView(removeImageButton)

            // Add the nameView to the main namesLayout
            namesLayout.addView(nameView)

        }
    }







    var pomocna2 =1

    private fun updateScrollView2() {
        namesLayout2.removeAllViews()



        val p = ContextCompat.getDrawable(this, R.drawable.dodaj)
        val k = ResourcesCompat.getFont(this, R.font.kulen)








        approving.forEach { name ->
            val nameView = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                background=p
                gravity= Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 0, 16) // 16dp bottom margin
                }
            }

            val br = approving.indexOf(name)+1
            val opisi = adesc[br-1]
            val redni = br.toString()
            val praviloo = redni + ".  " + name.uppercase()
            val opis = redni + ".  " + opisi

            // Create the name TextView
            val nameTextView = TextView(this).apply {
                if(pomocna2 == 1 ){ text = praviloo } else if(pomocna2 == 0) {text= opis}
                textSize = 16f
                typeface= k
                setTextColor(Color.parseColor("#42eba7"))
                setPadding(30, 16, 20, 16) // Padding around the text
                layoutParams = LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f // Make name take most of the space
                )

                setOnClickListener {
                    if(pomocna2 == 1){
                        pomocna2 = 0
                    } else if(pomocna2 == 0){
                        pomocna2 = 1
                    }
                    updateScrollView2()
                }
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
                    approving.remove(name)
                    adesc.remove(opisi)
                    var ckey : String = ""
                    var okey : String = ""
                    namesRef2.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (child in snapshot.children) {
                                if(child.value == name){
                                    ckey = child.key.toString()
                                    namesRef2.child(ckey).removeValue()
                                    break
                                }
                            }
                            namesRef4.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot2: DataSnapshot) {
                                    for (child in snapshot2.children) {
                                        if(child.value == opisi){
                                            okey = child.key.toString()
                                            namesRef4.child(okey).removeValue()
                                            break
                                        }
                                    }
                                    updateScrollView2()
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    println("Database error: ${error.message}")
                                }
                            })
                        }

                        override fun onCancelled(error: DatabaseError) {
                            println("Database error: ${error.message}")
                        }
                    })

                }
            }

            // Add both the name and the remove button to the same layout (horizontal)
            nameView.addView(nameTextView)
            nameView.addView(removeImageButton)

            // Add the nameView to the main namesLayout
            namesLayout2.addView(nameView)

        }
    }





}
