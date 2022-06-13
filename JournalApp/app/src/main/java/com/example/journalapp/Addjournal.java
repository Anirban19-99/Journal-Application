package com.example.journalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import java.util.Date;


import model.Journal;



public class Addjournal extends AppCompatActivity  implements View.OnClickListener
 {


    private static final int GALLERY_CODE = 1;
    private ImageView cover;
    private LinearLayout lay1;
    private ConstraintLayout lay2;
    private ImageView back;
    private ImageView postphoto;
    private ImageView profile;
    private TextView user_name;
    private TextView date;
    private ScrollView scrollView;
    private EditText title;
    private EditText thoughts;
    private Button save;
    //private SpinKitView progressBar;
    private String userid;
    private String username;
    ProgressDialog progress;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private FirebaseUser currentuser;

    private StorageReference storageReference;

    private Uri imageuri;
   private CollectionReference collectionReference=db.collection("Journalstudent");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addjournal);


        lay1=findViewById(R.id.lay1);
        lay2=findViewById(R.id.lay2);
        profile=findViewById(R.id.profile);

        back=findViewById(R.id.back);
        scrollView=findViewById(R.id.scroll);
        cover=findViewById(R.id.coverimage_add);
       postphoto=findViewById(R.id.postimage_add);
        user_name=findViewById(R.id.username_add);
         //date=findViewById(R.id.date_add);
        title=findViewById(R.id.title_add);
        thoughts=findViewById(R.id.thoughts_add);
        save=findViewById(R.id.save_add);



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Addjournal.this,Profile.class));
                Animatoo.animateCard(Addjournal.this);
            }
        });







        firebaseAuth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();

        postphoto.setOnClickListener(this);
        save.setOnClickListener(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Addjournal.this,Journallist.class));
                Animatoo.animateSlideDown(Addjournal.this);
            }
        });


        if(Journalapi.getInstance()!=null)
        { username=Journalapi.getInstance().getUsername();
          userid=Journalapi.getInstance().getUserid();
            Picasso.get().load(Journalapi.getInstance().getProfilephotourl()).into(profile);
        }
        user_name.setText(username);

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(currentuser!=null)
                {

                }
                else
                {

                }

            }
        };


    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.postimage_add: {
                Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                        gallery.setType("image/*");
                        startActivityForResult(gallery, GALLERY_CODE);

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

            case R.id.save_add:
               // progressBar.setVisibility(View.VISIBLE);
               save.setEnabled(false);
                save.setAlpha((float) 0.5);
                back.setAlpha((float) 0.5);
                lay1.setAlpha((float) 0.5);
                lay2.setAlpha((float) 0.5);
                title.setAlpha((float)0.5);
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
    //    progressBar.setVisibility(View.VISIBLE);
        final String thought=thoughts.getText().toString();
        final String titles=title.getText().toString();
       if(!TextUtils.isEmpty(thought)&&!TextUtils.isEmpty(titles)&& imageuri!=null)
        {


         final StorageReference filepath=storageReference.child("Journalstudent")
                .child("student"+Timestamp.now().getSeconds());




            filepath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                   filepath.getDownloadUrl().addOnSuccessListener(

                           new OnSuccessListener<Uri>() {
                       @Override
                       public void onSuccess(Uri uri) {

                         DocumentReference documentReference=collectionReference.document();
                         String id=documentReference.getId();



                           String imageurl=uri.toString();
                           final Journal journal=new Journal();
                           journal.setImageurl(imageurl);
                           journal.setDocid(id);
                           journal.setThougts(thought);
                           journal.setTime(new Timestamp(new Date()));
                           journal.setUsername(username);
                           journal.setCaption(titles);
                           journal.setUserid(userid);
                           collectionReference.document(id).set(journal).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {

                                   Intent intent=new Intent(Addjournal.this,Journallist.class);
                                   Animatoo.animateSlideDown(Addjournal.this);
                                   //progressBar.setVisibility(View.INVISIBLE);
                                   startActivity(intent);
                                   finish();


                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {


                                   save.setEnabled(true);
                                   progress.dismiss();
                                   save.setAlpha((float) 1.0);
                                   Toast.makeText(Addjournal.this, "case"+e, Toast.LENGTH_SHORT).show();

                               }
                           });

                        /*   collectionReference.add(journal).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                               @Override
                               public void onSuccess(DocumentReference documentReference) {

                                 String id=documentReference.getId();
                                 collectionReference.document(id).update("docid",id);
                                 journal.setDocid(id);
                                 Intent intent=new Intent(Addjournal.this,Journallist.class);
                                 //progressBar.setVisibility(View.INVISIBLE);
                                   startActivity(intent);
                                   finish();



                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   //progressBar.setVisibility(View.INVISIBLE);
                                   save.setEnabled(true);
                                   progress.dismiss();
                                   save.setAlpha((float) 1.0);
                                   Toast.makeText(Addjournal.this, "case", Toast.LENGTH_SHORT).show();
                               }
                           });*/


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
                   progress.dismiss();
                   lay2.setAlpha((float) 1);
                   title.setAlpha((float)1);
                   thoughts.setAlpha((float) 1);
                   Toast.makeText(Addjournal.this, "Not Saved", Toast.LENGTH_SHORT).show();

               }
           });




        }else
        {

        //    progressBar.setVisibility(View.INVISIBLE);
            save.setEnabled(true);
            save.setAlpha((float) 1.0);
            back.setAlpha((float) 1);
            lay1.setAlpha((float) 1);
            progress.dismiss();
            lay2.setAlpha((float) 1);
            title.setAlpha((float)1);
            thoughts.setAlpha((float) 1);
            Toast.makeText(this, "Please Enter All Details", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_CODE && resultCode==RESULT_OK)
        {
           if(data!=null)
           {
               imageuri=data.getData();
               cover.setImageURI(imageuri);
           }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentuser=firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseAuth!=null)
        {

            firebaseAuth.removeAuthStateListener(authStateListener);
        }







    }

     @Override
     public void onBackPressed() {
         super.onBackPressed();
         startActivity(new Intent(Addjournal.this,Journallist.class));
         Animatoo.animateSlideDown(Addjournal.this);
     }

 }
