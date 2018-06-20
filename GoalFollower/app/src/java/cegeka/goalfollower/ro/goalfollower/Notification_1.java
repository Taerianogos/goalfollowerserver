package cegeka.goalfollower.ro.goalfollower;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class Notification_1 extends AppCompatActivity {

    Button btn ;
    int h;
    int m;
    String Sh , Sm ;
    EditText hEt;
    EditText mEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_1);
        hEt = (EditText) findViewById(R.id.hour_edit_text);
        mEt = (EditText) findViewById(R.id.minute_edti_text);
        btn = (Button) findViewById(R.id.set_not_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Sh = hEt.getText().toString();
                Sm = mEt.getText().toString();
                h = Integer.parseInt(Sh);
                m = Integer.parseInt(Sm);

                if (h > 24 || h <= 0)
                    Toast.makeText(getApplicationContext(), "Ziua are 24 de ore", Toast.LENGTH_LONG).show();
                else if (m > 60 || m < 0)
                    Toast.makeText(getApplicationContext(), "ora are 60 de minute", Toast.LENGTH_LONG).show();
                else
                {

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, h);
                    calendar.set(Calendar.MINUTE, m);
                    calendar.set(Calendar.SECOND, 13);

                    Intent intent3 = new Intent(getApplicationContext() , Notification_reciever.class);
                    intent3.setAction("MY_NOTIFICATION_MESSAGE");
                    PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getApplicationContext(), 10000, intent3, PendingIntent.FLAG_UPDATE_CURRENT);

                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent2);
                    Toast.makeText(getApplicationContext(), "Notificare setata", Toast.LENGTH_LONG).show();
                    Intent intent6 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent6);

                }


            }
        });

    }
}
