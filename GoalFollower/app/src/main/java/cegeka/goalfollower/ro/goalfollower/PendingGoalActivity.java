package cegeka.goalfollower.ro.goalfollower;

import android.app.Activity;
import android.content.Intent;
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

public class PendingGoalActivity extends AppCompatActivity {

    public static ArrayList<Goal> pendingGoals=new ArrayList<Goal>();
    public static ArrayList<String> pendingNames=new ArrayList<String>();
    public static ArrayList<String> pendingDescrips=new ArrayList<String>();
    public static ArrayList<Date> pendingDates=new ArrayList<Date>();
    static Activity acti;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    public static ListView PendingListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_goal);
        PendingListView = (ListView) findViewById(R.id.PendingListView);
        acti = this;
        setTitle("Receive goal through internet");
        myRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Pending Goals")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        pendingGoals.clear();
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            pendingGoals.add(child.getValue(Goal.class));
                        }
                        pendingNames.clear();
                        pendingDates.clear();
                        pendingDescrips.clear();
                        for (Goal item : pendingGoals) {
                            pendingNames.add(item.desc);
                            pendingDescrips.add(item.descrip);
                            pendingDates.add(item.dueDate);
                        }
                        NewItemAdapter itemAdapt = new NewItemAdapter(PendingGoalActivity.this, pendingNames, pendingGoals);
                        PendingListView.setAdapter(itemAdapt);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
