package edu.newhaven.socialmediaapp.models


import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.HashMap


@IgnoreExtraProperties
data class Post(
    var uid: String? = "",
    var title: String? = "",
    var likes: Int = 0,
    var comments:Array<Comment> = arrayOf<Comment>()
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "title" to title,
            "likes" to likes,
            "comments" to comments,
        )
    }
}