package edu.newhaven.socialmediaapp.Adapter


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.newhaven.socialmediaapp.models.User
import edu.newhaven.socialmediaapp.R

class UserItemAdapter (private var context: Context,
                       private var userList: List<User>,
                       private var isFragment: Boolean = false) : RecyclerView.Adapter<UserItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_card_search, parent, false)
        Log.d("TAG333", "cchellocc")

        return UserItemAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("TAG333", userList.size.toString())
        return userList.size
    }

    override fun onBindViewHolder(holder: UserItemAdapter.ViewHolder, position: Int) {
        val userItem = userList[position]
        Log.d("TAG333", "ccbindcc")
        holder.usernameOtherUserText.text = userItem.username
        holder.fullnameOtherUserText.text = userItem.fullname
        Picasso.get().load(userItem.profileimage).placeholder(R.drawable.ic_logo).into(holder.userProfileImage)
    }


    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var usernameOtherUserText: TextView = itemView.findViewById(R.id.other_username_textView)
        var fullnameOtherUserText: TextView = itemView.findViewById(R.id.other_fullname_textView)
        var userProfileImage: ImageView = itemView.findViewById(R.id.other_user_profile_image)
    }
}

