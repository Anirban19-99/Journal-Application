package com.example.journalapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.journalapp.ui.Recyclerviewadapterofficial;
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
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import model.Journalofficial;

public class journallistofficial extends AppCompatActivity {


    private FirebaseUser user;
    private Toolbar toolbar;
    private SpinKitView progressBar;
    private SwipeRefreshLayout refresh;
    private FloatingActionButton add;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private List<Journalofficial> journalList;
    private TextView nojournal;
    private RecyclerView recyclerView;
    private FirebaseUser currentuser;
    private Recyclerviewadapterofficial recyclerviewadapter;
    String mail;
    private CollectionReference collectionReference=db.collection("Journalofficial");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journallistofficial);

        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        add=findViewById(R.id.addofficial);
        progressBar=findViewById(R.id.progress_listofficial);
        toolbar=findViewById(R.id.toolbarofficial);
        setSupportActionBar(toolbar);
        refresh=findViewById(R.id.refreshofficial);
        getSupportActionBar().setElevation(0);

        nojournal=findViewById(R.id.nojournalofficial);
        recyclerView=findViewById(R.id.journallistofficial);
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



                Toast.makeText(journallistofficial.this, ""+mail, Toast.LENGTH_SHORT).show();

                if(firebaseAuth!=null && user!=null)
                {

                    startActivity(new Intent(journallistofficial.this, Addjournalofficial.class));
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
                    startActivity(new Intent(journallistofficial.this, Login.class));
                    finish();
                }
                break;

            case  R.id.profile:
            {
                startActivity(new Intent(journallistofficial.this,profileofficial.class));

            }
        }
        return super.onOptionsItemSelected(item);

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
        currentuser =firebaseAuth.getCurrentUser();




        refresh.setRefreshing(true);
       collectionReference.
                orderBy("time", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty())
                {

                    for(QueryDocumentSnapshot journals: queryDocumentSnapshots)
                    {
                        Journalofficial journal=journals.toObject(Journalofficial.class);
                       journalList.add(journal);
                    }
                    refresh.setRefreshing(false);


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
    void showdata(List<Journalofficial> journalList)
    {
        recyclerviewadapter = new Recyclerviewadapterofficial(journallistofficial.this,journalList);
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(recyclerviewadapter);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(journallistofficial.this,Addjournalofficial.class));
    }

}

