package cegeka.goalfollower.ro.goalfollower;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static cegeka.goalfollower.ro.goalfollower.More_Info.filenamescore;
import static cegeka.goalfollower.ro.goalfollower.More_Info.opkivus;

public class MainActivity extends AppCompatActivity {

    Button prof_pic;
    Button mLogOut;
    Button mPendingBtn;
    String mCurrentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    String filename = "image";
    String usern;
    ImageView img_cam;
    TextView score ;
    TextView usernem;
    Button refresh ;
    Button setnot;
    String mSecondPhotoPath;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myRefforus = database.getReference();
    DatabaseReference myRefforsc = database.getReference();

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void Addg() {
        File myfile = new File(getApplicationContext().getFilesDir(), filename);
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, MODE_PRIVATE);
            ObjectOutputStream o = new ObjectOutputStream(outputStream);
            o.reset();
            o.writeObject(mCurrentPhotoPath);
            o.flush();
            o.close();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void Readf() {
        FileInputStream fis;
        try {
            fis = openFileInput("image");
            ObjectInputStream ois = new ObjectInputStream(fis);
            mCurrentPhotoPath = (String) ois.readObject();

            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void Readscor() {
        FileInputStream fis;
        try {
            fis = openFileInput(filenamescore);
            ObjectInputStream ois = new ObjectInputStream(fis);
            opkivus = (ArrayList<Integer>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    TextView mata;
    public static int scor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img_cam = (ImageView) findViewById(R.id.profile_img);
        prof_pic = (Button) findViewById(R.id.prof_pic);
        //score = (TextView) findViewById(R.id.score_text_view);
        mLogOut = findViewById(R.id.log_out);
        usernem=findViewById(R.id.textView);
        mata = findViewById(R.id.textView6);
        setTitle("Menu");
        Readf();
        Readscor();
        //More_Info.sum=opkivus.get(0);
        // score.setText("Your Score is : " + More_Info.sum);
        DatabaseReference myRefforsc = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Score");
        myRefforsc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //More_Info.sum=Integer.valueOf(dataSnapshot.getValue().toString());
                //opkivus.set(0,Integer.valueOf(dataSnapshot.getValue().toString())
                // );
                scor = dataSnapshot.getValue(Integer.class);
                mata.setText("Your score is : " + dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mSecondPhotoPath = mCurrentPhotoPath;

        if (mCurrentPhotoPath!=null)
        {
            File f= new File(mCurrentPhotoPath);
            Bitmap bMap = BitmapFactory.decodeFile(f.getAbsolutePath());
            if (bMap == null)
            {
                mCurrentPhotoPath = mSecondPhotoPath;
                f= new File(mCurrentPhotoPath);
                bMap = BitmapFactory.decodeFile(f.getAbsolutePath());
            }
            img_cam.setImageBitmap(bMap);
            img_cam.setRotation(0);
        }

        DatabaseReference myRefforus = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username");
        myRefforus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usernem.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        prof_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (camera_intent.resolveActivity(getPackageManager()) != null)
                {

                    mSecondPhotoPath = mCurrentPhotoPath;
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                    }
                    if (photoFile!=null)
                    {
                        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                                "cegeka.goalfollower.ro.goalfollower.fileprovider",
                                photoFile);
                        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(camera_intent, REQUEST_IMAGE_CAPTURE);

                    }

                }
            }
        });
        mPendingBtn = findViewById(R.id.pendingBtn);
        mPendingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ConfirmMenuActivity.class));
            }
        });
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, EntranceActivity.class));
            }
        });

        setnot = (Button) findViewById(R.id.notification_set);
        setnot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notificationIntent = new Intent(getApplicationContext() , Notification_1.class);
                startActivity(notificationIntent);
            }
        });

    }
    public void OpenUnlock(View view) {
        Intent intent =
                new Intent(MainActivity.this, UnlockActivity.class);
        startActivity(intent);
    }
    public void OpensetPass(View view) {
        Intent intent =
                new Intent(MainActivity.this, SetpassActivity.class);
        startActivity(intent);
    }

    public void OpenAddActivity(View view) {
        Intent intent =
                new Intent(MainActivity.this, AddActivity.class);
        startActivityForResult(intent,1);
    }
    public void OpenListActivity(View view) {
        Intent intent =
                new Intent(MainActivity.this, ListActivity.class);
        startActivity(intent);
    }
    public void OpenRecActivity(View view){Intent intent =
            new Intent(MainActivity.this, ReceiveMenuActivity.class);
        startActivity(intent);}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            switch (resultCode){
                case Activity.RESULT_OK:{break;}
                default: break;
            }
        }
        if (requestCode == 2)
        {
            File f= new File(mCurrentPhotoPath);
            Bitmap bMap = BitmapFactory.decodeFile(f.getAbsolutePath());
            if (bMap == null)
            {
                mCurrentPhotoPath = mSecondPhotoPath;
                f= new File(mCurrentPhotoPath);
                bMap = BitmapFactory.decodeFile(f.getAbsolutePath());
            }
            Addg();
            img_cam.setImageBitmap(bMap);
            img_cam.setRotation(0);
        }
    }

}