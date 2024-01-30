package com.example.loginpage

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.android.gms.tasks.Task
import android.view.View
import android.widget.Toast

class ToDoActivity : AppCompatActivity() {

    // Declare UI elements - that will be used later
    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var saveNoteBtn: ImageButton
    private lateinit var pageTitleTextView: TextView
    private lateinit var deleteNoteTextViewBtn: TextView

    // Data for editing existing note
    private var noteTitle: String? = null
    private var noteContent: String? = null
    private var noteDocId: String? = null
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        // Initialize UI elements
        titleEditText = findViewById(R.id.notes_title_text)    // R stands resource, contains reference to all the resources
        contentEditText = findViewById(R.id.notes_content_text)
        saveNoteBtn = findViewById(R.id.save_note_btn)
        pageTitleTextView = findViewById(R.id.page_title)
        deleteNoteTextViewBtn = findViewById(R.id.delete_note_text_view_btn)

        // Receive data from intent
        noteTitle = intent.getStringExtra("title")
        noteContent = intent.getStringExtra("content")
        noteDocId = intent.getStringExtra("docId")

        // Check if in edit mode
        if (noteDocId != null && noteDocId!!.isNotEmpty()) {
            isEditMode = true
        }

        // Set UI elements with received data
        titleEditText.setText(noteTitle)
        contentEditText.setText(noteContent)

        // Adjust UI for edit mode
        if (isEditMode) {
            pageTitleTextView.text = "Edit your task"
            deleteNoteTextViewBtn.visibility = View.VISIBLE
        }

        // Set click listeners for buttons
        saveNoteBtn.setOnClickListener { saveNote() }
        deleteNoteTextViewBtn.setOnClickListener { deleteNoteFromFirebase() }
    }

    // Save note to Firebase
    private fun saveNote() {
        val title = titleEditText.text.toString()
        val content = contentEditText.text.toString()

        // Validate title
        if (title.isNullOrBlank()) {
            titleEditText.error = "Title is required"
            return
        }

        // Create ToDo object
        val toDo = ToDo()
        toDo.title = title
        toDo.content = content
        toDo.timestamp = Timestamp.now()

        // Save note to Firebase
        saveNoteToFirebase(toDo)
    }

    // Save note to Firebase Firestore
    private fun saveNoteToFirebase(toDo: ToDo) {
        // Get document reference based on edit mode
        val documentReference: DocumentReference? = if (isEditMode) {
            Utility.getCollectionReferenceForNotes()?.document(noteDocId!!)
        } else {
            Utility.getCollectionReferenceForNotes()?.document()
        }

        // Check if document reference is not null
        documentReference?.let { docRef ->
            // Set the ToDo object to the document
            docRef.set(toDo).addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    // Note is added successfully
                    Toast.makeText(this@ToDoActivity, "Note added successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    // Failed to add note
                    Toast.makeText(this@ToDoActivity, "Failed to add Notes", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    // Delete note from Firebase Firestore
    private fun deleteNoteFromFirebase() {
        // Get document reference for the note to be deleted
        val documentReference: DocumentReference? = Utility.getCollectionReferenceForNotes()?.document(noteDocId!!)

        // Check if document reference is not null
        documentReference?.delete()?.addOnCompleteListener { task: Task<Void?> ->
            if (task.isSuccessful) {
                // Note is deleted successfully
                Toast.makeText(this@ToDoActivity, "Note deleted successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                // Failed to delete note
                Toast.makeText(this@ToDoActivity, "Failed to delete note", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
