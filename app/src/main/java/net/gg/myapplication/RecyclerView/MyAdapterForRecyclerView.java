package net.gg.myapplication.RecyclerView;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.gg.myapplication.MyModule.Task;
import net.gg.myapplication.R;

import java.util.List;


public class MyAdapterForRecyclerView extends RecyclerView.Adapter<MyAdapterForRecyclerView.MyViewHolder> {
    SharedPreferences sharedPreferences;
    List<Task> tasks;
    itemClickL itemClickL;

    public MyAdapterForRecyclerView (List<Task> tasks,itemClickL itemClickL){
        this.tasks=tasks;
        this.itemClickL=itemClickL;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =LayoutInflater.from(parent.getContext());
       View view= layoutInflater.inflate(R.layout.my_row,parent,false);
         sharedPreferences = PreferenceManager.getDefaultSharedPreferences(parent.getContext());
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterForRecyclerView.MyViewHolder holder, int position) {

       holder.TaskBy.setText("task by : "+sharedPreferences.getString("userName","username"));

        holder.title.setText(tasks.get(position).getTitle());
        holder.body.setText(tasks.get(position).getBody());
        holder.sate.setText(tasks.get(position).getState().toString());
        holder.itemView.setOnClickListener(v -> {
            itemClickL.OnItemClick(tasks.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public interface itemClickL{
        void OnItemClick(Task task);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView body;
        TextView sate;
        TextView TaskBy;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title= itemView.findViewById(R.id.text_view_title);
            body=itemView.findViewById(R.id.text_view_body);
            sate=itemView.findViewById(R.id.text_view_state);
            TaskBy = itemView.findViewById(R.id.text_view_taskBy);


        }
    }
}
