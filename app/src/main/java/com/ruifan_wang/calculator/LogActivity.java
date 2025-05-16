package com.ruifan_wang.calculator;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
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
        recyclerView.setAdapter(new MyLogRecyclerViewAdapter(
                viewModel.getLogList()));

        addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.activity_log, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.btn_clear_log) {
                    viewModel.clearLog_history();
                    Toast.makeText(LogActivity.this, "历史记录已清除", Toast.LENGTH_LONG).show();
                    recyclerView.setAdapter(new MyLogRecyclerViewAdapter(viewModel.getLogList())); // 刷新列表
                    return true;
                }
                return false;
            }
        });
    }
}