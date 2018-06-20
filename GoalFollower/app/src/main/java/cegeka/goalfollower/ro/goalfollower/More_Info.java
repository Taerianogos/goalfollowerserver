package cegeka.goalfollower.ro.goalfollower;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.app.PendingIntent.getActivity;
import static cegeka.goalfollower.ro.goalfollower.AddActivity.filename;

import static cegeka.goalfollower.ro.goalfollower.ListActivity.descrips;
import static cegeka.goalfollower.ro.goalfollower.ListActivity.returnlist;
import static cegeka.goalfollower.ro.goalfollower.ListActivity.sizelist;
import static cegeka.goalfollower.ro.goalfollower.UnlockRecActivity.ifvalidpass;

public class More_Info extends AppCompatActivity {

    static boolean[] check = new boolean[1001];
    ArrayList<Integer> charr = new ArrayList<>();
    String itemmf;
    String upoi;
    static int index;
    static int indexfordel;
    public static int sum ;
    String filenameforcheck="checkbox";
    String filenamefordesc="desctime";
    String filenameforname="nae234852045";
    static String filenamescore="odjfk";
    Button set;
    Button done;
    EditText name_not;
    EditText description_not;
    EditText duration_not;
    TextView desc_not;
    public static ArrayList<String> S_name_not = new ArrayList<>();
    public static ArrayList<String> S_description_not =  new ArrayList<>();
    public static ArrayList<Integer> opkivus=new ArrayList<>(Collections.nCopies(1, 0));
    String S_duration_not;
    int I_duration_not;
    int[] durations = new int[1001];
    AlarmManager asfasdodf;
    ArrayList<AlarmManager> alarmManagers = new ArrayList<>(Collections.nCopies(1001, asfasdodf));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more__info);


        Intent in = getIntent();
        done=findViewById(R.id.button2);
        index = in.getIntExtra("com.example.cristi.firstcegeka.Item" , -1);
        Toast.makeText(this, index + "" , Toast.LENGTH_LONG).show();

        done.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
            indexfordel=index;
        Intent passssssIntent = new Intent (More_Info.this ,UnlockRecActivity.class);
        startActivityForResult(passssssIntent,3);
        Toast.makeText(More_Info.this,ifvalidpass+"",Toast.LENGTH_LONG).show();
    }
});
        set = (Button) findViewById(R.id.set_not_info_btn);
        name_not = (EditText) findViewById(R.id.not_name_edit_text);
        description_not = (EditText) findViewById(R.id.not_description_edit_text);
        duration_not = (EditText) findViewById(R.id.not_time_edit_text);
        desc_not=(TextView) findViewById(R.id.textView4);
        Log.d("caba",descrips.get(index));
        desc_not.setText(descrips.get(index));
        //itemmf.des="a";
        //for(int i=1;i<=1001;i++) ndt.add(itemmf);
        //Adddesc();
        for(int i=0;i<1001;i++) S_name_not.add("");
        for(int i=0;i<1001;i++) S_description_not.add("");

        Readdesc();
        Readnem();
        itemmf=S_name_not.get(index);
        upoi=S_description_not.get(index);
        //Toast.makeText(More_Info.this,itemmf.nem,Toast.LENGTH_LONG).show();
        /*name_not.setText("");
        description_not.setText("");
        duration_not.setText("");*/
        name_not.setText(itemmf);
        description_not.setText(upoi);
       // duration_not.setText(itemmf.ti+"");
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                S_duration_not = duration_not.getText().toString();
                I_duration_not = Integer.parseInt(S_duration_not);
                Toast.makeText(getApplicationContext() , "" + I_duration_not , Toast.LENGTH_LONG).show();
                itemmf=name_not.getText().toString();
                S_name_not.set(index,itemmf);
                upoi=description_not.getText().toString();
                S_description_not.set(index,upoi);
                durations[index] = I_duration_not;
               // for (int f=0 ; f<val ; f++)
               // {

                    Intent intent13 = new Intent(getApplicationContext() , Notification_reciever_2.class);
                    intent13.putExtra("index" , index);
                    intent13.setAction("MY_NOTIFICATION_MESSAGE_2");
                    PendingIntent pendingIntent12 = PendingIntent.getBroadcast(getApplicationContext(), index, intent13, PendingIntent.FLAG_UPDATE_CURRENT);
