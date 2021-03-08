package com.example.chatfirebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder>
{
    private ArrayList<ListItem> content = new ArrayList<ListItem>();
    private Context mContext;

    public RecycleViewAdapter(ArrayList<ListItem> content, Context mContext) {
        this.content = content;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder =null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType)
        {
            case 1 : {


                View view1 = inflater.inflate(R.layout.list1, parent, false);
                holder = new ViewHolder(view1);
                break;
            }
            case 2 :
                {
                 View view2 = inflater.inflate(R.layout.list2, parent, false);
                 holder = new ViewHolder2(view2);
                 break;
             }
        }
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType())
        {
            case 1 :
            {
                ViewHolder holder1  = (ViewHolder) holder;
                holder1.view.setText(content.get(position).getText());
                break;
            }
            case 2 :
            {
                ViewHolder2 holder2  = (ViewHolder2) holder;
                holder2.view.setText(content.get(position).getText());
                break;
            }

        }
    }

    @Override
    public int getItemCount() {
        return content.size();
    }

    @Override
    public int getItemViewType(int position) {
        return content.get(position).getType();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    protected class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           view = itemView.findViewById(R.id.text);
        }
    }
    protected class ViewHolder2 extends RecyclerView.ViewHolder
    {
        private TextView view;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.text2);
        }
    }

}
