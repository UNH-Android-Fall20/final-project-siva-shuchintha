package edu.newhaven.socialmediaapp.models


import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.HashMap


@IgnoreExtraProperties
data class Post(
    var uid: String? = "",
    var username: String? = "",
    var title: String? = "",
    var image:String = "",
    var postid: String = "",
    var timestamp: String = "",


) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "username" to username,
            "title" to title,
            "image" to image,
            "postid" to postid,
        "timestamp"  to timestamp
        )
    }
}