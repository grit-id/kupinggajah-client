package com.example.kgapp

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import org.json.JSONStringer
import tech.gusavila92.websocketclient.WebSocketClient
import java.lang.ref.WeakReference
import java.net.URI
import java.util.*
import kotlin.collections.ArrayList
import java.net.URISyntaxException as URISyntaxException1

public class MessageListActivity : AppCompatActivity(), MsgAsyncCallback {


    private lateinit var webSocketClient: WebSocketClient
    private lateinit var mMessagerv: RecyclerView
    private lateinit var mMessageAdapter: MessageListAdapter
    lateinit var messageList: ArrayList<UserMessage>
    private var js: String = ""
    private lateinit var send: String
    private lateinit var etBox: EditText
    private lateinit var btSend: Button
    private lateinit var currentUser : String
    private lateinit var targetUser : String


    override fun onPreExecute() {

    }

    override fun onPostExecute(text: String) {

        Log.d("A", text)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)

        etBox = findViewById(R.id.edittext_chatbox)

        btSend = findViewById(R.id.button_chatbox_send)


//        createWebSocketClient()

        val bundle : Bundle = intent.extras ?: return

        if(bundle!=null){
            println(bundle.getString("currentUser"))
            currentUser = bundle.getString("currentUser").toString()
            targetUser = bundle.get("targetUser").toString()
        } else {
            currentUser = "user"
            targetUser = "user"
        }


        mMessagerv = findViewById(R.id.reyclerview_message_list)
        mMessagerv.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?

        messageList = MessagesData.getListMessages()

//        msgAsync.execute(messageList)
        var time = Timer()
        var st = ScheduleTask(messageList, this)
        time.schedule(st, 0,2500)

        mMessageAdapter = MessageListAdapter(this, messageList, currentUser)
        mMessagerv.adapter = mMessageAdapter

        btSend.setOnClickListener {
            send = etBox.text.toString()
            if (send.equals("")) {
                Toast.makeText(this, "Isi dulu", Toast.LENGTH_SHORT).show()
//                if (messageList.size != MessagesData.getListMessages().size) {
//                    for (x in messageList.size until (MessagesData.getListMessages().size)) {
//                        println(messageList.size)
//                        println(MessagesData.getListMessages().size)
//                        messageList.add(MessagesData.getListMessages().get(x))
//                    }
//
//                }
//                mMessageAdapter.notifyDataSetChanged()

            } else {
                createWebSocketClient(send,currentUser,targetUser)
                etBox.text.clear()
//                Thread.sleep(300)
//                createWebSocketClient()

            }
        }
    }

//        var js: String = "{\"key\":\"UYIOJNJIOI\",\"src\":\"081804323015\",\"dest\":\"081804323015\",\"msg\":\"hai halo\",\"msg_id\":\"2348797345897\",\"msg_time\":\"2019-05-29-18:30\"}"


//        msg = arrayOf("nur","url","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.","created2","receiver")
//        MessagesData.addMessage(msg)

//----------------------------------------------------------------

    private inner class MsgAsync : AsyncTask<ArrayList<UserMessage>, Void, String> {
        val LOG_ASYNC: String = "MsgSync"
        var myListener: WeakReference<MsgAsyncCallback>


        constructor(myListener: MsgAsyncCallback) {
            this.myListener = WeakReference(myListener)
        }

        override fun onPreExecute() {
            super.onPreExecute()
            Log.d(LOG_ASYNC, "status : onPreExecute")

            val myListener = this.myListener.get()
            myListener?.onPreExecute()
        }

        override fun doInBackground(vararg p0: ArrayList<UserMessage>): String {
            Log.d(LOG_ASYNC, "status : doInBg")
            createWebSocketClient()
            Thread.sleep(1000)
            if (p0.size != MessagesData.getListMessages().size) {
                for (x in p0[0].size until (MessagesData.getListMessages().size)) {
                    println(p0[0].size)
                    println(MessagesData.getListMessages().size)
                    messageList.add(MessagesData.getListMessages().get(x))
                }
            }
            Thread.sleep(1000)
            return ""
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            Log.d(LOG_ASYNC, "status : onPostExecute")
            mMessageAdapter.notifyDataSetChanged()
            val myListener = this.myListener.get()
            myListener?.onPostExecute(result)
        }
    }

