package com.example.instacloneapp

import android.Manifest
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

@Suppress("DEPRECATION", "DEPRECATION")
class UniqueComments : Fragment() {
    lateinit var UniqueAvatarImage: ImageView
    lateinit var addUniqueCommentButton: Button
    lateinit var deleteUniqueCommentButton: Button
    lateinit var switchUniqueComment: Switch

    lateinit var uncom: TextView
    lateinit var dirx:File
    lateinit var nickField: EditText;
    lateinit var drw: Drawable;
    lateinit var commentField: EditText;
    var hasnoUserIcon: Boolean = true
    var isCheckedVerif: Boolean = false
    var listUniqueComment = mutableListOf<String>()
    var listUniqueNick = mutableListOf<String>()
    val bitmapArray = ArrayList<Bitmap>()
    var boolList = mutableListOf<Boolean>()
    lateinit var scrollView: NestedScrollView
    lateinit var commentLayout: LinearLayout
    private val REQUEST_CODE_PICK_IMAGE = 201
    private val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 202
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_unique_comments, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        switchUniqueComment = view.findViewById(R.id.switchUniqueComments)
        addUniqueCommentButton = view.findViewById(R.id.adduniqueAvatarButton)
        deleteUniqueCommentButton = view.findViewById(R.id.deleteUniqueAvatarButton)
        scrollView = view.findViewById(R.id.UniqueCommentScroll)
        commentLayout = view.findViewById(R.id.commentLayout)
        dirx= File(requireContext().cacheDir.path+"/InstaUnique")
        bitmapArray.clear()
        val sharedPrefLang = requireContext().getSharedPreferences("lang", Context.MODE_PRIVATE)
        val savedEditTextValueLang = sharedPrefLang.getString("edit_text_value", "")

        uncom = view.findViewById(R.id.uniqueAvatarTxt)
        if (savedEditTextValueLang == "") {

            uncom.text = "Добавить/Удалить уникальный комментарий"
            addUniqueCommentButton.text = "Добавить"
            deleteUniqueCommentButton.text = "Удалить"

        } else {
            if (savedEditTextValueLang == "РУ") {
                uncom.text = "Добавить/Удалить уникальный комментарий"
                addUniqueCommentButton.text = "Добавить"
                deleteUniqueCommentButton.text = "Удалить"

            } else {

                uncom.text = "Add/Delete unique comment"
                addUniqueCommentButton.text = "Add"
                deleteUniqueCommentButton.text = "Delete"

            }
        }

        // Get the SharedPreferences object
        var switchBool: Boolean
        val sharedPrefsSwitchBool = requireContext().getSharedPreferences("switchBool", Context.MODE_PRIVATE)

// Retrieve the boolean value
        switchBool = sharedPrefsSwitchBool.getBoolean("myBoolean", false)

