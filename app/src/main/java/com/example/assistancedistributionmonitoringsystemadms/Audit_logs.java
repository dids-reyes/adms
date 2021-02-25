package com.example.assistancedistributionmonitoringsystemadms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;

public class Audit_logs extends AppCompatActivity {
    FirebaseRecyclerOptions<logs> options;
    FirebaseRecyclerAdapter<logs,logs_holder> adapter_logs;
    RecyclerView logs_view;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_logs);
        try {
        this.getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        int color = Color.parseColor("#FFFFFF");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
        logs_view = (RecyclerView) findViewById(R.id.logs_view);
        ref = FirebaseDatabase.getInstance().getReference().child("Audit_Logs");

            logs_view.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);
            logs_view.setLayoutManager(layoutManager);

            options = new FirebaseRecyclerOptions.Builder<logs>().setQuery(ref, logs.class).build();
            adapter_logs = new FirebaseRecyclerAdapter<logs, logs_holder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull logs_holder holder, int position, @NonNull logs model) {
                    holder.Attempt.setText("" + model.getAttempt());
                    holder.Email.setText("" + model.getEmail());
                    holder.Time.setText("" + model.getTime());
                    holder.Date.setText("" + model.getDate());
                    if(holder.Attempt.getText().equals("Succeed"))
                    {
                        holder.Attempt.setTextColor(Color.parseColor("#8BC34A"));
                    }
                    else if(holder.Attempt.getText().equals("Failed"))
                    {
                        holder.Attempt.setTextColor(Color.RED);
                    }
                    else if(holder.Attempt.getText().equals("Update"))
                    {
                        holder.txt_c.setText("Event Triggered:");
                        holder.Attempt.setTextColor(Color.parseColor("#0099FF"));
                    }
                    else if(holder.Attempt.getText().equals("Delete"))
                    {
                        holder.txt_c.setText("Event Triggered:");
                        holder.Attempt.setTextColor(Color.RED);
                    }
                    else if(holder.Attempt.getText().equals("Add Data"))
                    {
                        holder.txt_c.setText("Event Triggered:");
                        holder.Attempt.setTextColor(Color.parseColor("#8BC34A"));
                    }
                    else if(holder.Attempt.getText().equals("Announcement"))
                    {
                        holder.txt_c.setText("Event Triggered:");
                        holder.Attempt.setTextColor(Color.parseColor("#8BC34A"));
                    }
                }

                @NonNull
                @Override
                public logs_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_logs,parent,false);
                    return new logs_holder(v);
                }
            };
            adapter_logs.startListening();
            logs_view.setAdapter(adapter_logs);
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}