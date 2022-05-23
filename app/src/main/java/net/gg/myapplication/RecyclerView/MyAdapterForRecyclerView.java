package net.gg.myapplication.RecyclerView;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Task;

import net.gg.myapplication.R;

import java.util.List;


public  class MyAdapterForRecyclerView extends RecyclerView.Adapter<MyAdapterForRecyclerView.MyViewHolder> implements PopupMenu.OnMenuItemClickListener {

//    List<Task> tasks;
    List<com.amplifyframework.datastore.generated.model.Task> tasks;
    itemClickL itemClickL;
    deleteIconClickLester deleteIconClickLester;
    EditIconClickLester editIconClickLester;
    PopupMenu.OnMenuItemClickListener onMenuItemClickListener;



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
        holder.menu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
            popupMenu.inflate(R.menu.recycler_view_menu);
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(this);

        });

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
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

    public static class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView title;
        TextView body;
        TextView sate;
        ImageView menu;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            menu=itemView.findViewById(R.id.recycler_menu_icon);
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




    public interface test{
        void on(com.amplifyframework.datastore.generated.model.Task task);
    }





}
