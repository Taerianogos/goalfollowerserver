package cegeka.goalfollower.ro.goalfollower;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

public class Notification_reciever extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int id = (int) System.currentTimeMillis();
        Intent intent5 = new Intent (context , MainActivity.class);
        intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context , id , intent5 , PendingIntent.FLAG_UPDATE_CURRENT);
        Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.not_bad);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.robotboi)
                .setContentTitle("Cegeka says")
                .setContentText("Your Score is equal to " + More_Info.sum)
                .setSound(sound)
                .setAutoCancel(true);


        if (intent.getAction().equals("MY_NOTIFICATION_MESSAGE")) {
            notificationManager.notify(id , builder.build());
        }

    }
}
