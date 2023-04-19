package com.example.instacloneapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.*
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.instacloneapp.databinding.ActivityMainBinding
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.roundToInt


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    /////////////

    private lateinit var commentsLayout: LinearLayout;
    private lateinit var viewersLayout: LinearLayout;
    private lateinit var commentContainer: LinearLayout;
    lateinit var username:TextView;
    private val commentsList = mutableListOf<Comment>()

    val random = Random()
    ///////////////////////////////
    val avatarsRus = ArrayList<Drawable>()
    private lateinit var blackBottom:RelativeLayout;
    private lateinit var bottomBar:RelativeLayout;
    private lateinit var revIcon: ImageView;
    private lateinit var vidIcon: ImageView;
    private lateinit var avatarUser: ImageView;
    private lateinit var avatarCenter: ImageView;
    private lateinit var microIcon: ImageView;
    private lateinit var commentsField:EditText;
    private lateinit var commentsF:EditText;
    private lateinit var commentFieldHelper:ImageView;
    private lateinit var publicField:RelativeLayout;
    private lateinit var publicateButton:TextView;
    private lateinit var x:ImageView;
    lateinit var previewV: PreviewView;
    lateinit var countViews:TextView;
    private lateinit var imgForComment:ImageView;
    lateinit var greyLayout:RelativeLayout
    var a =0;
    var boolean=true;
    var booleanmicro=true;
    var frontalBackCameraCounter:Int=0;
    var clicked: Int=0;
    private lateinit var binding: ActivityMainBinding;
    private lateinit var cameraExecutor: ExecutorService;
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var live:TextView
lateinit var whoViewLayoutTop: RelativeLayout
    lateinit var whoViewLayout: RelativeLayout
    var listUniqueComment= mutableListOf<String>()
    var listUniqueNick= mutableListOf<String>()
    val bitmapArray = ArrayList<Bitmap>()
    var boolList = mutableListOf<Boolean>()
    lateinit var view:RelativeLayout
    lateinit var whoViewAbove:RelativeLayout
    @SuppressLint("WrongViewCast", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater,null,false);
        setContentView(binding.root)
//        setContentView(R.layout.activity_main)
        cameraExecutor = Executors.newSingleThreadExecutor()
        requestPermission()
        username=findViewById(R.id.username)
        avatarUser=findViewById(R.id.avataruser)
        avatarCenter=findViewById(R.id.avataruserCenter)
        countViews=findViewById(R.id.countviews)
        blackBottom=findViewById(R.id.blackBottom)
        bottomBar=findViewById(R.id.bottomBar)
        publicField=findViewById(R.id.publicfield)
        commentsField=findViewById(R.id.comment_field)
        commentsF=findViewById(R.id.comment_f)
        commentFieldHelper=findViewById(R.id.comment_field_helper)
        live=findViewById(R.id.live)
        view=findViewById(R.id.views)
        publicateButton=findViewById(R.id.publicateButton)
        whoViewAbove=findViewById(R.id.whoViewAbove)
    whoViewLayoutTop=findViewById(R.id.whoviewLayoutTop)
whoViewLayout=findViewById(R.id.whoviewLayout)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        /////////////////////////////////////////////

        whoViewLayout.visibility=View.GONE
whoViewLayoutTop.setOnClickListener{
    whoViewLayout.visibility=View.VISIBLE
}
        view.setOnClickListener{
            whoViewLayout.visibility=View.VISIBLE
        }
