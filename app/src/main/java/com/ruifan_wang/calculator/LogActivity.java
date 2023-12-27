package com.ruifan_wang.calculator;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LogActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        LogViewModel viewModel = new ViewModelProvider(this).get(LogViewModel.class);
        RecyclerView recyclerView=findViewById(R.id.log_history_list);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation== Configuration.ORIENTATION_PORTRAIT){
            LinearLayoutManager layoutManager=new LinearLayoutManager(LogActivity.this,LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(layoutManager);
        } else {
            GridLayoutManager gridLayoutManager=new GridLayoutManager(LogActivity.this,2,GridLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        recyclerView.setAdapter(new MyLogRecyclerViewAdapter(viewModel.getLogList()));
    }
}