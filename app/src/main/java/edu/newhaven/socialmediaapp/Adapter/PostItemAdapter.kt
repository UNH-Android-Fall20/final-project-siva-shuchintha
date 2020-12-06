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
import edu.newhaven.socialmediaapp.models.Post


class PostItemAdapter (private var context: Context,
                       private var postList: List<Post>,
                       private var isFragment: Boolean = false) : RecyclerView.Adapter<PostItemAdapter.ViewHolder>() {

    private var CurrentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private val data = hashMapOf("value" to "true")


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostItemAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.postcard_homefrag, parent, false)
        Log.d("TAG333", "postsss")
        return PostItemAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("TAG333", postList.size.toString())
        return postList.size
    }

    override fun onBindViewHolder(holder: PostItemAdapter.ViewHolder, position: Int) {
        val postItem = postList[position]
        Log.d("TAG333", "postsss1")
        holder.otheruser_username_textView.text = postItem.username
        holder.Title_post_textView.text = postItem.title
        Picasso.get().load(postItem.image).placeholder(R.drawable.ic_logo).into(holder.PostImage_imageView)
        UpdateLikeStatus(postItem,holder.Likes_post_button)

    }

    private fun UpdateLikeStatus(postItem: Post, likesPostButton: Button) {
        Firebase.firestore.collection("posts")
            .document(postItem.postid)
            .collection("likes")
            .document(CurrentUser!!.uid)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.d("postadapter", "profil22efrag")
                    return@addSnapshotListener
                }
                Log.d("postadapter", "profil33efrag " + snapshot?.data.toString())

                if (snapshot != null && snapshot.exists()) {
                    likesPostButton?.text = "Liked"
                    likesPostButton.setBackgroundColor(Color.RED)
                    Log.d("postadapter", "profil33e111frag " + snapshot?.exists().toString())
                } else {
                   likesPostButton?.text = "Like"
                    Log.d("postadapter", "profil33efrag " + snapshot?.exists().toString())
                }
            }
    }


    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var otheruser_username_textView: TextView = itemView.findViewById(R.id.otheruser_username_textView)
        var Title_post_textView: TextView = itemView.findViewById(R.id.Title_post_textView)
        var PostImage_imageView: ImageView = itemView.findViewById(R.id.PostImage_imageView)
        var Likes_post_button: Button = itemView.findViewById(R.id.Likes_post_button)
    }
}
