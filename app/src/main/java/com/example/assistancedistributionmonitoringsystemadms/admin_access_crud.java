package com.example.assistancedistributionmonitoringsystemadms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class admin_access_crud extends AppCompatActivity {
    FloatingActionButton floating_button,pa,fab,message_btn,db_fab,db_logs,check_pending_btn,set_up;
    Boolean isFABOpen = false;
    RecyclerView view;
    adapter_class adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_access_crud);
        setTitle("Search Residents Here");

        int color = Color.parseColor("#8BC34A");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }

            view = (RecyclerView) findViewById(R.id.recyclerView);
            view.setNestedScrollingEnabled(false);
            view.setLayoutManager(new LinearLayoutManager(this));

        view.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && floating_button.isShown())
                {
                    floating_button.hide();
                }
                if (dy > 0 || dy < 0 && pa.isShown())
                {
                    pa.hide();
                }
                if (dy > 0 || dy < 0 && pa.isShown())
                {
                    fab.hide();
                }
                if (dy > 0 || dy < 0 && pa.isShown())
                {
                    message_btn.hide();
                }
                if (dy > 0 || dy < 0 && pa.isShown())
                {
                    db_fab.hide();
                }
                if (dy > 0 || dy < 0 && pa.isShown())
                {
                    db_logs.hide();
                }
                if (dy > 0 || dy < 0 && pa.isShown())
                {
                    check_pending_btn.hide();
                }
                if (dy > 0 || dy < 0 && pa.isShown())
                {
                    set_up.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    floating_button.show();
                    pa.show();
                    fab.show();
                    message_btn.show();
                    db_fab.show();
                    db_logs.show();
                    check_pending_btn.show();
                    set_up.show();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

            FirebaseRecyclerOptions<S_and_G> options =
                    new FirebaseRecyclerOptions.Builder<S_and_G>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("User"), S_and_G.class)
                            .build();

            adapter = new adapter_class(options);
            view.setAdapter(adapter);

            floating_button = (FloatingActionButton) findViewById(R.id.floatingActionButton);
            pa = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
            fab = (FloatingActionButton) findViewById(R.id.fab);
            message_btn = (FloatingActionButton) findViewById(R.id.message_btn);
            db_fab = (FloatingActionButton) findViewById(R.id.db_fab);
            db_logs = (FloatingActionButton) findViewById(R.id.db_logs);
            check_pending_btn = (FloatingActionButton) findViewById(R.id.check_pending_users);
            set_up = (FloatingActionButton) findViewById(R.id.setting_up_e);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isFABOpen)
                    {
                        showFABMenu();
                    }
                    else {
                        closeFABMenu();
                    }
                }
            });

            pa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),public_a.class));
                }
            });

            floating_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(admin_access_crud.this,data_append.class);
                    intent.putExtra("req", "none");
                    startActivity(intent);
                }
            });
            message_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), message.class));
                }
            });

            db_logs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), Audit_logs.class));
                }
            });

            check_pending_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), check_pending.class));
                }
            });

            set_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), setup_admin_access.class));
                }
            });

            db_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CFAlertDialog.Builder rem = new CFAlertDialog.Builder(admin_access_crud.this);
                    rem.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
                    rem.setTitle("Database Recovery");
                    rem.setMessage("You're redirecting to a website containing a JSON file (i.e. use for importing data) of the database, you can download the file using your browser.");
                    rem.addButton("Continue", -1, Color.parseColor("#8BC34A"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                        dialog.dismiss();
                        Uri uri = Uri.parse("https://adms---db-default-rtdb.firebaseio.com/.json?print=pretty");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    });
                    rem.addButton("Cancel", -1, Color.parseColor("#FF0000"), CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                        dialog.dismiss();
                    });
                    rem.show();
                }
            });
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

        @Override
        public boolean onCreateOptionsMenu (Menu menu)
        {
            getMenuInflater().inflate(R.menu.searchmenu, menu);

            MenuItem item = menu.findItem(R.id.search);

            SearchView searchView = (SearchView) item.getActionView();

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    process_search(s);
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String s) {
                    process_search(s);
                    return false;
                }
            });
            return super.onCreateOptionsMenu(menu);
        }

        private void process_search (String s)
        {
            FirebaseRecyclerOptions<S_and_G> options =
                    new FirebaseRecyclerOptions.Builder<S_and_G>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("User").orderByChild("Name").startAt(s).endAt(s + "\uf8ff"), S_and_G.class)
                            .build();
            adapter = new adapter_class(options);
            adapter.startListening();
            view.setAdapter(adapter);
        }
    public void showFABMenu(){
        fab.setImageResource(R.drawable.ic_baseline_close_24);
        isFABOpen=true;
        floating_button.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        pa.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        message_btn.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
        db_fab.animate().translationY(-getResources().getDimension(R.dimen.standard_205));
        db_logs.animate().translationY(-getResources().getDimension(R.dimen.standard_255));
        check_pending_btn.animate().translationY(-getResources().getDimension(R.dimen.standard_305));
        set_up.animate().translationY(-getResources().getDimension(R.dimen.standard_355));
    }
    public void closeFABMenu(){
        fab.setImageResource(R.drawable.ic_baseline_add_24);
        isFABOpen=false;
        floating_button.animate().translationY(0);
        pa.animate().translationY(0);
        message_btn.animate().translationY(0);
        db_fab.animate().translationY(0);
        db_logs.animate().translationY(0);
        check_pending_btn.animate().translationY(0);
        set_up.animate().translationY(0);
    }
    public void onBackPressed(){
        CFAlertDialog.Builder rem = new CFAlertDialog.Builder(admin_access_crud.this);
        rem.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
        rem.setTitle("Exit Data Administration?");
        rem.addButton("Yes", -1, Color.parseColor("#8BC34A"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
            dialog.dismiss();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        });
        rem.addButton("No", -1, Color.parseColor("#FF0000"), CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
            dialog.dismiss();
        });
        rem.show();
    }
}