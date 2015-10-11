package fr.free.riquet.jeancharles.easyreminder;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class TaskAdapter extends BaseAdapter {
    List<Task> _taskList;
    LayoutInflater inflater;

    public TaskAdapter(Context context, List<Task> taskList) {
        inflater = LayoutInflater.from(context);
        this._taskList = taskList;
    }

    @Override
    public int getCount() {
        if (_taskList == null)
            return 0;
        else
            return _taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return _taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_task, null);

            holder._taskName = (TextView) convertView.findViewById(R.id.taskName);
            holder._taskDesc = (TextView) convertView.findViewById(R.id.taskDesc);
            holder._taskDate = (TextView) convertView.findViewById(R.id.taskLimiteDate);
            holder._taskPicture = (ImageView) convertView.findViewById(R.id.taskPicture);
            holder._taskCheckBox = (CheckBox) convertView.findViewById(R.id.tackCheckBox);
            //CheckBox cBox = (CheckBox) convertView.findViewById(R.id.tackCheckBox);
            //cBox.setTag(Integer.valueOf(position)); // set the tag so we can identify the correct row in the listener
            //cBox.setChecked(mChecked[position]); // set the status as we stored it
            //cBox.setOnCheckedChangeListener(mListener); // set the listener

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder._taskName.setText(_taskList.get(position).get_name());
        holder._taskDesc.setText(_taskList.get(position).get_desc());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh:mm");
        Date theDate = new Date();
        try {
            theDate = format.parse(_taskList.get(position).get_limiteDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar myCal = new GregorianCalendar();
        myCal.setTime(theDate);
        int Year = myCal.get(Calendar.YEAR);
        int Month = myCal.get(Calendar.MONTH);
        int Day = myCal.get(Calendar.DAY_OF_MONTH);
        String curTime = String.format("%02d:%02d", myCal.get(Calendar.HOUR_OF_DAY), myCal.get(Calendar.MINUTE));
        holder._taskDate.setText(Day + "/" + Month + "/" + Year + " " + curTime);
        holder._taskPicture.setImageDrawable(new ColorDrawable(_taskList.get(position).get_color()));
        holder._taskCheckBox.setChecked(_taskList.get(position).get_checked());
        holder._taskid = _taskList.get(position).get_id();

        holder._taskCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DBHandlerSingleton.getInstance(inflater.getContext()).setTaskDone(holder._taskid, isChecked);
            }
        });

        return convertView;
    }

    private class ViewHolder {
        long _taskid;
        TextView _taskName;
        TextView _taskDesc;
        TextView _taskDate;
        ImageView _taskPicture;
        CheckBox _taskCheckBox;
    }

    CompoundButton.OnCheckedChangeListener mListener = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = (Integer) buttonView.getTag();
            long id = _taskList.get(position).get_id();
            _taskList.get(position).set_checked(isChecked);
            DBHandlerSingleton.getInstance(inflater.getContext()).setTaskDone(id, isChecked);
        }
    };
}
