package com.example.knu_information_manager;

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
    public class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView id;
        protected TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.id = (TextView) itemView.findViewById(R.id.id_listItem);
            this.title = (TextView) itemView.findViewById(R.id.title_listItem);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.id.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        holder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);


        holder.id.setText(list.get(position).getId());
        holder.title.setText(list.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return (null!=list?list.size():0);
    }
}
