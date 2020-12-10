package edu.newhaven.socialmediaapp.Adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import edu.newhaven.socialmediaapp.R
import edu.newhaven.socialmediaapp.models.Comment
import edu.newhaven.socialmediaapp.models.Post

class CommentsAdapter (private var context: Context,
                       private var commentsList: List<Comment>,
                       private var isFragment: Boolean = false) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.post_comments_post, parent, false)
        Log.d("TAG333", "postsss")
        return CommentsAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("TAG333", commentsList.size.toString())
        return commentsList.size
    }

    override fun onBindViewHolder(holder: CommentsAdapter.ViewHolder, position: Int) {
        val commentItem = commentsList[position]
        Log.d("TAG333", "postsss1")
        holder.username_comments_textView.text = commentItem.username
        holder.comment_textView.text = commentItem.comment

    }

    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var username_comments_textView: TextView = itemView.findViewById(R.id.username_comments_textView)
        var comment_textView: TextView = itemView.findViewById(R.id.comment_textView)
    }
}