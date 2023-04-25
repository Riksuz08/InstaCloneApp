package com.example.instacloneapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.util.SparseIntArray
import android.view.Surface
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader


@Suppress("DEPRECATION")
class settings : AppCompatActivity() {

    lateinit var saveSubs:Button
    lateinit var editTextNumSubs:EditText
    lateinit var backBtn:ImageView
    lateinit var lang:TextView
    lateinit var n_sms:EditText
    lateinit var saveNsms:Button
    lateinit var addCommentButton:Button;
    lateinit var seekBar:SeekBar
    lateinit var percentTxt:TextView
    lateinit var selLang:TextView
    lateinit var txtnumsebs:TextView
    lateinit var txtsms:TextView
    lateinit var razbros:TextView
    lateinit var addComTxt:TextView
    lateinit var screenRec:TextView
    lateinit var topBarTxt:TextView
    lateinit var creator:TextView

    lateinit var ideaCreator:TextView
    private val REQUEST_CODE=1000;
    private val REQUEST_PERMISSION=1001;
    private lateinit var mediaProjectionManager: MediaProjectionManager
    private var mediaProjection: MediaProjection?=null
    private var virtualDisplay: VirtualDisplay?=null
    private lateinit var mediaProjectionCallback:MediaProjectionCallback

    private var mScreenDensity:Int?=null

    private var DISPLAY_WIDTH=720
    private var DISPLAY_HEIGHT=1280
    private var mediaRecorder: MediaRecorder?=null
    private lateinit var toggleButton: Button
    var isChecked=false
    //     private lateinit var videoView: VideoView
    private var videoUri:String=""
    private val ORIENTATION= SparseIntArray()

    init {
        ORIENTATION.append(Surface.ROTATION_0,90)
        ORIENTATION.append(Surface.ROTATION_90,0)
        ORIENTATION.append(Surface.ROTATION_180,270)
        ORIENTATION.append(Surface.ROTATION_270,180)
    }
    var recordingBool:Boolean=false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        backBtn=findViewById(R.id.backBtn)
        lang=findViewById(R.id.language)
        editTextNumSubs=findViewById(R.id.editTxtNumberSubs)
        saveSubs=findViewById(R.id.saveSubs)
        saveNsms=findViewById(R.id.saveNsms)
        n_sms=findViewById(R.id.n_sms)
        seekBar=findViewById(R.id.seekBar)
        percentTxt=findViewById(R.id.percent)
        addCommentButton=findViewById(R.id.addCommentButton)
        toggleButton=findViewById(R.id.screenRecordButton)
        selLang=findViewById(R.id.selLang)
        txtnumsebs=findViewById(R.id.txtnumbersubs)
        txtsms=findViewById(R.id.txtsms)
        razbros=findViewById(R.id.razbrosPercent)
        addComTxt=findViewById(R.id.addCommentTxt)
        screenRec=findViewById(R.id.screenRecorderTxt)
        topBarTxt=findViewById(R.id.topBarTxt)

        creator=findViewById(R.id.Rik)
        ideaCreator=findViewById(R.id.IdeaCreator)
        val sharedPrefLang = getSharedPreferences("lang", Context.MODE_PRIVATE)
        val savedEditTextValueLang = sharedPrefLang.getString("edit_text_value", "")


        if(savedEditTextValueLang==""){
            topBarTxt.text="Настройки"
            selLang.text="Выбрать язык: "
            txtnumsebs.text="Число актива:"
            txtsms.text="сообщений в минуту"
            razbros.text="Разброс:"
            addComTxt.text="Дополнительные опции:"
            screenRec.text="Запись экрана: "
            saveSubs.text="Сохранить"
            saveNsms.text="Сохранить"
            addCommentButton.text="Добавить"
            toggleButton.text="Начать"
            creator.text="Разработчик: Riksuz"
            ideaCreator.text="Автор идеи: overbafer1"
        }else {
            if (savedEditTextValueLang == "РУ") {
                topBarTxt.text="Настройки"
                selLang.text="Выбрать язык: "
                txtnumsebs.text="Число актива:"
                txtsms.text="сообщений в минуту"
                razbros.text="Разброс:"
                addComTxt.text="Дополнительные опции:"
                screenRec.text="Запись экрана: "
                saveSubs.text="Сохранить"
                saveNsms.text="Сохранить"
                addCommentButton.text="Добавить"
                toggleButton.text="Начать"
                creator.text="Разработчик: Riksuz"
                ideaCreator.text="Автор идеи: overbafer1"
            } else {
                topBarTxt.text="Settings"
                selLang.text="Language: "
                txtnumsebs.text="Number of Views:"
                txtsms.text="messages per minute"
                razbros.text="Changing views:"
                addComTxt.text="More Options:"
                screenRec.text="Screen recording: "
                saveSubs.text="Save"
                saveNsms.text="Save"
                addCommentButton.text="Add"
                toggleButton.text="Start"
                creator.text="App developer: Riksuz"
                ideaCreator.text="Author of Idea: overbafer1"
            }
        }


