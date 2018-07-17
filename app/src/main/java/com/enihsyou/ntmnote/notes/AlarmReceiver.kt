package com.enihsyou.ntmnote.notes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.enihsyou.ntmnote.App.Companion.CHANNEL_ID
import com.enihsyou.ntmnote.R

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.

        val label = intent.getStringExtra(EXT_NOTIFY_TITLE)
        val content = intent.getStringExtra(EXT_NOTIFY_CONTENT)

        val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(label)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        NotificationManagerCompat.from(context).notify(0, mBuilder.build())
    }

    companion object {
        private const val EXT_NOTIFY_TITLE = "title"
        private const val EXT_NOTIFY_CONTENT = "content"

        fun newIntent(context: Context?, label: String, content: String): Intent {
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.putExtra(AlarmReceiver.EXT_NOTIFY_TITLE, label)
            intent.putExtra(AlarmReceiver.EXT_NOTIFY_CONTENT, content)
            return intent
        }
    }
}
