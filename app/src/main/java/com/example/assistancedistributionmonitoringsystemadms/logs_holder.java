package com.example.assistancedistributionmonitoringsystemadms;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class logs_holder extends RecyclerView.ViewHolder{
    TextView Attempt, Email, Date, Time, txt_c;
    public logs_holder(@NonNull View itemView) {
        super(itemView);

        Attempt = itemView.findViewById(R.id.Attempt_text);
        Email = itemView.findViewById(R.id.Email_text);
        Date = itemView.findViewById(R.id.Date_text);
        Time = itemView.findViewById(R.id.Time_text);
        txt_c = itemView.findViewById(R.id.textView29);
    }
}
