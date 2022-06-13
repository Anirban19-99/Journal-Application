package com.example.journalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.ExifInterface;
import android.media.MediaDrm;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.journalapp.util.Journalapi;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.GoogleApiAvailabilityCache;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.yinglan.shadowimageview.ShadowImageView;



public class Login extends AppCompatActivity //implements GoogleApiClient.OnConnectionFailedListener
 {

    private Button signin;
    private  TextView resetpass;
    private ConstraintLayout constraintLayout;
    private ImageView bview;
    private View topview;
    private TextInputLayout textInputLayout;
    private TextView toptext;
    private ImageView topperson;
    private TextView register;
    private ImageView google;

    private TextView forgotpass;
    private TextView or;
    private EditText email;
    private EditText password;

    private SpinKitView progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("Users");
    private String name;
    private String id;
    private String mail;
    private GoogleApiClient googleApiClient;
    private static  final int SIGN_IN=1;

    Animation textanimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        constraintLayout=findViewById(R.id.constraintlayout);
        bview=findViewById(R.id.view);
        forgotpass=findViewById(R.id.forgotpassword);
        or=findViewById(R.id.or);
        textInputLayout=findViewById(R.id.textinput);
        register=findViewById(R.id.register);
        toptext=findViewById(R.id.toptext);
        topperson=findViewById(R.id.topperson);
        signin=findViewById(R.id.signin_login);
        google=findViewById(R.id.googlesignup);
        email=findViewById(R.id.email_login);
        resetpass=findViewById(R.id.forgotpassword);
        password=findViewById(R.id.password_login);
        progressBar=findViewById(R.id.progress_login);
        firebaseAuth=FirebaseAuth.getInstance();

        textanimation= AnimationUtils.loadAnimation(this,R.anim.textanimation);
        bview.animate().translationY(-1300).setDuration(1900).setStartDelay(300).alpha((float) 1.0);
        toptext.setAnimation(textanimation);
        topperson.setAnimation(textanimation);
        email.setAnimation(textanimation);
        signin.setAnimation(textanimation);
        forgotpass.setAnimation(textanimation);
        or.setAnimation(textanimation);
        register.setAnimation(textanimation);
        google.setAnimation(textanimation);
        textInputLayout.setAnimation(textanimation);







        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Intent intent=new Intent(Login.this,Signup.class);
               startActivity(intent);
               Animatoo.animateSlideRight(Login.this);
            }
        });














        Circle circle=new Circle();
        progressBar.setIndeterminateDrawable(circle);




        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,passwordreset.class));
                Animatoo.animateSlideRight(Login.this);
            }
        });



        /*


         */


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(email.getText().toString()))
                {
                    email.setError("Field Can't be empty");
                    email.requestFocus();
                }
                if(TextUtils.isEmpty(password.getText().toString()))
                {
                    password.setError("Field Can't be empty");
                    password.requestFocus();

                }


                signinwithemailandpassword(email.getText().toString(),password.getText().toString());
            }
        });
    }

    void  signinwithemailandpassword(final String email, final String password)
    {


        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
        {
            constraintLayout.setAlpha((float) 0.5);
            signin.setEnabled(false);
            signin.setAlpha((float) 0.5);
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                String userid=user.getUid();
                                collectionReference.whereEqualTo("userid",userid)
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                                if (!queryDocumentSnapshots.isEmpty()) {
                                                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                                        Journalapi journalapi = Journalapi.getInstance();
                                                        name=snapshot.getString("username");
                                                        id=snapshot.getString("userid");
                                                        mail=snapshot.getString("email");
                                                        journalapi.setUsername(snapshot.getString("username"));
                                                        journalapi.setUserid(snapshot.getString("userid"));
                                                        journalapi.setEmail(mail);
                                                        progressBar.setVisibility(View.INVISIBLE);

                                                        if(mail.contains("fcs@kiit.ac.in"))
                                                        {
                                                            //Toast.makeText(Login.this, "faculty", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(Login.this, Addjournalofficial.class));
                                                            Animatoo.animateCard(Login.this);

                                                        }
                                                        else
                                                        {
                                                            // Toast.makeText(Login.this, "student "+mail, Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(Login.this, Addjournal.class));
                                                            Animatoo.animateCard(Login.this);

                                                        }


                                                    }
                                                }
                                            }
                                        } ); }
                            else {
                                signin.setEnabled(true);
                                signin.setAlpha((float) 1);
                                constraintLayout.setAlpha((float) 1.0);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(Login.this, "Invalid Email or Password",
                                        Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
        }
        else
        {
            signin.setEnabled(true);
            signin.setAlpha((float) 1);
            constraintLayout.setAlpha((float) 1.0);
            progressBar.setVisibility(View.GONE);
            //  Toast.makeText(this, "Please Enter The Required Fields", Toast.LENGTH_LONG).show();
        }






    }

   @Override
    public void onBackPressed() {

        AlertDialog.Builder alert=new AlertDialog.Builder(Login.this);
        alert.setCancelable(true);
        alert.setMessage("Are you sure you want to Exit?");
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                finishAffinity();

            }
        });

        AlertDialog alertDialog=alert.create();
        alertDialog.show();



    }











}

