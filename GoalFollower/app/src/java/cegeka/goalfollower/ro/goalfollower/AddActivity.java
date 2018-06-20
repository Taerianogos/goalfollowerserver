package cegeka.goalfollower.ro.goalfollower;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.nfc.NdefRecord.createMime;


public class AddActivity extends AppCompatActivity {
    EditText editTextdesc = null;
    EditText textDate = null;
    EditText editTextdescrip=null;
    Button addbtn = null;
    Button beam = null;
    static String filename = "goals";
    Goal item = new Goal();
    static String concat=null;
    ArrayList<Goal> items = new ArrayList<>();
    String stupiddesc=null;
    String stupiddate=null;
    String stupiddescrip=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        editTextdesc = findViewById(R.id.editText);
        editTextdescrip=findViewById(R.id.editText5);
        textDate = findViewById(R.id.editText3);
        addbtn = findViewById(R.id.button3);
        beam = findViewById(R.id.beambttn);

        beam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validate()) {
                    stupiddesc = editTextdesc.getText().toString();
                    stupiddate=textDate.getText().toString();
                    stupiddescrip=editTextdescrip.getText().toString();
                    concat=stupiddesc+"/"+stupiddate+"/"+stupiddescrip+"/";
                    Intent intent =
                            new Intent(AddActivity.this, BeamActivity.class);
                    startActivity(intent);
                }
            }
        });

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validate()) {
                    Readf();
                    item.desc = editTextdesc.getText().toString();
                    item.descrip=editTextdescrip.getText().toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm");
                    try {
                        item.dueDate = sdf.parse(textDate.getText().toString());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    items.add(item);
                    Addg();
                    finish();
                }
            }
        });
    }

    public void Addg() {
        File myfile = new File(this.getFilesDir(), filename);
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, MODE_PRIVATE);
            ObjectOutputStream o = new ObjectOutputStream(outputStream);
            o.reset();
            o.writeObject(items);
            o.flush();
            o.close();
            Intent intent = new Intent(AddActivity.this, MainActivity.class);
            if (myfile.exists()) Toast.makeText(AddActivity.this, "yes", Toast.LENGTH_LONG).show();
            setResult(Activity.RESULT_OK, intent);
            finish();
        } catch (Exception e) {
            Toast.makeText(AddActivity.this, "no", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }

    public void Readf() {
        FileInputStream fis;
        try {
            fis = openFileInput("goals");
            ObjectInputStream ois = new ObjectInputStream(fis);
            items = (ArrayList<Goal>) ois.readObject();

            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean Validate() {
        if (editTextdesc.getText().toString().trim().equals("") || editTextdescrip.getText().toString().trim().equals("")||
                textDate.getText().toString().trim().equals("")) {
            Toast.makeText(AddActivity.this,
                    "All the fields are mandatory",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        SimpleDateFormat format =
                new SimpleDateFormat("yyyy-MM-dd-hh-mm");
        Date date = null;
        try {
            date =
                    format.parse(textDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(AddActivity.this,
                    "Invalid date format",
                    Toast.LENGTH_LONG).show();
            return false;

        }
        Date currentDate = new Date();
        if (date != null && date.compareTo(currentDate) <= 0) {
            Toast.makeText(AddActivity.this,
                    "Date should be at least today",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
    }
