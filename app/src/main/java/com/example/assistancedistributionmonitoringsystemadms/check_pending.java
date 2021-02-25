package com.example.assistancedistributionmonitoringsystemadms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class check_pending extends AppCompatActivity {
    RecyclerView pending_view;
    adapter_class_pending adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_pending);
        setTitle("Pending Requests");
        int color = Color.parseColor("#8BC34A");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }

        pending_view = (RecyclerView) findViewById(R.id.pending_view);
        pending_view.setNestedScrollingEnabled(false);
        pending_view.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<S_and_G> options =
                new FirebaseRecyclerOptions.Builder<S_and_G>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Pending"), S_and_G.class)
                        .build();

        adapter = new adapter_class_pending(options);
        pending_view.setAdapter(adapter);

    }
    @Override
    protected void onStart () {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop () {
        super.onStop();
        adapter.stopListening();
    }
}