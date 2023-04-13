package com.example.instacloneapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

import android.util.Base64
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.Directory
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.Manifest
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.nfc.Tag
import android.os.Environment
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.NestedScrollView
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import org.json.JSONArray
import java.io.*
import java.util.*
import java.util.Collections.addAll
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.properties.Delegates
import android.graphics.Bitmap as Bitmap


@Suppress("DEPRECATION")
class AddComment : AppCompatActivity() {

    lateinit var UniqueAvatarImage: ImageView
    lateinit var backBtn: ImageView
    lateinit var addNickButton: Button;
    lateinit var NickTextView: MaterialTextView;
    lateinit var deleteNickButton: Button
    lateinit var nickField: EditText;
    lateinit var commentField: EditText;
    lateinit var idUnique: TextView
    lateinit var addCommentButton: Button
    lateinit var deleteCommentButton: Button
    lateinit var CommentTextView: MaterialTextView
    lateinit var deleteAvatarButton: Button
    lateinit var addAvatarButton: Button
    lateinit var gridLayout: GridLayout;
    lateinit var addUniqueCommentButton: Button
    lateinit var deleteUniqueCommentButton: Button
    lateinit var adnik: TextView
    lateinit var adcom: TextView
    lateinit var adava: TextView
    lateinit var uncom: TextView
    var hasnoUserIcon: Boolean = true
    var isCheckedVerif: Boolean = false
    lateinit var drw: Drawable;
    private val REQUEST_CODE_PICK_IMAGE = 201
    private val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 202
    lateinit var switchUniqueComment: Switch
    var widthImage by Delegates.notNull<Float>()
    var list = mutableListOf<String>()
    var listComment = mutableListOf<String>()


    var listUniqueComment = mutableListOf<String>()
    var listUniqueNick = mutableListOf<String>()
    val bitmapArray = ArrayList<Bitmap>()
    var boolList = mutableListOf<Boolean>()
    lateinit var scrollView: NestedScrollView
    lateinit var commentLayout: LinearLayout
    val dir =
        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/InstagramLive")

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_comment)
        addNickButton = findViewById(R.id.addNickButton)
        backBtn = findViewById(R.id.backBtn)
        NickTextView = findViewById(R.id.nickTxt)
        deleteNickButton = findViewById(R.id.deleteNickButton)
        addCommentButton = findViewById(R.id.addCommentButton)
        deleteCommentButton = findViewById(R.id.deleteCommentButton)
        CommentTextView = findViewById(R.id.commentTxt)
        addAvatarButton = findViewById(R.id.addAvatarButton)
        gridLayout = findViewById(R.id.gridLayout)
        deleteAvatarButton = findViewById(R.id.deleteAvatarButton)
        addUniqueCommentButton = findViewById(R.id.adduniqueAvatarButton)
        deleteUniqueCommentButton = findViewById(R.id.deleteUniqueAvatarButton)
        switchUniqueComment = findViewById(R.id.switchUniqueComments)
        scrollView = findViewById(R.id.UniqueCommentScroll)
        commentLayout = findViewById(R.id.commentLayout)
        var switchBool: Boolean
        var addniktxt = findViewById<TextView>(R.id.addNickTxt)

        adcom = findViewById(R.id.addCommentTxt)
        adnik = findViewById(R.id.addNik)
        adava = findViewById(R.id.addAvatarTxt)
        uncom = findViewById(R.id.uniqueAvatarTxt)

        val sharedPrefLang = getSharedPreferences("lang", Context.MODE_PRIVATE)
        val savedEditTextValueLang = sharedPrefLang.getString("edit_text_value", "")


        if (savedEditTextValueLang == "") {
            adnik.text = "Добавить/Удалить никнейм"
            adcom.text = "Добавить/Удалить сообщение"
            adava.text = "Добавить/Удалить аватарку"
            uncom.text = "Добавить/Удалить уникальный комментарий"
            addniktxt.text = "Дополнительные опции"
            addNickButton.text = "Добавить"
            deleteNickButton.text = "Удалить"
            addCommentButton.text = "Добавить"
            deleteCommentButton.text = "Удалить"
            addAvatarButton.text = "Добавить"
            deleteAvatarButton.text = "Удалить"
            addUniqueCommentButton.text = "Добавить"
            deleteUniqueCommentButton.text = "Удалить"
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.add_nick_layout)
            dialog.findViewById<Button>(R.id.dialogSaveBtn).text = "Сохранить"
            dialog.findViewById<Button>(R.id.dialogCancelBtn).text = "Отменить"
        } else {
            if (savedEditTextValueLang == "РУ") {
                adnik.text = "Добавить/Удалить никнейм"
                adcom.text = "Добавить/Удалить сообщение"
                adava.text = "Добавить/Удалить аватарку"
                uncom.text = "Добавить/Удалить уникальный комментарий"
                addniktxt.text = "Дополнительные опции"
                addNickButton.text = "Добавить"
                deleteNickButton.text = "Удалить"
                addCommentButton.text = "Добавить"
                deleteCommentButton.text = "Удалить"
                addAvatarButton.text = "Добавить"
                deleteAvatarButton.text = "Удалить"
                addUniqueCommentButton.text = "Добавить"
                deleteUniqueCommentButton.text = "Удалить"
                val dialog = Dialog(this)
                dialog.setContentView(R.layout.add_nick_layout)
                dialog.findViewById<Button>(R.id.dialogSaveBtn).text = "Сохранить"
                dialog.findViewById<Button>(R.id.dialogCancelBtn).text = "Отменить"
            } else {
                adnik.text = "Add/Delete nickname"
                adcom.text = "Add/Delete comment"
                adava.text = "Add/Delete avatar"
                uncom.text = "Add/Delete unique comment"
                addNickButton.text = "Add"
                addniktxt.text = "More Options"
                deleteNickButton.text = "Delete"
                addCommentButton.text = "Add"
                deleteCommentButton.text = "Delete"
                addAvatarButton.text = "Add"
                deleteAvatarButton.text = "Delete"
                addUniqueCommentButton.text = "Add"
                deleteUniqueCommentButton.text = "Delete"
                val dialog = Dialog(this)
                dialog.setContentView(R.layout.add_nick_layout)
                dialog.findViewById<Button>(R.id.dialogSaveBtn).text = "Save"
                dialog.findViewById<Button>(R.id.dialogCancelBtn).text = "Cancel"
            }
        }


        val sharedPrefs = getSharedPreferences("Number", Context.MODE_PRIVATE)
        val myNumber = sharedPrefs.getFloat("myNumber", 300F)
        widthImage = myNumber

        // Get the SharedPreferences object
        val sharedPrefsSwitchBool = getSharedPreferences("switchBool", Context.MODE_PRIVATE)

