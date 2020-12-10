package edu.newhaven.socialmediaapp.models

import android.net.Uri
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var uid: String = "",
    var fullname: String? = "",
    var username: String? = "",
    var email: String? = "",
    var profileimage:String? = "",
    var bio: String? = ""

    )