whoViewAbove.setOnClickListener{
    whoViewLayout.visibility=View.GONE
}

        ///////////////////////////////////////////
        val sharedPrefLang = getSharedPreferences("lang", Context.MODE_PRIVATE)
        val savedEditTextValueLang = sharedPrefLang.getString("edit_text_value", "")


        if(savedEditTextValueLang==""){

            live.text="Прямой эфир"
            commentsField.hint="Добавьте комментарий..."
            commentsF.hint="Добавьте комментарий..."
            publicateButton.text="Отправить"

        }else {
            if (savedEditTextValueLang == "РУ") {
                commentsF.hint="Добавьте комментарий..."
                live.text="Прямой эфир"
                commentsField.hint="Добавьте комментарий..."
                publicateButton.text="Отправить"
            } else {
                commentsF.hint="Add a comment"
                live.text="Live"
                commentsField.hint="Add a comment"
                publicateButton.text="Post"
            }
        }


        Log.e(TAG,bitmapArray.toString())


        val sharedPrefsB = getSharedPreferences("bitmapList", Context.MODE_PRIVATE)
        val count = sharedPrefsB.getInt("bitmapCount", 0)
        val bitmapArrayB = ArrayList<Bitmap>()
        for (i in 0 until count) {
            val path = sharedPrefsB.getString("bitmap_$i", null)
            if (path != null) {
                val file = File(path)
                if (file.exists()) {
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    bitmapArrayB.add(bitmap)
                }
            }
        }

        bitmapArray.addAll(bitmapArrayB)

// Retrieve the JSON string from SharedPreferences
        val sharedPreferencesUnique = getSharedPreferences("UniqueNick", Context.MODE_PRIVATE)
        val jsonStringUnique = sharedPreferencesUnique.getString("UniqueNick", null)

// Convert the JSON string back to a MutableList using Gson
        val gsonUnique = Gson()
        if (jsonStringUnique==null) {
            Log.e(TAG,"okkk")
        } else {

            listUniqueNick =
                gsonUnique.fromJson(jsonStringUnique, object : TypeToken<MutableList<String>>() {}.type)

            Log.e(TAG, listUniqueNick.toString())
// Display the MutableList in a TextView
        }

        // Retrieve the JSON string from SharedPreferences
        val sharedPreferencesUniqueCom = getSharedPreferences("UniqueComment", Context.MODE_PRIVATE)
        val jsonStringUniqueCom = sharedPreferencesUniqueCom.getString("UniqueComment", null)

// Convert the JSON string back to a MutableList using Gson
        val gsonUniqueCom = Gson()
        if (jsonStringUniqueCom==null) {
            Log.e(TAG,"okkk")
        } else {

            listUniqueComment =
                gsonUniqueCom.fromJson(jsonStringUniqueCom, object : TypeToken<MutableList<String>>() {}.type)

            Log.e(TAG, listUniqueComment.toString())
// Display the MutableList in a TextView
        }


        // Get shared preferences object
        val sharedPrefsBool = this.getSharedPreferences("myPrefsBool", Context.MODE_PRIVATE)

// Retrieve boolean list from shared preferences
        val jsonBool = sharedPrefsBool.getString("boolListKey", null)
        val gsonBool = Gson()
        if(jsonBool==null) {
        }else{
            boolList = gsonBool.fromJson(jsonBool, object : TypeToken<List<Boolean>>() {}.type)
        }


        /////////////////////////////////////////
        ///////////////////////////////////////


        var list = mutableListOf<String>()
        val gson = Gson()
