package com.example.loginpage

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.text.SimpleDateFormat

class ToDoAdapter(options: FirestoreRecyclerOptions<ToDo>, private val context: Context) :
    FirestoreRecyclerAdapter<ToDo, ToDoAdapter.NoteViewHolder>(options) {

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int, model: ToDo) {
        // Bind data to the UI elements in each ViewHolder

        // Set title and content
        holder.titleTextView.text = model.title
        holder.contentTextView.text = model.content

        // Format and set timestamp
        val timestampFormat = SimpleDateFormat("MM/dd/yyyy")
        val formattedTimestamp = model.timestamp?.toDate()?.let { timestampFormat.format(it) } ?: ""
        holder.timestampTextView.text = formattedTimestamp

        // Set the checkbox state based on the task completion status
        holder.checkBox.isChecked = model.isChecked ?: false

        // Set a listener for checkbox state change
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            // Handle checkbox state change
            if (isChecked) {
                // Delete the task when the checkbox is checked
                deleteTask(position)
            } else {
                // Handle other actions when the checkbox is unchecked (if needed)
            }
        }

        // Set click listener for the entire item view to open ToDoActivity for editing
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ToDoActivity::class.java)
            intent.putExtra("title", model.title)
            intent.putExtra("content", model.content)
            val docId = snapshots.getSnapshot(position).id
            intent.putExtra("docId", docId)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        // Create and return a new ViewHolder for each item
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_todo_item, parent, false)
        return NoteViewHolder(view)
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // ViewHolder class to hold references to UI elements

        var titleTextView: TextView
        var contentTextView: TextView
        var timestampTextView: TextView
        var checkBox: CheckBox

        init {
            // Initialize UI elements in the ViewHolder
            titleTextView = itemView.findViewById(R.id.note_title_text_view)
            contentTextView = itemView.findViewById(R.id.note_content_text_view)
            timestampTextView = itemView.findViewById(R.id.note_timestamp_text_view)
            checkBox = itemView.findViewById(R.id.note_checkbox)
        }
    }

    // Function to delete a task based on position
    private fun deleteTask(position: Int) {
        val documentSnapshot = snapshots.getSnapshot(position)
        val documentReference = documentSnapshot.reference
        documentReference.delete()
            .addOnSuccessListener {
                // Task deleted successfully
                showToast("Task completed successfully")
            }
            .addOnFailureListener { e ->
                // Handle failure to delete task
                showToast("Failed to complete task")
            }
    }

    // Function to show a Toast message
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
