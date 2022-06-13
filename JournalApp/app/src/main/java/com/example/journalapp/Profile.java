package com.example.journalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaDrm;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.journalapp.ui.Recyclerviewadapter;
import com.example.journalapp.util.Journalapi;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Journal;


public class Profile extends AppCompatActivity {

    private TextView name,mail;
    private Button signout;
    private CircleImageView photo;
    private ImageView add;
    private SpinKitView progressbar;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
            RecyclerView recyclerView;
    private List<Journal> journalList;
    private Recyclerviewadapter adapterprofilestudent;
    CollectionReference collectionReference=db.collection("Journalstudent");
    private FirebaseUser user;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
       name = findViewById(R.id.name);
       mail=findViewById(R.id.mail);
       add=findViewById(R.id.add);
       progressbar=findViewById(R.id.progressbar);
       recyclerView=findViewById(R.id.recyclerview);
       signout=findViewById(R.id.signout);
       photo=findViewById(R.id.profilephoto);
       recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(Profile.this,R.color.profilegrad ));









        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        journalList=new ArrayList<>();

        Journalapi journalapi=Journalapi.getInstance();
       name.setText(journalapi.getUsername());
       mail.setText(journalapi.getEmail());

        Picasso.get().load(journalapi.getProfilephotourl()).into(photo);

        fetchdata();

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(Profile.this,Login.class));
                finish();
            }
        });







            }


    @Override
    protected void onStart() {
        super.onStart();

    }



    @Override
    protected void onPause() {
        super.onPause();
        if(firebaseAuth!=null)

        {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
    void fetchdata()
    {
        progressbar.setVisibility(View.VISIBLE);
      //  refresh.setRefreshing(true);
        Journalapi journalapi=Journalapi.getInstance();

        db.collection("Journalstudent"). whereEqualTo("userid",journalapi.getUserid()).
                get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty())
                {


                    for(QueryDocumentSnapshot journals: queryDocumentSnapshots)
                    {
                        Journal journal=journals.toObject(Journal.class);
                        journalList.add(journal);
                    }
                  //  refresh.setRefreshing(false);
                   adapterprofilestudent =new Recyclerviewadapter(Profile.this,journalList);
                    progressbar.setVisibility(View.INVISIBLE);
                    recyclerView.setAdapter(adapterprofilestudent);


                   // showdata(journalList);


                }
                else
                {

                   progressbar.setVisibility(View.INVISIBLE);
                   add.setVisibility(View.VISIBLE);
                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Profile.this,Addjournal.class));
                        }
                    });






                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {






            }
        });

    }
    void showdata(List<Journal> journalList)
    {
        adapterprofilestudent = new Recyclerviewadapter(Profile.this, journalList);
      //  progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(adapterprofilestudent);


    }










}



