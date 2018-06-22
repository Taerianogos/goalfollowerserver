package cegeka.goalfollower.ro.goalfollower;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static cegeka.goalfollower.ro.goalfollower.AddActivity.prostie;

public class SendViaNetActivity extends AppCompatActivity {

    Goal item;
    TextView mUserText;
    Button mSendBtn;
    String username;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_via_net);
        item =prostie;
        mUserText = findViewById(R.id.nameToSend);
        mSendBtn = findViewById(R.id.sendButton);
        setTitle("Send goal through internet");
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = mUserText.getText().toString().trim();
                myRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean ok = false;
                        String Uid = null;
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            String idk = child.child("username").getValue(String.class);
                            if(idk == null) continue;
                            Boolean idc = !(idk.equals(username));
                            if(idc) continue;
                            ok = true;
                            Uid = child.getKey().toString();
                        }
                        if(!ok) Toast.makeText(SendViaNetActivity.this, "Username not found", Toast.LENGTH_LONG).show();
                        else if(Uid != null){
                            myRef.child("users").child(Uid).child("Pending Goals").child(item.desc).setValue(item);
                            Toast.makeText(SendViaNetActivity.this, "Goal sent!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
