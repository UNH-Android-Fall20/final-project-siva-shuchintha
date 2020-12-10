package edu.newhaven.socialmediaapp.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Messages (
    var username: String? = "",
    var message: String? = "",
    var timestamp: String = "",
    var userid: String = ""


    ) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "username" to username,
            "message" to message,
            "timestamp"  to timestamp,
            "userid" to userid
        )
    }
}