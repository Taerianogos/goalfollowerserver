package cegeka.goalfollower.ro.goalfollower;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static cegeka.goalfollower.ro.goalfollower.PendingGoalActivity.PendingListView;
import static cegeka.goalfollower.ro.goalfollower.PendingGoalActivity.pendingDates;
import static cegeka.goalfollower.ro.goalfollower.PendingGoalActivity.pendingDescrips;
import static cegeka.goalfollower.ro.goalfollower.PendingGoalActivity.pendingGoals;
import static cegeka.goalfollower.ro.goalfollower.PendingGoalActivity.pendingNames;

public class NewItemAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    List<String> mnames;
    List<Date> mduedate;
    List<Goal> mGoal;
    Goal item = new Goal();
    Context cont;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    //  List<Boolean>checked;

    public NewItemAdapter(Context c , List<String> n ,List<Date> d, List<Goal> g/*, List<Boolean> ce*/)
    {
        cont = c;
        mnames = n;
        mduedate = d;
        mGoal = g;
        // checked = ce;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() { return mnames.size(); }

    @Override
    public Object getItem(int position) {
        return mnames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = mInflater.inflate(R.layout.custom_list_2,null);

        TextView name_taskTextView = v.findViewById(R.id.name_task2);
        TextView due_dateTextView = v.findViewById(R.id.due_date2);
        Button buttonYes = v.findViewById(R.id.YesBtn);
        Button buttonNo = v.findViewById(R.id.NoBtn);

        String name = mnames.get(position);
        Date date = mduedate.get(position);

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item = mGoal.get(position);
                myRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Goals").child(item.desc).setValue(item);
                myRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Pending Goals").child(item.desc).setValue(null);
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
                                PendingListView.setAdapter(new NewItemAdapter(cont, pendingNames, pendingDates, pendingGoals));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item = mGoal.get(position);
                myRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Pending Goals").child(item.desc).setValue(null);
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
                                PendingListView.setAdapter(new NewItemAdapter(cont, pendingNames, pendingDates, pendingGoals));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });
        SimpleDateFormat format =
                new SimpleDateFormat("yyyy-MM-dd");
        try {
            date =
                    format.parse(date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        name_taskTextView.setText(name);
        due_dateTextView.setText(date.toString());


        return v;
    }
}