//                    alarmManagers.add(null);
                    alarmManagers.set(index,(AlarmManager) getSystemService(ALARM_SERVICE));
                    alarmManagers.get(index).setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000 *durations[index], pendingIntent12);
                   // Toast.makeText(getApplicationContext() , "" + f , Toast.LENGTH_LONG).show();
                // intentArray.add(pendingIntent12);
                //}
                Toast.makeText(getApplicationContext(), "Notificare setata", Toast.LENGTH_LONG).show();
               // Intent intent6 = new Intent(getApplicationContext(), ListActivity.class);
               // startActivity(intent6);
                Adddesc();
                Addnem();
                //S_name_not.clear();
                //S_description_not.clear();
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3){
            switch (resultCode){
                case Activity.RESULT_OK:{Intent intent13 = new Intent(getApplicationContext() , Notification_reciever_2.class);
                    intent13.putExtra("index" , index);
                    intent13.setAction("MY_NOTIFICATION_MESSAGE_2");
                    PendingIntent pendingIntent12 = PendingIntent.getBroadcast(getApplicationContext(), sizelist-1, intent13, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManagers.set(sizelist-1,(AlarmManager) getSystemService(ALARM_SERVICE));
                    alarmManagers.get(sizelist-1).cancel(pendingIntent12);
                    alarmManagers.remove(sizelist-1);
                    returnlist.remove(index);
                    S_description_not.remove(index);
                    S_name_not.remove(index);
                    //Log.d("caba",index+"");
                    sum=sum+100;
                    opkivus.set(0,sum);
                    Addscor();
                    Addg();
                    Adddesc();
                    Addnem();
                    ListActivity.act.finish();
                    finish();
                    break;}
                default:
                    Toast.makeText(More_Info.this, "Parola gresita", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void Addgcheck() {
        File myfile = new File(this.getFilesDir(), filenameforcheck);
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filenameforcheck, MODE_PRIVATE);
            ObjectOutputStream o = new ObjectOutputStream(outputStream);
            o.reset();
            o.writeObject(charr);
            o.flush();
            o.close();
            if (myfile.exists()) Toast.makeText(More_Info.this, "yes", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(More_Info.this, "no", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
public void Addnem(){
            File myfile = new File(this.getFilesDir(), filenameforname);
            FileOutputStream outputStream;
            try {
                outputStream = openFileOutput(filenameforname, MODE_PRIVATE);
                ObjectOutputStream o = new ObjectOutputStream(outputStream);
                o.reset();
                o.writeObject(S_name_not);
                o.flush();
                o.close();
                if (myfile.exists()) Toast.makeText(More_Info.this, "yes", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(More_Info.this, "no", Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }
        }

    public void Adddesc() {
        File myfile = new File(this.getFilesDir(), filenamefordesc);
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filenamefordesc, MODE_PRIVATE);
            ObjectOutputStream o = new ObjectOutputStream(outputStream);
            o.reset();
            o.writeObject(S_description_not);
            o.flush();
            o.close();
            if (myfile.exists()) Toast.makeText(More_Info.this, "yes", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(More_Info.this, "no", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }


    }
    public void Readfcheck() {
        FileInputStream fis;
        try {
            fis = openFileInput(filenameforcheck);
            ObjectInputStream ois = new ObjectInputStream(fis);
            charr = (ArrayList<Integer>) ois.readObject();

            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void Readdesc() {
        FileInputStream fis;
        try {
            fis = openFileInput(filenamefordesc);
            ObjectInputStream ois = new ObjectInputStream(fis);
            S_description_not = (ArrayList<String>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void Readnem(){FileInputStream fis;
        try {
            fis = openFileInput(filenameforname);
            ObjectInputStream ois = new ObjectInputStream(fis);
            S_name_not = (ArrayList<String>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void Addg() {
        File myfile = new File(this.getFilesDir(), filename);
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, MODE_PRIVATE);
            ObjectOutputStream o = new ObjectOutputStream(outputStream);
            o.reset();
            o.writeObject(returnlist);
            o.flush();
            o.close();
            finish();
        } catch (Exception e) {
            Toast.makeText(More_Info.this, "no", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }
    public void Addscor() {
        File myfile = new File(this.getFilesDir(), filenamescore);
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filenamescore, MODE_PRIVATE);
            ObjectOutputStream o = new ObjectOutputStream(outputStream);
            o.reset();
            o.writeObject(opkivus);
            o.flush();
            o.close();
            if (myfile.exists()) Toast.makeText(More_Info.this, "yes", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(More_Info.this, "no", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }

}
