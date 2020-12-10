package edu.newhaven.socialmediaapp.models

import com.google.firebase.database.IgnoreExtraProperties
import java.sql.Timestamp

@IgnoreExtraProperties
data class Comment(
    var username: String? = "",
    var comment: String? = "",
    var timestamp: String? = ""
)