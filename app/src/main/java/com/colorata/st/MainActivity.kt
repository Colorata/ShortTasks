package com.colorata.st

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.content.ContextCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colorata.st.bubble.BubbleActivity
import com.colorata.st.bubble.BubbleAdapter
import com.colorata.st.extentions.GenerItems
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    //Init values
    private var nightMode = true
    private var channelCreated = false
    private val notifId = 1337
    private val notifChannel = "whatever"
    private val shortcutId = "something.unique"
    private lateinit var sharedPreference: SharedPreferences
    private var versionCounter = 0
    private var bufferName: String = ""
    private var bufferIcon: Int = 0
    private var bufferName2: String = ""
    private var bufferIcon2: Int = 0
    private var pos1: Int = 0
    private var pos2: Int = 0
    private var posIs: Boolean = false

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //TODO: fix bug with crashing
        packageManager.setComponentEnabledSetting(ComponentName(this@MainActivity, DarkApp::class.java),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP)
        packageManager.setComponentEnabledSetting(ComponentName(this@MainActivity, LightApp::class.java),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP)

        //Setting up NIGHT MODE
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK){
            Configuration.UI_MODE_NIGHT_NO -> {
                nightMode = false
                logo_main.setImageResource(R.drawable.ic_logo_main_light)
                packageManager.setComponentEnabledSetting(ComponentName(this@MainActivity, DarkApp::class.java),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP)
                packageManager.setComponentEnabledSetting(ComponentName(this@MainActivity, LightApp::class.java),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP)
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                nightMode = true
                logo_main.setImageResource(R.drawable.ic_logo_main_dark)
                packageManager.setComponentEnabledSetting(ComponentName(this@MainActivity, LightApp::class.java),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP)
                packageManager.setComponentEnabledSetting(ComponentName(this@MainActivity, DarkApp::class.java),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP)
            }
        }

        //Showing Accessibility SNACKBAR
        sharedPreference =  getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        var access = 0
        if (access < 2) {
            val snackBar = Snackbar.make(
                main_layout, "Please, turn on Accessibility on this app",
                Snackbar.LENGTH_INDEFINITE
            ).setAction("Go!"){
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                startActivity(intent)
            }
            snackBar.show()
            access += 1
        }

        //Creating ANIMATIONS and configuring GUIDELINES
        createVideos()
        guidelineConfig()

        //Setting up ONCLICKLISTENER
        help.setOnClickListener{ help() }
        about.setOnClickListener { about() }
        settings.setOnClickListener { settings() }
        add_button.setOnClickListener { addButton()}
        change_position.setOnClickListener { changePosition()}
    }

    //Fun for creating ANIMATIONS
    private fun createVideos(){
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.duration = 700

        val animation = AnimationSet(false)
        animation.addAnimation(fadeIn)
        logo_main.startAnimation(animation)
        text_main.startAnimation(animation)
    }

    //Fun for configuring GUIDELINES
    private fun guidelineConfig(){
        val displayMetrics: DisplayMetrics = baseContext.resources.displayMetrics
        val dpHeight = displayMetrics.heightPixels / displayMetrics.density
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density

        guideline_one_handed_main.setGuidelineBegin((dpHeight * 0.37).toInt())
        guideline_logo_main.setGuidelineBegin((dpWidth * 0.37).toInt())
    }

    //Configuring BUBBLES
    @RequiresApi(Build.VERSION_CODES.R)
    private fun buildBubbleNotification(appContext: Context): Notification {
        val pi = PendingIntent.getActivity(
            appContext,
            0,
            Intent(appContext, BubbleActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val bubble = NotificationCompat.BubbleMetadata.Builder()
            .setDesiredHeight(4000)
            .setIcon(IconCompat.createWithResource(appContext, R.drawable.ic_logo_bubble))
            .setIntent(pi)
            .apply { setAutoExpandBubble(true); setSuppressNotification(true) }
            .build()

        ShortcutManagerCompat.addDynamicShortcuts(
            this, mutableListOf(
                ShortcutInfoCompat.Builder(this, shortcutId)
                    .setLongLived(true)
                    .setShortLabel("Settings")
                    .setIntent(Intent(Settings.ACTION_SETTINGS))
                    .setIcon(IconCompat.createWithResource(this, R.drawable.ic_logo_bubble))
                    .build()
            )
        )


        val builder = NotificationCompat.Builder(
            appContext,
            notifChannel
        )
            .setSmallIcon(R.drawable.ic_logo_bubble)
            .setContentTitle("ShortTasks")
            .setShortcutId("Settings")
            .setShortcutId(shortcutId)
            .setBubbleMetadata(bubble)

        val person = Person.Builder()
            .setBot(true)
            .setName("A Bubble Bot")
            .setImportant(true)
            .build()

        val style = NotificationCompat.MessagingStyle(person)
            .setConversationTitle("Bubble Notification")

        style.addMessage("It's notification for bubble", System.currentTimeMillis(), person)
        builder.setStyle(style)

        return builder.build()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun showBubble(appContext: Context) {
        NotificationManagerCompat.from(appContext).let { mgr ->
            if (!channelCreated) {
                mgr.createNotificationChannel(
                    NotificationChannel(
                        notifChannel,
                        "Whatever",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                )
                channelCreated = true
            }
            mgr.notify(notifId, buildBubbleNotification(appContext))
        }
    }
    //End of configuring BUBBLES

    //Fun for BOTTOM SHEET for HELP
    @SuppressLint("InflateParams")
    fun help(){
        val materialBuilder = BottomSheetDialog(this)
        val inflater = layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.help_alert, null)

        //Founding BUTTONS
        val cancel = dialogLayout.findViewById<Button>(R.id.cancel_help)

        //Configuring BACKGROUND
        when(nightMode){
            false -> {
                dialogLayout.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.light_background))
            }
            true -> {
                dialogLayout.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.dark_background))
            }
        }

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for CANCEL BUTTON
        cancel.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
            }
        }
    }

    //Fun for BOTTOM SHEET for ABOUT
    @SuppressLint("InflateParams")
    private fun about(){
        val materialBuilder = BottomSheetDialog(this)
        val inflater = layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.about_alert, null)

        //Founding BUTTONS
        val cancel = dialogLayout.findViewById<Button>(R.id.cancel_version)
        val source = dialogLayout.findViewById<Button>(R.id.sourcecode)
        val support = dialogLayout.findViewById<Button>(R.id.support)
        val version = dialogLayout.findViewById<Button>(R.id.version)

        //Configuring BACKGROUND
        when(nightMode){
            false -> {
                dialogLayout.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.light_background))
            }
            true -> {
                dialogLayout.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.dark_background))
            }
        }

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for SOURCE CODE BUTTON
        source.setOnClickListener {
            if (materialBuilder.isShowing) {
                materialBuilder.dismiss()
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.addCategory(Intent.CATEGORY_BROWSABLE)
                intent.data = Uri.parse("https://github.com/Colorata/ShortTasks")
                startActivity(intent)
            }
        }

        //Click listener for SUPPORT US BUTTON
        support.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
                val intent1 = Intent()
                intent1.action = Intent.ACTION_VIEW
                intent1.addCategory(Intent.CATEGORY_BROWSABLE)
                intent1.data = Uri.parse("https://qiwi.com/n/COLORATA")
                startActivity(intent1)
            }
        }

        //Click listener for VERSION BUTTON
        version.setOnClickListener {
            if(materialBuilder.isShowing){
                //Showing EASTER EGG
                versionCounter += 1
                if (versionCounter == 10) {
                    Snackbar.make(it, "You're pretty", 1500).apply { view.elevation = 1000F }.show()
                }

                else if (versionCounter == 100) {
                    Snackbar.make(it, "Have nothing to do?", 1500).apply { view.elevation = 1000F }.show()
                    versionCounter = 0
                }
            }
        }

        //Click listener for CANCEL BUTTON
        cancel.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
            }
        }
    }

    //Fun for BOTTOM SHEET SETTINGS
    @SuppressLint("InflateParams")
    private fun settings(){
        val materialBuilder = BottomSheetDialog(this)
        val inflater = layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.settings_alert, null)

        //Founding BUTTONS
        val cancel = dialogLayout.findViewById<Button>(R.id.cancel_settings)
        val settings = dialogLayout.findViewById<Button>(R.id.settings_app)
        val enable = dialogLayout.findViewById<Button>(R.id.enable_bubbles)
        val city = dialogLayout.findViewById<Button>(R.id.city_change)

        //Configuring BACKGROUND
        when(nightMode){
            false -> {
                dialogLayout.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.light_background))
            }
            true -> {
                dialogLayout.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.dark_background))
            }
        }

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for APP SETTINGS BUTTON
        settings.setOnClickListener {
            if (materialBuilder.isShowing) {
                materialBuilder.dismiss()

                //Going to SYSTEM SETTINGS
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }

        //Click listener for ENABLE BUBBLE BUTTOn
        enable.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
                //Showing BUBBLE
                showBubble(this)
            }
        }

        //Click listener for CITY
        city.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
                val materialBuilderCity = BottomSheetDialog(this)
                val inflaterCity = layoutInflater
                val dialogLayoutCity: View = inflaterCity.inflate(R.layout.change_city_alert, null)

                //Founding BUTTON AND EDITTEXTS for CITY
                val okCity = dialogLayoutCity.findViewById<Button>(R.id.ok_change_city)
                val cancelCity = dialogLayoutCity.findViewById<Button>(R.id.cancel_change_city)
                val changeCityField = dialogLayoutCity.findViewById<TextInputEditText>(R.id.city_field_in)

                //Configuring BACKGROUND for CITY
                when(nightMode){
                    false -> {
                        dialogLayoutCity.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.light_background))
                    }
                    true -> {
                        dialogLayoutCity.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.dark_background))
                    }
                }

                //Showing BOTTOM SHEET for CITY
                materialBuilderCity.setContentView(dialogLayoutCity)
                materialBuilderCity.show()

                //Click listener for OK BUTTON for CITY
                okCity.setOnClickListener {
                    if(materialBuilderCity.isShowing) {

                        //Checking if EDITTEXT empty
                        if (changeCityField.text.toString()!="") {

                            //Putting CITY NAME to SHAREDPREFS
                            val editor = sharedPreference.edit()
                            editor.putString("city", changeCityField.text.toString())
                            editor.apply()
                            materialBuilderCity.dismiss()
                        } else {

                            //Showing SNACKBAR
                            Snackbar.make(it, "City name isn't entered", 1500).apply { view.elevation = 1000F }.show()
                        }
                    }
                }

                //Click listener for CANCEL BUTTON for CITY
                cancelCity.setOnClickListener {
                    if(materialBuilderCity.isShowing){
                        materialBuilderCity.dismiss()
                    }
                }
            }
        }

        //Click listener for CANCEL BUTTON
        cancel.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
            }
        }
    }

    //Fun for BOTTOM SHEET ADD BUTTONS
    @SuppressLint("InflateParams")
    private fun addButton(){
        val materialBuilder = BottomSheetDialog(this)
        val inflater = layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.add_button_alert, null)

        //Founding BUTTONS AND EDITTEXTS
        val ok = dialogLayout.findViewById<Button>(R.id.ok_add_button)
        val cancel = dialogLayout.findViewById<Button>(R.id.cancel_add_button)
        val titleField = dialogLayout.findViewById<TextInputEditText>(R.id.title_field_in)
        val packageField = dialogLayout.findViewById<TextInputEditText>(R.id.package_field_in)
        val activityField = dialogLayout.findViewById<TextInputEditText>(R.id.activity_field_in)

        //Configuring BACKGROUND
        when(nightMode){
            false -> {
                dialogLayout.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.light_background))
            }
            true -> {
                dialogLayout.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.dark_background))
            }
        }

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for OK BUTTON
        ok.setOnClickListener {
            if(materialBuilder.isShowing) {

                //Checking if EDITTEXTS empty
                if(titleField.text.toString()!="" && packageField.text.toString()!="" && activityField.text.toString()!="") {

                    //Putting APP INFO to SHAREDPREFS
                    val editor = sharedPreference.edit()
                    editor.putString("title", titleField.text.toString())
                    editor.putString("package", packageField.text.toString())
                    editor.putString("activity", activityField.text.toString())
                    editor.apply()
                    materialBuilder.dismiss()
                }
                else{

                    //Showing SNACKBAR
                    Snackbar.make(it, "Something isn't entered", 1500).apply { view.elevation = 1000F }.show()
                }
            }
        }

        //Click listener for CANCEL BUTTON
        cancel.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
            }
        }
    }


    //Fun for BOTTOM SHEET for CHANGE POSITION
    @SuppressLint("InflateParams")
    private fun changePosition(){
        val materialBuilder = BottomSheetDialog(this)
        val inflater = layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.change_controls_alert, null)

        //Founding BUTTONS and RECYCLERVIEW
        val changeRecycler = dialogLayout.findViewById<RecyclerView>(R.id.change_recycler)
        val ok = dialogLayout.findViewById<Button>(R.id.ok_change_position)
        val cancel = dialogLayout.findViewById<Button>(R.id.cancel_change_position)

        //Init values for CONTROLS
        val mutableNames = mutableListOf<String>()
        val mutableIcons = mutableListOf<Int>()

        //Getting LAST INFO about CONTROLS
        for (i in 0..GenerItems().names().lastIndex){
            mutableNames.add(
                    sharedPreference.getString(
                            "name $i",
                            GenerItems().names()[i]
                    ).toString()
            )
            mutableIcons.add(sharedPreference.getInt("icon $i", GenerItems().icons()[i]))
        }

        //Configuring RECYCLERVIEW
        changeRecycler.layoutManager = GridLayoutManager(applicationContext, 3)
        var bubbleAdapter = BubbleAdapter(mutableNames, mutableIcons)
        changeRecycler.adapter = bubbleAdapter

        //Configuring BACKGROUND
        when(nightMode){
            false -> {
                dialogLayout.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.light_background))
            }
            true -> {
                dialogLayout.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.dark_background))
            }
        }

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for CHANGING CONTROLS IN RECYCLERVIEW
        changeRecycler.addOnItemTouchListener(
                RecyclerItemClickListener(changeRecycler,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            @SuppressLint("WrongConstant")
                            override fun onItemClick(view: View, position: Int) {
                                //Checking if FIRST TIME CLICKED
                                if (!posIs){

                                    //Clicked FIRST TIME
                                    //Remembering WHERE CLICKED
                                    bufferName = mutableNames[position]
                                    bufferIcon = mutableIcons[position]
                                    pos1 = position
                                    posIs = !posIs
                                } else{

                                    //Clicked SECOND TIME
                                    //Remembering WHERE CLICKED
                                    bufferName2 = mutableNames[position]
                                    bufferIcon2 = mutableIcons[position]
                                    pos2 = position

                                    //Changing POSITIONS
                                    mutableNames[pos1] = bufferName2
                                    mutableIcons[pos1] = bufferIcon2
                                    mutableNames[pos2] = bufferName
                                    mutableIcons[pos2] = bufferIcon

                                    //Updating RECYCLERVIEW
                                    bubbleAdapter = BubbleAdapter(mutableNames, mutableIcons)
                                    changeRecycler.adapter?.notifyItemChanged(pos1)
                                    changeRecycler.adapter?.notifyItemChanged(pos2)
                                    GenerItems().icons = mutableIcons
                                    GenerItems().names = mutableNames
                                    posIs = !posIs
                                }
                            }
                        }))

        //Click listener for OK BUTTON
        ok.setOnClickListener {
            if(materialBuilder.isShowing) {

                //Putting CONTROLS to SHAREDPREFS
                sharedPreference.edit().clear().apply()
                val editor = sharedPreference.edit()
                for (i in 0..mutableNames.lastIndex){
                    editor.putString("name $i", mutableNames[i])
                    editor.putInt("icon $i", mutableIcons[i])
                }
                editor.apply()
                materialBuilder.dismiss()
            }
        }

        //Click listener for CANCEL BUTTON
        cancel.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
            }
        }
    }
}