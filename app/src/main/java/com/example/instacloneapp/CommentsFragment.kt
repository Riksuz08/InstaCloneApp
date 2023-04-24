package com.example.instacloneapp

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Suppress("DEPRECATION")
class CommentsFragment : Fragment() {
    lateinit var commentField: EditText;
    var listComment = mutableListOf<String>()
    lateinit var CommentTextView: MaterialTextView

    lateinit var adcom: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addCommentButton = view.findViewById<Button>(R.id.addCommentButton)
        val deleteCommentButton = view.findViewById<Button>(R.id.deleteCommentButton)

        adcom = view.findViewById(R.id.addCommentTxt)

        val sharedPrefLang = requireContext().getSharedPreferences("lang", Context.MODE_PRIVATE)
        val savedEditTextValueLang = sharedPrefLang.getString("edit_text_value", "")

        if (savedEditTextValueLang == "") {

            adcom.text = "Добавить/Удалить сообщение"
            addCommentButton.text = "Добавить"
            deleteCommentButton.text = "Удалить"

        } else {
            if (savedEditTextValueLang == "РУ") {

                adcom.text = "Добавить/Удалить сообщение"
                addCommentButton.text = "Добавить"
                deleteCommentButton.text = "Удалить"

            } else {

                adcom.text = "Add/Delete comment"
                addCommentButton.text = "Add"

                deleteCommentButton.text = "Delete"

            }
        }


        CommentTextView = view.findViewById(R.id.commentTxt)
        // Retrieve the JSON string from SharedPreferences
        val sharedPreferencesComment =
            requireContext().getSharedPreferences("comment", Context.MODE_PRIVATE)
        val jsonStringComment = sharedPreferencesComment.getString("comment", null)

// Convert the JSON string back to a MutableList using Gson
        val gsonComment = Gson()
        if (jsonStringComment == null) {
            Log.e(ContentValues.TAG, "okkk")
        } else {

            listComment = gsonComment.fromJson(
                jsonStringComment,
                object : TypeToken<MutableList<String>>() {}.type
            )

            Log.e(ContentValues.TAG, listComment.toString())
// Display the MutableList in a TextView

            var result = ""
            for ((index, element) in listComment.withIndex()) {
                result += (index + 1).toString() + ". $element\n"
            }
            CommentTextView.text = result

        }

        addCommentButton.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.add_nick_layout)

            val width = resources.displayMetrics.widthPixels * 0.99 // adjust this value as needed
            val height = resources.displayMetrics.heightPixels * 0.5 // adjust this value as needed
            dialog.window?.setLayout(width.toInt(), height.toInt())

            val topNameDialogtxt=dialog.findViewById<TextView>(R.id.topNameDialog)
            val saveButton = dialog.findViewById<Button>(R.id.dialogSaveBtn)
            val cancelButton = dialog.findViewById<Button>(R.id.dialogCancelBtn)
            val selFileButton = dialog.findViewById<Button>(R.id.addFile)
            val selfileinfo=dialog.findViewById<TextView>(R.id.selfileinfo)
            selFileButton.setOnClickListener {
                selectCommentsFile()

                dialog.dismiss()
            }
            commentField = dialog.findViewById(R.id.dialogtxtNick)
            if (savedEditTextValueLang == "") {
                selFileButton.text="Выбрать файл"
                selfileinfo.text="При нажатии на 'Выбрать файл' вы можете сразу добавить несколько комментарий. Для этого выберите .txt файл, где никнеймы перечисляются с запятыми."
                topNameDialogtxt.text="Добавить сообщение"
                saveButton.text = "Сохранить"
//                cancelButton.text = "Отменить"


            } else {
                if (savedEditTextValueLang == "РУ") {
                    selFileButton.text="Выбрать файл"
                    selfileinfo.text="При нажатии на 'Выбрать файл' вы можете сразу добавить несколько комментарий. Для этого выберите .txt файл, где никнеймы перечисляются с запятыми."
                    topNameDialogtxt.text="Добавить сообщение"
                    saveButton.text = "Сохранить"
//                    cancelButton.text = "Отменить"

                } else {
                    selFileButton.text="Select file"
                    topNameDialogtxt.text="Add comment"
                    saveButton.text = "Save"
                    selfileinfo.text="When you click on 'Select file', you can immediately add multiple comments. To do this, select a .txt file where the nicknames are listed separated by commas."
//                    cancelButton.text = "Cancel"

                }
            }
            saveButton.setOnClickListener {

                var text = commentField.text.toString()
                if (text.isNotEmpty()) {
                    listComment.add(text)

                    CommentTextView.append(listComment.size.toString() + ".$text\n")

                    val gson = Gson()
                    val jsonString = gson.toJson(listComment)

// Save the JSON string in SharedPreferences
                    val sharedPreferences =
                        requireContext().getSharedPreferences("comment", Context.MODE_PRIVATE)
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


            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.delete_nick_layout)

            val width = resources.displayMetrics.widthPixels * 0.99 // adjust this value as needed
            val height = resources.displayMetrics.heightPixels * 0.35 // adjust this value as needed
            dialog.window?.setLayout(width.toInt(), height.toInt())

            val topNameDialogDel = dialog.findViewById<TextView>(R.id.topNameDialogDel)
            val saveButton = dialog.findViewById<Button>(R.id.dialogSaveBtn)
            val cancelButton = dialog.findViewById<Button>(R.id.dialogCancelBtn)

            if (savedEditTextValueLang == "") {
                topNameDialogDel.text="Удалить сообщение"
                saveButton.text = "Удалить"
//                cancelButton.text = "Отменить"


            } else {
                if (savedEditTextValueLang == "РУ") {
                    topNameDialogDel.text="Удалить сообщение"
                    saveButton.text = "Удалить"
//                    cancelButton.text = "Отменить"

                } else {
                    topNameDialogDel.text="Delete comment"
                    saveButton.text = "Delete"
//                    cancelButton.text = "Cancel"

                }
            }

            commentField = dialog.findViewById(R.id.dialogtxtNick)
            commentField.hint = "1"

            saveButton.setOnClickListener {

                var text = commentField.text.toString()
                if (text.isNotEmpty()) {

                    if (!text.matches("-?\\d+(\\.\\d+)?".toRegex())) {
                        Toast.makeText(
                            requireContext(),
                            "Заполняйте только числами!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (text.toInt() < 1 || text.toInt() > listComment.size) {
                        Toast.makeText(
                            requireContext(),
                            "Нету такого элемента!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        listComment.removeAt(text.toInt() - 1)


                        var result = ""
                        for ((index, element) in listComment.withIndex()) {
                            result += (index + 1).toString() + ". $element\n"
                        }
                        CommentTextView.text = result

                        val gson = Gson()
                        val jsonString = gson.toJson(listComment)

// Save the JSON string in SharedPreferences
                        val sharedPreferences =
                            requireContext().getSharedPreferences("comment", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("comment", jsonString)
                        editor.apply()

                        dialog.dismiss()

                    }
                }else{
                    Toast.makeText(requireContext(), "Пустое поле", Toast.LENGTH_SHORT).show()

                }
                Log.e(ContentValues.TAG, listComment.toString())



            }


            cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

    }
        private val PICK_comment_FILE = 10101
        private fun selectCommentsFile() {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "text/plain"
            }
            startActivityForResult(intent, PICK_comment_FILE)
        }




}