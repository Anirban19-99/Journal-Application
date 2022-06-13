package com.example.journalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.journalapp.ui.Recyclerviewadapter;

import com.example.journalapp.util.Journalapi;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import model.Journal;

public class Journallist extends AppCompatActivity {


    private FirebaseUser user;
    private Toolbar toolbar;
    private SpinKitView progressBar;
    private SwipeRefreshLayout refresh;
    private FloatingActionButton add;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private List<Journal> journalList;
    private TextView nojournal;
    private RecyclerView recyclerView;
    private Recyclerviewadapter recyclerviewadapter;
    String mail;
    CollectionReference collectionReference=db.collection("Journalstudent");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jornallist);
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        add=findViewById(R.id.add);
        progressBar=findViewById(R.id.progress_list);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        refresh=findViewById(R.id.refresh);
        getSupportActionBar().setElevation(0);

        nojournal=findViewById(R.id.nojournal);
        recyclerView=findViewById(R.id.journallist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        journalList=new ArrayList<>();

        progressBar.setVisibility(View.VISIBLE);


        fetchdata();
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                journalList.clear();
                fetchdata();
            }
        });

       Journalapi journalapi=Journalapi.getInstance();
        mail= journalapi.getEmail();


    add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            //    Toast.makeText(Journallist.this, ""+mail, Toast.LENGTH_SHORT).show();

                if(firebaseAuth!=null && user!=null)
                {


                        startActivity(new Intent(Journallist.this, Addjournal.class));

                    }





            }
        });












    }

   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }



   @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {








       switch (item.getItemId())
        {


            case R.id.signout:
                if(firebaseAuth!=null && user!=null)
                {
                    firebaseAuth.signOut();
                    startActivity(new Intent(Journallist.this,Login.class));
                    finish();
                }
                break;

            case  R.id.profile:
                {
                    startActivity(new Intent(Journallist.this,Profile.class));

                }
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onStart() {
        super.onStart();




     /*   collectionReference.whereEqualTo("userid", Journalapi.getInstance().getUserid()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if(!queryDocumentSnapshots.isEmpty())
                        {
                           Log.d("all","ok here");
                            Toast.makeText(Journallist.this, "all ok here", Toast.LENGTH_SHORT).show();
                            for(QueryDocumentSnapshot journals: queryDocumentSnapshots)
                            {
                                Journal journal=journals.toObject(Journal.class);
                                 journalList.add(journal);


                            }
                            recyclerviewadapter =new Recyclerviewadapter(Journallist.this,journalList);
                            recyclerView.setAdapter(recyclerviewadapter);
                           // recyclerviewadapter.notifyDataSetChanged();

                        }
                        else
                        {
                            nojournal.setVisibility(View.VISIBLE);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("failed here","failed here");
                Toast.makeText(Journallist.this, "failed", Toast.LENGTH_SHORT).show();

            }
        });

      */




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


        refresh.setRefreshing(true);

        db.collection("Journalstudent"). orderBy("time", Query.Direction.DESCENDING).
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
                     refresh.setRefreshing(false);

                    recyclerviewadapter =new Recyclerviewadapter(Journallist.this,journalList);
                    recyclerView.setAdapter(recyclerviewadapter);


                    showdata(journalList);


                }
                else
                {

                    progressBar.setVisibility(View.INVISIBLE);
                    refresh.setRefreshing(false);
                    nojournal.setVisibility(View.VISIBLE);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {




                refresh.setRefreshing(false);


            }
        });








    }


    void showdata(List<Journal> journalList)
    {
        recyclerviewadapter = new Recyclerviewadapter(Journallist.this, journalList);
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(recyclerviewadapter);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Journallist.this,Addjournal.class));
        Animatoo.animateSlideRight(Journallist.this);
    }


}


