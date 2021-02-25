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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class adapter_class extends FirebaseRecyclerAdapter<S_and_G, adapter_class.holder_view> {
    public adapter_class(@NonNull FirebaseRecyclerOptions<S_and_G> options)
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


        /*gradle*/
        //Glide.with(holder.img.getContext()).load(S_and_G.getSubsidy()).into(holder.img);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus= DialogPlus.newDialog(holder.name.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialogcontent))
                        .setExpanded(true,1100)
                        .create();

                View view_dialogcontent = dialogPlus.getHolderView();

                final EditText name = view_dialogcontent.findViewById(R.id.uname);
                final EditText section = view_dialogcontent.findViewById(R.id.usection);
                final EditText email = view_dialogcontent.findViewById(R.id.uemail);
                final EditText address = view_dialogcontent.findViewById(R.id.uaddress);

                Button submit = view_dialogcontent.findViewById(R.id.usubmit);

                name.setText(S_and_G.getName());
                section.setText(S_and_G.getNumber());
                email.setText(S_and_G.getEmail());
                address.setText(S_and_G.getSubsidy());

                dialogPlus.show();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try
                        {
                            Map<String,Object> map=new HashMap<>();
                            map.put("Name", name.getText().toString());
                            map.put("Number", section.getText().toString());
                            map.put("Email", email.getText().toString());
                            map.put("Subsidy", address.getText().toString());

                            FirebaseDatabase.getInstance().getReference().child("User")
                                    .child(section.getText().toString()).updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            dialogPlus.dismiss();
                                            storeDate_up();
                                            Toast.makeText(view.getContext(), "Successfully Updated!", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialogPlus.dismiss();
                                            Toast.makeText(view.getContext(), "Failed! Make sure that you have an internet access", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        catch (Exception exception)
                        {
                            Toast.makeText(view.getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
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
                    FirebaseDatabase.getInstance().getReference().child("User")
                            .child(getRef(position).getKey()).removeValue();
                    storeDate_del();
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new holder_view(view);
    }


    class holder_view extends RecyclerView.ViewHolder
    {
        CircleImageView img; // unused
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
    public void storeDate_up() {
        try{
            SimpleDateFormat dt = new SimpleDateFormat("MM-dd-yyyy");
            SimpleDateFormat t = new SimpleDateFormat("h:mm a");
            Date date1 = new Date();
            Date date = new Date();
            String date_str = dt.format(date);
            String date1_str = t.format(date1);
            Map<String, Object> map = new HashMap<>();
            map.put("Email", "admin");
            map.put("Time", date1_str);
            map.put("Date", date_str);
            map.put("Attempt", "Update");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Audit_Logs");
            databaseReference.push().setValue(map);
        }
        catch (Exception e)
        {
        }
    }
    public void storeDate_del() {
        try{
            SimpleDateFormat dt = new SimpleDateFormat("MM-dd-yyyy");
            SimpleDateFormat t = new SimpleDateFormat("h:mm a");
            Date date1 = new Date();
            Date date = new Date();
            String date_str = dt.format(date);
            String date1_str = t.format(date1);
            Map<String, Object> map = new HashMap<>();
            map.put("Email", "admin");
            map.put("Time", date1_str);
            map.put("Date", date_str);
            map.put("Attempt", "Delete");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Audit_Logs");
            databaseReference.push().setValue(map);
        }
        catch (Exception e)
        {
        }
    }
}
