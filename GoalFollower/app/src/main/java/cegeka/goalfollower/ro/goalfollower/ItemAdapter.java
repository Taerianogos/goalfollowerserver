package cegeka.goalfollower.ro.goalfollower;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cegeka.goalfollower.ro.goalfollower.AddActivity;
import cegeka.goalfollower.ro.goalfollower.R;

public class ItemAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    List<String> names;

  //  List<Boolean>checked;

    public ItemAdapter(Context c , List<String> n ,List<Date> d /*, List<Boolean> ce*/)
    {
        names = n;
       // checked = ce;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() { return names.size(); }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = mInflater.inflate(R.layout.custom_list,null);

        TextView name_taskTextView = v.findViewById(R.id.name_task);
        String name = names.get(position);
        name_taskTextView.setText(name);

        return v;
    }
}
