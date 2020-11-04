package edu.newhaven.socialmediaapp.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var uid: String = "",
    var fullname: String? = "",
    var username: String? = "",
    var email: String? = "",
    var following: List<String> = listOf<String>(),
    var followers: List<String> = listOf<String>(),
    var post:List<Post> = listOf<Post>()
)