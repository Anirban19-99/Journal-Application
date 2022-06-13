package com.example.journalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.journalapp.ui.Recyclerviewadapterofficial;
import com.example.journalapp.util.Journalapi;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Journalofficial;

public class profileofficial extends AppCompatActivity {

    private TextView name,mail;
    private CircleImageView photo;
    private Button signout;
    private ImageView add;
    private SpinKitView progressbar;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    private List<Journalofficial> journalList;
    private Recyclerviewadapterofficial adapterprofilestudent;
    CollectionReference collectionReference=db.collection("Journalofficial");
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileofficial);

        name = findViewById(R.id.name);
        mail=findViewById(R.id.mail);
       add=findViewById(R.id.addjournal);
        progressbar=findViewById(R.id.progressbar);
        recyclerView=findViewById(R.id.recyclerview);
        photo=findViewById(R.id.profilephoto);
        signout=findViewById(R.id.signout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(profileofficial.this,R.color.profilegrad ));




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
                startActivity(new Intent(profileofficial.this,Login.class));
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

        db.collection("Journalofficial"). whereEqualTo("userid",journalapi.getUserid()).
                get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty())
                {


                    for(QueryDocumentSnapshot journals: queryDocumentSnapshots)
                    {
                        Journalofficial journal=journals.toObject(Journalofficial.class);
                        journalList.add(journal);
                    }
                    //  refresh.setRefreshing(false);
                    adapterprofilestudent =new Recyclerviewadapterofficial(profileofficial.this,journalList);
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
                            startActivity(new Intent(profileofficial.this,Addjournalofficial.class));
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

    void showdata(List<Journalofficial> journalList)
    {
        adapterprofilestudent = new Recyclerviewadapterofficial(profileofficial.this, journalList);
        //  progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(adapterprofilestudent);


    }








}
