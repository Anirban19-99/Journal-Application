package com.example.journalapp.ui;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journalapp.R;
import com.example.journalapp.util.Journalapi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.Journal;
import model.Journalofficial;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class Recyclerviewadapterofficial extends RecyclerView.Adapter<Recyclerviewadapterofficial.ViewHolder> {

    private Context context;
    private List<Journalofficial> journallist;
    private FirebaseUser user;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference= db.collection("Journalstudent");
    FirebaseAuth firebaseAuth;


    public Recyclerviewadapterofficial(Context context, List<Journalofficial> journallist) {
        this.context = context;
        this.journallist = journallist;
    }

    @NonNull
    @Override
    public Recyclerviewadapterofficial.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context)
                .inflate(R.layout.journalrowofficial,parent,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull final Recyclerviewadapterofficial.ViewHolder holder, int position) {


        final Journalofficial journal=journallist.get(position);


        final String imageurl;
        holder.title.setText(journal.getTitle());
        holder.thought.setText(journal.getThougts());

        holder.names.setText(journal.getUsername());
        String time= (String) DateUtils.getRelativeTimeSpanString(journal.getTime().getSeconds()*1000);
        holder.date.setText(time);
        imageurl=journal.getFileurl();

        holder.images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageurl==null)
                {
                    holder.images.setVisibility(View.INVISIBLE);
                }

                Toast.makeText(context, "Downloading", Toast.LENGTH_LONG).show();
                download(holder.title.getContext(),journal.getTitle(),DIRECTORY_DOWNLOADS,imageurl);
            }
        });

        Journalapi journalapi=Journalapi.getInstance();

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
                                db.collection("Journalofficial").document(journal.getDocid()).delete().addOnSuccessListener(
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
        public TextView title,thought,date,names;
        public ImageView images,option;
            public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

                context=ctx;
                option=itemView.findViewById(R.id.option);
                title=itemView.findViewById(R.id.titlesofficial);
                thought=itemView.findViewById(R.id.thoughtsofficial);
                date=itemView.findViewById(R.id.dateofficial);
                images=itemView.findViewById(R.id.pdfofficial);
                names=itemView.findViewById(R.id.nameofficial);
                thought.setMovementMethod(LinkMovementMethod.getInstance());

            }
    }



    public  void download(Context context,String filename,String dest,String url)
    {

        DownloadManager downloadManager=(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri=Uri.parse(url);
        DownloadManager.Request request= new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,dest,filename);
        downloadManager.enqueue(request);



    }
}