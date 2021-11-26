package com.example.knu_information_manager;

import android.annotation.SuppressLint;
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
    private Button deleteBtn;

    public interface OnItemClickEventListener{
        public void onItemClick(View view, int position);
    }

    private OnItemClickEventListener onItemClickEventListener;


    public KeyListAdapter(ArrayList<String> list) {
        this.list = list;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView keyword;
        protected Button deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.keyword = (TextView) itemView.findViewById(R.id.keyword_list);
            this.deleteBtn = (Button) itemView.findViewById(R.id.deleteBtn_list);
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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.keyword.setText(list.get(position));

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickEventListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null!=list?list.size():0);
    }

    public void setOnItemClickListener(OnItemClickEventListener a_listener) {
        onItemClickEventListener = a_listener;
    }

}