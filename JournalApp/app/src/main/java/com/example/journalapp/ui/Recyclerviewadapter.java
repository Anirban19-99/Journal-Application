package com.example.journalapp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompatSideChannelService;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journalapp.R;

import com.example.journalapp.util.Journalapi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.Journal;


public class Recyclerviewadapter extends RecyclerView.Adapter<Recyclerviewadapter.ViewHolder>  {
    private Context context;
    private List<Journal> journallist;
    private FirebaseUser user;
    private  FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference= db.collection("Journalstudent");


    public Recyclerviewadapter(Context context, List<Journal> journallist) {
        this.context = context;
        this.journallist = journallist;
    }

    @NonNull
    @Override
    public Recyclerviewadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {



        View view= LayoutInflater.from(context)
                .inflate(R.layout.journalrow,parent,false);

        return new ViewHolder(view,context);
    }


    @Override
    public void onBindViewHolder(@NonNull final Recyclerviewadapter.ViewHolder holder, final int position) {


      /*  collectionReference.whereEqualTo("userid",user).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments())
                {
                    String title=document.getString("title");
                    String thoughts=document.getString("thoughts");
                    String name=document.getString("username");

                    Toast.makeText(context, "usernamesuccess"+name, Toast.LENGTH_SHORT).show();


                }

            }
        });

       */
       final Journal journal=journallist.get(position);

        final Journalapi journalapi=Journalapi.getInstance();

        String imageurl;
       holder.titles.setText(journal.getCaption());
       holder.thoughts.setText(journal.getThougts());
       holder.name.setText(journal.getUsername());
        String time= (String) DateUtils.getRelativeTimeSpanString(journal.getTime().getSeconds()*1000);
        holder.dates.setText(time);
        imageurl=journal.getImageurl();
        holder.image.setVisibility(View.VISIBLE);
        Picasso.get().load(imageurl)
               // .placeholder(R.drawable.group)
                .into(holder.image);
      //  holder.option.setVisibility(View.INVISIBLE);


        final String name1=journal.getUsername();
        final String name2=journalapi.getUsername();

      holder.option.setVisibility(View.INVISIBLE);


        if(name1.equals(name2))
        {
            holder.option.setVisibility(View.VISIBLE);
        }














        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu=new PopupMenu(context,holder.option);
                popupMenu.inflate(R.menu.optionmenu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()){
                            case R.id.delete:
                                firebaseAuth= FirebaseAuth.getInstance();
                                user=firebaseAuth.getCurrentUser();
                                String uid=user.getUid();

                                final AlertDialog.Builder alert=new AlertDialog.Builder(context);
                                alert.setCancelable(true);
                                alert.setMessage("Are you sure to Delete this Journal?");
                                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                      dialogInterface.cancel();
                                    }
                                });
                                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        db.collection("Journalstudent").document(journal.getDocid()).delete().addOnSuccessListener(
                                                new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Toast.makeText(context, "Data Deleted,Please Refresh the list", Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                        ).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });


                                    }
                                });
                                AlertDialog alertDialog= alert.create();
                                alertDialog.show();







                               // journallist.remove(position);
                              //  notifyDataSetChanged();


                                break;
                        }

                        return false;
                    }
                });

          popupMenu.show();



            }
        });














    }

    @Override
    public int getItemCount() {
        return journallist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titles,thoughts,dates,name;
        public ImageView image,option;
        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context=ctx;
            option=itemView.findViewById(R.id.option);
            titles=itemView.findViewById(R.id.titles);
            thoughts=itemView.findViewById(R.id.thoughts);
            dates=itemView.findViewById(R.id.date);
            image=itemView.findViewById(R.id.image);
            name=itemView.findViewById(R.id.name);
            thoughts.setMovementMethod(LinkMovementMethod.getInstance());






        }

    }





}
