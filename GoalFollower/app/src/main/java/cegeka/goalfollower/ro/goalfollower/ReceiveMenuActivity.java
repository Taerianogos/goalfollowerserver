package cegeka.goalfollower.ro.goalfollower;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReceiveMenuActivity extends AppCompatActivity {

    Button nfc, net;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_menu);
        setTitle("Receive goal menu");
        nfc = findViewById(R.id.receiveNfcBtn);
        net = findViewById(R.id.receiveNetBtn);
        nfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RecActivity.class));
            }
        });
        net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PendingGoalActivity.class));
            }
        });
    }
}
