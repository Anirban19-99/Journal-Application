package com.example.journalapp;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.StatusBarManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.journalapp.util.Journalapi;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import model.Journal;


public class Signup extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
 {

    private View decorview;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions googleSignInOptions ;
    private EditText Username;
    private EditText Email;
    private EditText Password;
    private TextView waittext;
    private ImageView back;
    private ImageView imageView;
    private ConstraintLayout signup;
    private String age;
    private CardView cardView;
    private Button cancel;
    private String address;
    private TextView signintext;
    private ProgressBar progressBar;
    private ConstraintLayout mainlayout;
    private GoogleSignInButton signupbutton;
    private ConstraintLayout constraintLayout;
    private FirebaseUser currentuser;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String photourl;


    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference= db.collection("Users");

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        imageView=findViewById(R.id.imageview);
        imageView.animate().translationY(-1580).setDuration(0);
        Username=findViewById(R.id.username_signup);
        Email=findViewById(R.id.mail_signup);
        signup=findViewById(R.id.signuplayout);
        Password=findViewById(R.id.password_signup);
        waittext=findViewById(R.id.waittext);
        progressBar=findViewById(R.id.progress_signup);
        back=findViewById(R.id.back);
        signintext=findViewById(R.id.signintext);
        signupbutton=findViewById(R.id.signupbutton);
        mainlayout=findViewById(R.id.mainlayout);
        constraintLayout=findViewById(R.id.constraintlayout);
        cancel=findViewById(R.id.cancel);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(Signup.this,R.color.blue));

        decorview=getWindow().getDecorView();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signup.this,Login.class));
                Animatoo.animateSlideLeft(Signup.this);
            }
        });

       Email.setFocusable(false);
       Email.setClickable(true);
        Username.setFocusable(false);
        Username.setClickable(true);


        googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).
                addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions).build();


        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin();
            }
        });




   /*     getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


    */
        signintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signup.this,Login.class));
                Animatoo.animateSlideLeft(Signup.this);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                        Intent intent=new Intent(Signup.this,Login.class);
                        startActivity(intent);
                        Animatoo.animateSlideLeft(Signup.this);

                    }
                });

            }
        });

        firebaseAuth=FirebaseAuth.getInstance();

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                currentuser = firebaseAuth.getCurrentUser();

                if (currentuser != null) {

                } else {
                }
            }
        };


        signup.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                String email=Email.getText().toString();
                String password=Password.getText().toString();
                String username=Username.getText().toString();
               /* Journalapi journalapi=Journalapi.getInstance();
                journalapi.setUsername(username);

                */

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username) /*&& email.contains("kiit.ac.in")*/)
                {
                    signup.setBackgroundResource(R.drawable.gradient);
                    createaccountwithemail(email,password,username,address,age);
                }
                else if(TextUtils.isEmpty(email))
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Email.setError("Field Can't be empty");
                    Email.requestFocus();
                    //    Toast.makeText(Signup.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(username))
                {
                    Username.setError("Field Can't be empty");
                    Username.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                    // Toast.makeText(Signup.this, "Please Enter Username", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(password))
                {
                    Password.setError("Field Can't be empty");
                    Password.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                    //  Toast.makeText(Signup.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                }
              /*  else if(!email.contains("kiit.ac.in"))
                {
                    Email.setError("Please enter your KIIT Mail id");
                    Email.requestFocus();
                    Toast.makeText(Signup.this, "Please use your KIIT mail id to continue", Toast.LENGTH_LONG).show();
                }

               */

                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }


        });


    }
    @SuppressLint("ResourceAsColor")
    void createaccountwithemail(final String email, final String password, final String username, final String address, final String age)
    {
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)  /*&&email.contains("kiit.ac.in")*/ )
        {




            waittext.setText("Please wait..");
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                signup.setEnabled(false);
                                signup.setAlpha((float) 0.5);

                                currentuser=firebaseAuth.getCurrentUser();
                                assert currentuser != null;
                                final String currentuserid=currentuser.getUid();


                                // TO CREATE A USER IN COLLECTION
                                Map<String,String> userobject=new HashMap<>();
                                userobject.put("userid",currentuserid);
                                userobject.put("username",username);
                                userobject.put("email",email);
                                userobject.put("profilephotourl",photourl);



                                //SAVE TO FIREBASE

                                collectionReference.add(userobject).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.getResult().exists())
                                                {

                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    String name=task.getResult().getString("username");
                                                    Journalapi journalapi=Journalapi.getInstance();
                                                    journalapi.setUserid(currentuserid);
                                                    journalapi.setUsername(name);
                                                    journalapi.setProfilephotourl(photourl);

                                                    if(email.contains("fcs@kiit.ac.in"))
                                                    {
                                                        //Toast.makeText(Login.this, "faculty", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(Signup.this, journallistofficial.class));
                                                        Animatoo.animateSlideDown(Signup.this);

                                                    }
                                                    else
                                                    {
                                                        // Toast.makeText(Login.this, "student "+mail, Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(Signup.this, Journallist.class));
                                                        Animatoo.animateSlideDown(Signup.this);

                                                    }



                                                }
                                                else
                                                {
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                }
                                            }
                                        });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        signup.setBackgroundResource(R.drawable.gradient);
                                        waittext.setText("Sign Up");
                                        Toast.makeText(Signup.this, "network", Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }
                            else if(task.getException() instanceof FirebaseAuthUserCollisionException)
                            {
                                signup.setBackgroundResource(R.drawable.gradient);
                                waittext.setText("Sign Up");
                                Toast.makeText(Signup.this, "You Have already Regestered", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    signup.setBackgroundResource(R.drawable.gradient);
                    signup.setEnabled(true);
                    signup.setAlpha((float) 1.0);
                    progressBar.setVisibility(View.INVISIBLE);
                    int len=password.length();
                    if(len<6)
                    {
                        Password.setError("Enter a Strong Password");
                        Password.requestFocus();
                    }else  if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                    {
                        Email.setError("Invalid Email");
                        Email.requestFocus();

                    }

                }
            });
        }else
        {
        }




    }


    @Override
    protected void onStart() {
        super.onStart();
        currentuser=firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);














    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        constraintLayout.setVisibility(View.VISIBLE);
        mainlayout.setVisibility(View.INVISIBLE);
        startActivity(new Intent(Signup.this,Signup.class));

    }


     @Override
     public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

     }


     private void signin()
     {

         Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
         startActivityForResult(intent,999);

     }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);

         if(result.isSuccess()) {
             if (requestCode == 999) {
                 GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                 signinresult(googleSignInResult);
             }
             else
             {

             }
         }
         else
         {


         }

     }


     private void signinresult(GoogleSignInResult googleSignInResult)
     {



         if(googleSignInResult.isSuccess())
         {
            GoogleSignInAccount ac=googleSignInResult.getSignInAccount();
            String gmail=ac.getEmail();
            String gname=ac.getDisplayName();

            if(ac.getPhotoUrl()!=null){


                photourl=ac.getPhotoUrl().toString();
            }
            else
            {
                photourl=null;
            }



            Email.setText(gmail);
             Username.setText(gname);
           constraintLayout.setVisibility(View.GONE);
             mainlayout.setVisibility(View.VISIBLE);
             signout();
         }
         else
         {
             signupbutton.setVisibility(View.VISIBLE);
             mainlayout.setVisibility(View.INVISIBLE);
         }

     }



     private void signout()
     {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

            }
        });
     }







 }










