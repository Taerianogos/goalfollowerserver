package cegeka.goalfollower.ro.goalfollower;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class SetpassActivity extends AppCompatActivity {
static ArrayList<String> pass=new ArrayList<>(Collections.nCopies(1, ""));
static String filenameforpass="asfdo";
EditText setpassssssssss;
Button setasdklfjals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpass);
        setpassssssssss=findViewById(R.id.editText9);
        setasdklfjals=findViewById(R.id.button6);
        Readpass();
        setpassssssssss.setText(pass.get(0));


        setasdklfjals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass.set(0,setpassssssssss.getText().toString());
                Addpass();
                finish();
            }
        });

    }
    public void Addpass() {
        File myfile = new File(this.getFilesDir(), filenameforpass);
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filenameforpass, MODE_PRIVATE);
            ObjectOutputStream o = new ObjectOutputStream(outputStream);
            o.reset();
            o.writeObject(pass);
            o.flush();
            o.close();
            finish();
        } catch (Exception e) {
            Toast.makeText(SetpassActivity.this, "no", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }
    public void Readpass() {
        FileInputStream fis;
        try {
            fis = openFileInput(filenameforpass);
            ObjectInputStream ois = new ObjectInputStream(fis);
            pass = (ArrayList<String>) ois.readObject();

            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
