package cegeka.goalfollower.ro.goalfollower;

import android.content.Intent;
import android.provider.ContactsContract;
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

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        final EditText mUser, mEmail, mPass;
        setTitle("Create a new account!");
        mUser = findViewById(R.id.usersignup);
        mEmail = findViewById(R.id.emailsignup);
        mPass = findViewById(R.id.passsignup);
        Button mSignUp = findViewById(R.id.signupbtn);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final String user = mUser.getText().toString().trim();
               final String email = mEmail.getText().toString().trim();
               final String pass =  mPass.getText().toString().trim();
               if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(SignUpActivity.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    return ;
                }
               if(TextUtils.isEmpty(user))
               {
                   Toast.makeText(SignUpActivity.this, "Please enter a username", Toast.LENGTH_LONG).show();
                   return ;
               }
               if(TextUtils.isEmpty(pass))
               {
                   Toast.makeText(SignUpActivity.this, "Please enter a password", Toast.LENGTH_LONG).show();
                   return ;
               }
               if(pass.length() < 6){
                   Toast.makeText(SignUpActivity.this, "The password must have more than 6 characters", Toast.LENGTH_LONG).show();
                   return ;
               }
               mAuth.createUserWithEmailAndPassword(email, pass)
                       .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                               if(task.isSuccessful()){
                                   mAuth.signInWithEmailAndPassword(email,pass)
                                           .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                               @Override
                                               public void onComplete(@NonNull Task<AuthResult> task) {
                                                   if(task.isSuccessful()){
                                                       myRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").setValue(user);
                                                       myRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Nr Goals").setValue(0+"");
                                                       myRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Score").setValue(0);
                                                       Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                                       startActivity(intent);
                                                   }
                                               }
                                           });
                               }
                               else Toast.makeText(SignUpActivity.this, "Sign up failure", Toast.LENGTH_LONG).show();
                           }
                       });
            }
        });
    }
}
