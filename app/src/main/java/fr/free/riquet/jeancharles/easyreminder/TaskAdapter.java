package fr.free.riquet.jeancharles.easyreminder;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_task, null);

            holder._taskName = (TextView) convertView.findViewById(R.id.taskName);
            holder._taskDesc = (TextView) convertView.findViewById(R.id.taskDesc);
            holder._taskDate = (TextView) convertView.findViewById(R.id.taskLimiteDate);
            holder._taskPicture = (ImageView) convertView.findViewById(R.id.taskPicture);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder._taskName.setText(_taskList.get(position).get_name());
        holder._taskDesc.setText(_taskList.get(position).get_desc());
        holder._taskDate.setText(_taskList.get(position).get_limiteDate());
        holder._taskPicture.setImageDrawable(new ColorDrawable(_taskList.get(position).get_color()));

        return convertView;
    }

    private class ViewHolder {
        TextView _taskName;
        TextView _taskDesc;
        TextView _taskDate;
        ImageView _taskPicture;
    }
}
