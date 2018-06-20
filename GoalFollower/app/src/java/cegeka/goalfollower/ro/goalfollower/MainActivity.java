package cegeka.goalfollower.ro.goalfollower;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button prof_pic;
    String mCurrentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    String filename = "image";
    ImageView img_cam;
    TextView score ;
    Button refresh ;
    Button setnot;
    String mSecondPhotoPath;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Menu");
        img_cam = (ImageView) findViewById(R.id.profile_img);
        prof_pic = (Button) findViewById(R.id.prof_pic);
        score = (TextView) findViewById(R.id.score_text_view);
        Readf();

        score.setText("Your Score is : " + More_Info.sum);

        refresh = (Button) findViewById(R.id.refresh_btn);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score.setText("Your Score is : " + More_Info.sum );
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
            img_cam.setRotation(90);
        }


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
                        Toast.makeText(getApplicationContext() , "fiser facut " + mCurrentPhotoPath , Toast.LENGTH_LONG).show();
                    } catch (IOException ex) {
                        Toast.makeText(getApplicationContext() , "nu s-a putut crea fisierul" , Toast.LENGTH_LONG).show();
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

        setnot = (Button) findViewById(R.id.notification_set);
        setnot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notificationIntent = new Intent(getApplicationContext() , Notification_1.class);
                startActivity(notificationIntent);
            }
        });

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
            new Intent(MainActivity.this, RecActivity.class);
        startActivity(intent);}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            switch (resultCode){
                case Activity.RESULT_OK:{
                    Toast.makeText(MainActivity.this, "Merge aparent ", Toast.LENGTH_SHORT).show();
                    break;}
                default:
                    Toast.makeText(MainActivity.this, "nu mrg", Toast.LENGTH_LONG).show();
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
            img_cam.setRotation(90);
        }
    }

}

