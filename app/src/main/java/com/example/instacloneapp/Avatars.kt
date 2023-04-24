package com.example.instacloneapp

import android.Manifest
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.properties.Delegates

@Suppress("DEPRECATION")
class Avatars : Fragment() {
lateinit var gridLayout:GridLayout
lateinit var avatarField:EditText
    private val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 202
    private val REQUEST_CODE_PICK_IMAGE = 201
    private var widthImage =300F
    lateinit var dir:File;
    lateinit var adava: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_avatars, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adava = view.findViewById(R.id.addAvatarTxt)

        dir =
            File(requireContext().cacheDir.path + "/InstagramLive")

        val addAvatarButton = view.findViewById<Button>(R.id.addAvatarButton)
        gridLayout = view.findViewById(R.id.gridLayout)
       val deleteAvatarButton = view.findViewById<Button>(R.id.deleteAvatarButton)


        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
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
            Log.e(ContentValues.TAG, uriList.toString())

        }
        for (i in uriList.indices) {
            val imageView = ImageView(requireContext())
            val layoutParams = GridLayout.LayoutParams()
            layoutParams.width = widthImage.toInt()
            layoutParams.height = widthImage.toInt()
            imageView.layoutParams = layoutParams
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.setPadding(5, 5, 5, 5)
            imageView.setImageURI(uriList[i])
            gridLayout.addView(imageView)

        }





        val sharedPrefLang = requireContext().getSharedPreferences("lang", Context.MODE_PRIVATE)
        val savedEditTextValueLang = sharedPrefLang.getString("edit_text_value", "")

        if (savedEditTextValueLang == "") {

            adava.text = "Добавить/Удалить аватарку"

            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.add_nick_layout)
            dialog.findViewById<Button>(R.id.dialogSaveBtn).text = "Сохранить"
            dialog.findViewById<Button>(R.id.dialogCancelBtn).text = "Отменить"
        } else {
            if (savedEditTextValueLang == "РУ") {

                adava.text = "Добавить/Удалить аватарку"

                addAvatarButton.text = "Добавить"
                deleteAvatarButton.text = "Удалить"
                val dialog = Dialog(requireContext())
                dialog.setContentView(R.layout.add_nick_layout)
                dialog.findViewById<Button>(R.id.dialogSaveBtn).text = "Сохранить"
                dialog.findViewById<Button>(R.id.dialogCancelBtn).text = "Отменить"
            } else {

                adava.text = "Add/Delete avatar"

                addAvatarButton.text = "Add"
                deleteAvatarButton.text = "Delete"
                val dialog = Dialog(requireContext())
                dialog.setContentView(R.layout.add_nick_layout)
                dialog.findViewById<Button>(R.id.dialogSaveBtn).text = "Save"
                dialog.findViewById<Button>(R.id.dialogCancelBtn).text = "Cancel"
            }
        }


        val sharedPrefs = requireContext().getSharedPreferences("Number", Context.MODE_PRIVATE)
        val myNumber = sharedPrefs.getFloat("myNumber", 300F)
        widthImage = myNumber
        addAvatarButton.setOnClickListener {

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
        }

        deleteAvatarButton.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.delete_nick_layout)

            val width = resources.displayMetrics.widthPixels * 0.99 // adjust this value as needed
            val height = resources.displayMetrics.heightPixels * 0.35 // adjust this value as needed
            dialog.window?.setLayout(width.toInt(), height.toInt())


            val saveButton = dialog.findViewById<Button>(R.id.dialogSaveBtn)
            val cancelButton = dialog.findViewById<Button>(R.id.dialogCancelBtn)
            val topNameDialogDel = dialog.findViewById<TextView>(R.id.topNameDialogDel)
            if (savedEditTextValueLang == "") {
                topNameDialogDel.text="Удалить аватарку"
                saveButton.text = "Удалить"
//                cancelButton.text = "Отменить"


            } else {
                if (savedEditTextValueLang == "РУ") {
                    topNameDialogDel.text="Удалить аватарку"
                    saveButton.text = "Удалить"
//                    cancelButton.text = "Отменить"

                } else {
                    topNameDialogDel.text="Delete avatar"
                    saveButton.text = "Delete"
//                    cancelButton.text = "Cancel"

                }
            }

            avatarField = dialog.findViewById(R.id.dialogtxtNick)
            avatarField.hint = "1"

            saveButton.setOnClickListener {

                val folder =  File(requireContext().cacheDir.path , "InstagramLive")
                val files = folder.listFiles()
                var text = avatarField.text.toString()
                if (text.isNotEmpty()) {


                    if (!text.matches("-?\\d+(\\.\\d+)?".toRegex())) {
                        Toast.makeText(
                            requireContext(),
                            "Заполняйте только числами!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (text.toInt() < 1 || text.toInt() >folder.list().size) {
                        Toast.makeText(
                            requireContext(),
                            "Нету такого элемента!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                    val fileName = text + ".jpg"
                    Log.e(ContentValues.TAG, fileName)
                    val folder = dir
                    Log.e(ContentValues.TAG, folder.toString())
                    val fileToDelete = File(folder, fileName)
                    Log.e(ContentValues.TAG, fileToDelete.absolutePath)
                    Log.e(ContentValues.TAG,fileToDelete.exists().toString())
                    if (fileToDelete.exists()) {
                        val isDeleted = fileToDelete.delete()
                        Log.e(ContentValues.TAG,isDeleted.toString())


                        for (i in 0 until files.size) {
                            val file = files[i]
                            var a = i + 1
                            val fileName = "$a.jpg"
                            val newFile = File(folder, fileName)
                            if (file.exists()) {
                                file.renameTo(newFile)
                            }
                        }


                        Log.e(ContentValues.TAG, isDeleted.toString())
                        if (isDeleted) {
                            gridLayout.removeAllViews()

                            val fileList = dir.listFiles()
                            val uriList = ArrayList<Uri>()
                            if (fileList != null) {

                                for (file in fileList) {
                                    val uri = Uri.fromFile(file)
                                    uriList.add(uri)
                                }
                                Log.e(ContentValues.TAG, uriList.toString())

                            }
                            for (i in uriList.indices) {
                                val imageView = ImageView(requireContext())

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
                            Log.e(ContentValues.TAG, uriList.toString())
                        }
                    } else {
                        // file does not exist
                    }
                        dialog.dismiss()

                }
                }else{
                    Toast.makeText(requireContext(), "Пустое поле", Toast.LENGTH_SHORT).show()

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

        if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK) {
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

    }
    private fun setImagesToGridLayout(imagesUriList: ArrayList<Uri>) {
        for (i in imagesUriList.indices) {
            val imageView = ImageView(requireContext())
            val layoutParams = GridLayout.LayoutParams()
            layoutParams.width = (gridLayout.width - 150) / 3
            layoutParams.height = (gridLayout.width - 150) / 3

            imageView.layoutParams = layoutParams
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.setPadding(5, 5, 5, 5)
            imageView.setImageURI(imagesUriList[i])
            gridLayout.addView(imageView)
            widthImage = ((gridLayout.width - 150) / 3).toFloat()
            Log.e(ContentValues.TAG, widthImage.toString())


            // Get the shared preferences
            val sharedPrefs = requireContext().getSharedPreferences("Number", Context.MODE_PRIVATE)
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
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

}