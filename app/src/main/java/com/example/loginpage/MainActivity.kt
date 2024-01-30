package com.example.loginpage

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginpage.Utility.getCollectionReferenceForNotes
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity() {

    // Declaring the required variables along with their data types
    private var addNoteBtn: FloatingActionButton? = null
    private var logoutBtn: ImageButton? = null
    var recyclerView: RecyclerView? = null
    var toDoAdapter: ToDoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Store all the contents from the main activity into the variables
        addNoteBtn = findViewById(R.id.add_note_btn)
        logoutBtn = findViewById(R.id.menu_btn)
        recyclerView = findViewById(R.id.recyler_view)

        // ? checks if it is null or not
        addNoteBtn?.setOnClickListener {
            startActivity(Intent(this, ToDoActivity::class.java))
        }

        // on click listener on logout button at top
        logoutBtn?.setOnClickListener {
            FirebaseAuth.getInstance().signOut()    // using predefined method in firebase
            startActivity(Intent(this, LoginActivity::class.java))      // activity that should be seen after logout
            finish()
        }

        // Setup the RecyclerView
        setupRecyclerView()
    }

    // Function to set up the RecyclerView
    private fun setupRecyclerView() {

        // Define a Firestore query to retrieve TO-DO items from the Firebase Firestore database
        val query: Query = getCollectionReferenceForNotes()!!
            .orderBy("timestamp", Query.Direction.DESCENDING)

        // Configure options for the FirestoreRecyclerAdapter - retrieve the list which is added in firestore
        val options = FirestoreRecyclerOptions.Builder<ToDo>()
            .setQuery(query, ToDo::class.java).build()

        // Set up the RecyclerView with a LinearLayoutManager
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        // Create an instance of the ToDoAdapter with the configured options
        toDoAdapter = ToDoAdapter(options, this)

        // Set the RecyclerView adapter to the created ToDoAdapter
        recyclerView!!.adapter = toDoAdapter     // !! used as double bang operator, consider the value as non-null in any case
    }

    // Called when the activity is becoming visible to the user - all are predefined methods
    override fun onStart() {
        super.onStart()     // use super to manage lifecycle of progrma correctly
        toDoAdapter!!.startListening()
        // we use override to use customizable logic inside the functions
    }

    // Called when the activity is no longer visible to the user
    override fun onStop() {
        super.onStop()
        toDoAdapter!!.stopListening()
    }

    // Called after the activity has been paused and restarted
    override fun onResume() {
        super.onResume()
        toDoAdapter!!.notifyDataSetChanged()
    }
}
