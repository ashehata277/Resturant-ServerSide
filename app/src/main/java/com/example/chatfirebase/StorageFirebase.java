package com.example.chatfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.media.tv.TvContract;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Calendar;

public class StorageFirebase extends AppCompatActivity
{
    private StorageReference reference;
    private ProgressDialog dialog;
    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_storage);
        dialog=  new ProgressDialog(this);
        reference=FirebaseStorage.getInstance().getReference();
        img=(ImageView)findViewById(R.id.imageView2);

    }
    public void upload(View view)
    {
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.drawable.sm);
        img.setImageURI(uri);
        Calendar calender = Calendar.getInstance();
        dialog.setMessage("Wait to upload");
        dialog.show();
        StorageReference filepath = reference.child("Photos").child("img"+calender.getTimeInMillis());
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Upload successed",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Upload Failed",Toast.LENGTH_LONG).show();
                e.printStackTrace();
                dialog.dismiss();
            }
        });

    }
}