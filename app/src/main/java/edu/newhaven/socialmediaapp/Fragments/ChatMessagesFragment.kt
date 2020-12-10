package edu.newhaven.socialmediaapp.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import edu.newhaven.socialmediaapp.Adapter.ChatMessagesAdapter
import edu.newhaven.socialmediaapp.R
import edu.newhaven.socialmediaapp.models.Comment
import edu.newhaven.socialmediaapp.models.Messages
import kotlinx.android.synthetic.main.fragment_chat_messages.*
import kotlinx.android.synthetic.main.fragment_chat_messages.view.*
import kotlinx.android.synthetic.main.fragment_current_post.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ChatMessagesFragment : Fragment() {
    private var OtherUser: String? = null
    private lateinit var CurrentUser: FirebaseUser
    private var messageList: MutableList<Messages>? = null
    private var chatMessagesAdapter: ChatMessagesAdapter? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_messages, container, false)
        OtherUser = arguments!!.getString("OtherUser")
        CurrentUser = FirebaseAuth.getInstance().currentUser!!
        recyclerView = view.findViewById(R.id.Chat_messages_RecyclerView)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        messageList = ArrayList()
        chatMessagesAdapter = context?.let { ChatMessagesAdapter(it, messageList as ArrayList<Messages>, true) }
        recyclerView?.adapter = chatMessagesAdapter
        FetchAllMessages()
        view.save_message_button.setOnClickListener {
            saveMessage(messageList as ArrayList<Messages>,addMessage_editText)
        }
        return view
    }


    private fun saveMessage(messageList: ArrayList<Messages>, addMessage_editText: EditText) {
        if(addMessage_editText.text.toString().isEmpty()){
            addMessage_editText.error = "Message missing!"
            addMessage_editText.requestFocus()
            return
        }
        var userName = ""
        Firebase.firestore.collection("users").document(CurrentUser!!.uid)
            .get().addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("user name", document.data!!["username"].toString())
                    userName = document.data!!["username"].toString()
                    setMessagesToFirestore(userName, addMessage_editText)
                }else {
                    Log.d("post", "error finding doc")
                }
            }.addOnFailureListener { exception ->
                Log.d("post", "got failed with ", exception)
            }
    }

    private fun setMessagesToFirestore(userName: String, addMessage_editText: EditText?) {
        Log.d("USERNAMEHERE", userName.toString())
        val dNow = Date()
        val ft = SimpleDateFormat("yyMMddhhmmssMs")
        var timestamp = ft.format(dNow)
        Log.d("USERNAMEHERE", userName.toString())

        var message = Messages(userName, addMessage_editText?.text.toString(),timestamp,CurrentUser.uid)
        val idMessage = timestamp + CurrentUser!!.uid + OtherUser.toString()
        Firebase.firestore.collection("users")
            .document(CurrentUser.uid)
            .collection("chats")
            .document(OtherUser.toString()).collection("messages")
            .document(idMessage)
            .set(message)
            .addOnCompleteListener {
                setMessagesToOtherUserFirestore(userName,addMessage_editText)
                addMessage_editText!!.text.clear()
            }
        Log.d("comment", "Comment successfull")
    }

    private fun setMessagesToOtherUserFirestore(userName: String, addMessage_editText: EditText?) {
        Log.d("USERNAMEHERE", userName.toString())
        val dNow = Date()
        val ft = SimpleDateFormat("yyMMddhhmmssMs")
        var timestamp = ft.format(dNow)
        Log.d("USERNAMEHERE", userName.toString())

        var message = Messages(userName, addMessage_editText?.text.toString(),timestamp,CurrentUser.uid)
        val idMessage = timestamp  + OtherUser.toString() + CurrentUser!!.uid
        Firebase.firestore.collection("users")
            .document(OtherUser.toString())
            .collection("chats")
            .document(CurrentUser.uid).collection("messages")
            .document(idMessage)
            .set(message)
            .addOnCompleteListener {
                FetchAllMessages()
            }
        Log.d("comment", "Comment successfull")
    }



    private fun FetchAllMessages() {
        Firebase.firestore.collection("users")
            .document(CurrentUser.uid).collection("chats").document(OtherUser.toString()).collection("messages")
            .get().addOnSuccessListener { documents ->
                Log.d("tagabc", "ddd " + documents!!.isEmpty())
                messageList?.clear()
                for (document in documents) {
                    Log.d("TAG22222", "${document.id} => ${document.data}")
                    val message = document.toObject<Messages>()
                    if (message != null )
                    {
                        messageList?.add(message)
                    }
                }
                chatMessagesAdapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }
    }

}