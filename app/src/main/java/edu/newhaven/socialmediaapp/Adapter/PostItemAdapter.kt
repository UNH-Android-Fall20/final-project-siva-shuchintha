package edu.newhaven.socialmediaapp.Adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import edu.newhaven.socialmediaapp.Fragments.CurrentPostFragment
import edu.newhaven.socialmediaapp.Fragments.OtherUserProfileFragment
import edu.newhaven.socialmediaapp.R
import edu.newhaven.socialmediaapp.models.Comment
import edu.newhaven.socialmediaapp.models.Post
import java.text.SimpleDateFormat
import java.util.*


class PostItemAdapter (private var context: Context,
                       private var postList: List<Post>,
                       private var isFragment: Boolean = false) : RecyclerView.Adapter<PostItemAdapter.ViewHolder>() {

    private var CurrentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private val data = hashMapOf("value" to true)


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
       // holder.Title_post_textView.text = postItem.title
        Picasso.get().load(postItem.image).placeholder(R.drawable.ic_logo).into(holder.PostImage_imageView)
        UpdateLikeStatus(postItem,holder.Likes_post_button)

        holder.Likes_post_button.setOnClickListener {
            AddUserLiked(postItem,holder.Likes_post_button)
        }

        holder.save_comments_button.setOnClickListener {
            saveComment(postItem,holder.addComment_editText)
        }

//        holder.Comments_post_textView.setOnClickListener(View.OnClickListener {
//            val preference = context.getSharedPreferences("POST", Context.MODE_PRIVATE).edit()
//            preference.putString("PostID", postItem.postid)
//            preference.apply()
//
//            (context as FragmentActivity).supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, CurrentPostFragment()).commit()
//        })

        holder.itemView.setOnClickListener (View.OnClickListener {
            val preference = context.getSharedPreferences("POST", Context.MODE_PRIVATE).edit()
            preference.putString("PostID", postItem.postid)
            preference.apply()

            (context as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CurrentPostFragment()).commit()
        })
    }

    private fun saveComment(postItem: Post, addcommentEdittext: EditText) {
        if(addcommentEdittext.text.toString().isEmpty()){
            addcommentEdittext.error = "Comment missing!"
            addcommentEdittext.requestFocus()
            return
        }

        var userName = ""

        Firebase.firestore.collection("users").document(CurrentUser!!.uid)
            .get().addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("user name", document.data!!["username"].toString())
                    userName = document.data!!["username"].toString()
                    setCommentToFirestore(userName, addcommentEdittext, postItem)
                }else {
                    Log.d("post", "error finding doc")
                }
            }.addOnFailureListener { exception ->
                Log.d("post", "got failed with ", exception)
            }
    }

    private fun setCommentToFirestore(userName: String, addcommentEdittext: EditText, postItem: Post) {
        Log.d("USERNAMEHERE", userName.toString())
        val dNow = Date()
        val ft = SimpleDateFormat("yyMMddhhmmssMs")
        var timestamp = ft.format(dNow)
        Log.d("USERNAMEHERE", userName.toString())

        var comment = Comment(userName.toString(), addcommentEdittext.text.toString(),timestamp)
        val idcomment = timestamp + CurrentUser!!.uid
        Firebase.firestore.collection("posts")
            .document(postItem.postid)
            .collection("comments")
            .document(idcomment)
            .set(comment)
            .addOnCompleteListener {
                addcommentEdittext.text.clear()
            }
        Log.d("comment", "Comment successfull")
    }

    private fun AddUserLiked(postItem: Post, likesPostButton: ImageView) {
        if(likesPostButton.tag == 1){
            Firebase.firestore.collection("posts")
                .document(postItem.postid)
                .collection("likes")
                .document(CurrentUser!!.uid)
                .set(data)
        }else if(likesPostButton.tag == 0){
            Firebase.firestore.collection("posts")
                .document(postItem.postid)
                .collection("likes")
                .document(CurrentUser!!.uid)
                .delete()
        }
    }

    private fun UpdateLikeStatus(postItem: Post, likesPostButton: ImageView) {
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
                    likesPostButton?.setImageResource(R.drawable.ic_like_icon)
                    likesPostButton.setTag(1)
                    Log.d("postadapter", "profil33e111frag " + snapshot?.exists().toString())
                } else {
                    likesPostButton?.setImageResource(R.drawable.ic_unlike_icon)
                    likesPostButton.setTag(0)

                    Log.d("postadapter", "profil33efrag " + snapshot?.exists().toString())
                }
            }
    }


    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var otheruser_username_textView: TextView = itemView.findViewById(R.id.otheruser_username_textView)
      //  var Title_post_textView: TextView = itemView.findViewById(R.id.Title_post_textView)
        var PostImage_imageView: ImageView = itemView.findViewById(R.id.PostImage_imageView)
        var Likes_post_button: ImageView = itemView.findViewById(R.id.Likes_post_button)
        var addComment_editText: EditText = itemView.findViewById(R.id.addComment_editText)
        var save_comments_button: ImageView = itemView.findViewById(R.id.send_comment)
       // var Comments_post_textView: TextView = itemView.findViewById(R.id.Comments_post_textView)

    }
}
