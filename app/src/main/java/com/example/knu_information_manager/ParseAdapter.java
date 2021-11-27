package com.example.knu_information_manager;

import android.annotation.SuppressLint;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ParseAdapter extends RecyclerView.Adapter<ParseAdapter.ViewHolder> {
    private ArrayList<ViewData> list;

    public ParseAdapter(ArrayList<ViewData> list) {
        this.list = list;
    }

    public void setOnItemClickListener(ParseAdapter.OnItemClickEventListener a_listener) {
        onItemClickEventListener = a_listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView id;
        protected TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.id = (TextView) itemView.findViewById(R.id.id_listItem);
            this.title = (TextView) itemView.findViewById(R.id.title_listItem);
        }

    }

    public interface OnItemClickEventListener{
        public void onItemClick(View view, int position);
    }

    private ParseAdapter.OnItemClickEventListener onItemClickEventListener;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.id.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        holder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);


        holder.id.setText(list.get(position).getId());
        holder.title.setText(list.get(position).getTitle());

        holder.title.setOnClickListener(new View.OnClickListener() {
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

}
