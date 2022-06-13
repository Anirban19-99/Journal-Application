package com.example.journalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class passwordreset extends AppCompatActivity {

    private EditText email;
    private Button reset;
    private Button cancel;
    private ImageView back;
    String mail;
    private SpinKitView progressbar;
   private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordreset);

        email = findViewById(R.id.email);
        reset = findViewById(R.id.reset);
        cancel = findViewById(R.id.cancel);
        back = findViewById(R.id.back);
        progressbar=findViewById(R.id.progressbar);



        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(passwordreset.this, R.color.white));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(passwordreset.this, Login.class));
                Animatoo.animateSlideLeft(passwordreset.this);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(passwordreset.this, Login.class));
                Animatoo.animateSlideLeft(passwordreset.this);
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressbar.setVisibility(View.VISIBLE);
                mail = email.getText().toString().trim();
            if(!TextUtils.isEmpty(mail)) {
                firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(passwordreset.this, "sent", Toast.LENGTH_SHORT).show();
                            progressbar.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(getApplicationContext(), resetmailsent.class));
                            Animatoo.animateSlideRight(passwordreset.this);


                        } else {
                            progressbar.setVisibility(View.INVISIBLE);
                            Toast.makeText(passwordreset.this, "Email Not Registered,Please SignUp", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressbar.setVisibility(View.INVISIBLE);
                        Toast.makeText(passwordreset.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else
            {
                progressbar.setVisibility(View.INVISIBLE);
                Toast.makeText(passwordreset.this, "Please Enter Registered Email", Toast.LENGTH_SHORT).show();
            }


            }
        });

    }

}