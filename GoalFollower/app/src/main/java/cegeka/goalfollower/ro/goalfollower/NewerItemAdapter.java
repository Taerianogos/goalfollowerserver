package cegeka.goalfollower.ro.goalfollower;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

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

import static cegeka.goalfollower.ro.goalfollower.FinishInternetActivity.fDates;
import static cegeka.goalfollower.ro.goalfollower.FinishInternetActivity.fDescrips;
import static cegeka.goalfollower.ro.goalfollower.FinishInternetActivity.fGoals;
import static cegeka.goalfollower.ro.goalfollower.FinishInternetActivity.fListView;
import static cegeka.goalfollower.ro.goalfollower.FinishInternetActivity.fNames;

public class NewerItemAdapter extends BaseAdapter{
    LayoutInflater mInflater;
    List<String> mnames;
    List<Goal> mGoal;
    Goal item = new Goal();
    Context cont;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    //  List<Boolean>checked;

    public NewerItemAdapter(Context c , List<String> n , List<Goal> g/*, List<Boolean> ce*/)
    {
        cont = c;
        mnames = n;
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

        View v = mInflater.inflate(R.layout.custom_list_3,null);

        TextView name_taskTextView = v.findViewById(R.id.name_task2);
        Button buttonYes = v.findViewById(R.id.AgreeBtn);
        final Button buttonNo = v.findViewById(R.id.RejectBtn);

        String name = mnames.get(position);

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item = mGoal.get(position);
                //foloseste child(goalname + "" + username)
                myRef.child("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("For Confirmation Goals UID")
                        .child(item.desc)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String UID = dataSnapshot.getValue(String.class);
                                myRef.child("users").child(UID).child("Goals").child(item.desc).child("pass").setValue(null);
                                myRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("For Confirmation Goals UID").child(item.desc).setValue(null);
                                myRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("For Confirmation Goals").child(item.desc).setValue(null);
                                myRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("For Confirmation Goals")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                fGoals.clear();
                                                for (DataSnapshot child : dataSnapshot.getChildren()) {
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
                                                fListView.setAdapter(new NewerItemAdapter(cont, fNames, fGoals));
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
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
                myRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("For Confirmation Goals").child(item.desc).setValue(null);
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
                                fListView.setAdapter(new NewerItemAdapter(cont, fNames, fGoals));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });
        name_taskTextView.setText(name);


        return v;
    }
}