//---------------------------------------------------------------

    private inner class ScheduleTask : TimerTask{
        var list: ArrayList<UserMessage>
        var ctx: MsgAsyncCallback
        constructor(a: ArrayList<UserMessage>, b: MsgAsyncCallback){
            list = a
            ctx = b

        }

        override fun run() {
            MsgAsync(ctx).execute(list)
        }

    }


    private fun createWebSocketClient(msg: String, user: String, target: String) {

        val uri: URI
        try {
            uri = URI("wss://kupinggajah.grit.id/connect")
        } catch (e: URISyntaxException1) {
            e.printStackTrace()
            return
        }

        webSocketClient = object : WebSocketClient(uri) {
            override fun onOpen() {
                println("onOpen")
                Log.d("a","hello world")
//                webSocketClient.send("081804323015")
//                webSocketClient.send("{\"key\":\"UYIOJNJIOI\",\"src\":\"081804323015\",\"dest\":\"081804323016\",\"msg\":\""+ msg+"\",\"msg_id\":\"2348797345897\",\"msg_time\":\"2019-05-29-18:30\"}")
                webSocketClient.send("{\"key\":\"UYIOJNJIOI\",\"src\":\"$user\",\"dest\":\"$target\",\"msg\":\"$msg\",\"msg_id\":\"2348797345897\",\"msg_time\":\"2019-05-29-18:30\"}")
                var msgs = arrayOf(user,"url", msg,"19.00",target)
                MessagesData.addMessage(msgs)
            }

            override fun onTextReceived(message: String) {
                println("onTextReceived")
//                println(message)
//                js = message
//

                if(js.equals("")){
                    println("No message received")
                } else{
//                    var json = JSONObject(js.substring(1,js.length-1))
//                    println(json.get("key").toString())
//                    println(json.get("dest").toString())
//                    println(json.get("msg").toString())
//
////                    var msg = arrayOf("anas","url","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.","created","receiver")
//
//                    var msg = arrayOf(String.format("%s",json.get("src").toString()),String.format("%s","url"),String.format("%s",json.get("msg").toString()),String.format("%s",json.get("msg_time").toString()),String.format("%s",json.get("dest").toString()))
//
//                    MessagesData.addMessage(msg)
                }


            }

            override fun onBinaryReceived(data: ByteArray) {
                println("onBinaryReceived")
            }

            override fun onPingReceived(data: ByteArray) {
                println("onPingReceived")
                println(data)
            }

            override fun onPongReceived(data: ByteArray) {
                println("onPongReceived")
            }

            override fun onException(e: Exception) {
                println(e.message)
            }

            override fun onCloseReceived() {
                println("onCloseReceived")
            }


        }

        webSocketClient.setConnectTimeout(10000)
        webSocketClient.setReadTimeout(60000)
        webSocketClient.enableAutomaticReconnection(5000)
        webSocketClient.connect()
    }

    fun createWebSocketClient() {

        val uri: URI
        try {
            uri = URI("wss://kupinggajah.grit.id/ask")
        } catch (e: URISyntaxException1) {
            e.printStackTrace()
            return
        }

        webSocketClient = object : WebSocketClient(uri) {
            override fun onOpen() {
                println("onOpen")
                Log.d("a","hello world")
//                webSocketClient.send("081804323015")
                webSocketClient.send(currentUser)
//                webSocketClient.send("{\"key\":\"UYIOJNJIOI\",\"src\":\"081804323015\",\"dest\":\"081804323016\",\"msg\":\"hai halo\",\"msg_id\":\"2348797345897\",\"msg_time\":\"2019-05-29-18:30\"}")
//                webSocketClient.send(String.format("{\"key\":\"UYIOJNJIOI\",\"src\":\"081804323016\",\"dest\":\"081804323015\",\"msg\":\"%s\",\"msg_id\":\"2348797345897\",\"msg_time\":\"2019-05-29-18:30\"}",msg))
            }

            override fun onTextReceived(message: String) {
                println("onTextReceived")
                println(message)
                js = message


                if(js.equals("")){
                    println("No message received")
                } else{
                    var json = JSONObject(js.substring(1,js.length-1))
                    println(json.get("key").toString())
                    println(json.get("dest").toString())
                    println(json.get("msg").toString())

//                    var msg = arrayOf("anas","url","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.","created","receiver")

                    var msg = arrayOf(String.format("%s",json.get("src").toString()),String.format("%s","url"),String.format("%s",json.get("msg").toString()),String.format("%s",json.get("msg_time").toString()),String.format("%s",json.get("dest").toString()))

                    MessagesData.addMessage(msg)



                }


            }

            override fun onBinaryReceived(data: ByteArray) {
                println("onBinaryReceived")
            }

            override fun onPingReceived(data: ByteArray) {
                println("onPingReceived")
                println(data)
            }

            override fun onPongReceived(data: ByteArray) {
                println("onPongReceived")
            }

            override fun onException(e: Exception) {
                println(e.message)
            }

            override fun onCloseReceived() {
                println("onCloseReceived")
            }


        }

        webSocketClient.setConnectTimeout(10000)
        webSocketClient.setReadTimeout(60000)
        webSocketClient.enableAutomaticReconnection(5000)
        webSocketClient.connect()
    }
}

