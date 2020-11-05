package edu.newhaven.socialmediaapp.models


import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.HashMap


@IgnoreExtraProperties
data class Post(
    var uid: String? = "",
    var username: String? = "",
    var title: String? = "",
    var likes: Int = 0,
    var comments: List<Comment> = listOf<Comment>(),
    var image:String = ""

) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "username" to username,
            "title" to title,
            "likes" to likes,
            "comments" to comments,
            "image" to image
        )
    }
}