// Retrieve the JSON string from SharedPreferences
        val sharedPreferencesNick = getSharedPreferences("nick", Context.MODE_PRIVATE)
        val jsonString = sharedPreferencesNick.getString("nick", null)
        if(jsonString==null){
            Log.e(TAG,"json is null")
        }else {
            list = gson.fromJson(jsonString, object : TypeToken<MutableList<String>>() {}.type)
        }


        var listComment = mutableListOf<String>()
        val gsonComment = Gson()

        val sharedPreferencesComment = getSharedPreferences("comment", Context.MODE_PRIVATE)
        val jsonStringComment = sharedPreferencesComment.getString("comment", null)

        if(jsonStringComment==null){
            Log.e(TAG,"json is null")
        }else {
            listComment = gsonComment.fromJson(jsonStringComment, object : TypeToken<MutableList<String>>() {}.type)
        }
        Log.e(TAG,list.toString())
        Log.e(TAG,listComment.toString())
        commentFieldHelper.setOnClickListener{
            commentsF.requestFocus();
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(commentsF, InputMethodManager.SHOW_IMPLICIT)
            clicked=1;
            if(clicked==1){
               bottomBar.alpha=0F;
                publicField.alpha=1F;
                blackBottom.alpha=1F;

                val layoutParams = publicField.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.bottomMargin = 15 // set the bottom margin to 15 pixels
                publicField.layoutParams = layoutParams


            }


        }
        // Устанавливаем сохраненное значение в EditText
        val smsCountShPref = getSharedPreferences("sms_count", MODE_PRIVATE)
        val smsSavedValue = smsCountShPref.getString("edit_text_value", "")
        val oneMsgTime:Double;
        var countOfsms =10.0
        // Устанавливаем сохраненное значение в EditText
        if(smsSavedValue!=""){
            countOfsms = smsSavedValue?.toDouble()!!
             oneMsgTime = 60/ countOfsms!!
        }else{
            val countOfsms = 10
            oneMsgTime = (60/ countOfsms!!).toDouble()
        }





        val sharedPref = getSharedPreferences("my_prefs", MODE_PRIVATE)
        val savedEditTextValue = sharedPref.getString("edit_text_value", "")
        if(savedEditTextValue==""){
            username.setText("username")
        }else {
            username.setText(savedEditTextValue)
        }
        val sharedPrefViews = getSharedPreferences("subs_number", MODE_PRIVATE)
        val savedEditTextValueViews = sharedPrefViews.getString("edit_text_value", "")
        var m:Double
        if(savedEditTextValueViews!=""){
            m = savedEditTextValueViews?.toDouble()!!
            countViews.setText(savedEditTextValueViews)
        }else{
            m=30.0
            countViews.setText("30")
        }


        if((m*10/100)> countOfsms!!  ||  (m*60/100)<countOfsms!!){
            if(savedEditTextValueLang=="РУ") {
                Toast.makeText(this, "Установленные параметры не правдоподобны!", Toast.LENGTH_LONG)
                    .show()
            }else{

                Toast.makeText(this, "The set parameters are not plausible!", Toast.LENGTH_LONG)
                    .show()
            }
        }


        val sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)
        val imagePath = sharedPreferences.getString("imagePath", null)
        val bitmap = BitmapFactory.decodeFile(imagePath)
        if (imagePath != null) {


            avatarUser.setImageBitmap(bitmap)
            avatarCenter.setImageBitmap(bitmap)
        }






        ///////////////////
        commentsLayout = findViewById(R.id.commentsLayout)
        viewersLayout=findViewById(R.id.comLayoutx)
//
//        commentContainer=findViewById(R.id.commentContainer)
        val handler = Handler(Looper.getMainLooper());

        handler.postDelayed(object : Runnable {
            override fun run() {
                addRandomComment()
                handler.postDelayed(this, (oneMsgTime*1000).toLong()) // repeat every 1 seconds
            }
        }, 1000) // start after 1 seconds

        val handlerJoined = Handler(Looper.getMainLooper());

        handlerJoined.postDelayed(object : Runnable {
            @SuppressLint("NewApi")
            override fun run() {
                addRandomCommentJoined()
                handler.postDelayed(this, (oneMsgTime*10000).toLong()) // repeat every 10 seconds
            }
        }, 1000) // start after 1 seconds

        // Get the SharedPreferences object
        val sharedPrefsSwitchBool = getSharedPreferences("switchBool", Context.MODE_PRIVATE)

