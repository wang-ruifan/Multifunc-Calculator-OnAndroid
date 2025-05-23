package com.ruifan_wang.calculator;

import android.annotation.SuppressLint;
import android.app.Activity;
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
        private String chosen_answer;
        private boolean isEmpty=true;

        public LogItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("isempty", isEmpty);
                resultIntent.putExtra("chosen_answer", chosen_answer);
                Activity activity = (Activity) view.getContext();
                activity.setResult(Activity.RESULT_OK, resultIntent);
                activity.finish();
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
                double answer_value=log_history.getLog_answer();
                if ((int) answer_value == answer_value) {
                    answer.setText(String.format("%d" ,(int)answer_value));
                    chosen_answer= String.format("%d",(int)answer_value);
                } else {
                    // 小数情况 - 使用智能格式化处理
                    String formattedValue = formatDecimal(answer_value);
                    answer.setText(formattedValue);
                    chosen_answer= String.valueOf(answer_value);
                }
            }
        }

        @SuppressLint("DefaultLocale")
        private String formatDecimal(double value) {
            // 先转成字符串看有多少小数位
            String plainStr = String.valueOf(value);

            // 如果小数点后超过4位，格式化为4位
            if (plainStr.contains(".") &&
                    plainStr.substring(plainStr.indexOf(".") + 1).length() > 4) {
                return String.format("%.4f", value);
            }

            // 否则保持原样，避免添加不必要的0
            return plainStr;
        }
    }
}


