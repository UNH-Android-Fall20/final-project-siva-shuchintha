package edu.newhaven.socialmediaapp.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Comment(
    var author: String? = "",
    var comment: String? = ""
)