// Retrieve the boolean value
        switchBool = sharedPrefsSwitchBool.getBoolean("myBoolean", false)

        switchUniqueComment.isChecked = switchBool
        switchUniqueComment.setOnClickListener {
            switchBool = switchUniqueComment.isChecked
            // Get the SharedPreferences object
            Log.e(TAG, switchBool.toString())
            val sharedPrefs = getSharedPreferences("switchBool", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putBoolean("myBoolean", switchBool)
            editor.apply()
        }




        Log.e(TAG, bitmapArray.toString())


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
                    Log.e(TAG, bitmapArrayB.toString())
                }
            }
        }


        bitmapArray.addAll(bitmapArrayB)


// Retrieve the JSON string from SharedPreferences
        val sharedPreferencesUnique = getSharedPreferences("UniqueNick", Context.MODE_PRIVATE)
        val jsonStringUnique = sharedPreferencesUnique.getString("UniqueNick", null)

// Convert the JSON string back to a MutableList using Gson
        val gsonUnique = Gson()
        if (jsonStringUnique == null) {
            Log.e(TAG, "okkk")
        } else {

            listUniqueNick =
                gsonUnique.fromJson(
                    jsonStringUnique,
                    object : TypeToken<MutableList<String>>() {}.type
                )

            Log.e(TAG, listUniqueNick.toString())
// Display the MutableList in a TextView
        }

        // Retrieve the JSON string from SharedPreferences
        val sharedPreferencesUniqueCom = getSharedPreferences("UniqueComment", Context.MODE_PRIVATE)
        val jsonStringUniqueCom = sharedPreferencesUniqueCom.getString("UniqueComment", null)

// Convert the JSON string back to a MutableList using Gson
        val gsonUniqueCom = Gson()
        if (jsonStringUniqueCom == null) {
            Log.e(TAG, "okkk")
        } else {

            listUniqueComment =
                gsonUniqueCom.fromJson(
                    jsonStringUniqueCom,
                    object : TypeToken<MutableList<String>>() {}.type
                )

            Log.e(TAG, listUniqueComment.toString())
// Display the MutableList in a TextView
        }


        // Get shared preferences object
        val sharedPrefsBool = this.getSharedPreferences("myPrefsBool", Context.MODE_PRIVATE)

