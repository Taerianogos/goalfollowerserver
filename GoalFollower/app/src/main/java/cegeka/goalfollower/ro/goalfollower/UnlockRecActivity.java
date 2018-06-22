package cegeka.goalfollower.ro.goalfollower;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;

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
import static cegeka.goalfollower.ro.goalfollower.ListActivity.returnlist;
import static cegeka.goalfollower.ro.goalfollower.More_Info.indexfordel;
import static cegeka.goalfollower.ro.goalfollower.SetpassActivity.pass;

public class UnlockRecActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback,NfcAdapter.OnNdefPushCompleteCallback {
TextView textvalid;
    Button deltolist;
    NfcAdapter mmNfcAdapter;
   static String validpass=null;
   static boolean ifvalidpass=false;
   Goal jfoladalsf=new Goal();
    int i=0;
    ArrayList<Goal> recitems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock_rec);
        mmNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        setTitle("Receive confirmation through NFC");
        deltolist=findViewById(R.id.button7);
        if (mmNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        // Register callback
        mmNfcAdapter.setNdefPushMessageCallback((NfcAdapter.CreateNdefMessageCallback) this, this);
        mmNfcAdapter.setOnNdefPushCompleteCallback((NfcAdapter.OnNdefPushCompleteCallback) this, this);

deltolist.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        finish();
    }
});
    }

    public NdefMessage createNdefMessage(NfcEvent event) {
        NdefMessage msg = new NdefMessage(
                new NdefRecord[] { createMime(
                        "application/cegeka.goalfollower.ro", FirebaseAuth.getInstance().getCurrentUser().getUid().getBytes())
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
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        String neconcat= new String(msg.getRecords()[0].getPayload());
        textvalid=findViewById(R.id.textView5);
        validpass=neconcat;
        Readf();
jfoladalsf=returnlist.get(indexfordel);
        if(jfoladalsf.pass.equals(validpass)) {textvalid.setText("Password valid");Intent intentshu = new Intent(UnlockRecActivity.this, More_Info.class);
            setResult(Activity.RESULT_OK, intent); } else textvalid.setText("Password invalid");
    }
    @Override
    public void onNdefPushComplete(NfcEvent event) {

    }

    public void Readf(){FileInputStream fis;
        try {
            fis = openFileInput("goals");
            ObjectInputStream ois = new ObjectInputStream(fis);
            returnlist = (ArrayList<Goal>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            Toast.makeText(UnlockRecActivity.this,"Err read",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