// Retrieve the boolean value
        var switchBool = sharedPrefsSwitchBool.getBoolean("myBoolean", false)

 if(switchBool){
     val handlerUnique = Handler(Looper.getMainLooper());

     handlerUnique.postDelayed(object : Runnable {
         var index:Int=0
         override fun run() {

             if(index<bitmapArray.size) {
                 addUniqueComment(index)
                 index++
             }

             handler.postDelayed(this, (oneMsgTime*33000).toLong()) // repeat every 33 seconds
         }
     }, 13000) // start after 13 seconds

 }
        // add some example comments






        var nicknames = arrayOf("oliver", "charlotte", "william", "ava", "james", "sophia", "benjamin", "amelia", "lucas", "mia", "henry", "evelyn", "alexander", "harper", "michael", "abigail", "ethan", "emily", "daniel", "elizabeth", "matthew", "ella", "jackson", "mila", "david", "avery", "joseph", "sofia", "samuel", "victoria")

        var messages = arrayOf("Great post!", "Thanks for sharing!", "Awesome content!", "Love this!", "Keep it up!","Hello","Hi","How are you?","Where are you?","What are you doing?","Can you help me?", "Love you","Do you know me", "Nice to meet you","What's up?","Wow, stunning picture!","Your posts are always so inspiring!","This is such a beautiful place, I need to visit it someday.","I love the creativity of your content!","That's such an interesting perspective, I never thought about it that way.","Keep up the good work!","I always look forward to your posts.","You're so talented, I'm in awe!","You have such a unique style, I love it.","Your creativity is truly inspiring.","Your positive energy is contagious.","You are such an inspiration to others.","Your authenticity is what sets you apart, keep being you!")
        val avatars =  ArrayList<Drawable>()
        avatars.add(resources.getDrawable(R.drawable.e1))
        avatars.add(resources.getDrawable(R.drawable.e2))
        avatars.add(resources.getDrawable(R.drawable.e13))
        avatars.add(resources.getDrawable(R.drawable.e4))
        avatars.add(resources.getDrawable(R.drawable.e5))
        avatars.add(resources.getDrawable(R.drawable.e6))
        avatars.add(resources.getDrawable(R.drawable.e7))
        avatars.add(resources.getDrawable(R.drawable.e8))
        avatars.add(resources.getDrawable(R.drawable.e9))
        avatars.add(resources.getDrawable(R.drawable.e10))
        avatars.add(resources.getDrawable(R.drawable.e11))
        avatars.add(resources.getDrawable(R.drawable.e12))
        avatars.add(resources.getDrawable(R.drawable.e14))
        avatars.add(resources.getDrawable(R.drawable.e15))
        avatars.add(resources.getDrawable(R.drawable.e16))
        avatars.add(resources.getDrawable(R.drawable.e24))
        avatars.add(resources.getDrawable(R.drawable.e20))
        avatars.add(resources.getDrawable(R.drawable.e21))


        var nikRus= arrayOf("alenka_98", "stasik2003", "natashka_25", "maximilian_007", "misha_king", "elena_flower", "nikita_fox", "larisa_lioness", "dmitry_phoenix", "valery_sky", "nina_butterfly", "andrei_moon", "svetlana_star", "kirill_dark", "viktoriya_rainbow", "artem_tiger", "tanya_dreamer", "alexey_eagle", "anna_beauty", "ivan_iceberg", "maria_mystic", "vladimir_wolf", "olga_smile", "ruslan_fire", "eva_angel", "boris_thunder", "dasha_sunshine", "roman_dream", "anastasia_queen", "denis_silver")


        var msgRus= arrayOf("Привет "+savedEditTextValue,"Ого, какая красота!", "Мне нравится ваше шоу!", "Как же я соскучился по вашим эфирам!", "Вы сегодня выглядите потрясающе!", "Смотрю вас уже несколько лет и не могу налюбоваться!", "Очень интересный контент, продолжайте в том же духе!", "Какой у вас микрофон? Очень четкий звук!", "Все ваши гости такие умные и образованные!", "Это лучшее, что я видел на протяжении всего дня!", "Я ждал этот эфир целый день!", "Вы меня очень вдохновляете!", "Так интересно слушать вас!", "Какая у вас камера? Качество картинки на высоте!", "Мне очень нравится ваше чувство юмора!", "Вы всегда поднимаете мне настроение!", "Ваш контент стал для меня настоящей находкой!", "Как вы успеваете делать все эти проекты?", "Продолжайте радовать нас своими эфирами!", "Ваше мнение для меня очень важно!", "С нетерпением жду вашего следующего эфира!", "Я смотрю вас каждый день и не могу остановиться!", "Какой у вас талант, так много знаний и умений!", "Очень хороший выбор музыки в фоне!", "Вы выглядите очень естественно и раскованно на камеру!", "Продолжайте так же, вы просто молодцы!", "Мне очень нравится, как вы подаете информацию!", "Ваши эфиры помогают мне забыть обо всем на свете!", "Как же я люблю ваши стримы!", "Вы такой мастер своего дела!", "Смотрю вас уже с первого эфира и не могу надышаться!")

        avatarsRus.add(resources.getDrawable(R.drawable.e1))
        avatarsRus.add(resources.getDrawable(R.drawable.e2))
        avatarsRus.add(resources.getDrawable(R.drawable.e13))
        avatarsRus.add(resources.getDrawable(R.drawable.e4))
        avatarsRus.add(resources.getDrawable(R.drawable.e5))
        avatarsRus.add(resources.getDrawable(R.drawable.e6))
        avatarsRus.add(resources.getDrawable(R.drawable.e7))
        avatarsRus.add(resources.getDrawable(R.drawable.e8))
        avatarsRus.add(resources.getDrawable(R.drawable.e9))
        avatarsRus.add(resources.getDrawable(R.drawable.e10))
        avatarsRus.add(resources.getDrawable(R.drawable.e11))
        avatarsRus.add(resources.getDrawable(R.drawable.e12))
        avatarsRus.add(resources.getDrawable(R.drawable.e14))
        avatarsRus.add(resources.getDrawable(R.drawable.e15))
        avatarsRus.add(resources.getDrawable(R.drawable.e16))
        avatarsRus.add(resources.getDrawable(R.drawable.e24))
        avatarsRus.add(resources.getDrawable(R.drawable.e20))
        avatarsRus.add(resources.getDrawable(R.drawable.e21))


        if(savedEditTextValueLang==""){
            nikRus=nikRus+list.toTypedArray()
            Log.e(TAG, nikRus.toMutableList().toString())
            msgRus=msgRus+listComment.toTypedArray()
            Log.e(TAG, msgRus.toMutableList().toString())

            val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/InstagramLive")
            val avatarDir = ArrayList<Drawable>()

            if (dir.exists() && dir.isDirectory) {
                for (file in dir.listFiles()) {
                    if (file.isFile && file.extension == "jpg") {
                        val drawable = Drawable.createFromPath(file.absolutePath)
                        avatarDir.add(drawable!!)
                    }
                }
            }
        avatarsRus.addAll(avatarDir)


        }else{
            if(savedEditTextValueLang=="РУ"){
                nikRus=nikRus+list.toTypedArray()
                Log.e(TAG, nikRus.toMutableList().toString())
                msgRus=msgRus+listComment.toTypedArray()
                Log.e(TAG, msgRus.toMutableList().toString())

                val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/InstagramLive")
                val avatarDir = ArrayList<Drawable>()

                if (dir.exists() && dir.isDirectory) {
                    for (file in dir.listFiles()) {
                        if (file.isFile && file.extension == "jpg") {
                            val drawable = Drawable.createFromPath(file.absolutePath)
                            avatarDir.add(drawable!!)
                        }
                    }
                }
                avatarsRus.addAll(avatarDir)



            }else{
                nicknames=nicknames+list.toTypedArray()
                Log.e(TAG, nicknames.toMutableList().toString())
                messages=messages+listComment.toTypedArray()
                Log.e(TAG, messages.toMutableList().toString())

                val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/InstagramLive")
                val avatarDir = ArrayList<Drawable>()

                if (dir.exists() && dir.isDirectory) {
                    for (file in dir.listFiles()) {
                        if (file.isFile && file.extension == "jpg") {
                            val drawable = Drawable.createFromPath(file.absolutePath)
                            avatarDir.add(drawable!!)
                        }
                    }
                }
                avatars.addAll(avatarDir)




            }
        }



        val usedMessages = mutableListOf<String>()

        fun getRandomMessage(): String {
            if (usedMessages.size == messages.size) {
                usedMessages.clear() // reset used messages if all have been used
            }
            var message = messages.random()
            while (usedMessages.contains(message)) {
                message = messages.random()
            }
            usedMessages.add(message)
//            Log.e(TAG,message)
            return message

        }

        val usedMessagesRus = mutableListOf<String>()

        fun getRandomMessageRus(): String {
            if (usedMessagesRus.size == msgRus.size) {
                usedMessagesRus.clear() // reset used messages if all have been used
            }
            var message = msgRus.random()
            while (usedMessages.contains(message)) {
                message = msgRus.random()
            }
            usedMessagesRus.add(message)
//            Log.e(TAG,message)
            return message

        }
