package com.example.instacloneapp

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Suppress("DEPRECATION")
class UsernameFragment : Fragment() {

    lateinit var nickField: EditText;
    var list = mutableListOf<String>()
    lateinit var NickTextView: MaterialTextView;
    lateinit var deleteNickButton: Button
    lateinit var adnik: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_username, container, false)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adnik = view.findViewById(R.id.addNik)

        val addNickButton = view.findViewById<Button>(R.id.addNickButton)
        NickTextView = view.findViewById(R.id.nickTxt)
        deleteNickButton = view.findViewById(R.id.deleteNickButton)

        val sharedPrefLang = requireContext().getSharedPreferences("lang", Context.MODE_PRIVATE)
        val savedEditTextValueLang = sharedPrefLang.getString("edit_text_value", "")

        if (savedEditTextValueLang == "") {
            adnik.text = "Добавить/Удалить никнейм"
            addNickButton.text = "Добавить"
            deleteNickButton.text = "Удалить"

        } else {
            if (savedEditTextValueLang == "РУ") {
                adnik.text = "Добавить/Удалить никнейм"
                addNickButton.text = "Добавить"
                deleteNickButton.text = "Удалить"


            } else {
                adnik.text = "Add/Delete nickname"

                addNickButton.text = "Add"

                deleteNickButton.text = "Delete"


            }
        }
        val sharedPreferences = requireContext().getSharedPreferences("nick", Context.MODE_PRIVATE)
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


        addNickButton.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.add_nick_layout)

            val width = resources.displayMetrics.widthPixels * 0.99 // adjust this value as needed
            val height = resources.displayMetrics.heightPixels * 0.5 // adjust this value as needed
            dialog.window?.setLayout(width.toInt(), height.toInt())


            val saveButton = dialog.findViewById<Button>(R.id.dialogSaveBtn)
            val cancelButton = dialog.findViewById<Button>(R.id.dialogCancelBtn)
            val selFileButton = dialog.findViewById<Button>(R.id.addFile)
            nickField = dialog.findViewById(R.id.dialogtxtNick)
            val selfileinfo=dialog.findViewById<TextView>(R.id.selfileinfo)
            val topNameDialogtxt=dialog.findViewById<TextView>(R.id.topNameDialog)
            if (savedEditTextValueLang == "") {

                selFileButton.text="Выбрать файл"
                selfileinfo.text="При нажатии на 'Выбрать файл' вы можете сразу добавить несколько никнеймов. Для этого выберите .txt файл, где никнеймы перечисляются с запятыми."

                topNameDialogtxt.text="Добавить никнейм"
                saveButton.text = "Сохранить"
//                cancelButton.text = "Отменить"


            } else {
                if (savedEditTextValueLang == "РУ") {
                    selFileButton.text="Выбрать файл"
                    selfileinfo.text="При нажатии на 'Выбрать файл' вы можете сразу добавить несколько никнеймов. Для этого выберите .txt файл, где никнеймы перечисляются с запятыми."

                    topNameDialogtxt.text="Добавить никнейм"
                    saveButton.text = "Сохранить"
//                    cancelButton.text = "Отменить"

                } else {
                    selfileinfo.text="When you click on 'Select file', you can immediately add multiple nicknames. To do this, select a .txt file where the nicknames are listed separated by commas."
//
                    selFileButton.text="Select file"
                    topNameDialogtxt.text="Add nickname"
                    saveButton.text = "Save"
//                    cancelButton.text = "Cancel"

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
                    val sharedPreferences = requireContext().getSharedPreferences("nick", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("nick", jsonString)
                    editor.apply()

                    dialog.dismiss()
                }else{
                    Toast.makeText(requireContext(), "Заполните поле!", Toast.LENGTH_SHORT).show()
                }



            }


            cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        deleteNickButton.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.delete_nick_layout)

            val width = resources.displayMetrics.widthPixels * 0.99 // adjust this value as needed
            val height = resources.displayMetrics.heightPixels * 0.35 // adjust this value as needed
            dialog.window?.setLayout(width.toInt(), height.toInt())


            val saveButton = dialog.findViewById<Button>(R.id.dialogSaveBtn)
            val cancelButton = dialog.findViewById<Button>(R.id.dialogCancelBtn)
            val topNameDialogDel = dialog.findViewById<TextView>(R.id.topNameDialogDel)
            if (savedEditTextValueLang == "") {
                topNameDialogDel.text="Удалить никнейм"
                saveButton.text = "Удалить"
//                cancelButton.text = "Отменить"


            } else {
                if (savedEditTextValueLang == "РУ") {
                    topNameDialogDel.text="Удалить никнейм"
                    saveButton.text = "Удалить"
//                    cancelButton.text = "Отменить"

                } else {
                    topNameDialogDel.text="Delete nickname"
                    saveButton.text = "Delete"
//                    cancelButton.text = "Cancel"

                }
            }

            nickField = dialog.findViewById(R.id.dialogtxtNick)
            nickField.hint = "1"

            saveButton.setOnClickListener {

                var text = nickField.text.toString()
                if (text.isNotEmpty() ) {
                    if (!text.matches("-?\\d+(\\.\\d+)?".toRegex())) {
                        Toast.makeText(requireContext(), "Заполняйте только числами!", Toast.LENGTH_SHORT).show()
                    }else if(text.toInt()<1 || text.toInt()>list.size){

                    }else {
                        list.removeAt(text.toInt() - 1)


                        var result = ""
                        for ((index, element) in list.withIndex()) {
                            result += (index + 1).toString() + ". $element\n"
                        }
                        NickTextView.text = result

                        val gson = Gson()
                        val jsonString = gson.toJson(list)

// Save the JSON string in SharedPreferences
                        val sharedPreferences =
                            requireContext().getSharedPreferences("nick", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("nick", jsonString)
                        editor.apply()

                        dialog.dismiss()
                    }

                } else{
                    Toast.makeText(requireContext(), "Пустое поле!", Toast.LENGTH_SHORT).show()

                }
                Log.e(TAG, list.toString())



            }


            cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }




    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_TXT_FILE && resultCode == AppCompatActivity.RESULT_OK) {
            val selectedFile = data?.data
            selectedFile?.let { uri ->
                val inputStream = requireContext().contentResolver.openInputStream(uri)
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
            val sharedPreferences = requireContext().getSharedPreferences("nick", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("nick", jsonString)
            editor.apply()
        }



    }

    private val PICK_TXT_FILE = 1010
    private fun selectTxtFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "text/plain"
        }
        startActivityForResult(intent, PICK_TXT_FILE)
    }
}