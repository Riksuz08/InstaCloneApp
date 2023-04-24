package com.example.instacloneapp

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("DEPRECATION")
class FragmentsLayoutAddOption : AppCompatActivity() {
    lateinit var backBtn: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragments_layout_add_option)
        var addniktxt = findViewById<TextView>(R.id.addNickTxt)




        val sharedPrefLang = getSharedPreferences("lang", Context.MODE_PRIVATE)
        val savedEditTextValueLang = sharedPrefLang.getString("edit_text_value", "")


        if (savedEditTextValueLang == "") {

            addniktxt.text = "Дополнительные опции"
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.add_nick_layout)
            dialog.findViewById<Button>(R.id.dialogSaveBtn).text = "Сохранить"
            dialog.findViewById<Button>(R.id.dialogCancelBtn).text = "Отменить"
        } else {
            if (savedEditTextValueLang == "РУ") {

                addniktxt.text = "Дополнительные опции"

                val dialog = Dialog(this)
                dialog.setContentView(R.layout.add_nick_layout)
                dialog.findViewById<Button>(R.id.dialogSaveBtn).text = "Сохранить"
                dialog.findViewById<Button>(R.id.dialogCancelBtn).text = "Отменить"
            } else {

                addniktxt.text = "More Options"

                val dialog = Dialog(this)
                dialog.setContentView(R.layout.add_nick_layout)
                dialog.findViewById<Button>(R.id.dialogSaveBtn).text = "Save"
                dialog.findViewById<Button>(R.id.dialogCancelBtn).text = "Cancel"
            }
        }


        backBtn = findViewById(R.id.backBtn)


        val usernamesFragment=UsernameFragment()
        val uniqueCommentFragments=UniqueComments()
        val commentsFragment=CommentsFragment()
        val avatarFragment=Avatars()
val bottomNav=findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        makeCurrentFragment(usernamesFragment)
       bottomNav.setOnNavigationItemSelectedListener {
           when(it.itemId){
               R.id.commentsfrag -> makeCurrentFragment(commentsFragment)
               R.id.avatarsfrag ->makeCurrentFragment(avatarFragment)
               R.id.uniquefrag ->makeCurrentFragment(uniqueCommentFragments)
               R.id.usernamefrag ->makeCurrentFragment(usernamesFragment)
           }
           true
       }

        backBtn.setOnClickListener {

            val intent = Intent(this, settings::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container,fragment)
            commit()
        }

}