package com.example.journalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class splashscreen extends AppCompatActivity {



    ImageView logo,signup,signin,c1,c2,c3;
    ImageView welcomescreen;
    Animation textanimation;
    LinearLayout welcometext,icons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        logo=findViewById(R.id.logo);
        welcometext=findViewById(R.id.welcometext);
        welcomescreen=findViewById(R.id.welcomescreen);
        icons=findViewById(R.id.icons);
        signup=findViewById(R.id.signup);
        signin=findViewById(R.id.signin);
        c1=findViewById(R.id.c1);
        textanimation=AnimationUtils.loadAnimation(this,R.anim.textanimation);
        welcomescreen.animate().translationY(-1300).setDuration(1800).setStartDelay(300).alpha((float) 1.0);
        logo.animate().alpha(0).setDuration(800).setStartDelay(300);
        welcometext.setAnimation(textanimation);
        icons.setAnimation(textanimation);
        c1.setAnimation(textanimation);


        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(splashscreen.this,Signup.class));
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(splashscreen.this,Login.class));
            }
        });



    }
}