        // Get the SharedPreferences object
        val sharedPrefsSwitchBool = getSharedPreferences("recordBool", Context.MODE_PRIVATE)

// Retrieve the boolean value
        recordingBool = sharedPrefsSwitchBool.getBoolean("recordBool", false)
        isChecked=recordingBool

        if (isChecked){
            if(savedEditTextValueLang=="РУ"){
            toggleButton.text="Остановить"
            }else{
                toggleButton.text="Stop"
            }
        }else{
            if (savedEditTextValueLang=="РУ"){
            toggleButton.text="Начать"
            } else{
                toggleButton.text="Start"
            }
        }
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        DISPLAY_WIDTH=metrics.widthPixels
        DISPLAY_HEIGHT=metrics.heightPixels
        mScreenDensity=metrics.densityDpi
        mediaRecorder=MediaRecorder()
        mediaProjectionManager=getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
Log.e(TAG,isChecked.toString())

        toggleButton.setOnClickListener{
            Log.e(TAG,"Clicking")
            if(
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                )+ ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO
                )!= PackageManager.PERMISSION_GRANTED
            ){
                Log.e(TAG,"if")
                isChecked=false
                val sharedPrefs = getSharedPreferences("recordBool", Context.MODE_PRIVATE)
                val editor = sharedPrefs.edit()
                editor.putBoolean("recordBool", isChecked)
                editor.apply()
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO
                    ),REQUEST_CODE
                )
            }
            else{
                Log.e(TAG,"else")
                toggleScreenShare(toggleButton)
            }
        }

