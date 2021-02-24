package com.colorata.st

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
        createVideos()
        guidelineConfig()

        help.setOnClickListener{
            val materialBuilder = BottomSheetDialog(this)
            val inflater = layoutInflater
            val dialogLayout: View = inflater.inflate(R.layout.help_alert, null)

            val cancel = dialogLayout.findViewById<Button>(R.id.cancel_help)

            materialBuilder.setContentView(dialogLayout)
            materialBuilder.show()

            cancel.setOnClickListener {
                if(materialBuilder.isShowing){
                    materialBuilder.dismiss()
                }
            }
        }

        about.setOnClickListener {
            val materialBuilder = BottomSheetDialog(this)
            val inflater = layoutInflater
            val dialogLayout: View = inflater.inflate(R.layout.about_alert, null)

            val cancel = dialogLayout.findViewById<Button>(R.id.cancel_version)
            val source = dialogLayout.findViewById<Button>(R.id.sourcecode)
            val support = dialogLayout.findViewById<Button>(R.id.support)
            val version = dialogLayout.findViewById<Button>(R.id.version)

            materialBuilder.setContentView(dialogLayout)
            materialBuilder.show()

            source.setOnClickListener {
                if (materialBuilder.isShowing) {
                    materialBuilder.dismiss()
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.addCategory(Intent.CATEGORY_BROWSABLE)
                    intent.data = Uri.parse("https://github.com/renattele/Colorata")
                    startActivity(intent)
                }
            }
            support.setOnClickListener {
                if(materialBuilder.isShowing){
                    materialBuilder.dismiss()
                    val intent1 = Intent()
                    intent1.action = Intent.ACTION_VIEW
                    intent1.addCategory(Intent.CATEGORY_BROWSABLE)
                    intent1.data = Uri.parse("https://qiwi.ru")
                    startActivity(intent1)
                }
            }
            version.setOnClickListener {
                if(materialBuilder.isShowing){
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
            cancel.setOnClickListener {
                if(materialBuilder.isShowing){
                    materialBuilder.dismiss()
                }
            }
        }


        settings.setOnClickListener {
            val materialBuilder = BottomSheetDialog(this)
            val inflater = layoutInflater
            val dialogLayout: View = inflater.inflate(R.layout.settings_alert, null)

            val cancel = dialogLayout.findViewById<Button>(R.id.cancel_settings)
            val settings = dialogLayout.findViewById<Button>(R.id.settings_app)
            val enable = dialogLayout.findViewById<Button>(R.id.enable_bubbles)
            val city = dialogLayout.findViewById<Button>(R.id.city_change)

            materialBuilder.setContentView(dialogLayout)
            materialBuilder.show()

            settings.setOnClickListener {
                if (materialBuilder.isShowing) {
                    materialBuilder.dismiss()
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
            }
            enable.setOnClickListener {
                if(materialBuilder.isShowing){
                    materialBuilder.dismiss()
                    showBubble(this)
                }
            }
            city.setOnClickListener {
                if(materialBuilder.isShowing){
                    materialBuilder.dismiss()
                    val materialBuilderCity = BottomSheetDialog(this)
                    val inflaterCity = layoutInflater
                    val dialogLayoutCity: View = inflaterCity.inflate(
                        R.layout.change_city_alert,
                        null
                    )
                    val okCity = dialogLayoutCity.findViewById<Button>(R.id.ok_change_city)
                    val cancelCity = dialogLayoutCity.findViewById<Button>(R.id.cancel_change_city)
                    val changeCityField = dialogLayoutCity.findViewById<TextInputEditText>(R.id.city_field_in)

                    materialBuilderCity.setContentView(dialogLayoutCity)
                    materialBuilderCity.show()

                    okCity.setOnClickListener {
                        if(materialBuilderCity.isShowing) {
                            if (changeCityField.text.toString()!="") {
                                val editor = sharedPreference.edit()
                                editor.putString("city", changeCityField.text.toString())
                                editor.apply()
                                materialBuilderCity.dismiss()
                            } else {
                                Snackbar.make(it, "Something isn't entered", 1500).apply { view.elevation = 1000F }.show()
                            }
                        }
                    }
                    cancelCity.setOnClickListener {
                        if(materialBuilderCity.isShowing){
                            materialBuilderCity.dismiss()
                        }
                    }
                }
            }
            cancel.setOnClickListener {
                if(materialBuilder.isShowing){
                    materialBuilder.dismiss()
                }
            }
        }
        add_button.setOnClickListener {
            val materialBuilder = BottomSheetDialog(this)
            val inflater = layoutInflater
            val dialogLayout: View = inflater.inflate(R.layout.add_button_alert, null)

            val ok = dialogLayout.findViewById<Button>(R.id.ok_add_button)
            val cancel = dialogLayout.findViewById<Button>(R.id.cancel_add_button)
            val titleField = dialogLayout.findViewById<TextInputEditText>(R.id.title_field_in)
            val packageField = dialogLayout.findViewById<TextInputEditText>(R.id.package_field_in)
            val activityField = dialogLayout.findViewById<TextInputEditText>(R.id.activity_field_in)

            materialBuilder.setContentView(dialogLayout)
            materialBuilder.show()

            ok.setOnClickListener {
                if(materialBuilder.isShowing) {
                    if(titleField.text.toString()!="" && packageField.text.toString()!="" && activityField.text.toString()!="") {
                        val editor = sharedPreference.edit()
                        editor.putString("title", titleField.text.toString())
                        editor.putString("package", packageField.text.toString())
                        editor.putString("activity", activityField.text.toString())
                        editor.apply()
                        materialBuilder.dismiss()
                    }
                    else{
                        Snackbar.make(it, "Something isn't entered", 1500).apply { view.elevation = 1000F }.show()
                    }
                }
            }
            cancel.setOnClickListener {
                if(materialBuilder.isShowing){
                    materialBuilder.dismiss()
                }
            }

        }
        change_position.setOnClickListener {
            val materialBuilder = BottomSheetDialog(this)
            val inflater = layoutInflater
            val dialogLayout: View = inflater.inflate(R.layout.change_controls_alert, null)
            val changeRecycler = dialogLayout.findViewById<RecyclerView>(R.id.change_recycler)
            val ok = dialogLayout.findViewById<Button>(R.id.ok_change_position)
            val cancel = dialogLayout.findViewById<Button>(R.id.cancel_change_position)
            val mutableNames = mutableListOf<String>()
            val mutableIcons = mutableListOf<Int>()
            for (i in 0..GenerItems().names().lastIndex){
                    mutableNames.add(
                        sharedPreference.getString(
                            "name $i",
                            GenerItems().names()[i]
                        ).toString()
                    )
                    mutableIcons.add(sharedPreference.getInt("icon $i", GenerItems().icons()[i]))
            }

            Log.d("aaa", mutableNames.toString())
            Log.d("ii", sharedPreference.getString("name 12", null).toString())
            changeRecycler.layoutManager = GridLayoutManager(applicationContext, 3)
            var bubbleAdapter = BubbleAdapter(mutableNames, mutableIcons)
            changeRecycler.adapter = bubbleAdapter
            materialBuilder.setContentView(dialogLayout)
            materialBuilder.show()
            changeRecycler.addOnItemTouchListener(
                RecyclerItemClickListener(changeRecycler,
                    object : RecyclerItemClickListener.OnItemClickListener {
                        @SuppressLint("WrongConstant")
                        override fun onItemClick(view: View, position: Int) {
                            if (!posIs){
                                bufferName = mutableNames[position]
                                bufferIcon = mutableIcons[position]
                                pos1 = position
                                posIs = !posIs
                            } else{
                                bufferName2 = mutableNames[position]
                                bufferIcon2 = mutableIcons[position]
                                pos2 = position
                                mutableNames[pos1] = bufferName2
                                mutableIcons[pos1] = bufferIcon2
                                mutableNames[pos2] = bufferName
                                mutableIcons[pos2] = bufferIcon
                                bubbleAdapter = BubbleAdapter(mutableNames, mutableIcons)
                                changeRecycler.adapter?.notifyItemChanged(pos1)
                                changeRecycler.adapter?.notifyItemChanged(pos2)
                                GenerItems().icons = mutableIcons
                                GenerItems().names = mutableNames
                                posIs = !posIs
                            }
                        }
                    }))

            ok.setOnClickListener {
                if(materialBuilder.isShowing) {
                    sharedPreference.edit().clear().apply()
                    val editor = sharedPreference.edit()
                    for (i in 0..mutableNames.lastIndex){
                        editor.putString("name $i", mutableNames[i])
                        editor.putInt("icon $i", mutableIcons[i])
                    }
                    editor.apply()
                    Log.d("i", sharedPreference.getString("name 2", null).toString())
                    materialBuilder.dismiss()
                }
            }
            cancel.setOnClickListener {
                if(materialBuilder.isShowing){
                    Log.d("aa", sharedPreference.getString("name 2", null).toString())
                    materialBuilder.dismiss()
                }
            }
        }
    }


    private fun createVideos(){
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.duration = 700

        val animation = AnimationSet(false)
        animation.addAnimation(fadeIn)
        logo_main.startAnimation(animation)
        text_main.startAnimation(animation)
    }

    private fun guidelineConfig(){
        val displayMetrics: DisplayMetrics = baseContext.resources.displayMetrics
        val dpHeight = displayMetrics.heightPixels / displayMetrics.density
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density

        guideline_one_handed_main.setGuidelineBegin((dpHeight * 0.37).toInt())
        guideline_logo_main.setGuidelineBegin((dpWidth * 0.37).toInt())
    }

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
            .setIcon(IconCompat.createWithResource(appContext, R.drawable.ic_logo_settings))
            .setIntent(pi)
            .apply { setAutoExpandBubble(true); setSuppressNotification(true) }
            .build()

        ShortcutManagerCompat.addDynamicShortcuts(
            this, mutableListOf(
                ShortcutInfoCompat.Builder(this, shortcutId)
                    .setLongLived(true)
                    .setShortLabel("Settings")
                    .setIntent(Intent(Settings.ACTION_SETTINGS))
                    .setIcon(IconCompat.createWithResource(this, R.drawable.ic_logo_settings))
                    .build()
            )
        )


        val builder = NotificationCompat.Builder(
            appContext,
            notifChannel
        )
            .setSmallIcon(R.drawable.ic_logo_settings)
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

    private fun getUserTitle(): String{
        return sharedPreference.getString("title", "User's").toString()
    }

}