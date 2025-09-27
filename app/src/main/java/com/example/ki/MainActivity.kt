package com.example.ki

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ki.ui.theme.KITheme
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs

class MainActivity : AppCompatActivity() {



    private lateinit var database: FirebaseDatabase
    private lateinit var namesRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var nameInput: EditText
    private lateinit var addButton: ImageButton
    private lateinit var seharaButton: ImageButton
    private lateinit var numberOfNames: TextView
    private lateinit var turn: Button
    private lateinit var zaok: ImageView

    private val names = mutableSetOf<String>()

    private val since = "2024-05-10"





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



            val decorView = window.decorView
            val uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            decorView.systemUiVisibility = uiOptions


        // Initialize Firebase

        database= Firebase.database("https://kiii-9113e-default-rtdb.europe-west1.firebasedatabase.app")
        namesRef = database.getReference("names")

        // Check if notification permission is granted
//        if (checkNotificationPermission(applicationContext)) {
//            createNotification(applicationContext)  // Post the notification
//        } else {
//            requestNotificationPermission(this)  // Request permission
//        }

        nameInput = findViewById(R.id.nameInput)
        addButton = findViewById(R.id.addButton)
        numberOfNames = findViewById(R.id.numberOfNames)
        turn = findViewById(R.id.turn)
        zaok = findViewById(R.id.image_placeholder)

//        namesRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // Clear the existing list
//                names.clear()
//
//                // Iterate through each child of the "names" node
//                for (childSnapshot in dataSnapshot.children) {
//                    // Get the value of the child as a String
//                    val name = childSnapshot.getValue(String::class.java)
//
//                    // Add the name to the list if it's not null
//                    if (name != null) {
//                        names.add(name)
//                    }
//                }
//                updateNumberOfNames()
//                updateRijadaa()
//
//                // Now the "names" list contains all the names from the database
//                // You can use this list to display the names in your app
//                // ...
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(this@MainActivity, "Error reading data: ${error.message}", Toast.LENGTH_LONG).show()
//            }
//        })
        seharaButton = findViewById(R.id.seharaB) // Your ImageButton for opening fragment

        seharaButton.setOnClickListener {
            val intent = Intent(this, Meni::class.java)
            intent.putStringArrayListExtra("NAMES_LIST", ArrayList(names))
            startActivity(intent)
        }

        turn = findViewById(R.id.turn) // Your ImageButton for opening fragment

        turn.setOnClickListener {
            val intent = Intent(this, Rezerva::class.java)
            startActivity(intent)
        }




        namesRef.get().addOnSuccessListener { snapshot ->
            snapshot.children.forEach { child ->
                val name1 = child.getValue(String::class.java)
                if (name1 != null) {
                    names.add(name1)
                }
            }

            updateNumberOfNames()
            updateRijadaa()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to sync with Firebase", Toast.LENGTH_SHORT).show()
        }

        var tacno = true

