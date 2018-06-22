package cegeka.goalfollower.ro.goalfollower;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.nfc.NdefRecord.createMime;
import static cegeka.goalfollower.ro.goalfollower.AddActivity.concat;
import static cegeka.goalfollower.ro.goalfollower.AddActivity.filename;

public class RecActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback,NfcAdapter.OnNdefPushCompleteCallback {
    EditText editdesc=null;
    EditText editdate=null;
    EditText editdescrip=null;
    Button addtolist=null;
    NfcAdapter mmNfcAdapter;
    Goal recitem=new Goal();
    int i=0,s=0;
    ArrayList<Goal> recitems = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec);
        setTitle("Receive goal through NFC");
        editdesc = findViewById(R.id.editText2);
        editdescrip=findViewById(R.id.editText6);
        addtolist = findViewById(R.id.button);
        mmNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mmNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        // Register callback
        mmNfcAdapter.setNdefPushMessageCallback((NfcAdapter.CreateNdefMessageCallback) this, this);
       mmNfcAdapter.setOnNdefPushCompleteCallback((NfcAdapter.OnNdefPushCompleteCallback) this, this);
        addtolist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Readfrec();
                recitem.desc = editdesc.getText().toString();
                recitem.descrip=editdescrip.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm");
                try {
                    recitem.dueDate = sdf.parse(editdate.getText().toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                myRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Goals").child(recitem.desc).setValue(recitem);
                recitems.add(recitem);
                Addgrec();

            }
        });
    }

    public NdefMessage createNdefMessage(NfcEvent event) {
        NdefMessage msg = new NdefMessage(
                new NdefRecord[] { createMime(
                        "application/cegeka.goalfollower.ro", concat.getBytes())
                        ,NdefRecord.createApplicationRecord("cegeka.goalfollower.ro")
                });
        return msg;
    }

    @Override
    public void onResume () {
        super.onResume();
        // Check to see that the Activity started due to an Android Beam

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        PendingIntent pi = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                0);
        adapter.enableForegroundDispatch(this, pi, null, null);
    }

    @Override
    public void onNewIntent (Intent intent){
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    void processIntent (Intent intent){
        editdesc = findViewById(R.id.editText2);
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        String neconcat= new String(msg.getRecords()[0].getPayload());
        for (String retval: neconcat.split("/")) {
            if(i==0) editdesc.setText(retval);
            //if(i==1) editdate.setText(retval);
            if(i==2) editdescrip.setText(retval);
            if(i==3) recitem.pass=retval;
            i++;
        }
    }
    @Override
    public void onNdefPushComplete(NfcEvent event) {

    }
    public void Addgrec() {
        File myfile = new File(this.getFilesDir(), filename);
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, MODE_PRIVATE);
            ObjectOutputStream o = new ObjectOutputStream(outputStream);
            o.reset();
            o.writeObject(recitems);
            o.flush();
            o.close();
            finish();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public void Readfrec() {
        FileInputStream fis;
        try {
            fis = openFileInput("goals");
            ObjectInputStream ois = new ObjectInputStream(fis);
            recitems = (ArrayList<Goal>) ois.readObject();

            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