if(savedEditTextValueLang==""){
    for (i in 1..30) { // Generate 10 comments

        val nickname = nikRus.random()
        val message = getRandomMessageRus()
        val avatar = avatarsRus.random()
        commentsList.add(Comment(nickname, message, avatar))
    }
    }else {
    if (savedEditTextValueLang == "EN") {

        for (i in 1..30) { // Generate 10 comments

            val nickname = nicknames.random()
            val message = getRandomMessage()
            val avatar = avatars.random()
            commentsList.add(Comment(nickname, message, avatar))


        }
    } else {
        for (i in 1..30) { // Generate 10 comments

            val nickname = nikRus.random()
            val message = getRandomMessageRus()
            val avatar = avatarsRus.random()
            commentsList.add(Comment(nickname, message, avatar))


        }
    }
}


        val PercentShPref = getSharedPreferences("percent", MODE_PRIVATE)
        val PercentValue = PercentShPref.getString("edit_text_value", "")

        // Устанавливаем сохраненное значение в EditText
        var k = 0;
        var n:Double;
        var kf:Double;
        if(PercentValue==""){
             kf = 15!!.toDouble() / 100
            n = 400 * kf / m!!
        }else {

             kf = PercentValue!!.toDouble() / 100
            n = 400 * kf / m!!
        }

        // Simulation Views
        val handlerSimulationView = Handler(Looper.getMainLooper());

        handlerSimulationView.postDelayed(object : Runnable {
            override fun run() {

                if(k==0) {
                    m = m?.times(n+(50*kf))?.div(100)?.let { m?.plus(it) }!!

                    k=1;
                }else if(k==1){
                    m = m?.times(n+40*kf)?.div(100)?.let { m?.minus(it) }!!
                    k=0
                }
                Log.e(TAG,m.toString())
                if(m!! >1000 && m!!<1000000){
                    var o =(m!! - m!! %100)/1000
                    countViews.setText(o?.toDouble().toString()+"k")
                }else if(m!!>1000000){
                    var o =(m!! - m!! %100000)/1000000
                    countViews.setText(o?.toDouble().toString()+"M")
                }
                else{
                    countViews.setText(m?.roundToInt().toString())
                }

                handler.postDelayed(this, (60000/ (m?.times(0.5)?.div(0.025* m!!)!!)).toLong()) // repeat every 1 seconds
            }
        }, 5000) // start after 1 seconds



        /////////////////////
        x=findViewById(R.id.x);
        avatarCenter=findViewById(R.id.avataruserCenter);
        previewV =findViewById(R.id.preview);


        x.setOnClickListener{
            val intent = Intent(this, start_activity::class.java)
            startActivity(intent)
            finish();
        }
        /// No micro
        microIcon=findViewById(R.id.micro)
        microIcon.setOnClickListener{
            if(booleanmicro==true){
                microIcon.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.nomicro))
                booleanmicro=false;
            }
            else{
                microIcon.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.micro))
                booleanmicro=true;
            }
        }


        publicateButton=findViewById(R.id.publicateButton)
        publicateButton.setOnClickListener{

                val newCommentText = commentsF.text.toString()
                if (newCommentText.isNotEmpty()) {
                    val commentView = layoutInflater.inflate(R.layout.activity_comment, null)
                    commentView.findViewById<TextView>(R.id.nickTextView).text = savedEditTextValue;
                    commentView.findViewById<TextView>(R.id.commentTextView).text = newCommentText
                    commentView.findViewById<ImageView>(R.id.avatarImageView).setImageBitmap(bitmap)
                    commentsLayout.addView(commentView)





                    // scroll to the bottom of the scrollview
                    val scrollView: ScrollView = findViewById(R.id.commentScrollView)
                    scrollView.postDelayed({
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN)
                    }, 100)

                    // clear the edit text
                    commentsF.text.clear()
                    Log.e(TAG,""+commentView)
                }
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

            // Скрываем клавиатуру
            imm.hideSoftInputFromWindow(publicateButton.windowToken, 0)

            val layoutParams = publicField.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.bottomMargin = -80 // set the bottom margin to 15 pixels
            publicField.layoutParams = layoutParams
            bottomBar.alpha=1F;
            publicField.alpha=0F;
            blackBottom.alpha=0F;
            clicked=0;
        }





