package cegeka.goalfollower.ro.goalfollower;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class FinishInternetActivity extends AppCompatActivity {

    public static ArrayList<Goal> fGoals=new ArrayList<Goal>();
    public static ArrayList<String> fNames=new ArrayList<String>();
    public static ArrayList<String> fDescrips=new ArrayList<String>();
    public static ArrayList<Date> fDates=new ArrayList<Date>();
    static Activity activ;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    public static ListView fListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_internet);
        fListView = (ListView) findViewById(R.id.finishListView);
        setTitle("Confirm through Internet");
        activ = this;
        myRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("For Confirmation Goals")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        fGoals.clear();
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            fGoals.add(child.getValue(Goal.class));
                        }
                        fNames.clear();
                        fDates.clear();
                        fDescrips.clear();
                        for (Goal item : fGoals) {
                            fNames.add(item.desc);
                            fDescrips.add(item.descrip);
                            fDates.add(item.dueDate);
                        }
                        NewerItemAdapter itemAdapt = new NewerItemAdapter(FinishInternetActivity.this, fNames, fGoals);
                        fListView.setAdapter(itemAdapt);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
