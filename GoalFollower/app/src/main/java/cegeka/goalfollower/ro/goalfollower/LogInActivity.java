package cegeka.goalfollower.ro.goalfollower;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogInActivity extends AppCompatActivity {

    Button mLogIn;
    EditText mEmail, mPass;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setTitle("Log in to GoalFollower");
        mLogIn = findViewById(R.id.button10);
        mEmail = findViewById(R.id.emaillogin);
        mPass = findViewById(R.id.passlogin);

        mLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String pass = mPass.getText().toString().trim();
                if(pass.length() >= 6 && !TextUtils.isEmpty(email)){
                    mAuth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        startActivity(new Intent(LogInActivity.this, MainActivity.class));
                                    }
                                    else Toast.makeText(LogInActivity.this, "Log in failure", Toast.LENGTH_LONG).show();
                                }
                            });

                }
                else Toast.makeText(LogInActivity.this, "Email or password invalid", Toast.LENGTH_LONG).show();
            }
        });
    }
}
