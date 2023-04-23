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
import android.net.Uri
import android.os.*
import android.provider.MediaStore
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
import androidx.core.view.marginTop
import com.example.instacloneapp.databinding.ActivityMainBinding
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.IOException
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
    lateinit var dirx:File
    lateinit var dir:File
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
        dir =
            File(cacheDir.path + "/InstagramLive")
        dirx= File(cacheDir.path+"/InstaUnique")
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


        val bitmapArrayB = ArrayList<Bitmap>()
        val fileListx = dirx.listFiles()
        val uriListx = ArrayList<Uri>()

        if (fileListx != null) {
            for (file in fileListx) {
                val uri = Uri.fromFile(file)
                uriListx.add(uri)
            }

            for (uri in uriListx) {
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    bitmapArrayB.add(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
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
Log.e(TAG,"unqiue")
             Log.e(TAG,bitmapArray.size.toString())
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

        var messages = arrayOf("Hi "+savedEditTextValue, "How was your day?", "What topics would you be interested in discussing in future live broadcasts?", "How did you find your passion for blogging?", "What camera do you use for filming?", "What is your secret to success on Instagram?", "What are your plans for the near future?", "What hobbies do you have besides blogging?", "What is your favorite video format?", "What do you think about collaborating with other bloggers?", "What did you do over the weekend?", "How do you keep your channel in good shape?", "What advice can you give to beginners on Instagram?", "How do you come up with content for your channel?", "How do you find time to create content for your channel?", "Long time no see! How have you been?", "How do you deal with stress while creating content?", "What projects are you planning in the near future?", "How do you handle criticism from your viewers?", "How do you work with marketing companies?", "What sources do you use for inspiration?", "When can we expect a new video from you?", "How do you choose music for your videos?", "What is your favorite moment in blogging?", "How is your day going?", "How do you find new subscribers for your channel?", "How do you work with comments from your viewers?", "What are your interests and hobbies?", "How do you deal with creative block?", "What book are you currently reading?", "What movies have you recently watched?", "What is your mood for today?", "What is your favorite restaurant in town?", "How do you feel about working with designers and photographers?", "What are the main topics that will be covered on your channel next month?", "What advice can you give on building a personal brand?", "How do you feel about promoting your social networks through other platforms?", "How are you feeling?", "Where are you right now?", "What will be in the broadcast today?", "I love you!", "What is your favorite movie?", "What is your favorite song?", "What do you usually do in your free time?", "How do you like the new season of your favorite show?", "What are your plans for the weekend?", "Great content, keep up the good work!", "What's your favorite sport?", "I've been wanting to ask you a question for a while now", "How do you find motivation for your work?", "What do you think of my new avatar?", "I want to thank you for sharing your experience and knowledge.", "What do you think of the new makeup trend?", "Your advice is so helpful, I'm already applying it in my life!", "What are your favorite clothing brands?", "Hello!", "I saw your stories where you talked about your favorite restaurant.", "How do you like their food?", "What's your favorite restaurant in town?")
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
        avatars.add(resources.getDrawable(R.drawable.r1))
        avatars.add(resources.getDrawable(R.drawable.r2))
        avatars.add(resources.getDrawable(R.drawable.r3))
        avatars.add(resources.getDrawable(R.drawable.r14))
        avatars.add(resources.getDrawable(R.drawable.r5))
        avatars.add(resources.getDrawable(R.drawable.r6))
        avatars.add(resources.getDrawable(R.drawable.r7))
        avatars.add(resources.getDrawable(R.drawable.r13))
        avatars.add(resources.getDrawable(R.drawable.r9))
        avatars.add(resources.getDrawable(R.drawable.r10))
        avatars.add(resources.getDrawable(R.drawable.r11))
        avatars.add(resources.getDrawable(R.drawable.r12))
        avatars.add(resources.getDrawable(R.drawable.r14))


        var nikRus= arrayOf("alenka_98", "stasik2003", "natashka_25", "maximilian_007", "misha_king", "elena_flower", "nikita_fox", "larisa_lioness", "dmitry_phoenix", "valery_sky", "nina_butterfly", "andrei_moon", "svetlana_star", "kirill_dark", "viktoriya_rainbow", "artem_tiger", "tanya_dreamer", "alexey_eagle", "anna_beauty", "ivan_iceberg", "maria_mystic", "vladimir_wolf", "olga_smile", "ruslan_fire", "eva_angel", "boris_thunder", "dasha_sunshine", "roman_dream", "anastasia_queen", "denis_silver")


        var msgRus= arrayOf(
            "Привет "+savedEditTextValue,"Как прошел твой день?", "Какие темы тебе было бы интересно обсудить в будущих прямых эфирах?", "Как ты нашел свою страсть к блогингу?", "Какая камера у тебя используется для съемок?", "Какой у тебя секрет успеха в инстаграме?", "Какие у тебя планы на ближайшее время?", "Какие хобби у тебя есть, помимо блогинга?", "Какой твой самый любимый видео формат?", "Что ты думаешь о сотрудничестве с другими блогерами?", "Что ты делал на выходных?", "Как ты поддерживаешь свой канал в хорошей форме?", "Какие советы ты можешь дать новичкам в инстаграме?", "Каким образом ты придумываешь контент для своего канала?", "Как ты находишь время для создания контента на своем канале?", "Давно не виделись! Как поживаешь?", "Как ты справляешься со стрессом во время создания контента?", "Какие проекты ты планируешь в ближайшем будущем?", "Как ты относишься к критике своих зрителей?", "Как ты работаешь с маркетинговыми компаниями?", "Какие источники ты используешь для получения вдохновения?", "Когда ждать новое видео от тебя?", "Как ты подбираешь музыку для своих видео?", "Какой твой любимый момент в блогинге?", "Как проходит день?", "Как ты находишь новых подписчиков для своего канала?","Как ты работаешь с комментариями своих зрителей?" , "Какие у тебя увлечения и хобби?", "Как ты справляешься с креативным кризисом?", "Какую книгу ты сейчас читаешь?", "Какие фильмы ты недавно посмотрел?", "Как твой настрой на сегодняшний день?", "Какой твой любимый ресторан в городе?", "Как ты относишься к работе с дизайнерами и фотографами?", "Какие главные темы будут рассмотрены на твоем канале в следующем месяце?", "Какие советы ты можешь дать по созданию личного бренда?", "Как ты относишься к продвижению своих социальных сетей через другие платформы?", "Как настроение?", "Где ты находишься сейчас?", "Что сегодня будет в эфире?", "Я тебя обожаю!", "Какой твой любимый фильм?", "Какая твоя любимая песня?", "Что ты обычно делаешь в свободное время?", "Как тебе новый сезон твоего любимого шоу?", "Какие планы на выходные?",
             "Очень крутой контент, продолжай в том же духе!", "Какой твой любимый вид спорта?","Я давно хотела задать тебе вопрос", "как ты находишь мотивацию для твоей работы?", "Как тебе мой новый аватар?", "Хочу поблагодарить тебя за то, что делишься своим опытом и знаниями.", "Что ты думаешь о новом тренде в макияже?")
//    val msgR= arrayOf( "Твои советы настолько полезны, я уже применяю их в своей жизни!", "Какие твои любимые бренды одежды?", "Привет!", "Я видела твои сторис, где ты рассказывал о своем любимом ресторане.", "Как тебе их блюда?", "Какой твой любимый ресторан в городе?", "Хочу спросить твое мнение на счет одного интересного проекта.","Что ты думаешь об этом?", "Ты выглядишь так стильно!", "Можешь рассказать, где ты покупаешь свою одежду?", "Какой твой любимый напиток?", "Хочу сказать, что ты делаешь отличную работу и мне очень нравится твой контент.", "Какой твой любимый вид транспорта?", "Большое спасибо за то, что ты делишься своей жизнью с нами!", "Какая твоя любимая книга?"
//        , "Ты умеешь прекрасно говорить на камеру, у тебя есть секреты?", "Я нашелся в твоих сторис!", "Круто, что мы можем общаться таким образом.", "Какой твой любимый жанр кино?", "Хочу узнать твое мнение на тему книг.", "Какую книгу ты советуешь прочитать?", "Какой твой любимый видеоблогер?", "Ты настоящая звезда, продолжай светить!", "Как твой день проходит?", "Какой твой любимый цвет?", "Хочу спросить, как ты думаешь о последних изменениях в инстаграме?", "Твой контент всегда такой интересный и разнообразный, как ты находишь идеи для новых постов?","Что ты думаешь о новом тренде в моде?","Ты так много путешествуешь! Можешь поделиться своим опытом путешествий?", "Какой твой любимый парк?","Что ты обычно заказываешь в кафе?","Хочу сказать, что твои посты очень мотивирующие и вдохновляющие!","Какой твой любимый фрукт?","Ты кажешься такой энергичной и позитивной! Как ты находишь баланс между работой и личной жизнью?","Что тебе больше всего нравится в твоей работе?","Какой твой любимый город?","Ты выглядишь потрясающе","Какой твой любимый праздник?", "Спасибо за то, что ты делаешь, твои посты всегда очень интересны.", "Какой твой любимый сезон года?", "Как ты поддерживаешь мотивацию для тренировок?", "Какой твой любимый фильм?", "Ты вдохновляешь меня быть лучше каждый день.", "Как ты управляешь своим временем, чтобы успевать все делать?", "Какой твой секрет для поддержания здорового образа жизни?"
//    )

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
        avatarsRus.add(resources.getDrawable(R.drawable.r1))
        avatarsRus.add(resources.getDrawable(R.drawable.r2))
        avatarsRus.add(resources.getDrawable(R.drawable.r3))
        avatarsRus.add(resources.getDrawable(R.drawable.r14))
        avatarsRus.add(resources.getDrawable(R.drawable.r5))
        avatarsRus.add(resources.getDrawable(R.drawable.r6))
        avatarsRus.add(resources.getDrawable(R.drawable.r7))
        avatarsRus.add(resources.getDrawable(R.drawable.r13))
        avatarsRus.add(resources.getDrawable(R.drawable.r9))
        avatarsRus.add(resources.getDrawable(R.drawable.r10))
        avatarsRus.add(resources.getDrawable(R.drawable.r11))
        avatarsRus.add(resources.getDrawable(R.drawable.r12))
        avatarsRus.add(resources.getDrawable(R.drawable.r14))

        if(savedEditTextValueLang==""){
            nikRus=nikRus+list.toTypedArray()
            Log.e(TAG, nikRus.toMutableList().toString())
            msgRus=msgRus+listComment.toTypedArray()
            Log.e(TAG, msgRus.toMutableList().toString())


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



        var messages2= arrayOf<String>()
        var messages3= arrayOf<String>()
        while (messages3.size<151){
            for (i in 0 until messages.size) {
                val randomelement = messages.random()
                messages2 = messages2.plus(randomelement)
                messages = messages.filterNot { it == randomelement }.toTypedArray()
                Log.e(TAG, messages2.size.toString())

                Log.e(TAG, randomelement)
            }
            Log.e(TAG, messages.size.toString())
            messages = messages.plus(messages2)
            messages3=messages3.plus(messages2)
            messages2= emptyArray()

        }
        Log.e(TAG, messages3.size.toString())
        Log.e(TAG, messages3.count { it == "Мне очень нравится, как вы подаете информацию!" }.toString())


        var nicknames2= arrayOf<String>()
        var nicknames3= arrayOf<String>()
        while (nicknames3.size<151) {
            for (i in 0 until nicknames.size) {
                val randomelement = nicknames.random()
                nicknames2 = nicknames2.plus(randomelement)
                nicknames = nicknames.filterNot { it == randomelement }.toTypedArray()
                Log.e(TAG, nicknames2.size.toString())

                Log.e(TAG, randomelement)
            }
            Log.e(TAG, nicknames.size.toString())
            nicknames = nicknames.plus(nicknames2)
            nicknames3=nicknames3.plus(nicknames2)
            nicknames2= emptyArray()

        }
        Log.e(TAG, nicknames3.size.toString())
        Log.e(TAG, nicknames3.count { it == "natashka_25" }.toString())


        val avatars2 = ArrayList<Drawable>()

        val avatars3 = ArrayList<Drawable>()
        while (avatars3.size<151) {
            for (i in 0 until avatars.size) {
                val randomelement = avatars.random()
                avatars2.add(randomelement)
                avatars.remove(randomelement)
            }
            Log.e(TAG, avatars.size.toString())
            avatars.addAll(avatars2)
            avatars3.addAll(avatars2)
            avatars.clear()
            Log.e(TAG, avatars3.size.toString())

        }





        var msgRus2= arrayOf<String>()
        var msgRus3= arrayOf<String>()
        Log.e(TAG,"aaaaa1111aaaa"+msgRus.size.toString())
        while (msgRus3.size<151){
            for (i in 0 until msgRus.size) {
                val randomelement = msgRus.random()
                msgRus2 = msgRus2.plus(randomelement)
                msgRus = msgRus.filterNot { it == randomelement }.toTypedArray()
                Log.e(TAG, msgRus2.size.toString())

                Log.e(TAG, randomelement)
            }
            Log.e(TAG, msgRus.size.toString())
            msgRus = msgRus.plus(msgRus2)
            msgRus3=msgRus3.plus(msgRus2)
            msgRus2= emptyArray()

        }
        Log.e(TAG, msgRus3.size.toString())
        Log.e(TAG, msgRus3.count { it == "Мне очень нравится, как вы подаете информацию!" }.toString())


        var nikRus2= arrayOf<String>()
        var nikRus3= arrayOf<String>()
        while (nikRus3.size<151) {
            for (i in 0 until nikRus.size) {
                val randomelement = nikRus.random()
                nikRus2 = nikRus2.plus(randomelement)
                nikRus = nikRus.filterNot { it == randomelement }.toTypedArray()
                Log.e(TAG, nikRus2.size.toString())

                Log.e(TAG, randomelement)
            }
            Log.e(TAG, nikRus.size.toString())
            nikRus = nikRus.plus(nikRus2)
            nikRus3=nikRus3.plus(nikRus2)
            nikRus2= emptyArray()

        }
        Log.e(TAG, nikRus3.size.toString())
        Log.e(TAG, nikRus3.count { it == "natashka_25" }.toString())


        val avatarsRus2 = ArrayList<Drawable>()

        val avatarsRus3 = ArrayList<Drawable>()
        while (avatarsRus3.size<151) {
            for (i in 0 until avatarsRus.size) {
                val randomelement = avatarsRus.random()
                avatarsRus2.add(randomelement)
                avatarsRus.remove(randomelement)
            }
            Log.e(TAG, avatarsRus.size.toString())
            avatarsRus.addAll(avatarsRus2)
            avatarsRus3.addAll(avatarsRus2)
            avatarsRus2.clear()
            Log.e(TAG, avatarsRus3.size.toString())

        }
if(savedEditTextValueLang==""){
    for (i in 1..150) { // Generate 30 comments

        val nickname = nikRus3[i]
        val message = msgRus3[i]
        val avatar = avatarsRus3[i]
        commentsList.add(Comment(nickname, message, avatar))
    }
    }else {
    if (savedEditTextValueLang == "EN") {

        for (i in 1..150) { // Generate 10 comments

            val nickname = nicknames3[i]
            val message = messages3[i]
            val avatar = avatars3[i]
            commentsList.add(Comment(nickname, message, avatar))


        }
    } else {
        for (i in 1..150) { // Generate 10 comments


            val nickname = nikRus3[i]
            val message = msgRus3[i]
            val avatar = avatarsRus3[i]
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
if(a>150){
    a=0
}
      Log.e(TAG,a.toString())
           var comment = commentsList[a]
        a++

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
    var b:Int=80
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun addRandomCommentJoined() {
        if(b<1){
            b=80
        }
        Log.e(TAG,b.toString())
        var comment = commentsList[b]
        b--
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



