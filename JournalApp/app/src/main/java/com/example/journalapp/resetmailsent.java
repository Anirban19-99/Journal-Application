package com.example.journalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class resetmailsent extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_resetmailsent);





        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(resetmailsent.this, R.color.black));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(resetmailsent.this,Login.class));
        Animatoo.animateSlideLeft(resetmailsent.this);
    }

}
