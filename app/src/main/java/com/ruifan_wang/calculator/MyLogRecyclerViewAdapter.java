package com.ruifan_wang.calculator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class MyLogRecyclerViewAdapter extends RecyclerView.Adapter<MyLogRecyclerViewAdapter.LogItemViewHolder> {

    private final List<LogHistory> log_history_list;


    public MyLogRecyclerViewAdapter(List<LogHistory> items) {
        log_history_list = items;
    }

    @NonNull
    @Override
    public LogItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item,parent,false);
        return new LogItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LogItemViewHolder holder, int position) {
        holder.bind(log_history_list.get(position));
    }

    @Override
    public int getItemCount() {
        return log_history_list.size();
    }

    public static class LogItemViewHolder extends RecyclerView.ViewHolder{
        private final TextView log;
        private final TextView answer;
        private double chosen_answer;
        private boolean isEmpty=true;

        public LogItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                Context context=view.getContext();
                Intent intent=new Intent(context,MainActivity.class);
                intent.putExtra("isempty",isEmpty);
                intent.putExtra("chosen_answer",chosen_answer);
                context.startActivity(intent);
            });
            log=itemView.findViewById(R.id.log);
            answer=itemView.findViewById(R.id.answer);
        }

        @SuppressLint("DefaultLocale")
        public void bind(LogHistory log_history){
            if(LogRepository.getInstance().isEmpty()){
                log.setText(log_history.getLog_String());
            }else {
                log.setText(log_history.getLog_display());
                isEmpty=false;
                chosen_answer=log_history.getLog_answer();
                if ((int) log_history.getLog_answer() == log_history.getLog_answer()) {
                    answer.setText(String.format("%d" ,(int)log_history.getLog_answer()));
                } else {
                    answer.setText(String.format("%.2f",log_history.getLog_answer()));
                }
            }
        }
    }
}