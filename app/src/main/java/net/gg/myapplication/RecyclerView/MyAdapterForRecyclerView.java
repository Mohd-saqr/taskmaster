package net.gg.myapplication.RecyclerView;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Task;

import net.gg.myapplication.R;

import java.util.List;


public class MyAdapterForRecyclerView extends RecyclerView.Adapter<MyAdapterForRecyclerView.MyViewHolder> {

//    List<Task> tasks;
    List<com.amplifyframework.datastore.generated.model.Task> tasks;
    itemClickL itemClickL;
    deleteIconClickLester deleteIconClickLester;
    EditIconClickLester editIconClickLester;


    public MyAdapterForRecyclerView (List<com.amplifyframework.datastore.generated.model.Task> tasks){
        this.tasks=tasks;
        this.itemClickL=itemClickL;
    }
    public MyAdapterForRecyclerView (List<com.amplifyframework.datastore.generated.model.Task> tasks,itemClickL itemClickL,deleteIconClickLester deleteIconClickLester){
        this.tasks=tasks;
        this.itemClickL=itemClickL;
        this.deleteIconClickLester=deleteIconClickLester;
    }
    public MyAdapterForRecyclerView (List<com.amplifyframework.datastore.generated.model.Task> tasks,itemClickL itemClickL,deleteIconClickLester deleteIconClickLester,EditIconClickLester editIconClickLester){
        this.tasks=tasks;
        this.itemClickL=itemClickL;
        this.deleteIconClickLester=deleteIconClickLester;
        this.editIconClickLester=editIconClickLester;
    }
    public MyAdapterForRecyclerView (itemClickL itemClickL,deleteIconClickLester deleteIconClickLester,EditIconClickLester editIconClickLester){

        this.itemClickL=itemClickL;
        this.deleteIconClickLester=deleteIconClickLester;
        this.editIconClickLester=editIconClickLester;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =LayoutInflater.from(parent.getContext());
       View view= layoutInflater.inflate(R.layout.my_row,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterForRecyclerView.MyViewHolder holder, int position) {

        holder.title.setText(tasks.get(position).getTitle());
        holder.body.setText(tasks.get(position).getBody());
        holder.sate.setText(tasks.get(position).getState().toString());
        holder.itemView.setOnClickListener(v -> {
            itemClickL.OnItemClick(tasks.get(position));
        });
        holder.deleteIcon.setOnClickListener(v -> {
            deleteIconClickLester.onDeleteClick(tasks.get(position));
        });
        holder.editIcon.setOnClickListener(v -> {
            editIconClickLester.onEditClick(tasks.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public interface itemClickL{
        void OnItemClick(com.amplifyframework.datastore.generated.model.Task task);
    }

    public interface deleteIconClickLester{
        void onDeleteClick(com.amplifyframework.datastore.generated.model.Task task);
    }
    public interface EditIconClickLester{
        void onEditClick(com.amplifyframework.datastore.generated.model.Task task);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView body;
        TextView sate;

        ImageView deleteIcon;
        ImageView editIcon;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            editIcon=itemView.findViewById(R.id.icon_edit_task);
             deleteIcon = itemView.findViewById(R.id.icon_delete_task);
            title= itemView.findViewById(R.id.text_view_title);
            body=itemView.findViewById(R.id.text_view_body);
            sate=itemView.findViewById(R.id.text_view_state);



        }
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }


    public void updateData(List<Task> list){
        tasks.clear();
        tasks.addAll(list);
        notifyDataSetChanged();
    }
}
