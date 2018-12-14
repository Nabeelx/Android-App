package com.society.blooddonation.lifesavers.utils;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.society.blooddonation.lifesavers.R;

import java.util.ArrayList;
import java.util.List;


public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.eventViewHolder>{

    private List<EventsList> data;
    public EventsAdapter(List<EventsList> data) {
        Log.d("EventsAdapter: ", "The Adapter has been called");
        this.data=data;
        Log.d("THe List: ", data.toString());

    }

    @NonNull
    @Override
    public eventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.eventitems,parent,false);
        Log.d("EventsAdapter: ", "The Adapter;s create view");

        return new eventViewHolder(v);
    }




    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



    @Override
    public void onBindViewHolder(@NonNull eventViewHolder holder, int position) {

        Log.d("EventsAdapter: ", "The Adapter;s bind view");

        String desc=data.get(position).desc;
        String address=data.get(position).place;
        String name=data.get(position).name ;
        String start=data.get(position).start_time;
        String end=data.get(position).end_time ;
        holder.desc.setText(desc);
        holder.name.setText(name);
        holder.address.setText(address);
        holder.start.setText(start);
        holder.end.setText(end);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class eventViewHolder extends RecyclerView.ViewHolder{

        TextView end,start,address,name,desc;

        public eventViewHolder(View itemView) {

            super(itemView);
            Log.d("EventsAdapter: ", "The Adapter;s viewhplder view");
            desc=itemView.findViewById(R.id.desc);
            name=itemView.findViewById(R.id.name);
            address=itemView.findViewById(R.id.address);
            start=itemView.findViewById(R.id.start);
            end=itemView.findViewById(R.id.end);

        }
    }


}
