package com.example.knu_information_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class KeyListAdapter extends RecyclerView.Adapter<KeyListAdapter.ViewHolder> {
    private ArrayList<String> list;

    public KeyListAdapter(ArrayList<String> list) {
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView keyword;
        protected Button delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.keyword = (TextView) itemView.findViewById(R.id.keyword_list);
            this.delete = (Button) itemView.findViewById(R.id.deleteBtn_list);
        }

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_drawer, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.keyword.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return (null!=list?list.size():0);
    }


}