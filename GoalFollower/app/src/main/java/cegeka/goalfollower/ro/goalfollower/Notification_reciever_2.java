package cegeka.goalfollower.ro.goalfollower;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class Notification_reciever_2 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager_2 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int index ;
        index = intent.getIntExtra("index" , -1);
        int id = (int) System.currentTimeMillis()+1;
        Intent intent5 = new Intent (context , MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context , id , intent5 , PendingIntent.FLAG_UPDATE_CURRENT);
        Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.not_bad);

        if (index != -1)
        {
            NotificationCompat.Builder builder_2 = new NotificationCompat.Builder(context)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.notificon)
                    .setContentTitle(More_Info.S_name_not.get(index) + "" + index)
                    .setContentText(More_Info.S_description_not.get(index))
                    .setSound(sound)
                    .setAutoCancel(true);


            if (intent.getAction().equals("MY_NOTIFICATION_MESSAGE_2")) {
                notificationManager_2.notify(id , builder_2.build());
            }
        }



    }



}

