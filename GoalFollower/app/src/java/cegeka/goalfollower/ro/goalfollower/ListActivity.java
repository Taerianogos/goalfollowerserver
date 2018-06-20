package cegeka.goalfollower.ro.goalfollower;

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

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListActivity extends AppCompatActivity {
    ArrayList<Goal> returnlist=new ArrayList<Goal>();
    List<String> names=new ArrayList<>();
     static List<String> descrips=new ArrayList<>();
    List<Date> duedate=new ArrayList<>();
    Button save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Readf();
        SimpleDateFormat format =
                new SimpleDateFormat("yyyy-MM-dd");
        ListView GoalListView = (ListView) findViewById(R.id.GoalListView);
        for (Goal item : returnlist) {
            names.add(item.desc);
            descrips.add(item.descrip);
            duedate.add(item.dueDate);
        }
        ItemAdapter itemAdapt =  new ItemAdapter(this , names , duedate  );
        GoalListView.setAdapter(itemAdapt);

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
        }}
}
