package com.example.journalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.journalapp.util.Journalapi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.spark.submitbutton.SubmitButton;

import model.Journal;

public class MainActivity extends AppCompatActivity {

    private SubmitButton getstarted;
    private FirebaseUser currentuser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("Users");
    private FirebaseAuth.AuthStateListener authStateListener;
    private String mail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getstarted=findViewById(R.id.getstarted);

        firebaseAuth=FirebaseAuth.getInstance();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.blue));





        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentuser=firebaseAuth.getCurrentUser();
                if(currentuser!=null)
                {
                    currentuser=firebaseAuth.getCurrentUser();
                    String userid=currentuser.getUid();


                   collectionReference.whereEqualTo("userid",userid)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                                    if(e!=null)
                                    {
                                        return;
                                    }
                                    if(!queryDocumentSnapshots.isEmpty())
                                    {
                                        for(QueryDocumentSnapshot snapshot:queryDocumentSnapshots)
                                        {
                                            Journalapi journalapi= Journalapi.getInstance();
                                            journalapi.setUserid(snapshot.getString("userid"));
                                            journalapi.setUsername(snapshot.getString("username"));
                                            journalapi.setProfilephotourl(snapshot.getString("profilephotourl"));

                                            mail=snapshot.getString("email");
                                            journalapi.setEmail(mail);



                                            if(mail.contains("fcs@kiit.ac.in"))
                                            {
                                                startActivity(new Intent(MainActivity.this, journallistofficial.class));

                                            }
                                            else
                                            {

                                                startActivity(new Intent(MainActivity.this, Journallist.class));
                                            }
                                            finish();


                                        }
                                    }



                                }
                            });







                }
                else
                {

                }
            }
        } ;


        getstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(2000);
                            startActivity(new Intent(MainActivity.this,Login.class));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }
                });
                thread.start();
            }
        });




        

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentuser=firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(firebaseAuth!=null)

        {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }


}
