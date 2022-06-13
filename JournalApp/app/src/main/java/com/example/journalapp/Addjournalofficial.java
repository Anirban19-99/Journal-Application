package com.example.journalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.journalapp.util.Journalapi;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.HashMap;

import io.reactivex.internal.operators.parallel.ParallelRunOn;
import model.Journalofficial;

public class Addjournalofficial extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout lay1;
    private ImageView back;
    private ImageView profile;
    private TextView pdf;
    private TextView user_name;
    private TextView date;
    private ScrollView scrollView;
    private EditText title;
    private EditText thoughts;
    private Button save;
   // private SpinKitView progressBar;
    private String userid;
    private String username;
    ProgressDialog progress;


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentuser;

    private StorageReference storageReference;
    private CollectionReference collectionReference = db.collection("Journalofficial");
    private Uri fileuri;
    HashMap<String,String> metamap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addjournalofficial);


        lay1 = findViewById(R.id.lay1official);
        profile=findViewById(R.id.profile);
        back = findViewById(R.id.backofficial);
        scrollView= findViewById(R.id.scrollofficial);
        pdf = findViewById(R.id.pdf_addofficial);
        user_name = findViewById(R.id.username_addofficial);
        //date=findViewById(R.id.date_add);
        title = findViewById(R.id.title_addofficial);
        thoughts = findViewById(R.id.thoughts_addofficial);
        save = findViewById(R.id.save_addofficial);
      //  progressBar = findViewById(R.id.progress_addofficial);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        pdf.setOnClickListener(this);
        save.setOnClickListener(this);



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Addjournalofficial.this,profileofficial.class));
                Animatoo.animateCard(Addjournalofficial.this);
            }
        });




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Addjournalofficial.this, journallistofficial.class));
                Animatoo.animateSlideDown(Addjournalofficial.this);
            }
        });


        if (Journalapi.getInstance() != null) {
            username = Journalapi.getInstance().getUsername();
            userid = Journalapi.getInstance().getUserid();
            Picasso.get().load(Journalapi.getInstance().getProfilephotourl()).into(profile);

        }
        user_name.setText(username);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (currentuser != null) {

                } else {

                }

            }
        };


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pdf_addofficial:{
                Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                                Intent intent = new Intent();
                                intent.setType("application/pdf|audio/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);

                                startActivityForResult(Intent.createChooser(intent,"Select PDF File"),1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {



                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();

            }




                break;

            case R.id.save_addofficial:

              //  progressBar.setVisibility(View.VISIBLE);
                save.setEnabled(false);
                save.setAlpha((float) 0.5);
                back.setAlpha((float) 0.5);
                lay1.setAlpha((float) 0.5);
                title.setAlpha((float) 0.5);
                thoughts.setAlpha((float) 0.5);


                savejournal();
                break;

        }

    }


    private void savejournal() {



        progress=new ProgressDialog(this,R.style.Dialogue);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setTitle("Uploading");
        progress.setProgress(0);
        progress.setCancelable(false);
        progress.incrementProgressBy(2);
        progress.show();






        // progressBar.setVisibility(View.VISIBLE);
        final String thought = thoughts.getText().toString();
        final String titles = title.getText().toString();
        if (!TextUtils.isEmpty(thought)  && !TextUtils.isEmpty(titles)  && fileuri!= null) {


            final StorageReference filepath = storageReference.child("Journalofficial")
                    .child("official" + Timestamp.now().getSeconds()+".docx");


            filepath.putFile(fileuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess( UploadTask.TaskSnapshot taskSnapshot) {

                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String fileurl = uri.toString();
                            Journalofficial journalofficial = new Journalofficial();

                            DocumentReference documentReference=collectionReference.document();
                            String id=documentReference.getId();

                           journalofficial.setDocid(id);
                           journalofficial.setFileurl(fileurl);
                            journalofficial.setThougts(thought);
                            journalofficial.setTime(new Timestamp(new Date()));
                            journalofficial.setUsername(username);
                            journalofficial.setTitle(titles);
                            journalofficial.setUserid(userid);
                            collectionReference.document(id).set(journalofficial).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Intent intent = new Intent(Addjournalofficial.this, journallistofficial.class);
                                    startActivity(intent);
                                    Animatoo.animateSlideDown(Addjournalofficial.this);
                                    finish();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    save.setEnabled(true);
                                    save.setAlpha((float) 1.0);
                                    progress.dismiss();
                                    Toast.makeText(Addjournalofficial.this, "Upload Failed", Toast.LENGTH_SHORT).show();

                                }
                            });





                                    /*addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Intent intent = new Intent(Addjournalofficial.this, journallistofficial.class);
                               //     progressBar.setVisibility(View.INVISIBLE);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                 //   progressBar.setVisibility(View.INVISIBLE);
                                    save.setEnabled(true);
                                    save.setAlpha((float) 1.0);
                                    progress.dismiss();
                                    Toast.makeText(Addjournalofficial.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                                }
                            });

                                     */



                        }
                    });

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void    onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                    //Toast.makeText(Addjournalofficial.this, "uploading", Toast.LENGTH_SHORT).show();

                   double currentprogress= (double) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());

                    progress.setMessage("Uploading...");


                    progress.setProgress((int) currentprogress);




                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                  //  progressBar.setVisibility(View.INVISIBLE);
                    save.setEnabled(true);
                    save.setAlpha((float) 1.0);
                    back.setAlpha((float) 1);
                    lay1.setAlpha((float) 1);
                    title.setAlpha((float) 1);
                    thoughts.setAlpha((float) 1);
                    progress.dismiss();
                    Toast.makeText(Addjournalofficial.this, "Not Saved", Toast.LENGTH_SHORT).show();

                }
            });


        } else {

         //   progressBar.setVisibility(View.INVISIBLE);
            save.setEnabled(true);
            save.setAlpha((float) 1.0);
            back.setAlpha((float) 1);
            lay1.setAlpha((float) 1);
            title.setAlpha((float) 1);
            progress.dismiss();
            thoughts.setAlpha((float) 1);
            Toast.makeText(this, "Please Enter All Field", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1 && resultCode == RESULT_OK ) {

            if (data != null) {
               fileuri = data.getData();

               pdf.setText(fileuri.toString());
               // cover.setImageURI(fileuri);
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentuser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuth != null) {

            firebaseAuth.removeAuthStateListener(authStateListener);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Addjournalofficial.this,journallistofficial.class));
        Animatoo.animateSlideDown(Addjournalofficial.this);
    }


}
