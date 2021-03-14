 package com.colorata.st

import android.annotation.SuppressLint
import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colorata.st.bubble.BubbleActivity
import com.colorata.st.bubble.BubbleAdapter
import com.colorata.st.bubble.RecyclerItemClickListener
import com.colorata.st.extentions.GenerItems
import com.colorata.st.userbutton.UserButtonAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_bubble.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bubble_manager
import kotlinx.android.synthetic.main.first_change_alert.*
import kotlinx.android.synthetic.main.help_alert.*
import java.util.*


 class MainActivity : AppCompatActivity() {

    //Init values
    private var nightMode = true
    private var channelCreated = false
    private val notifId = 1337
    private val notifChannel = "Bubble Manager"
    private val shortcutId = "Bubble Manager"
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

        //Configuring SHAREDPREFS
        sharedPreference =  getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)

        if (sharedPreference.getInt("welcome", 0) < 1) {
            val editor = sharedPreference.edit()
            editor.putInt("welcome", sharedPreference.getInt("welcome", 0) + 1)
            editor.apply()
            welcome()
        }

        //Creating ANIMATIONS and configuring GUIDELINES
        createVideos()
        guidelineConfig()

        //Setting up ONCLICKLISTENER
        help.setOnClickListener{ help() }
        about.setOnClickListener { about() }
        settings.setOnClickListener { settings() }
        bubble_manager.setOnClickListener { bubbleManager()}
        weather_director.setOnClickListener { weatherDirector() }

        //Changing COLOR TO ELEMENTS
        text_main.setTextColor(getButtonColor())
        help.setTextColor(getButtonColor())

        about.setTextColor(getButtonColor())

        settings.setTextColor(getButtonColor())

        bubble_manager.setTextColor(getButtonColor())

        weather_director.setTextColor(getButtonColor())

        main_layout.setBackgroundColor(getBackground())

        window.navigationBarColor = getBackground()
        window.statusBarColor = getBackground()
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
                        .setShortLabel("Bubble Manager")
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
            .setShortcutId("Bubble Manager")
            .setShortcutId(shortcutId)
            .setBubbleMetadata(bubble)

        val person = Person.Builder()
            .setBot(true)
            .setName("A Bubble Bot")
            .setImportant(true)
            .build()

        val style = NotificationCompat.MessagingStyle(person)
            .setConversationTitle("Bubble Manager")

        style.addMessage("It's Bubble Manager", System.currentTimeMillis(), person)
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
        val enable = dialogLayout.findViewById<Button>(R.id.enable_help)
        val goAccess = dialogLayout.findViewById<Button>(R.id.go_access)
        val imageBubble1 = dialogLayout.findViewById<ImageView>(R.id.image_bubble_1)
        val imageBubble2 = dialogLayout.findViewById<ImageView>(R.id.image_bubble_2)
        val imageBubble3 = dialogLayout.findViewById<ImageView>(R.id.image_bubble_3)
        val imagePower1 = dialogLayout.findViewById<ImageView>(R.id.image_power_1)
        val imagePower2 = dialogLayout.findViewById<ImageView>(R.id.image_power_2)

        //Configuring pictures
        if(nightMode){
            imagePower1.setImageResource(R.drawable.ic_power_assistant_1_dark)
            imagePower2.setImageResource(R.drawable.ic_power_assistant_2_dark)
            imageBubble1.setImageResource(R.drawable.ic_bubble_manager_1_dark)
            imageBubble2.setImageResource(R.drawable.ic_bubble_manager_2_dark)
            imageBubble3.setImageResource(R.drawable.ic_bubble_manager_3_dark)
        }

        //Configuring BACKGROUND
        configBack(dialogLayout)

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for ENABLE BUTTON
        enable.setOnClickListener {
            if (materialBuilder.isShowing){
                showBubble(this)
            }
        }

        //Click listener for GO BUTTON
        goAccess.setOnClickListener {
            if (materialBuilder.isShowing) {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                startActivity(intent)
            }
        }
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
        val text = dialogLayout.findViewById<TextView>(R.id.text_about)

        //Changing COLORS FOR ELEMENTS
        cancel.setTextColor(getButtonColor())
        source.setTextColor(getButtonColor())
        support.setTextColor(getButtonColor())
        version.setTextColor(getButtonColor())
        text.setTextColor(getButtonColor())

        //Configuring BACKGROUND
        configBack(dialogLayout)

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
        val clear = dialogLayout.findViewById<Button>(R.id.clear_data)

        
        //Configuring BACKGROUND
        configBack(dialogLayout)

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

        //Click listener for CLEAR BUTTON
        clear.setOnClickListener {
            if (materialBuilder.isShowing){
                materialBuilder.dismiss()
                clearData()
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
        val appRecycler = dialogLayout.findViewById<RecyclerView>(R.id.app_recycler)
        val cancel = dialogLayout.findViewById<Button>(R.id.cancel_add_button)

        //Configuring FOR ALL APPS
        val mutableLabels = getAppsLabel()
        val mutablePackages = getAppsPackage()
        val mutableIcons = getAppsIcon()

        //Configuring RECYCLERVIEW
        appRecycler.layoutManager = GridLayoutManager(applicationContext, 3)
        val bubbleAdapter = UserButtonAdapter(mutableLabels, mutableIcons)
        appRecycler.adapter = bubbleAdapter
        appRecycler.addOnItemTouchListener(
                RecyclerItemClickListener(appRecycler,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            @SuppressLint("WrongConstant")
                            override fun onItemClick(view: View, position: Int) {
                                val editor = sharedPreference.edit()
                                editor.putString("userPackage", mutablePackages[position])
                                editor.putString("userLabel", mutableLabels[position])
                                editor.apply()
                                Snackbar.make(view.rootView, "Your control changed to ${mutableLabels[position]}", 1500).apply { view.elevation = 1000F }.show()
                            }
                        }))

        //Configuring BACKGROUND
        configBack(dialogLayout)

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
        configBack(dialogLayout)

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
                                if (!posIs) {

                                    //Clicked FIRST TIME
                                    //Remembering WHERE CLICKED
                                    bufferName = mutableNames[position]
                                    bufferIcon = mutableIcons[position]
                                    pos1 = position
                                    posIs = !posIs
                                } else {

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

    //Fun BOTTOM SHEET for BUBBLE MANAGER
    @SuppressLint("InflateParams")
    private fun bubbleManager(){

        val materialBuilder = BottomSheetDialog(this)
        val inflater = layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.bubble_manager_alert, null)

        //Founding BUTTONS
        val cancel = dialogLayout.findViewById<Button>(R.id.cancel_bubble_manager)
        val enable = dialogLayout.findViewById<Button>(R.id.enable)
        val changePosition = dialogLayout.findViewById<Button>(R.id.change_position)
        val addButton = dialogLayout.findViewById<Button>(R.id.add_button)

        //Configuring BACKGROUND
        configBack(dialogLayout)

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for ENABLE BUBBLE BUTTOn
        enable.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
                //Showing BUBBLE
                showBubble(this)
            }
        }

        //Click listener for CHANGE POSITION
        changePosition.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
                if (sharedPreference.getInt("firstChange", 0) < 1) {
                    val editor = sharedPreference.edit()
                    editor.putInt("firstChange", sharedPreference.getInt("firstChange", 0) + 1)
                    editor.apply()
                    firstChange()
                } else {
                    changePosition()
                }
            }
        }

        //Click listener for ADD BUTTON
        addButton.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
                if (sharedPreference.getInt("firstAddButton", 0) < 1) {
                    val editor = sharedPreference.edit()
                    editor.putInt("firstAddButton", sharedPreference.getInt("firstAddButton", 0) + 1)
                    editor.apply()
                    firstAddButton()
                } else {
                    addButton()
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

    //Fun for BOTTOM SHEET for WEATHER DIRECTOR
    @SuppressLint("InflateParams")
    fun weatherDirector(){
        val materialBuilder = BottomSheetDialog(this)
        val inflater = layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.weather_director_alert, null)

        //Founding BUTTONS
        val cancel = dialogLayout.findViewById<Button>(R.id.cancel_weather_director)
        val changeCity = dialogLayout.findViewById<Button>(R.id.city_change)

        //Configuring BACKGROUND
        configBack(dialogLayout)

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for CITY
        changeCity.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
                if (sharedPreference.getInt("firstChangeCity", 0) < 1) {
                    val editor = sharedPreference.edit()
                    editor.putInt("firstChangeCity", sharedPreference.getInt("firstChangeCity", 0) + 1)
                    editor.apply()
                    firstChangeCity()
                } else{
                    changeCity()
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

    //Fun for BOTTOM SHEET for CHANGE CITY
    @SuppressLint("InflateParams")
    fun changeCity(){
        val materialBuilderCity = BottomSheetDialog(this)
        val inflaterCity = layoutInflater
        val dialogLayoutCity: View = inflaterCity.inflate(R.layout.change_city_alert, null)

        //Founding BUTTON AND EDITTEXTS for CITY
        val okCity = dialogLayoutCity.findViewById<Button>(R.id.ok_change_city)
        val cancelCity = dialogLayoutCity.findViewById<Button>(R.id.cancel_change_city)
        val changeCityField = dialogLayoutCity.findViewById<TextInputEditText>(R.id.city_field_in)

        //Configuring BACKGROUND for CITY
        configBack(dialogLayoutCity)

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

    //Fun for CONFIGURING BACKGROUND
    private fun configBack(dialogLayout: View){
        when(nightMode){
            false -> {
                dialogLayout.setBackgroundColor(getBackground())
            }
            true -> {
                dialogLayout.setBackgroundColor(getBackground())
            }
        }
        dialogLayout.rootView.fitsSystemWindows = true
    }

    //Fun for the FIRST CHANGE in BUBBLE MANAGER
    @SuppressLint("InflateParams")
    private fun firstChange(){
        val materialBuilder = BottomSheetDialog(this)
        val inflater = layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.first_change_alert, null)

        //Founding BUTTONS
        val understand = dialogLayout.findViewById<Button>(R.id.understand_first_change)

        //Configuring BACKGROUND
        configBack(dialogLayout)

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for CANCEL BUTTON
        understand.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
                changePosition()
            }
        }
    }

    //Fun for the FIRST CHANGE CITY ALERT in WEATHER DIRECTOR
    @SuppressLint("InflateParams")
    private fun firstChangeCity(){
        val materialBuilder = BottomSheetDialog(this)
        val inflater = layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.first_change_city_alert, null)

        //Founding BUTTONS
        val understand = dialogLayout.findViewById<Button>(R.id.understand_first_change_city_alert)

        //Configuring BACKGROUND
        configBack(dialogLayout)

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for CANCEL BUTTON
        understand.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
                changeCity()
            }
        }
    }

    //Fun for the FIRST ADD BUTTON ALERT in BUBBLE MANAGER
    @SuppressLint("InflateParams")
    private fun firstAddButton(){
        val materialBuilder = BottomSheetDialog(this)
        val inflater = layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.first_add_button_alert, null)

        //Founding BUTTONS
        val understand = dialogLayout.findViewById<Button>(R.id.understand_first_add)

        //Configuring BACKGROUND
        configBack(dialogLayout)

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for CANCEL BUTTON
        understand.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
                addButton()
            }
        }
    }

    //Fun for the WELCOME
    @SuppressLint("InflateParams")
    private fun welcome(){
        val materialBuilder = BottomSheetDialog(this)
        val inflater = layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.welcome_alert, null)

        //Founding BUTTONS
        val power = dialogLayout.findViewById<Button>(R.id.bubble)
        val bubble = dialogLayout.findViewById<Button>(R.id.power)
        val logo = dialogLayout.findViewById<ImageView>(R.id.image_logo_shorttasks)
        val imageBubble = dialogLayout.findViewById<ImageView>(R.id.image_bubble)

        //Configuring pictures
        if(nightMode){
            logo.setImageResource(R.drawable.ic_logo_main_dark)
            imageBubble.setImageResource(R.drawable.ic_bubble_manager_3_dark)
        }
        //Configuring BACKGROUND
        configBack(dialogLayout)

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for BUBBLE BUTTON
        bubble.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
                bubbleManagerFirst()
            }
        }

        //Click listener for POWER BUTTON
        power.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
                powerAssistantFirst()
            }
        }
    }

    //Fun for the BUBBLE MANAGER FIRST
    @SuppressLint("InflateParams")
    private fun bubbleManagerFirst(){
        val materialBuilder = BottomSheetDialog(this)
        val inflater = layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.bubble_manager_first_alert, null)

        //Founding BUTTONS
        val understand = dialogLayout.findViewById<Button>(R.id.cancel_bubble_manager_first)
        val enable = dialogLayout.findViewById<Button>(R.id.enable_first)
        val image1 = dialogLayout.findViewById<ImageView>(R.id.image_bubble_1_first)
        val image2 = dialogLayout.findViewById<ImageView>(R.id.image_bubble_2_first)
        val image3 = dialogLayout.findViewById<ImageView>(R.id.image_bubble_3_first)

        //Configuring pictures
        if(nightMode){
            image1.setImageResource(R.drawable.ic_bubble_manager_1_dark)
            image2.setImageResource(R.drawable.ic_bubble_manager_2_dark)
            image3.setImageResource(R.drawable.ic_bubble_manager_3_dark)
        }

        //Configuring BACKGROUND
        configBack(dialogLayout)

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for UNDERSTAND BUTTON
        understand.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
            }
        }

        //Click listener for ENABLE BUTTON
        enable.setOnClickListener {
            if(materialBuilder.isShowing){
                showBubble(this)
            }
        }
    }

    //Fun for the POWER ASSISTANT FIRST
    @SuppressLint("InflateParams")
    private fun powerAssistantFirst(){
        val materialBuilder = BottomSheetDialog(this)
        val inflater = layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.power_assistant_first_alert, null)

        //Founding BUTTONS
        val understand = dialogLayout.findViewById<Button>(R.id.cancel_power_assistant_first)
        val go = dialogLayout.findViewById<Button>(R.id.go_access_first)
        val image1 = dialogLayout.findViewById<ImageView>(R.id.image_power_1_first)
        val image2 = dialogLayout.findViewById<ImageView>(R.id.image_power_2_first)

        //Configuring pictures
        if(nightMode){
            image1.setImageResource(R.drawable.ic_power_assistant_1_dark)
            image2.setImageResource(R.drawable.ic_power_assistant_2_dark)
        }

        //Configuring BACKGROUND
        configBack(dialogLayout)

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for UNDERSTAND BUTTON
        understand.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
            }
        }

        //Click listener for GO BUTTON
        go.setOnClickListener {
            if(materialBuilder.isShowing){
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                startActivity(intent)
            }
        }
    }

    //Fun for the CLEAR DATA
    @SuppressLint("InflateParams")
    private fun clearData(){
        val materialBuilder = BottomSheetDialog(this)
        val inflater = layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.clear_data_allowing_alert, null)

        //Founding BUTTONS
        val ok = dialogLayout.findViewById<Button>(R.id.ok_clear)
        val cancel = dialogLayout.findViewById<Button>(R.id.cancel_clear)

        //Configuring BACKGROUND
        configBack(dialogLayout)

        //Showing BOTTOM SHEET
        materialBuilder.setContentView(dialogLayout)
        materialBuilder.show()

        //Click listener for UNDERSTAND BUTTON
        ok.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
                sharedPreference.edit().clear().apply()
            }
        }

        //Click listener for GO BUTTON
        cancel.setOnClickListener {
            if(materialBuilder.isShowing){
                materialBuilder.dismiss()
            }
        }
    }

    //Fun for get LABELS
    private fun getAppsLabel(): MutableList<String> {
        val manager = packageManager
        val userLabel = mutableListOf<String>()
        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)
        val availableActivities: List<ResolveInfo> = manager.queryIntentActivities(i, 0)
        for (ri in availableActivities) {
            userLabel.add(ri.loadLabel(manager).toString())
        }

        return userLabel
    }

    //Fun for get PACKAGES
    private fun getAppsPackage(): MutableList<String> {
        val manager = packageManager
        val userPackage = mutableListOf<String>()
        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)
        val availableActivities: List<ResolveInfo> = manager.queryIntentActivities(i, 0)
        for (ri in availableActivities) {
            userPackage.add(ri.activityInfo.packageName)
        }

        return userPackage
    }

    //Fun for get ICONS
    private fun getAppsIcon(): MutableList<Drawable?> {
        val manager = packageManager
        val userIcon = mutableListOf<Drawable?>()
        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)
        val availableActivities: List<ResolveInfo> = manager.queryIntentActivities(i, 0)
        for (ri in availableActivities) {
            userIcon.add(ri.activityInfo.loadIcon(manager))
        }

        return userIcon
    }

    //Fun for get Phone THEME
    private fun getColor(): Int {
        val typedValue = TypedValue()
        val contextThemeWrapper = ContextThemeWrapper(this,
                android.R.style.Theme_DeviceDefault)
        contextThemeWrapper.theme.resolveAttribute(android.R.attr.colorAccent,
                typedValue, true)
        return typedValue.data
    }

     //Fun for get COLOR THEME BACKGROUND
     private fun getBackground(): Int {
         var color = ""
         when(nightMode){
             true -> {
                 when(getColor()) {
                     -7686920 -> { color = "#162A49" }
                     -4359937 -> { color = "#2F1845" }
                     -3625836 -> { color = "#34271C" }
                     -3955038 -> { color = "#543C38" }
                     -942723 -> { color = "#4D3830" }
                     -15007797 -> { color = "#2C4F47" }
                     -2629914 -> { color = "#242527" }
                     -8076920 -> { color = "#3D523E" }
                     -14107177 -> { color = "#395458" }
                     -6705972 -> { color = "#374151" }
                     -18727 -> { color = "#503D46" }
                     -12722945 -> { color = "#2B4449" }
                     -1668371 -> { color = "#594B5A" }
                     -4871684 -> { color = "#3D3953" }
                 }
             } false -> {
             when(getColor()) {
                 -7686920 -> { color = "#8AB4F8" }
                 -4359937 -> { color = "#C89EF1" }
                 -3625836 -> { color = "#C8AC94" }
                 -3955038 -> { color = "#C3A6A2" }
                 -942723 -> { color = "#E3AF9A" }
                 -15007797 -> { color = "#95D4C6" }
                 -2629914 -> { color = "#D7DEE6" }
                 -8076920 -> { color = "#A1C7A3" }
                 -14107177 -> { color = "#91CBD4" }
                 -6705972 -> { color = "#A4B4CE" }
                 -18727 -> { color = "#FFD6E9" }
                 -12722945 -> { color = "#B8F2FF" }
                 -1668371 -> { color = "#EAC1ED" }
                 -4871684 -> { color = "#C5BBFE" }
             }
         }
         }
         return Color.parseColor(color)
     }

     //Fun for get COLOR THEME BUTTON
     private fun getButtonColor(): Int {
         var color = "#ffffff"
         when(nightMode){
             true -> {
                 when(getColor()) {
                     -7686920 -> { color = "#8AB4F8" }
                     -4359937 -> { color = "#C89EF1" }
                     -3625836 -> { color = "#C8AC94" }
                     -3955038 -> { color = "#C3A6A2" }
                     -942723 -> { color = "#E3AF9A" }
                     -15007797 -> { color = "#95D4C6" }
                     -2629914 -> { color = "#D7DEE6" }
                     -8076920 -> { color = "#A1C7A3" }
                     -14107177 -> { color = "#91CBD4" }
                     -6705972 -> { color = "#A4B4CE" }
                     -18727 -> { color = "#FFD6E9" }
                     -12722945 -> { color = "#B8F2FF" }
                     -1668371 -> { color = "#EAC1ED" }
                     -4871684 -> { color = "#C5BBFE" }
                 }
             } false -> {
                when(getColor()) {
                    -7686920 -> { color = "#162A49" }
                    -4359937 -> { color = "#2F1845" }
                    -3625836 -> { color = "#34271C" }
                    -3955038 -> { color = "#543C38" }
                    -942723 -> { color = "#4D3830" }
                    -15007797 -> { color = "#2C4F47" }
                    -2629914 -> { color = "#242527" }
                    -8076920 -> { color = "#3D523E" }
                    -14107177 -> { color = "#395458" }
                    -6705972 -> { color = "#374151" }
                    -18727 -> { color = "#503D46" }
                    -12722945 -> { color = "#2B4449" }
                    -1668371 -> { color = "#594B5A" }
                    -4871684 -> { color = "#3D3953" }
             }
         }
         }
         return Color.parseColor(color)
     }
}