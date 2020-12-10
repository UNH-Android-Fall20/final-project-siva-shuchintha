package edu.newhaven.socialmediaapp.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Comment(
    var username: String? = "",
    var comment: String? = "",
    var timestamp: String? = ""
)