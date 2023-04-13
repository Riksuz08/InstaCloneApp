package com.example.instacloneapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
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
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.transition.Transition
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.yalantis.ucrop.UCrop

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.ArrayList

@Suppress("DEPRECATION")
class start_activity : AppCompatActivity() {

    lateinit var username: EditText;
    lateinit var startButton: Button;
    lateinit var saveusername: Button;
    lateinit var avatarImage: ImageView;
    private val REQUEST_CODE_PICK_IMAGE = 101
    private val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 102
   lateinit var settingsButton:ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        username = findViewById(R.id.username);
        startButton = findViewById(R.id.startlivebutton);
        saveusername = findViewById(R.id.saveusername);
        avatarImage = findViewById(R.id.avatarstart)
       settingsButton=findViewById(R.id.settings)

        val sharedPrefLang = getSharedPreferences("lang", Context.MODE_PRIVATE)
        val savedEditTextValueLang = sharedPrefLang.getString("edit_text_value", "")


        if(savedEditTextValueLang==""){
            saveusername.text="Сохранить"
            startButton.text="Прямой эфир"
            username.hint="Никнейм"
        }else {
            if (savedEditTextValueLang == "РУ") {
                saveusername.text="Сохранить"
                startButton.text="Прямой эфир"
                username.hint="Никнейм"
            } else {
                saveusername.text="Save"
                startButton.text="Live"
                username.hint="Nickname"
            }
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 200)
        }




        settingsButton.setOnClickListener{
            val intent = Intent(this, settings::class.java)
            startActivity(intent)

        }

        startButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }



        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val imagePath = sharedPreferences.getString("imagePath", null)

        if (imagePath != null) {

            val bitmap = BitmapFactory.decodeFile(imagePath)
            avatarImage.setImageBitmap(bitmap)
        }


        avatarImage.setOnClickListener {


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
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
//                val intent =
//                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
            }


        }



        saveusername.setOnClickListener {

            val editTextValue = username.text.toString().lowercase();

            // Сохраняем текст в SharedPreferences
            val sharedPref = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("edit_text_value", editTextValue)
                apply()
            }

            // Скрываем клавиатуру
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(saveusername.windowToken, 0)

        }
        // Получаем сохраненное значение из SharedPreferences
        val sharedPref = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val savedEditTextValue = sharedPref.getString("edit_text_value", "")

        // Устанавливаем сохраненное значение в EditText
        username.setText(savedEditTextValue)
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

    //            val inputStream = contentResolver.openInputStream(imageUri!!)
//            val bitmapX = BitmapFactory.decodeStream(inputStream)
//
//            val byteArrayOutputStream = ByteArrayOutputStream()
//            bitmapX.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
//            val byteArray = byteArrayOutputStream.toByteArray()
//
//            val decoded = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//            avatarImage.setImageBitmap(decoded)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            val imageUri = data?.data
            if (imageUri != null) {
                // Start crop activity
                CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this)
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            // Handle crop result
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val croppedImageUri = result.uri
                avatarImage.setImageURI(croppedImageUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                // Handle error
                val error = result.error
                Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
            }

//            avatarImage.setImageURI(imageUri)

            val context=this
            val bitmap = (avatarImage.drawable as BitmapDrawable).bitmap
            val file = File(context.externalCacheDir, "image.png")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()

            val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("imagePath", file.absolutePath)
            editor.apply()

        }

    }


}




