package edu.newhaven.socialmediaapp.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var uid: String = "",
    var username: String? = "",
    var email: String? = "",
    var fullname: String? = "",
    var following: Array<String> = arrayOf<String>(),
    var followers: Array<String> = arrayOf<String>(),
    var post:Array<Post> = arrayOf<Post>()
)