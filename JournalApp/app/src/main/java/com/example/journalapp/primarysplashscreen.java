package com.example.journalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.example.journalapp.util.Journalapi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class primarysplashscreen extends AppCompatActivity {
    Handler handler;
    private FirebaseUser currentuser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("Users");
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primarysplashscreen);




        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                firebaseAuth=FirebaseAuth.getInstance();
                authStateListener=new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        currentuser=firebaseAuth.getCurrentUser();

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

                                                    startActivity(new Intent(primarysplashscreen.this,Journallist.class));
                                                    finish();


                                                }
                                            }



                                        }
                                    });








                    }
                } ;




                startActivity(new Intent(primarysplashscreen.this,MainActivity.class));
               finish();
            }
        },2000);



    }
}
