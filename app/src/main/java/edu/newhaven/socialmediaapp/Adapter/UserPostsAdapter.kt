package edu.newhaven.socialmediaapp.Adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import edu.newhaven.socialmediaapp.Fragments.CurrentPostFragment
import edu.newhaven.socialmediaapp.R
import edu.newhaven.socialmediaapp.models.Comment
import edu.newhaven.socialmediaapp.models.Post
import java.text.SimpleDateFormat
import java.util.*

class UserPostsAdapter(private var context: Context,
                       private var userPostList: List<Post>,
                       private var isFragment: Boolean = false) : RecyclerView.Adapter<UserPostsAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPostsAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_post_item_profilepage, parent, false)
        Log.d("Userpostsadapter", "postsss")
        return UserPostsAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("Userpostsadapter", userPostList.size.toString())
        return userPostList.size
    }

    override fun onBindViewHolder(holder: UserPostsAdapter.ViewHolder, position: Int) {
        val postItem = userPostList[position]
        Log.d("Userpostsadapter", "postsss1")
        Picasso.get().load(postItem.image).placeholder(R.drawable.ic_logo).into(holder.UserPostImage_imageView)

        holder.itemView.setOnClickListener(View.OnClickListener {
            val preference = context.getSharedPreferences("POST", Context.MODE_PRIVATE).edit()
            preference.putString("PostID", postItem.postid)
            preference.apply()

            (context as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CurrentPostFragment()).commit()
        })
    }


    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var UserPostImage_imageView: ImageView = itemView.findViewById(R.id.UserPostImage_imageView)
    }
}
