package com.colorata.st.extensions

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.colorata.st.R
import com.colorata.st.activities.BubbleActivity

class ShowBubble(private val context: Context) {

    private var channelCreated = false
    private val notifId = 1337
    private val notifChannel = "Bubble Manager"
    private val shortcutId = "Bubble Manager"
    private val flagMutable = 0x02000000

    @RequiresApi(Build.VERSION_CODES.R)
    private fun buildBubbleNotification(appContext: Context): Notification {
        
        val piFlag = if (Build.VERSION.SDK_INT == 10000) {
            flagMutable or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT or flagMutable
        }

        val pi = PendingIntent.getActivity(
            appContext,
            0,
            Intent(appContext, BubbleActivity::class.java),
            piFlag
        )
        val bubble = NotificationCompat.BubbleMetadata.Builder(
            pi,
            IconCompat.createWithResource(appContext, R.drawable.logo_st)
        )
            .setDesiredHeight(Resources.getSystem().displayMetrics.heightPixels)
            .apply { setAutoExpandBubble(true); setSuppressNotification(true) }
            .build()

        ShortcutManagerCompat.addDynamicShortcuts(
            context, mutableListOf(
                ShortcutInfoCompat.Builder(context, shortcutId)
                    .setLongLived(true)
                    .setShortLabel("Bubble Manager")
                    .setIntent(Intent(Settings.ACTION_SETTINGS))
                    .setIcon(IconCompat.createWithResource(context, R.drawable.ic_outline_announcement_24))
                    .build()
            )
        )


        val builder = NotificationCompat.Builder(
            appContext,
            notifChannel
        )
            .setSmallIcon(R.drawable.ic_outline_announcement_24)
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
    fun show() {
        NotificationManagerCompat.from(context).let { mgr ->
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
            mgr.notify(notifId, buildBubbleNotification(context))
        }
    }
}