        val rotateAnimation = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 1000
            interpolator = LinearInterpolator()
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation?) {
                    if(tacno){
                    zaok.setBackgroundResource(R.drawable.kruzic2)
                    }
                    else{
                        zaok.setBackgroundResource(R.drawable.kruzic3)
                    }

                    Handler(Looper.getMainLooper()).postDelayed({
                        zaok.setBackgroundResource(R.drawable.kruzic)
                    }, 1000)
                }
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }


        updateNumberOfNames()
        updateRijadaa()

        val limit = daysSince(since)+24

        addButton.setOnClickListener {
            val name = nameInput.text.toString().trim().lowercase()
            if (names.size == limit) {
                Toast.makeText(this, "Eha zekane", Toast.LENGTH_SHORT).show()
                nameInput.text.clear()
                return@setOnClickListener
            }
            if (name.isEmpty() || name.length <= 2) {
                Toast.makeText(this, "To ja glup jel", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (names.contains(name)) {
                tacno = false
                zaok.startAnimation(rotateAnimation)
                Handler(Looper.getMainLooper()).postDelayed({
                    Toast.makeText(this, "Đuvegija to je bilo", Toast.LENGTH_SHORT).show()
                    nameInput.text.clear()
                }, 1250)


            } else {


                val words = name.split(" ")
                val hasKiEnding = words.any { it.endsWith("ki") }

                if (!hasKiEnding) {
                    tacno = false
                    zaok.startAnimation(rotateAnimation)
                    Handler(Looper.getMainLooper()).postDelayed({
                        Toast.makeText(this, "Sabane ne rimuje se sa ki", Toast.LENGTH_SHORT).show()
                        nameInput.text.clear()
                    }, 1250)


                } else {

                    tacno = true
                    zaok.startAnimation(rotateAnimation)

                    names.add(name)



                    namesRef.push().setValue(name).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Handler(Looper.getMainLooper()).postDelayed({
                                Toast.makeText(this, "Dnevna dužnost ispunjena", Toast.LENGTH_SHORT)
                                    .show()
                                nameInput.text.clear()
                                updateNumberOfNames()
                                updateRijadaa()
                            }, 1250)
                        } else {
                            Toast.makeText(
                                this,
                                "Failed to add name to Firebase",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }


                }
            }
        }

//        scheduleNotification(applicationContext, 22, 30)



    }
    private fun updateNumberOfNames() {
        numberOfNames.text = "${names.size}"
    }
    private fun updateRijadaa() {
        if(names.size % 2 == 0){
        turn.text = "Efe"
        }else{
            turn.text = "Rile"
        }
    }


    fun daysSince(date: String, dateFormat: String = "yyyy-MM-dd"): Int {
        val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
        return try {
            // Parse the input date
            val parsedDate = sdf.parse(date)
            // Get today's date
            val today = java.util.Calendar.getInstance().time
            // Calculate the difference in milliseconds
            val diffInMillis = abs(today.time - (parsedDate?.time ?: 0))
            // Convert milliseconds to days
            (diffInMillis / (1000 * 60 * 60 * 24)).toInt()
        } catch (e: Exception) {
            // Handle parsing errors or invalid dates
            println("Error parsing date: ${e.message}")
            -1 // Return -1 to indicate an error
        }

    }


//    private fun createNotification(context: Context) {
//        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val channelId = "your_channel_id"
//
//        // Create notification channel for Android 8.0+ (Oreo and above)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                channelId,
//                "Channel Name",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        // Build the notification
//        val notification: Notification = NotificationCompat.Builder(context, channelId)
//            .setContentTitle("Ki Dana")
//            .setContentText("Dilbere jesi poslo za danas?")
//            .setSmallIcon(R.drawable.kontiki)
//            .build()
//
//        // Trigger the notification
//        notificationManager.notify(0, notification)
//    }

//    fun checkNotificationPermission(context: Context): Boolean {
//        return ContextCompat.checkSelfPermission(
//            context,
//            android.Manifest.permission.POST_NOTIFICATIONS
//        ) == PackageManager.PERMISSION_GRANTED
//    }
//
//    private val REQUEST_CODE_NOTIFICATION_PERMISSION = 100
//
//    fun requestNotificationPermission(activity: Activity) {
//        ActivityCompat.requestPermissions(
//            activity,
//            arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
//            REQUEST_CODE_NOTIFICATION_PERMISSION
//        )
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if (requestCode == REQUEST_CODE_NOTIFICATION_PERMISSION) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted, you can post notifications now
//                createNotification(applicationContext)
//            } else {
//                // Permission denied
//                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }






//    fun scheduleNotification(context: Context, hour: Int, minute: Int) {
//        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.HOUR_OF_DAY, hour)
//        calendar.set(Calendar.MINUTE, minute)
//        calendar.set(Calendar.SECOND, 0)
//
//        // Create the intent that will trigger the notification
//        val intent = Intent(context, NotificationReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        // Set up the AlarmManager to fire the intent at the specified time
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
//    }


}




