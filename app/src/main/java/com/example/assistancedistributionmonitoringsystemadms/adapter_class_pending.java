package com.example.assistancedistributionmonitoringsystemadms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class adapter_class_pending extends FirebaseRecyclerAdapter<S_and_G, adapter_class_pending.holder_view> {
    public adapter_class_pending(@NonNull FirebaseRecyclerOptions<S_and_G> options)
    {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final holder_view holder, final int position, @NonNull final S_and_G S_and_G)
    {
        holder.name.setText(S_and_G.getName());
        holder.section.setText(S_and_G.getNumber());
        holder.email.setText(S_and_G.getEmail());
        holder.address.setText(S_and_G.getSubsidy());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CFAlertDialog.Builder rem = new CFAlertDialog.Builder(view.getContext());
                rem.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
                rem.setTitle("Confirmation Panel");
                rem.setMessage("Are you sure you want to Add this Data?");
                rem.addButton("Yes", -1, Color.parseColor("#8BC34A"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                    dialog.dismiss();
                    Map<String, Object> map = new HashMap<>();
                    map.put("Name", holder.name.getText().toString());
                    map.put("Number", holder.section.getText().toString());
                    map.put("Email", holder.email.getText().toString());
                    map.put("Subsidy", holder.address.getText().toString());
                    FirebaseDatabase.getInstance().getReference().child("User").child(holder.section.getText().toString())
                            .setValue(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(view.getContext(), "Added Successfully", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(view.getContext(), "Could not insert", Toast.LENGTH_LONG).show();
                                }
                            });
                });
                rem.addButton("No", -1, Color.parseColor("#FF0000"), CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                    dialog.dismiss();
                });
                rem.show();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CFAlertDialog.Builder rem = new CFAlertDialog.Builder(view.getContext());
                rem.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
                rem.setTitle("Delete Panel");
                rem.setMessage("Are you sure you want to Delete this Data?");
                rem.addButton("Yes", -1, Color.parseColor("#8BC34A"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                    dialog.dismiss();
                    FirebaseDatabase.getInstance().getReference().child("Pending")
                            .child(getRef(position).getKey()).removeValue();
                    Toast.makeText(view.getContext(), "Deleted ", Toast.LENGTH_SHORT).show();
                });
                rem.addButton("No", -1, Color.parseColor("#FF0000"), CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                    dialog.dismiss();
                });
                rem.show();
            }
        });

    }

    @NonNull
    @Override
    public holder_view onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow1,parent,false);
        return new holder_view(view);
    }


    class holder_view extends RecyclerView.ViewHolder
    {
        ImageView edit,delete;
        TextView name,section,email,address;
        public holder_view(@NonNull View itemView)
        {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.nametext);
            section=(TextView)itemView.findViewById(R.id.stud_sec);
            email=(TextView)itemView.findViewById(R.id.emailtext);
            address=(TextView)itemView.findViewById(R.id.stud_address);

            edit=(ImageView)itemView.findViewById(R.id.editicon);
            delete=(ImageView)itemView.findViewById(R.id.deleteicon);
        }
    }
}
