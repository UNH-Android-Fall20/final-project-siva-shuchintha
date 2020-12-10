package edu.newhaven.socialmediaapp.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.newhaven.socialmediaapp.Fragments.OtherUserProfileFragment
import edu.newhaven.socialmediaapp.R
import edu.newhaven.socialmediaapp.models.Messages
import edu.newhaven.socialmediaapp.models.User

class ChatMessagesAdapter (private var context: Context,
                           private var messageList: List<Messages>,
                           private var isFragment: Boolean = false) : RecyclerView.Adapter<ChatMessagesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessagesAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chat_mesage_card, parent, false)
        Log.d("TAG333", "cchellocc")

        return ChatMessagesAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("TAG333", messageList.size.toString())
        return messageList.size
    }

    override fun onBindViewHolder(holder: ChatMessagesAdapter.ViewHolder, position: Int) {
        val messageItem = messageList[position]
        Log.d("TAG333", "ccbindcc")
        holder.message_username_textView.text = messageItem.username
        holder.message_textView.text = messageItem.message
    }


    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var message_username_textView: TextView = itemView.findViewById(R.id.message_username_textView)
        var message_textView: TextView = itemView.findViewById(R.id.message_textView)
    }
}