// Retrieve boolean list from shared preferences
        val jsonBool = sharedPrefsBool.getString("boolListKey", null)
        val gsonBool = Gson()
        if (jsonBool == null) {
        } else {
            boolList = gsonBool.fromJson(jsonBool, object : TypeToken<List<Boolean>>() {}.type)
        }




        for (i in 0 until bitmapArray.size) {

            val newCommentLayout = LayoutInflater.from(this).inflate(R.layout.comment_layout, null)
            val avatarImage = newCommentLayout.findViewById<ImageView>(R.id.avatarImage)
            val nicknameText = newCommentLayout.findViewById<TextView>(R.id.nicknameText)
            val commentText = newCommentLayout.findViewById<TextView>(R.id.commentText)
            val idUnique = newCommentLayout.findViewById<TextView>(R.id.idUnique)
            val verification = newCommentLayout.findViewById<ImageView>(R.id.verification)
            Log.e(TAG, "bitmapArray " + bitmapArray.toString())
            Log.e(TAG, listUniqueNick.toString())
            avatarImage.setImageBitmap(bitmapArray[i])
            nicknameText.text = listUniqueNick[i]
            commentText.text = listUniqueComment[i]
            idUnique.setText((i + 1).toString())
            if (boolList[i]) {
                verification.alpha = 1F
            } else {
                verification.alpha = 0F;
            }
            commentLayout.addView(newCommentLayout)
            scrollView.post {
                scrollView.fullScroll(View.FOCUS_DOWN)
            }


        }


        val dialogU = Dialog(this)
        dialogU.setContentView(R.layout.unique_comment)
        UniqueAvatarImage = dialogU.findViewById<ImageView>(R.id.avatarImage)


        Log.e(TAG, listUniqueNick.toString())
        Log.e(TAG, listUniqueComment.toString())
        Log.e(TAG, bitmapArray.toString())

        addUniqueCommentButton.setOnClickListener {

            val width = resources.displayMetrics.widthPixels * 0.99 // adjust this value as needed
            val height = resources.displayMetrics.heightPixels * 0.4 // adjust this value as needed
            dialogU.window?.setLayout(width.toInt(), height.toInt())


            val saveButton = dialogU.findViewById<Button>(R.id.saveUnique)
            val cancelButton = dialogU.findViewById<Button>(R.id.cancelUnique)
            val verifCheck = dialogU.findViewById<CheckBox>(R.id.verificationCheck)
            val addCont = dialogU.findViewById<Button>(R.id.addContinue)
            nickField = dialogU.findViewById(R.id.nicknameText)
            commentField = dialogU.findViewById(R.id.commentText)
            if (savedEditTextValueLang == "") {
                saveButton.text = "Сохранить"
                cancelButton.text = "Отменить"
                nickField.hint = "Никнейм"
                commentField.hint = "Коммент"


            } else {
                if (savedEditTextValueLang == "РУ") {
                    saveButton.text = "Сохранить"
                    cancelButton.text = "Отменить"
                    nickField.hint = "Никнейм"
                    commentField.hint = "Коммент"
                } else {
                    saveButton.text = "Save"
                    cancelButton.text = "Cancel"
                    nickField.hint = "Nickname"
                    commentField.hint = "Comment"
                }
            }
            addCont.setOnClickListener {
                drw = UniqueAvatarImage.drawable
// Compare the drawable resource to a specific drawable resource using its resource ID
                hasnoUserIcon =
                    !drw.getConstantState()!!
                        .equals(getResources().getDrawable(R.drawable.no_user).getConstantState())
                Log.e(TAG, hasnoUserIcon.toString())
                if (commentField.text.toString().isNotEmpty() && nickField.text.toString()
                        .isNotEmpty() && hasnoUserIcon
                ) {

                    var comment = commentField.text.toString()
                    var nick = nickField.text.toString()
                    if (comment.isNotEmpty()) {
                        listUniqueComment.add(comment)

                        commentField.text = null
                        val gson = Gson()
                        val jsonString = gson.toJson(listUniqueComment)

// Save the JSON string in SharedPreferences
                        val sharedPreferences =
                            getSharedPreferences("UniqueComment", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("UniqueComment", jsonString)
                        editor.apply()


                    }
                    if (nick.isNotEmpty()) {
                        listUniqueNick.add(nick)


                        val gson = Gson()
                        val jsonString = gson.toJson(listUniqueNick)

// Save the JSON string in SharedPreferences
                        val sharedPreferences =
                            getSharedPreferences("UniqueNick", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("UniqueNick", jsonString)
                        editor.apply()
                    }

                    boolList.add(isCheckedVerif)

                    val gson = Gson()
                    val json = gson.toJson(boolList)

                    val sharedPrefsBool =
                        this.getSharedPreferences("myPrefsBool", Context.MODE_PRIVATE)

                    val editorBool = sharedPrefsBool.edit()

                    editorBool.putString("boolListKey", json)

                    editorBool.apply()


                    val context = this
                    val bitmap = (UniqueAvatarImage.drawable as BitmapDrawable).bitmap
                    val file = File(context.externalCacheDir, "imageU.png")
                    val outputStream = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    bitmapArray.add(bitmap)
                    outputStream.close()


                    val sharedPrefs = getSharedPreferences("bitmapList", Context.MODE_PRIVATE)

                    val pathsx = ArrayList<String>()
// Create a directory to store the bitmap files
                    val dir = File(applicationContext.filesDir, "bitmapDir")
                    if (!dir.exists()) {
                        dir.mkdir()
                    }
                    // Define a coroutine scope
                    CoroutineScope(Dispatchers.Default).launch {
                        // Iterate through each bitmap in the array
                        bitmapArray.forEach { bitmap ->
                            // Create a unique file name for each bitmap
                            val fileName = "bitmap_${System.currentTimeMillis()}.png"
                            val file = File(dir, fileName)

                            // Save the bitmap to the file in a background thread
                            withContext(Dispatchers.IO) {
                                val stream = FileOutputStream(file)
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                                stream.close()
                            }

                            pathsx.add(file.absolutePath)
                        }

                        // Store the list of file paths in SharedPreferences in the main thread
                        withContext(Dispatchers.Main) {
                            val editor = sharedPrefs.edit()
                            editor.putInt("bitmapCount", pathsx.size)
                            for (i in pathsx.indices) {
                                editor.putString("bitmap_$i", pathsx[i])
                            }
                            editor.apply()
                        }
                    }




                val newCommentLayout =
                    LayoutInflater.from(this).inflate(R.layout.comment_layout, null)
                val avatarImage = newCommentLayout.findViewById<ImageView>(R.id.avatarImage)
                val nicknameText = newCommentLayout.findViewById<TextView>(R.id.nicknameText)
                val commentText = newCommentLayout.findViewById<TextView>(R.id.commentText)
                val idUnique = newCommentLayout.findViewById<TextView>(R.id.idUnique)
                val verification = newCommentLayout.findViewById<ImageView>(R.id.verification)
                Log.e(TAG, bitmapArray.toString())
                avatarImage.setImageBitmap(bitmapArray.last())
                nicknameText.text = nick
                commentText.text = comment
                idUnique.setText(bitmapArray.size.toString())
                if (isCheckedVerif) {
                    verification.alpha = 1F
                } else {
                    verification.alpha = 0F;
                }

                commentLayout.addView(newCommentLayout)
                scrollView.post {
                    scrollView.fullScroll(View.FOCUS_DOWN)
                }


            } else {
            Toast.makeText(this, "Заполните все поля и загружайте аватарку", Toast.LENGTH_LONG)
                .show()
                }

            commentField.text = null
        }



            verifCheck.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    isCheckedVerif = true
                } else {
                    isCheckedVerif = false
                }
            }


            commentField.text = null
            nickField.text = null
            isCheckedVerif = false
            verifCheck.isChecked = false
            UniqueAvatarImage.setImageResource(R.drawable.no_user)

            UniqueAvatarImage.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                    )
                } else {
                    val intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
                }


            }


            saveButton.setOnClickListener {
                Log.e(TAG, commentField.text.toString().isNotEmpty().toString())
                Log.e(TAG, nickField.text.toString().isNotEmpty().toString())
                Log.e(TAG, (UniqueAvatarImage != null).toString())


                coment()
                dialogU.dismiss()
            }


            cancelButton.setOnClickListener {
                dialogU.dismiss()
            }

            dialogU.show()

        }


        deleteUniqueCommentButton.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.add_nick_layout)

            val width = resources.displayMetrics.widthPixels * 0.99 // adjust this value as needed
            val height = resources.displayMetrics.heightPixels * 0.4 // adjust this value as needed
            dialog.window?.setLayout(width.toInt(), height.toInt())


            val saveButton = dialog.findViewById<Button>(R.id.dialogSaveBtn)
            val cancelButton = dialog.findViewById<Button>(R.id.dialogCancelBtn)
            val addfile=dialog.findViewById<Button>(R.id.addFile)
            addfile.alpha=0F
            if (savedEditTextValueLang == "") {
                saveButton.text = "Удалить"
                cancelButton.text = "Отменить"


            } else {
                if (savedEditTextValueLang == "РУ") {
                    saveButton.text = "Удалить"
                    cancelButton.text = "Отменить"

                } else {
                    saveButton.text = "Delete"
                    cancelButton.text = "Cancel"

                }
            }


            nickField = dialog.findViewById(R.id.dialogtxtNick)
            nickField.text = null
            nickField.hint = "1"

            saveButton.setOnClickListener {

                var text = nickField.text.toString()
                if (text.isNotEmpty()) {

                    listUniqueComment.removeAt(text.toInt() - 1)
                    listUniqueNick.removeAt(text.toInt() - 1)
                    bitmapArray.removeAt(text.toInt() - 1)
                    boolList.removeAt(text.toInt() - 1)
                    val gson = Gson()
                    val jsonString = gson.toJson(listUniqueComment)

// Save the JSON string in SharedPreferences
                    val sharedPreferences =
                        getSharedPreferences("UniqueComment", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("UniqueComment", jsonString)
                    editor.apply()


                    val gsonBool = Gson()
                    val json = gsonBool.toJson(boolList)

                    val sharedPrefsBool =
                        this.getSharedPreferences("myPrefsBool", Context.MODE_PRIVATE)

                    val editorBool = sharedPrefsBool.edit()

                    editorBool.putString("boolListKey", json)

                    editorBool.apply()

                    val gsonNick = Gson()
                    val jsonStringNick = gsonNick.toJson(listUniqueComment)

// Save the JSON string in SharedPreferences
                    val sharedPreferencesNick =
                        getSharedPreferences("UniqueNick", Context.MODE_PRIVATE)
                    val editorNick = sharedPreferencesNick.edit()
                    editorNick.putString("UniqueNick", jsonStringNick)
                    editorNick.apply()


//                    val sharedPreferencesx = getSharedPreferences("bitmapList", Context.MODE_PRIVATE)
//                    val editorx = sharedPreferencesx.edit()
//
//// Create a directory to store the bitmap files
//                    val dir = File(applicationContext.filesDir, "bitmapDir")
//                    dir.mkdirs()
//
//                    val paths = ArrayList<String>()
//
//// Save the bitmaps to files and add the file paths to the list
//                    for ((index, bitmap) in bitmapArray.withIndex()) {
//                        // Create a unique file name for each bitmap
//                        val fileName = "bitmap_${System.currentTimeMillis() + index}.png"
//                        val file = File(dir, fileName)
//
//                        // Save the bitmap to the file
//                        FileOutputStream(file).use { stream ->
//                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
//                        }
//
//                        paths.add(file.absolutePath)
//                    }
//
//// Store the list of file paths in SharedPreferences
//                    editorx.putStringSet("bitmapPaths", paths.toSet())
//                    editorx.apply()

                    saveImUniq()




                    Log.e(TAG, bitmapArray.toString())
                }
                commentLayout.removeAllViews()
                for (i in 0 until bitmapArray.size) {

                    val newCommentLayout =
                        LayoutInflater.from(this).inflate(R.layout.comment_layout, null)
                    val avatarImage = newCommentLayout.findViewById<ImageView>(R.id.avatarImage)
                    val nicknameText = newCommentLayout.findViewById<TextView>(R.id.nicknameText)
                    val commentText = newCommentLayout.findViewById<TextView>(R.id.commentText)
                    val verification = newCommentLayout.findViewById<ImageView>(R.id.verification)
                    val idUnique = newCommentLayout.findViewById<TextView>(R.id.idUnique)
                    Log.e(TAG, bitmapArray.toString())
                    avatarImage.setImageBitmap(bitmapArray[i])
                    nicknameText.text = listUniqueNick[i]
                    commentText.text = listUniqueComment[i]
                    if (boolList[i]) {
                        verification.alpha = 1F
                    } else {
                        verification.alpha = 0F;
                    }

                    idUnique.setText((i + 1).toString())
                    commentLayout.addView(newCommentLayout)
                    scrollView.post {
                        scrollView.fullScroll(View.FOCUS_DOWN)
                    }


                }

                dialog.dismiss()

            }


            cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }









        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                200
            )
        }

        val fileList = dir.listFiles()
        val uriList = ArrayList<Uri>()
        if (fileList != null) {

            for (file in fileList) {
                val uri = Uri.fromFile(file)
                uriList.add(uri)
            }
            Log.e(TAG, uriList.toString())

        }
        for (i in uriList.indices) {
            val imageView = ImageView(this)
            val layoutParams = GridLayout.LayoutParams()
            layoutParams.width = widthImage.toInt()
            layoutParams.height = widthImage.toInt()
            imageView.layoutParams = layoutParams
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.setPadding(5, 5, 5, 5)
            imageView.setImageURI(uriList[i])
            gridLayout.addView(imageView)

        }


        deleteAvatarButton.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.add_nick_layout)

            val width = resources.displayMetrics.widthPixels * 0.99 // adjust this value as needed
            val height = resources.displayMetrics.heightPixels * 0.4 // adjust this value as needed
            dialog.window?.setLayout(width.toInt(), height.toInt())


            val saveButton = dialog.findViewById<Button>(R.id.dialogSaveBtn)
            val cancelButton = dialog.findViewById<Button>(R.id.dialogCancelBtn)
            val addfile=dialog.findViewById<Button>(R.id.addFile)
            addfile.alpha=0F
            if (savedEditTextValueLang == "") {
                saveButton.text = "Удалить"
                cancelButton.text = "Отменить"


            } else {
                if (savedEditTextValueLang == "РУ") {
                    saveButton.text = "Удалить"
                    cancelButton.text = "Отменить"

                } else {
                    saveButton.text = "Delete"
                    cancelButton.text = "Cancel"

                }
            }

            nickField = dialog.findViewById(R.id.dialogtxtNick)
            nickField.hint = "1"

            saveButton.setOnClickListener {


                var text = nickField.text.toString()
                if (text.isNotEmpty()) {

                    val fileName = text + ".jpg"
                    Log.e(TAG, fileName)
                    val folder = dir
                    val fileToDelete = File(folder, fileName)
                    Log.e(TAG, fileToDelete.absolutePath)
                    if (fileToDelete.exists()) {
                        val isDeleted = fileToDelete.delete()
                        val folder = File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                            "InstagramLive"
                        )
                        val files = folder.listFiles()

                        for (i in 0 until files.size) {
                            val file = files[i]
                            var a = i + 1
                            val fileName = "$a.jpg"
                            val newFile = File(folder, fileName)
                            if (file.exists()) {
                                file.renameTo(newFile)
                            }
                        }


                        Log.e(TAG, isDeleted.toString())
                        if (isDeleted) {
                            gridLayout.removeAllViews()

                            val fileList = dir.listFiles()
                            val uriList = ArrayList<Uri>()
                            if (fileList != null) {

                                for (file in fileList) {
                                    val uri = Uri.fromFile(file)
                                    uriList.add(uri)
                                }
                                Log.e(TAG, uriList.toString())

                            }
                            for (i in uriList.indices) {
                                val imageView = ImageView(this)

                                val layoutParams = GridLayout.LayoutParams()
                                layoutParams.width = widthImage.toInt()
                                layoutParams.height = widthImage.toInt()
                                imageView.layoutParams = layoutParams
                                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                                imageView.setPadding(5, 5, 5, 5)
                                imageView.setImageURI(uriList[i])

                                gridLayout.addView(imageView)


                            }

                        } else {
                            // failed to delete file
                            Log.e(TAG, uriList.toString())
                        }
                    } else {
                        // file does not exist
                    }


                }

                dialog.dismiss()

            }



            cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }




        addAvatarButton.setOnClickListener {

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
        }