        switchUniqueComment.isChecked = switchBool
        switchUniqueComment.setOnClickListener {
            switchBool = switchUniqueComment.isChecked
            // Get the SharedPreferences object
            Log.e(ContentValues.TAG, switchBool.toString())
            val sharedPrefs = requireContext().getSharedPreferences("switchBool", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putBoolean("myBoolean", switchBool)
            editor.apply()
        }


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
                    val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
                    bitmapArrayB.add(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        bitmapArray.addAll(bitmapArrayB)


// Retrieve the JSON string from SharedPreferences
        val sharedPreferencesUnique = requireContext().getSharedPreferences("UniqueNick", Context.MODE_PRIVATE)
        val jsonStringUnique = sharedPreferencesUnique.getString("UniqueNick", null)

// Convert the JSON string back to a MutableList using Gson
        val gsonUnique = Gson()
        if (jsonStringUnique == null) {
            Log.e(ContentValues.TAG, "okkk")
        } else {

            listUniqueNick =
                gsonUnique.fromJson(
                    jsonStringUnique,
                    object : TypeToken<MutableList<String>>() {}.type
                )

            Log.e(ContentValues.TAG, listUniqueNick.toString())
// Display the MutableList in a TextView
        }

        // Retrieve the JSON string from SharedPreferences
        val sharedPreferencesUniqueCom = requireContext().getSharedPreferences("UniqueComment", Context.MODE_PRIVATE)
        val jsonStringUniqueCom = sharedPreferencesUniqueCom.getString("UniqueComment", null)

// Convert the JSON string back to a MutableList using Gson
        val gsonUniqueCom = Gson()
        if (jsonStringUniqueCom == null) {
            Log.e(ContentValues.TAG, "okkk")
        } else {

            listUniqueComment =
                gsonUniqueCom.fromJson(
                    jsonStringUniqueCom,
                    object : TypeToken<MutableList<String>>() {}.type
                )

            Log.e(ContentValues.TAG, listUniqueComment.toString())
// Display the MutableList in a TextView
        }


        // Get shared preferences object
        val sharedPrefsBool = requireContext().getSharedPreferences("myPrefsBool", Context.MODE_PRIVATE)

// Retrieve boolean list from shared preferences
        val jsonBool = sharedPrefsBool.getString("boolListKey", null)
        val gsonBool = Gson()
        if (jsonBool == null) {
        } else {
            boolList = gsonBool.fromJson(jsonBool, object : TypeToken<List<Boolean>>() {}.type)
        }




        for (i in 0 until bitmapArray.size) {

            val newCommentLayout = LayoutInflater.from(requireContext()).inflate(R.layout.comment_layout, null)
            val avatarImage = newCommentLayout.findViewById<ImageView>(R.id.avatarImage)
            val nicknameText = newCommentLayout.findViewById<TextView>(R.id.nicknameText)
            val commentText = newCommentLayout.findViewById<TextView>(R.id.commentText)
            val idUnique = newCommentLayout.findViewById<TextView>(R.id.idUnique)
            val verification = newCommentLayout.findViewById<ImageView>(R.id.verification)
            Log.e(ContentValues.TAG, "bitmapArray " + bitmapArray.toString())
            Log.e(ContentValues.TAG, listUniqueNick.toString())
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


        val dialogU = Dialog(requireContext())
        dialogU.setContentView(R.layout.unique_comment)
        UniqueAvatarImage = dialogU.findViewById<ImageView>(R.id.avatarImage)


        Log.e(ContentValues.TAG, listUniqueNick.toString())
        Log.e(ContentValues.TAG, listUniqueComment.toString())
        Log.e(ContentValues.TAG, bitmapArray.toString())

        addUniqueCommentButton.setOnClickListener {

            val width = resources.displayMetrics.widthPixels * 0.99 // adjust this value as needed
            val height = resources.displayMetrics.heightPixels * 0.5 // adjust this value as needed
            dialogU.window?.setLayout(width.toInt(), height.toInt())

            val topUniq=dialogU.findViewById<TextView>(R.id.topUniq)
            val saveButton = dialogU.findViewById<Button>(R.id.saveUnique)
            val cancelButton = dialogU.findViewById<Button>(R.id.cancelUnique)
            val verifCheck = dialogU.findViewById<CheckBox>(R.id.verificationCheck)
            val addCont = dialogU.findViewById<Button>(R.id.addContinue)
            nickField = dialogU.findViewById(R.id.nicknameText)
            commentField = dialogU.findViewById(R.id.commentText)
            if (savedEditTextValueLang == "") {
                saveButton.text = "Сохранить"
                topUniq.text="Уникальный комментарий"
//                cancelButton.text = "Отменить"
                nickField.hint = "Никнейм"
                commentField.hint = "Коммент"


            } else {
                if (savedEditTextValueLang == "РУ") {
                    saveButton.text = "Сохранить"
                    topUniq.text="Уникальный комментарий"
//                    cancelButton.text = "Отменить"
                    nickField.hint = "Никнейм"
                    commentField.hint = "Коммент"
                } else {
                    saveButton.text = "Save"
                    topUniq.text="Unique comment"
//                    cancelButton.text = "Cancel"
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
                Log.e(ContentValues.TAG, hasnoUserIcon.toString())
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
                            requireContext().getSharedPreferences("UniqueComment", Context.MODE_PRIVATE)
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
                            requireContext().getSharedPreferences("UniqueNick", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("UniqueNick", jsonString)
                        editor.apply()
                    }

                    boolList.add(isCheckedVerif)

                    val gson = Gson()
                    val json = gson.toJson(boolList)

                    val sharedPrefsBool =
                        requireContext().getSharedPreferences("myPrefsBool", Context.MODE_PRIVATE)

                    val editorBool = sharedPrefsBool.edit()

                    editorBool.putString("boolListKey", json)

                    editorBool.apply()


                    val bitmap: Bitmap = (UniqueAvatarImage.drawable as BitmapDrawable).bitmap





                    if (!dirx.exists()) {
                        dirx.mkdirs()
                    }


                    val files = dirx.listFiles()

                    val numberOfFiles = files?.size ?: 0
                    val file = File(dirx, (numberOfFiles + 1).toString() + ".jpg")

                    val outputStream: OutputStream = FileOutputStream(file)

// Compress the Bitmap to JPEG format with 100% quality
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

// Flush and close the output stream
                    outputStream.flush()
                    outputStream.close()


                    bitmapArray.add(bitmap)

                    Log.e(ContentValues.TAG,bitmapArray.toString()+"in")


                    val newCommentLayout =
                        LayoutInflater.from(requireContext()).inflate(R.layout.comment_layout, null)
                    val avatarImage = newCommentLayout.findViewById<ImageView>(R.id.avatarImage)
                    val nicknameText = newCommentLayout.findViewById<TextView>(R.id.nicknameText)
                    val commentText = newCommentLayout.findViewById<TextView>(R.id.commentText)
                    val idUnique = newCommentLayout.findViewById<TextView>(R.id.idUnique)
                    val verification = newCommentLayout.findViewById<ImageView>(R.id.verification)
                    Log.e(ContentValues.TAG, bitmapArray.toString())
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


                }
                else {
                    if(savedEditTextValueLang=="" || savedEditTextValueLang=="РУ") {
                    Toast.makeText(requireContext(), "Заполните все поля и загружайте аватарку!", Toast.LENGTH_LONG)
                        .show()
                    }else{
                        Toast.makeText(requireContext(), "Fill in all fields and upload your avatar!", Toast.LENGTH_LONG).show()
                    }
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
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
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




                Log.e(ContentValues.TAG, commentField.text.toString().isNotEmpty().toString())
                Log.e(ContentValues.TAG, nickField.text.toString().isNotEmpty().toString())
                Log.e(ContentValues.TAG, (UniqueAvatarImage != null).toString())

                drw = UniqueAvatarImage.drawable
// Compare the drawable resource to a specific drawable resource using its resource ID
                hasnoUserIcon =
                    !drw.getConstantState()!!
                        .equals(getResources().getDrawable(R.drawable.no_user).getConstantState())
                Log.e(ContentValues.TAG, hasnoUserIcon.toString())
                if (commentField.text.toString().isNotEmpty() && nickField.text.toString()
                        .isNotEmpty() && hasnoUserIcon
                ) {
                    coment()
                    dialogU.dismiss()

                }
                else {
                if(savedEditTextValueLang=="" || savedEditTextValueLang=="РУ") {
                    Toast.makeText(requireContext(), "Заполните все поля и загружайте аватарку!", Toast.LENGTH_LONG)
                        .show()
                }else{
                    Toast.makeText(requireContext(), "Fill in all fields and upload your avatar!", Toast.LENGTH_LONG).show()
                }
            }
            }


            cancelButton.setOnClickListener {
                dialogU.dismiss()
            }

            dialogU.show()

        }


        deleteUniqueCommentButton.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.delete_nick_layout)

            val width = resources.displayMetrics.widthPixels * 0.99 // adjust this value as needed
            val height = resources.displayMetrics.heightPixels * 0.35 // adjust this value as needed
            dialog.window?.setLayout(width.toInt(), height.toInt())


            val saveButton = dialog.findViewById<Button>(R.id.dialogSaveBtn)
            val cancelButton = dialog.findViewById<Button>(R.id.dialogCancelBtn)
            val topNameDialogDel = dialog.findViewById<TextView>(R.id.topNameDialogDel)
            if (savedEditTextValueLang == "") {
                topNameDialogDel.text="Удалить комментарий"
                saveButton.text = "Удалить"
//                cancelButton.text = "Отменить"


            } else {
                if (savedEditTextValueLang == "РУ") {
                    topNameDialogDel.text="Удалить комментарий"
                    saveButton.text = "Удалить"
//                    cancelButton.text = "Отменить"

                } else {
                    topNameDialogDel.text="Delete comment"
                    saveButton.text = "Delete"
//                    cancelButton.text = "Cancel"

                }
            }


            nickField = dialog.findViewById(R.id.dialogtxtNick)
            nickField.text = null
            nickField.hint = "1"

            saveButton.setOnClickListener {

                var text = nickField.text.toString()
                if (text.isNotEmpty()) {
                    if (!text.matches("-?\\d+(\\.\\d+)?".toRegex())) {
                        if(savedEditTextValueLang=="" || savedEditTextValueLang=="РУ") {
                            Toast.makeText(requireContext(), "Заполните только числами!", Toast.LENGTH_SHORT)
                                .show()
                        }else{
                            Toast.makeText(requireContext(), "Fill in only with numbers!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else if (text.toInt() < 1 || text.toInt() >listUniqueNick.size) {
                        if(savedEditTextValueLang=="" || savedEditTextValueLang=="РУ") {
                            Toast.makeText(requireContext(), "Нету такого элемента!", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(requireContext(), "There is no such element!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                    listUniqueComment.removeAt(text.toInt() - 1)
                    listUniqueNick.removeAt(text.toInt() - 1)



                    val fileName = text + ".jpg"
                    val folder = dirx
                    Log.e(ContentValues.TAG, folder.toString())
                    val fileToDelete = File(folder, fileName)
                    Log.e(ContentValues.TAG, fileToDelete.absolutePath)
                    Log.e(ContentValues.TAG,fileToDelete.exists().toString())
                    if (fileToDelete.exists()) {
                        val isDeleted = fileToDelete.delete()
                        Log.e(ContentValues.TAG, isDeleted.toString())
                        val folder = File(requireContext().cacheDir.path, "InstaUnique")
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

                    }

                    boolList.removeAt(text.toInt() - 1)
                    val gson = Gson()
                    val jsonString = gson.toJson(listUniqueComment)

// Save the JSON string in SharedPreferences
                    val sharedPreferences =
                        requireContext().getSharedPreferences("UniqueComment", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("UniqueComment", jsonString)
                    editor.apply()


                    val gsonBool = Gson()
                    val json = gsonBool.toJson(boolList)

                    val sharedPrefsBool =
                        requireContext().getSharedPreferences("myPrefsBool", Context.MODE_PRIVATE)

                    val editorBool = sharedPrefsBool.edit()

                    editorBool.putString("boolListKey", json)

                    editorBool.apply()

                    val gsonNick = Gson()
                    val jsonStringNick = gsonNick.toJson(listUniqueComment)

// Save the JSON string in SharedPreferences
                    val sharedPreferencesNick =
                        requireContext().getSharedPreferences("UniqueNick", Context.MODE_PRIVATE)
                    val editorNick = sharedPreferencesNick.edit()
                    editorNick.putString("UniqueNick", jsonStringNick)
                    editorNick.apply()

                    bitmapArray.clear()
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
                                val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
                                bitmapArrayB.add(bitmap)
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }

                    bitmapArray.addAll(bitmapArrayB)




                    Log.e(ContentValues.TAG, bitmapArray.toString())

                        dialog.dismiss()
                }
                commentLayout.removeAllViews()
                for (i in 0 until bitmapArray.size) {

                    val newCommentLayout =
                        LayoutInflater.from(requireContext()).inflate(R.layout.comment_layout, null)
                    val avatarImage = newCommentLayout.findViewById<ImageView>(R.id.avatarImage)
                    val nicknameText = newCommentLayout.findViewById<TextView>(R.id.nicknameText)
                    val commentText = newCommentLayout.findViewById<TextView>(R.id.commentText)
                    val verification = newCommentLayout.findViewById<ImageView>(R.id.verification)
                    val idUnique = newCommentLayout.findViewById<TextView>(R.id.idUnique)
                    Log.e(ContentValues.TAG, bitmapArray.toString())
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


                }else{
                    if(savedEditTextValueLang=="" || savedEditTextValueLang=="РУ") {
                        Toast.makeText(requireContext(), "Пустое поле!", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(), "Empty field!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }


            cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {
            val imageUri = data?.data
            UniqueAvatarImage.setImageURI(imageUri)


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
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun coment() {
        drw = UniqueAvatarImage.drawable
// Compare the drawable resource to a specific drawable resource using its resource ID
        hasnoUserIcon =
            !drw.getConstantState()!!
                .equals(getResources().getDrawable(R.drawable.no_user).getConstantState())
        Log.e(ContentValues.TAG, hasnoUserIcon.toString())
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
                val sharedPreferences = requireContext().getSharedPreferences("UniqueComment", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("UniqueComment", jsonString)
                editor.apply()


            }
            if (nick.isNotEmpty()) {
                listUniqueNick.add(nick)


                val gson = Gson()
                val jsonString = gson.toJson(listUniqueNick)

// Save the JSON string in SharedPreferences
                val sharedPreferences = requireContext().getSharedPreferences("UniqueNick", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("UniqueNick", jsonString)
                editor.apply()
            }

            boolList.add(isCheckedVerif)

            val gson = Gson()
            val json = gson.toJson(boolList)

            val sharedPrefsBool = requireContext().getSharedPreferences("myPrefsBool", Context.MODE_PRIVATE)

            val editorBool = sharedPrefsBool.edit()

            editorBool.putString("boolListKey", json)

            editorBool.apply()



            saveImUniq()

            Log.e(ContentValues.TAG,bitmapArray.toString())
            val newCommentLayout = LayoutInflater.from(requireContext()).inflate(R.layout.comment_layout, null)
            val avatarImage = newCommentLayout.findViewById<ImageView>(R.id.avatarImage)
            val nicknameText = newCommentLayout.findViewById<TextView>(R.id.nicknameText)
            val commentText = newCommentLayout.findViewById<TextView>(R.id.commentText)
            val idUnique = newCommentLayout.findViewById<TextView>(R.id.idUnique)
            val verification = newCommentLayout.findViewById<ImageView>(R.id.verification)
            Log.e(ContentValues.TAG, bitmapArray.toString())
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
            Toast.makeText(requireContext(), "Заполните все поля и загружайте аватарку", Toast.LENGTH_LONG)
                .show()
        }

        commentField.text = null
    }

    private fun saveImUniq() {

        val bitmap: Bitmap = (UniqueAvatarImage.drawable as BitmapDrawable).bitmap





        if (!dirx.exists()) {
            dirx.mkdirs()
        }


        val files = dirx.listFiles()

        val numberOfFiles = files?.size ?: 0
        val file = File(dirx, (numberOfFiles + 1).toString() + ".jpg")

        val outputStream: OutputStream = FileOutputStream(file)

// Compress the Bitmap to JPEG format with 100% quality
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

// Flush and close the output stream
        outputStream.flush()
        outputStream.close()


        bitmapArray.add(bitmap)
    }
}