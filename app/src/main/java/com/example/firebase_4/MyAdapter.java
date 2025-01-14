package com.example.firebase_4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> list;
    private myAdapterEvents myAdapterEvents;
    public int i=1;

    public MyAdapter(Context context,myAdapterEvents myAdapterEvents,ArrayList<User> list ) {
        this.context = context;
        this.myAdapterEvents=myAdapterEvents;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_1,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = list.get(position);
        holder.firstName.setText((position+1)+". "+user.getName());
        holder.bind(user, myAdapterEvents);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView firstName;
        View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.tvfirstName);
            view = itemView.findViewById(R.id.relLayout);
        }

       public void bind(User user, myAdapterEvents listener)
       {
           view.setOnClickListener(v -> {
               if(listener != null) {
                   listener.onNameClick(user);
               }
           });
       }
    }

    public interface myAdapterEvents
    {
        void onNameClick(User user);
    }

    
}
