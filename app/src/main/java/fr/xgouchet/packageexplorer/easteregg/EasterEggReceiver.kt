package fr.xgouchet.packageexplorer.easteregg

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import fr.xgouchet.packageexplorer.R


class EasterEggReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        val host = intent?.data?.host ?: return

        val message = when (host) {
            // STANLEY
            "7826539" -> "Congrats, you found this easter egg ! Will you find the other ones ? If you need a hint, just ask for it ;)"
            // ANDROID
            "2637643" -> "Yes, yes, yes. We're all big android fans out there !"
            // HINT
            "4468" -> "Okay, here's a hint : it's something old, something blue, something borrowed, and so so blue. And it's phone doesn't work."
            // TARDIS
            "827347" -> "Oh, still there ? You like treasure hunts right ? Okay, next hint : "
            // 42, H2G2
            "42", "4242" -> "Sure thing, the answer is always 42 ! "
            // LOST, 4815162342
            "4815162342", "5678" -> "Really, you're “lost” ? Let me help you, you're here ↓."
            else -> return
        }

        val builder = NotificationCompat.Builder(context, "")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Easter Egg")
                .setColor(ContextCompat.getColor(context, R.color.accent))
                .setContentText(message)

        // Add as notification
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, builder.build())
    }
}
