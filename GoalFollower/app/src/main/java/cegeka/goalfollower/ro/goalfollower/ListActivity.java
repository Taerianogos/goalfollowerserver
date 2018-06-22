package cegeka.goalfollower.ro.goalfollower;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static cegeka.goalfollower.ro.goalfollower.AddActivity.filename;
import static cegeka.goalfollower.ro.goalfollower.More_Info.S_description_not;
import static cegeka.goalfollower.ro.goalfollower.More_Info.S_name_not;
import static cegeka.goalfollower.ro.goalfollower.More_Info.index;

public class ListActivity extends AppCompatActivity {

  public static ArrayList<Goal> returnlist=new ArrayList<Goal>();
    List<String> names=new ArrayList<>();
    static List<String> descrips=new ArrayList<>();
    List<Date> duedate=new ArrayList<>();
    Button save;
    static Activity act;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
static int sizelist=0;
boolean delay=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        act = this;
        setTitle("My goals");

        //Readf();
        final ListView GoalListView = (ListView) findViewById(R.id.GoalListView);

           DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Goals");
           myRef.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   returnlist.clear();
                   for (DataSnapshot child : dataSnapshot.getChildren()) {
                       returnlist.add(child.getValue(Goal.class));
                   }
                   Addg();
                   names.clear();
                   descrips.clear();
                   duedate.clear();
                   for (Goal item : returnlist) {
                       names.add(item.desc);
                       descrips.add(item.descrip);
                       duedate.add(item.dueDate);
                   }
                   sizelist = names.size();
                   ItemAdapter itemAdapt = new ItemAdapter(ListActivity.this, names, duedate);

                   GoalListView.setAdapter(itemAdapt);

               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });

        SimpleDateFormat format =
                new SimpleDateFormat("yyyy-MM-dd");
        /*names.clear();
        descrips.clear();
        duedate.clear();
        for (Goal item : returnlist) {
            names.add(item.desc);
            descrips.add(item.descrip);
            duedate.add(item.dueDate);
        }
        sizelist=names.size();
        ItemAdapter itemAdapt =  new ItemAdapter(this , names , duedate  );

        GoalListView.setAdapter(itemAdapt);*/

        GoalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent detailIntent = new Intent (getApplicationContext() , More_Info.class);
                detailIntent.putExtra("com.example.cristi.firstcegeka.Item" , position);
                startActivity(detailIntent);
            }
        });
    }
    public void Readf(){FileInputStream fis;
        try {
            fis = openFileInput("goals");
            ObjectInputStream ois = new ObjectInputStream(fis);
                returnlist = (ArrayList<Goal>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            Toast.makeText(ListActivity.this,"Err read",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public void Addg() {
        File myfile = new File(this.getFilesDir(), filename);
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, MODE_PRIVATE);
            ObjectOutputStream o = new ObjectOutputStream(outputStream);
            o.reset();
            o.writeObject(returnlist);
            o.flush();
            o.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


}
