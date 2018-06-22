package cegeka.goalfollower.ro.goalfollower;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConfirmMenuActivity extends AppCompatActivity {
    Button net, nfc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_menu);
        setTitle("Confirm goals you've sent!");
        net = findViewById(R.id.confirmNetBtn);
        nfc = findViewById(R.id.confirmNfcBtn);
        net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FinishInternetActivity.class));
            }
        });
        nfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UnlockActivity.class));
            }
        });
    }
}
