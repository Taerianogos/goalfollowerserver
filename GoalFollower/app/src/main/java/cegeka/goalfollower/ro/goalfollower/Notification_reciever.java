package cegeka.goalfollower.ro.goalfollower;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import static cegeka.goalfollower.ro.goalfollower.More_Info.filenamescore;
import static cegeka.goalfollower.ro.goalfollower.More_Info.opkivus;

public class Notification_reciever extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int id = (int) System.currentTimeMillis();
        Intent intent5 = new Intent (context , MainActivity.class);
        intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context , id , intent5 , PendingIntent.FLAG_UPDATE_CURRENT);
        Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.not_bad);

        FileInputStream fis;
        try {
            fis = context.openFileInput(filenamescore);
            ObjectInputStream ois = new ObjectInputStream(fis);
            opkivus = (ArrayList<Integer>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        More_Info.sum=opkivus.get(0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.notificon)
                .setContentTitle("Cegeka says")
                .setContentText("Your Score is equal to " + MainActivity.scor)
                .setSound(sound)
                .setAutoCancel(true);


        if (intent.getAction().equals("MY_NOTIFICATION_MESSAGE")) {
            notificationManager.notify(id , builder.build());
        }

    }
}