//        val intentForg = Intent(this, MediaProjectionService::class.java)
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//
//            startForegroundService(intentForg)
//
//        } else {
//
//            startService(intentForg)
//        }



        addCommentButton.setOnClickListener{
            val intent = Intent(this, FragmentsLayoutAddOption::class.java)
            startActivity(intent)

        }

        if(savedEditTextValueLang==""){
            lang.setText("РУ")
        }else{
            lang.setText(savedEditTextValueLang)
        }
        // Устанавливаем сохраненное значение в EditText




        val PercentShPref = getSharedPreferences("percent", Context.MODE_PRIVATE)
        val PercentValue = PercentShPref.getString("edit_text_value", "")

        // Устанавливаем сохраненное значение в EditText

        if(PercentValue!=""){
            percentTxt.setText(PercentValue+"%")
        seekBar.setProgress(PercentValue!!.toInt())}
        else{
            percentTxt.setText("15%")
            seekBar.setProgress(15)
        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            // Called when the value of the SeekBar is changed
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Get the value of the SeekBar and do something with it
                val seekBarValue = progress


                val sharedPrefPercent = getSharedPreferences("percent", Context.MODE_PRIVATE)
                with(sharedPrefPercent.edit()) {
                    putString("edit_text_value", seekBarValue.toString())
                    apply()
                }
                // Do something with seekBarValue, such as update a TextView or perform some other action
                percentTxt.setText(seekBarValue.toString()+"%")
            }

            // Called when the user starts touching the SeekBar
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            // Called when the user stops touching the SeekBar
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        saveNsms.setOnClickListener {
            val editTextValue = n_sms.text.toString();
            // Сохраняем текст в SharedPreferences

            if (editTextValue.isNotEmpty() ) {
                if (!editTextValue.matches("-?\\d+(\\.\\d+)?".toRegex())) {
                    if(savedEditTextValueLang=="" || savedEditTextValueLang=="РУ") {
                        Toast.makeText(this, "Заполните только числами!", Toast.LENGTH_SHORT)
                            .show()
                    }else{
                        Toast.makeText(this, "Fill in only with numbers!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }else if(editTextValue.toInt()<1) {

                    if(savedEditTextValueLang=="" || savedEditTextValueLang=="РУ") {
                    Toast.makeText(this, "Напишите число побольше!", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "Write a bigger number!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }else
                {
            val sharedPref = getSharedPreferences("sms_count", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("edit_text_value", editTextValue)
                apply()
            }
            // Скрываем клавиатуру
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(saveSubs.windowToken, 0)
                    if(savedEditTextValueLang=="" || savedEditTextValueLang=="РУ") {
                    Toast.makeText(this, "Сохранено!", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT)
                            .show()
                    }
        }
        }else{
                if(savedEditTextValueLang=="" || savedEditTextValueLang=="РУ") {
            Toast.makeText(this, "Пустое поле!", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Empty field!", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }
        // Получаем сохраненное значение из SharedPreferences
        val smsCountShPref = getSharedPreferences("sms_count", Context.MODE_PRIVATE)
        val smsSavedValue = smsCountShPref.getString("edit_text_value", "")

        // Устанавливаем сохраненное значение в EditText
        n_sms.setText(smsSavedValue)


        saveSubs.setOnClickListener {

            val editTextValue = editTextNumSubs.text.toString();
            if (editTextValue.isNotEmpty() ) {
                if (!editTextValue.matches("-?\\d+(\\.\\d+)?".toRegex())) {
                    if(savedEditTextValueLang=="" || savedEditTextValueLang=="РУ") {
                        Toast.makeText(this, "Заполните только числами!", Toast.LENGTH_SHORT)
                            .show()
                    }else{
                        Toast.makeText(this, "Fill in only with numbers!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }else if(editTextValue.toInt()<1) {
                    if(savedEditTextValueLang=="" || savedEditTextValueLang=="РУ") {
                        Toast.makeText(this, "Напишите число побольше!", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "Write a bigger number!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }else
                 {

                val sharedPref = getSharedPreferences("subs_number", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("edit_text_value", editTextValue)
                    apply()
                }
                // Скрываем клавиатуру
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(saveSubs.windowToken, 0)
                     if(savedEditTextValueLang=="" || savedEditTextValueLang=="РУ") {
                         Toast.makeText(this, "Сохранено!", Toast.LENGTH_SHORT).show()
                     }else{
                         Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT)
                             .show()
                     }
            }
        }else{
                if(savedEditTextValueLang=="" || savedEditTextValueLang=="РУ") {
                    Toast.makeText(this, "Пустое поле!", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Empty field!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        // Получаем сохраненное значение из SharedPreferences
        val sharedPref = getSharedPreferences("subs_number", Context.MODE_PRIVATE)
        val savedEditTextValue = sharedPref.getString("edit_text_value", "")

        // Устанавливаем сохраненное значение в EditText
        editTextNumSubs.setText(savedEditTextValue)



        lang.setOnClickListener{
            if(lang.text=="РУ"){
                lang.text="EN"
                topBarTxt.text="Settings"
                selLang.text="Language: "
                txtnumsebs.text="Number of Views:"
                txtsms.text="messages per minute"
                razbros.text="Changing views:"
                addComTxt.text="More Options:"
                screenRec.text="Screen recording: "
                saveSubs.text="Save"
                saveNsms.text="Save"
                addCommentButton.text="Add"
                toggleButton.text="Start"
                creator.text="App developer: Riksuz"
                ideaCreator.text="Author of Idea: overbafer1"

            }else{
                lang.text="РУ"
                topBarTxt.text="Настройки"
                selLang.text="Выбрать язык: "
                txtnumsebs.text="Число актива:"
                txtsms.text="сообщений в минуту"
                razbros.text="Разброс:"
                addComTxt.text="Добавить комментарий:"
                screenRec.text="Запись экрана: "
                saveSubs.text="Сохранить"
                saveNsms.text="Сохранить"
                addCommentButton.text="Добавить"
                toggleButton.text="Начать"
                creator.text="Разработчик: Riksuz"
                ideaCreator.text="Автор идеи: overbafer1"
            }

            val sharedPref = getSharedPreferences("lang", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("edit_text_value", lang.text as String)
                apply()
            }

        }
        backBtn.setOnClickListener{

            val intent = Intent(this, start_activity::class.java)
            startActivity(intent)

        }


    }


    private fun toggleScreenShare(v: Button?) {
        if (!isChecked){

            val intentForg = Intent(this, MediaProjectionService::class.java)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


                startForegroundService(intentForg)

            } else {

                startService(intentForg)
            }
            initRecorder()
            recordScreen()
            isChecked=true


            val sharedPrefs = getSharedPreferences("recordBool", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putBoolean("recordBool", isChecked)
            editor.apply()

        }else{
            try {
                mediaRecorder!!.stop()
                mediaRecorder!!.reset()
                stopRecordingScreen()
            }catch (e:Exception){
                e.printStackTrace()
            }
            val sharedPrefLang = getSharedPreferences("lang", Context.MODE_PRIVATE)
            val savedEditTextValueLang = sharedPrefLang.getString("edit_text_value", "")
            val newPath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
            val folder=File(newPath,"InstaRec/")
            isChecked=false
            val intentForg = Intent(this, MediaProjectionService::class.java)
stopService(intentForg)
            val sharedPrefs = getSharedPreferences("recordBool", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putBoolean("recordBool", isChecked)
            editor.apply()
            if(savedEditTextValueLang=="РУ") {
                Toast.makeText(this, "Сохранено в " + folder, Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Saved in" + folder, Toast.LENGTH_LONG).show()
            }


            if (savedEditTextValueLang=="РУ"){
                toggleButton.text="Начать"
            } else{
                toggleButton.text="Start"
            }
        }

    }




    private fun initRecorder() {
        try {
            var recordingFile=("ScreenRec${System.currentTimeMillis()}.mp4")
            mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder!!.setVideoSource(MediaRecorder.VideoSource.SURFACE)
            mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)

            val newPath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
            val folder=File(newPath,"InstaRec/")
            if(!folder.exists()){
                folder.mkdirs()
            }
            val file1=File(folder,recordingFile)
            videoUri=file1.absolutePath

            mediaRecorder!!.setOutputFile(videoUri)
            mediaRecorder!!.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT)

             mediaRecorder!!.setVideoEncoder(MediaRecorder.VideoEncoder.H264)

            mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mediaRecorder!!.setVideoEncodingBitRate(8192 * 1000) // Increase bit rate to 8192 kbps
            mediaRecorder!!.setVideoFrameRate(90)

            var rotation = windowManager.defaultDisplay.rotation
            var orientation = ORIENTATION.get(rotation+90)
            mediaRecorder!!.setOrientationHint(orientation)
            mediaRecorder!!.prepare()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    private fun recordScreen() {
        if(mediaProjection==null){
            startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(),REQUEST_CODE)
        }
        virtualDisplay=createVirtualDisplay()
        try {
            mediaRecorder!!.start()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun createVirtualDisplay(): VirtualDisplay? {
        return mediaProjection?.createVirtualDisplay(
            "MainActivity",DISPLAY_WIDTH,DISPLAY_HEIGHT,
            mScreenDensity!!,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            mediaRecorder!!.surface,null,null
        )
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)



        if(requestCode!=REQUEST_CODE){
            Toast.makeText(this,"Unk Error",Toast.LENGTH_LONG).show()
            return
        }
        if(resultCode!= RESULT_OK){
            Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show()
            isChecked=false

            val sharedPrefs = getSharedPreferences("recordBool", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putBoolean("recordBool", isChecked)
            editor.apply()
            return
        }else{
            val sharedPrefLang = getSharedPreferences("lang", Context.MODE_PRIVATE)
            val savedEditTextValueLang = sharedPrefLang.getString("edit_text_value", "")


            if (savedEditTextValueLang=="РУ"){
                toggleButton.text="Остановить"
            } else{
                toggleButton.text="Stop"
            }
            Log.e(TAG,isChecked.toString())

            val sharedPrefs = getSharedPreferences("recordBool", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putBoolean("recordBool", isChecked)
            editor.apply()
        }
        mediaProjectionCallback=MediaProjectionCallback(
            mediaRecorder!!,mediaProjection
        )
        mediaProjection=mediaProjectionManager.getMediaProjection(resultCode, data!!)
        mediaProjection!!.registerCallback(mediaProjectionCallback,null)
        virtualDisplay=createVirtualDisplay()
        try {
            mediaRecorder!!.start()
        }catch (e:Exception){
            e.printStackTrace()
        }
 }


    private fun stopRecordingScreen() {
        if(virtualDisplay==null)
            return
        virtualDisplay!!.release()
        destroyMediaProjection()
    }

    private fun destroyMediaProjection() {
        if(mediaProjection!=null){
            mediaProjection!!.unregisterCallback(mediaProjectionCallback)
            mediaProjection!!.stop()
            mediaProjection=null
        }

    }

    inner class MediaProjectionCallback (
        var mediaRecorder:MediaRecorder,
        var mediaProjection:MediaProjection?
    ):MediaProjection.Callback()
    {
        override fun onStop() {
            if(isChecked){
                isChecked=false

                val sharedPrefs = getSharedPreferences("recordBool", Context.MODE_PRIVATE)
                val editor = sharedPrefs.edit()
                editor.putBoolean("recordBool", isChecked)
                editor.apply()
                mediaRecorder.stop()
                mediaRecorder.reset()
            }
            mediaProjection=null
            stopRecordingScreen()
            super.onStop()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_PERMISSION ->{
                if(grantResults.size>0
                    && grantResults[0]+grantResults[1]==PackageManager.PERMISSION_GRANTED){
                    toggleScreenShare(toggleButton)
                }else{
                    isChecked=false

                    val sharedPrefs = getSharedPreferences("recordBool", Context.MODE_PRIVATE)
                    val editor = sharedPrefs.edit()
                    editor.putBoolean("recordBool", isChecked)
                    editor.apply()
                    ActivityCompat.requestPermissions(
                        this, arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO
                        ),REQUEST_CODE
                    )
                }
            }
        }
    }



}