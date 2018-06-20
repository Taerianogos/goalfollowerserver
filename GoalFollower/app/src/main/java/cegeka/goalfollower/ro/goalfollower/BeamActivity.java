package cegeka.goalfollower.ro.goalfollower;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.nfc.NdefRecord.createMime;
import static cegeka.goalfollower.ro.goalfollower.AddActivity.concat;

public class BeamActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback,NfcAdapter.OnNdefPushCompleteCallback {
    NfcAdapter mNfcAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beam);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Register callback
        mNfcAdapter.setNdefPushMessageCallback((NfcAdapter.CreateNdefMessageCallback) this, this);
        mNfcAdapter.setOnNdefPushCompleteCallback((NfcAdapter.OnNdefPushCompleteCallback) this, this);

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
        TextView textView = (TextView) findViewById(R.id.textView2);
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        textView.setText(new String(msg.getRecords()[0].getPayload()));
    }
    @Override
    public void onNdefPushComplete(NfcEvent event) {

    }
}