//////////// No video
        vidIcon=findViewById(R.id.vid);
        vidIcon.setOnClickListener{


            startCamera();
            if(boolean==true){
            vidIcon.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.novid))
            boolean=false;
                previewV.alpha= 0F;
                avatarCenter.alpha=1F;
            }
            else{
                vidIcon.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.vid))
                boolean=true;
                previewV.alpha=1F;
                avatarCenter.alpha=0F;
            }
        }
        ////////Поменять камеру
        revIcon = findViewById(R.id.rev);
        revIcon.setOnClickListener{
            startCamera();
            frontalBackCameraCounter+=1;
            Log.e(TAG,""+frontalBackCameraCounter)
            a=frontalBackCameraCounter%2;
            Log.e(TAG,""+a)
        }




        val sourceLayout = layoutInflater.inflate(R.layout.avanickclick, null)
        viewersLayout=sourceLayout.findViewById(R.id.comLayoutx)
        val userName = sourceLayout.findViewById<TextView>(R.id.LiveNick)
        userName.text=savedEditTextValue
        val livetextShtor = sourceLayout.findViewById<TextView>(R.id.livetextshtor)
        livetextShtor.text="Прямой эфир "+savedEditTextValue
        val commentTextViewx = sourceLayout.findViewById<TextView>(R.id.commentTextView)
        commentTextViewx.text=savedEditTextValue+" ·Организатор"
        val avatarImageView = sourceLayout.findViewById<ShapeableImageView>(R.id.avatarImageView)
        avatarImageView.setImageBitmap(bitmap)
        val targetLayout = findViewById<RelativeLayout>(R.id.full)
        addViewers()
        targetLayout.removeAllViews()

        targetLayout.addView(sourceLayout)

    }

    private fun requestPermission() {
      requestCameraPermissionIfMissing{ granted ->
          if(granted)
              startCamera()
          else
              Toast.makeText(this,"Please Allow the Permission",Toast.LENGTH_SHORT).show();
      }
    }
    private fun requestCameraPermissionIfMissing(onResult:((Boolean)-> Unit)){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED)
            onResult(true)
        else
            registerForActivityResult(ActivityResultContracts.RequestPermission()){
                onResult(it)
            }.launch(Manifest.permission.CAMERA)
    }
    fun startCamera(){
        val processCameraProvider = ProcessCameraProvider.getInstance(this)
        processCameraProvider.addListener({
            try {
                val cameraProvider = processCameraProvider.get();
                val previewUseCase=buildPreviewUseCase();
                cameraProvider.unbindAll();
                if(boolean==true){
                if(a==0){
                cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_FRONT_CAMERA,previewUseCase);}
                else{
                    cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA,previewUseCase);
                }}else{
                cameraProvider.unbindAll();}
            }catch (e: Exception){
                Log.e("ERROR",e.message.toString() )
                Toast.makeText(this,"Error starting the camera", Toast.LENGTH_SHORT).show();
            }


        },ContextCompat.getMainExecutor(this))


    }
    fun buildPreviewUseCase(): Preview {
        return Preview.Builder().build().also { it.setSurfaceProvider(binding.preview.surfaceProvider);}

    }
    @SuppressLint("MissingInflatedId")
    private fun addUniqueComment(int:Int) {

         val commentsList = mutableListOf<Comment>()
        val commentView = layoutInflater.inflate(R.layout.activity_comment, null)



        for(i in 0 until bitmapArray.size){
            val bitmap =bitmapArray[i]
            val drawable = BitmapDrawable(resources, bitmap)

            commentsList.add(Comment(listUniqueNick[i], listUniqueComment[i], drawable))
        }


            val comment = commentsList[int]
            var bool = boolList[int]
        if(bool){
            commentView.findViewById<ImageView>(R.id.verification).alpha=1F;
        }
            commentView.findViewById<TextView>(R.id.nickTextView).text = comment.nickname
            commentView.findViewById<TextView>(R.id.commentTextView).text = comment.text
            commentView.findViewById<ImageView>(R.id.avatarImageView)
                .setImageDrawable(comment.avatar)



            commentsLayout.addView(commentView)


        // scroll to the bottom of the scrollview

        val scrollView: ScrollView = findViewById(R.id.commentScrollView)
        scrollView.postDelayed({
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }, 100)

    }
    private fun addRandomComment() {
        val randomIndex = random.nextInt(commentsList.size)
        val comment = commentsList[randomIndex]

        val commentView = layoutInflater.inflate(R.layout.activity_comment, null)
        commentView.findViewById<TextView>(R.id.nickTextView).text = comment.nickname
        commentView.findViewById<TextView>(R.id.commentTextView).text = comment.text
        commentView.findViewById<ImageView>(R.id.avatarImageView).setImageDrawable(comment.avatar)



        commentsLayout.addView(commentView)


        // scroll to the bottom of the scrollview

        val scrollView: ScrollView = findViewById(R.id.commentScrollView)
        scrollView.postDelayed({
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }, 100)

    }
    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("MissingInflatedId")
    private fun addRandomCommentJoined() {
        val randomIndex = random.nextInt(commentsList.size)
        val comment = commentsList[randomIndex]
        val sharedPrefLang = getSharedPreferences("lang", Context.MODE_PRIVATE)
        val savedEditTextValueLang = sharedPrefLang.getString("edit_text_value", "")


        val commentView = layoutInflater.inflate(R.layout.activity_comment, null)
        commentView.findViewById<TextView>(R.id.nickTextView).text = comment.nickname
        if(savedEditTextValueLang==""){
        commentView.findViewById<TextView>(R.id.commentTextView).text = "Присоединился(-ась)"}
        else{
            if(savedEditTextValueLang=="РУ"){
                commentView.findViewById<TextView>(R.id.commentTextView).text = "Присоединился(-ась)"
            }else{
                commentView.findViewById<TextView>(R.id.nickTextView).text=comment.nickname+" joined"
            }
        }
        commentView.findViewById<ImageView>(R.id.avatarImageView).setImageDrawable(comment.avatar)
        if(savedEditTextValueLang=="") {
            commentView.findViewById<TextView>(R.id.hiBtn)
        }else{
            if(savedEditTextValueLang=="РУ"){
                commentView.findViewById<TextView>(R.id.hiBtn).setText("\uD83D\uDC4B Помахать")
            }else{
                commentView.findViewById<TextView>(R.id.hiBtn).setText("\uD83D\uDC4B Wave")
            }
        }
        commentView.findViewById<RelativeLayout>(R.id.hiLay).alpha=0.5f
        commentView.findViewById<TextView>(R.id.hiBtn).alpha=1f



        commentsLayout.addView(commentView)


        // scroll to the bottom of the scrollview

        val scrollView: ScrollView = findViewById(R.id.commentScrollView)
        scrollView.postDelayed({
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }, 100)

    }
    data class Comment(val nickname: String, val text: String, val avatar: Drawable)


@SuppressLint("MissingInflatedId", "SuspiciousIndentation")
private fun addViewers(){

for(i in 0 until commentsList.size) {

    val comment = commentsList[i]
    val commentView = layoutInflater.inflate(R.layout.activity_viewers, null)
    commentView.findViewById<TextView>(R.id.commentTextView).text = comment.nickname
    commentView.findViewById<ImageView>(R.id.avatarImageView).setImageDrawable(comment.avatar)
    viewersLayout.addView(commentView)
}
}
    override fun onDestroy() {
        super.onDestroy()

        // Clear the flag when the activity is destroyed
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }


}