// Retrieve the JSON string from SharedPreferences
        val sharedPreferences = getSharedPreferences("nick", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString("nick", null)

// Convert the JSON string back to a MutableList using Gson
        val gson = Gson()
        if (jsonString == null) {
            Log.e(TAG, "okkk")
        } else {

            list = gson.fromJson(jsonString, object : TypeToken<MutableList<String>>() {}.type)

            Log.e(TAG, list.toString())
// Display the MutableList in a TextView

            var result = ""
            for ((index, element) in list.withIndex()) {
                result += (index + 1).toString() + ". $element\n"
            }
            NickTextView.text = result

        }

        // Retrieve the JSON string from SharedPreferences
        val sharedPreferencesComment = getSharedPreferences("comment", Context.MODE_PRIVATE)
        val jsonStringComment = sharedPreferencesComment.getString("comment", null)

// Convert the JSON string back to a MutableList using Gson
        val gsonComment = Gson()
        if (jsonStringComment == null) {
            Log.e(TAG, "okkk")
        } else {

            listComment = gsonComment.fromJson(
                jsonStringComment,
                object : TypeToken<MutableList<String>>() {}.type
            )

            Log.e(TAG, listComment.toString())
// Display the MutableList in a TextView

            var result = ""
            for ((index, element) in listComment.withIndex()) {
                result += (index + 1).toString() + ". $element\n"
            }
            CommentTextView.text = result

        }

        addNickButton.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.add_nick_layout)

            val width = resources.displayMetrics.widthPixels * 0.99 // adjust this value as needed
            val height = resources.displayMetrics.heightPixels * 0.4 // adjust this value as needed
            dialog.window?.setLayout(width.toInt(), height.toInt())


            val saveButton = dialog.findViewById<Button>(R.id.dialogSaveBtn)
            val cancelButton = dialog.findViewById<Button>(R.id.dialogCancelBtn)
            val selFileButton = dialog.findViewById<Button>(R.id.addFile)
            nickField = dialog.findViewById(R.id.dialogtxtNick)

            if (savedEditTextValueLang == "") {
                saveButton.text = "Сохранить"
                cancelButton.text = "Отменить"


            } else {
                if (savedEditTextValueLang == "РУ") {
                    saveButton.text = "Сохранить"
                    cancelButton.text = "Отменить"

                } else {
                    saveButton.text = "Save"
                    cancelButton.text = "Cancel"

                }
            }
            selFileButton.setOnClickListener {
                selectTxtFile()

                dialog.dismiss()
            }
            saveButton.setOnClickListener {

                var text = nickField.text.toString()
                if (text.isNotEmpty()) {
                    text = text.lowercase()
                    list.add(text)

                    NickTextView.append(list.size.toString() + ".$text\n")


                    val gson = Gson()
                    val jsonString = gson.toJson(list)

// Save the JSON string in SharedPreferences
                    val sharedPreferences = getSharedPreferences("nick", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("nick", jsonString)
                    editor.apply()


                }

                dialog.dismiss()

            }


            cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        deleteNickButton.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.add_nick_layout)

            val width = resources.displayMetrics.widthPixels * 0.99 // adjust this value as needed
            val height = resources.displayMetrics.heightPixels * 0.4 // adjust this value as needed
            dialog.window?.setLayout(width.toInt(), height.toInt())


            val saveButton = dialog.findViewById<Button>(R.id.dialogSaveBtn)
            val cancelButton = dialog.findViewById<Button>(R.id.dialogCancelBtn)
            val addfile=dialog.findViewById<Button>(R.id.addFile)
            addfile.alpha=0F
            if (savedEditTextValueLang == "") {
                saveButton.text = "Удалить"
                cancelButton.text = "Отменить"


            } else {
                if (savedEditTextValueLang == "РУ") {
                    saveButton.text = "Удалить"
                    cancelButton.text = "Отменить"

                } else {
                    saveButton.text = "Delete"
                    cancelButton.text = "Cancel"

                }
            }

            nickField = dialog.findViewById(R.id.dialogtxtNick)
            nickField.hint = "1"

            saveButton.setOnClickListener {

                var text = nickField.text.toString()
                if (text.isNotEmpty()) {

                    list.removeAt(text.toInt() - 1)


                    var result = ""
                    for ((index, element) in list.withIndex()) {
                        result += (index + 1).toString() + ". $element\n"
                    }
                    NickTextView.text = result

                    val gson = Gson()
                    val jsonString = gson.toJson(list)

// Save the JSON string in SharedPreferences
                    val sharedPreferences = getSharedPreferences("nick", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("nick", jsonString)
                    editor.apply()


                }
                Log.e(TAG, list.toString())


                dialog.dismiss()

            }


            cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }





        addCommentButton.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.add_nick_layout)

            val width = resources.displayMetrics.widthPixels * 0.99 // adjust this value as needed
            val height = resources.displayMetrics.heightPixels * 0.4 // adjust this value as needed
            dialog.window?.setLayout(width.toInt(), height.toInt())


            val saveButton = dialog.findViewById<Button>(R.id.dialogSaveBtn)
            val cancelButton = dialog.findViewById<Button>(R.id.dialogCancelBtn)
            val selFileButton = dialog.findViewById<Button>(R.id.addFile)

            selFileButton.setOnClickListener {
                selectCommentsFile()

                dialog.dismiss()
            }
            nickField = dialog.findViewById(R.id.dialogtxtNick)
            if (savedEditTextValueLang == "") {
                saveButton.text = "Сохранить"
                cancelButton.text = "Отменить"


            } else {
                if (savedEditTextValueLang == "РУ") {
                    saveButton.text = "Сохранить"
                    cancelButton.text = "Отменить"

                } else {
                    saveButton.text = "Save"
                    cancelButton.text = "Cancel"

                }
            }
            saveButton.setOnClickListener {

                var text = nickField.text.toString()
                if (text.isNotEmpty()) {
                    listComment.add(text)

                    CommentTextView.append(listComment.size.toString() + ".$text\n")

                    val gson = Gson()
                    val jsonString = gson.toJson(listComment)

// Save the JSON string in SharedPreferences
                    val sharedPreferences = getSharedPreferences("comment", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("comment", jsonString)
                    editor.apply()


                }

                dialog.dismiss()

            }


            cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        deleteCommentButton.setOnClickListener {


            val dialog = Dialog(this)
            dialog.setContentView(R.layout.add_nick_layout)

            val width = resources.displayMetrics.widthPixels * 0.99 // adjust this value as needed
            val height = resources.displayMetrics.heightPixels * 0.4 // adjust this value as needed
            dialog.window?.setLayout(width.toInt(), height.toInt())


            val saveButton = dialog.findViewById<Button>(R.id.dialogSaveBtn)
            val cancelButton = dialog.findViewById<Button>(R.id.dialogCancelBtn)
            val addfile=dialog.findViewById<Button>(R.id.addFile)
            addfile.alpha=0F
            if (savedEditTextValueLang == "") {
                saveButton.text = "Удалить"
                cancelButton.text = "Отменить"


            } else {
                if (savedEditTextValueLang == "РУ") {
                    saveButton.text = "Удалить"
                    cancelButton.text = "Отменить"

                } else {
                    saveButton.text = "Delete"
                    cancelButton.text = "Cancel"

                }
            }

            nickField = dialog.findViewById(R.id.dialogtxtNick)
            nickField.hint = "1"

            saveButton.setOnClickListener {

                var text = nickField.text.toString()
                if (text.isNotEmpty()) {

                    listComment.removeAt(text.toInt() - 1)


                    var result = ""
                    for ((index, element) in listComment.withIndex()) {
                        result += (index + 1).toString() + ". $element\n"
                    }
                    CommentTextView.text = result

                    val gson = Gson()
                    val jsonString = gson.toJson(listComment)

// Save the JSON string in SharedPreferences
                    val sharedPreferences = getSharedPreferences("comment", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("comment", jsonString)
                    editor.apply()


                }
                Log.e(TAG, listComment.toString())


                dialog.dismiss()

            }


            cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }






        backBtn.setOnClickListener {

            val intent = Intent(this, settings::class.java)
            startActivity(intent)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_TXT_FILE && resultCode == RESULT_OK) {
            val selectedFile = data?.data
            selectedFile?.let { uri ->
                val inputStream = contentResolver.openInputStream(uri)
                val text = inputStream?.bufferedReader()
                    .use { it?.readText()?.replace(",", "\n")?.replace(",", "") }
                val wordList = text?.split("\n")
                val mutableList = mutableListOf<String>()
                wordList?.forEach { word ->
                    val newWords = word.split(" ")
                    mutableList.addAll(newWords)
                }
                list.addAll(mutableList)
            }
            Log.e(TAG, list.toString())
            var result = ""
            for ((index, element) in list.withIndex()) {
                result += (index + 1).toString() + ". $element\n"
            }
            NickTextView.text = result
            val gson = Gson()
            val jsonString = gson.toJson(list)

// Save the JSON string in SharedPreferences
            val sharedPreferences = getSharedPreferences("nick", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("nick", jsonString)
            editor.apply()
        }


        if (requestCode == PICK_comment_FILE && resultCode == RESULT_OK) {
            val selectedFile = data?.data
            selectedFile?.let { uri ->
                val inputStream = contentResolver.openInputStream(uri)
                val text = inputStream?.bufferedReader()
                    .use { it?.readText()?.replace(",", "\n")?.replace(",", "") }
                val wordList = text?.split("\n")
                val mutableList = mutableListOf<String>()
                wordList?.forEach { word ->
                    val newWords = word.split(" ")
                    mutableList.addAll(newWords)
                }
                listComment.addAll(mutableList)
            }
            Log.e(TAG, list.toString())
            var result = ""
            for ((index, element) in listComment.withIndex()) {
                result += (index + 1).toString() + ". $element\n"
            }
            CommentTextView.text = result
            val gson = Gson()
            val jsonString = gson.toJson(listComment)

// Save the JSON string in SharedPreferences
            val sharedPreferences = getSharedPreferences("comment", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("comment", jsonString)
            editor.apply()
        }






        if (requestCode == 1 && resultCode == RESULT_OK) {
            val imagesUriList = ArrayList<Uri>()
            val clipData = data?.clipData
            if (clipData != null) {
                for (i in 0 until clipData.itemCount) {
                    val item = clipData.getItemAt(i)
                    imagesUriList.add(item.uri)
                }
            } else {
                val uri = data?.data
                if (uri != null) {
                    imagesUriList.add(uri)
                }
            }
            setImagesToGridLayout(imagesUriList)

        }

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            val imageUri = data?.data
            UniqueAvatarImage.setImageURI(imageUri)


        }

    }


    private fun setImagesToGridLayout(imagesUriList: ArrayList<Uri>) {
        for (i in imagesUriList.indices) {
            val imageView = ImageView(this)
            val layoutParams = GridLayout.LayoutParams()
            layoutParams.width = (gridLayout.width - 150) / 3
            layoutParams.height = (gridLayout.width - 150) / 3

            imageView.layoutParams = layoutParams
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.setPadding(5, 5, 5, 5)
            imageView.setImageURI(imagesUriList[i])
            gridLayout.addView(imageView)
            widthImage = ((gridLayout.width - 150) / 3).toFloat()
            Log.e(TAG, widthImage.toString())


            // Get the shared preferences
            val sharedPrefs = getSharedPreferences("Number", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putFloat("myNumber", widthImage.toFloat())
            editor.apply()


            val bitmap: Bitmap = (imageView.drawable as BitmapDrawable).bitmap





            if (!dir.exists()) {
                dir.mkdirs()
            }


            val files = dir.listFiles()

            val numberOfFiles = files?.size ?: 0
            val file = File(dir, (numberOfFiles + 1).toString() + ".jpg")

            val outputStream: OutputStream = FileOutputStream(file)

// Compress the Bitmap to JPEG format with 100% quality
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

// Flush and close the output stream
            outputStream.flush()
            outputStream.close()


        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private val PICK_TXT_FILE = 1010
    private val PICK_comment_FILE = 10101
    private fun selectTxtFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "text/plain"
        }
        startActivityForResult(intent, PICK_TXT_FILE)
    }

    private fun selectCommentsFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "text/plain"
        }
        startActivityForResult(intent, PICK_comment_FILE)
    }

    private fun coment() {
        drw = UniqueAvatarImage.drawable
// Compare the drawable resource to a specific drawable resource using its resource ID
        hasnoUserIcon =
            !drw.getConstantState()!!
                .equals(getResources().getDrawable(R.drawable.no_user).getConstantState())
        Log.e(TAG, hasnoUserIcon.toString())
        if (commentField.text.toString().isNotEmpty() && nickField.text.toString()
                .isNotEmpty() && hasnoUserIcon
        ) {

            var comment = commentField.text.toString()
            var nick = nickField.text.toString()
            if (comment.isNotEmpty()) {
                listUniqueComment.add(comment)

                commentField.text = null
                val gson = Gson()
                val jsonString = gson.toJson(listUniqueComment)

// Save the JSON string in SharedPreferences
                val sharedPreferences = getSharedPreferences("UniqueComment", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("UniqueComment", jsonString)
                editor.apply()


            }
            if (nick.isNotEmpty()) {
                listUniqueNick.add(nick)


                val gson = Gson()
                val jsonString = gson.toJson(listUniqueNick)

// Save the JSON string in SharedPreferences
                val sharedPreferences = getSharedPreferences("UniqueNick", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("UniqueNick", jsonString)
                editor.apply()
            }

            boolList.add(isCheckedVerif)

            val gson = Gson()
            val json = gson.toJson(boolList)

            val sharedPrefsBool = this.getSharedPreferences("myPrefsBool", Context.MODE_PRIVATE)

            val editorBool = sharedPrefsBool.edit()

            editorBool.putString("boolListKey", json)

            editorBool.apply()


            val context = this
            val bitmap = (UniqueAvatarImage.drawable as BitmapDrawable).bitmap
            val file = File(context.externalCacheDir, "imageU.png")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            bitmapArray.add(bitmap)
            outputStream.close()


//            val sharedPreferences = getSharedPreferences("bitmapList", Context.MODE_PRIVATE)
//            val editor = sharedPreferences.edit()
//
//// Create a directory to store the bitmap files
//            val dir = File(applicationContext.filesDir, "bitmapDir")
//            dir.mkdirs()
//
//            val paths = ArrayList<String>()
//
//// Save the bitmaps to files and add the file paths to the list
//            for ((index, bitmap) in bitmapArray.withIndex()) {
//                // Create a unique file name for each bitmap
//                val fileName = "bitmap_${System.currentTimeMillis() + index}.png"
//                val file = File(dir, fileName)
//
//                // Save the bitmap to the file
//                FileOutputStream(file).use { stream ->
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
//                }
//
//                paths.add(file.absolutePath)
//            }
//
//// Store the list of file paths in SharedPreferences
//            editor.putStringSet("bitmapPaths", paths.toSet())
//            editor.apply()


            saveImUniq()

Log.e(TAG,bitmapArray.toString())
            val newCommentLayout = LayoutInflater.from(this).inflate(R.layout.comment_layout, null)
            val avatarImage = newCommentLayout.findViewById<ImageView>(R.id.avatarImage)
            val nicknameText = newCommentLayout.findViewById<TextView>(R.id.nicknameText)
            val commentText = newCommentLayout.findViewById<TextView>(R.id.commentText)
            val idUnique = newCommentLayout.findViewById<TextView>(R.id.idUnique)
            val verification = newCommentLayout.findViewById<ImageView>(R.id.verification)
            Log.e(TAG, bitmapArray.toString())
            avatarImage.setImageBitmap(bitmapArray.last())
            nicknameText.text = nick
            commentText.text = comment
            idUnique.setText(bitmapArray.size.toString())
            if (isCheckedVerif) {
                verification.alpha = 1F
            } else {
                verification.alpha = 0F;
            }

            commentLayout.addView(newCommentLayout)
            scrollView.post {
                scrollView.fullScroll(View.FOCUS_DOWN)
            }


        } else {
            Toast.makeText(this, "Заполните все поля и загружайте аватарку", Toast.LENGTH_LONG)
                .show()
        }

        commentField.text = null
    }

    private fun saveImUniq() {


        val sharedPrefs = getSharedPreferences("bitmapList", Context.MODE_PRIVATE)

        val paths = ArrayList<String>()
// Create a directory to store the bitmap files
        val dir = File(applicationContext.filesDir, "bitmapDir")
        if (!dir.exists()) {
            dir.mkdir()
        }

        // Define a coroutine scope
        CoroutineScope(Dispatchers.Default).launch {
            // Iterate through each bitmap in the array
            bitmapArray.forEach { bitmap ->
                // Create a unique file name for each bitmap
                val fileName = "bitmap_${System.currentTimeMillis()}.png"
                val file = File(dir, fileName)

                // Save the bitmap to the file in a background thread
                withContext(Dispatchers.IO) {
                    val stream = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    stream.close()
                }

                paths.add(file.absolutePath)
            }

            // Store the list of file paths in SharedPreferences in the main thread
            withContext(Dispatchers.Main) {
                val editor = sharedPrefs.edit()
                editor.putInt("bitmapCount", paths.size)
                for (i in paths.indices) {
                    editor.putString("bitmap_$i", paths[i])
                }
                editor.apply()
            }
        }

    }

}



