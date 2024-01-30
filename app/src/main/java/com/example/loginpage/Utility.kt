package com.example.loginpage

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat

object Utility {

    // Function to get the CollectionReference for notes in Firestore
    @JvmStatic    // - tells that the declared function is static and so no need for objects for calling it
    fun getCollectionReferenceForNotes(): CollectionReference? {
        // Get the current user - by its ID
        val currentUser = FirebaseAuth.getInstance().currentUser

        // Return the CollectionReference for the user's notes
        return FirebaseFirestore.getInstance().collection("notes")
            .document(currentUser!!.uid).collection("my_notes")
    }

    // Function to convert a Timestamp to a formatted string
    @JvmStatic
    fun timestampToString(timestamp: Timestamp): String {
        val format = SimpleDateFormat("MM/dd/yyyy")
        return format.format(timestamp.toDate())
    }
}
