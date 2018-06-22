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
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    EditText editpass;
    Button addbtn = null;
    Button beam = null;
    static String filename = "goals";
    Goal item = new Goal();
    Button mSendBtn = null;
    static String concat=null;
    ArrayList<Goal> items = new ArrayList<>();
    String stupiddesc=null;
    String stupiddate=null;
    String stupiddescrip=null;
    String stupidpass=null;
    int s;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    static Goal prostie = new Goal();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setTitle("Add or send a new goal");
        editTextdesc = findViewById(R.id.editText);
        editTextdescrip=findViewById(R.id.editText5);
        addbtn = findViewById(R.id.button3);
        beam = findViewById(R.id.beambttn);
        mSendBtn = findViewById(R.id.sendBtn);

        beam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validate()) {
                    stupiddesc = editTextdesc.getText().toString();
                    stupiddate="9999-9-9-9-9";
                    stupiddescrip=editTextdescrip.getText().toString();
                    stupidpass=FirebaseAuth.getInstance().getCurrentUser().getUid();
                    concat=stupiddesc+"/"+stupiddate+"/"+stupiddescrip+"/"+stupidpass+"/";
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
                        item.dueDate = sdf.parse("9999-9-9-9-9");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Nr Goals");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            s = dataSnapshot.getValue(int.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    s++;
                    myRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Nr Goals").setValue(s);
                    myRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Goals").child(item.desc).setValue(item);
                    items.add(item);
                    Addg();
                    finish();
                }
            }
        });
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Validate()) return;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm");
                try{
                    prostie.dueDate = sdf.parse("9999-9-9-9-9");
                } catch(Exception ex){
                    ex.printStackTrace();
                }
                prostie.desc = editTextdesc.getText().toString();
                prostie.descrip = editTextdescrip.getText().toString();
                prostie.pass = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Intent intent = new Intent(AddActivity.this, SendViaNetActivity.class);
                startActivity(intent);
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
        if (editTextdesc.getText().toString().trim().equals("") || editTextdescrip.getText().toString().trim().equals("")) {
            Toast.makeText(AddActivity.this,
                    "All the fields are mandatory",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    }
