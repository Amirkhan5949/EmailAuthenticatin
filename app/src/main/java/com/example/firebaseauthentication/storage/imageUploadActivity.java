package com.example.firebaseauthentication.storage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.firebaseauthentication.Loader;
import com.example.firebaseauthentication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.myhexaville.smartimagepicker.OnImagePickedListener;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

public class imageUploadActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button upload;
    private final int PERMISSION_ALL = 1234;
    private  ImagePicker imagePicker;
    private Loader loader;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        imageView=findViewById(R.id.image);
        upload=findViewById(R.id.upload);

        loader = new Loader(this);

        final String[] PERMISSIONS = {
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                 android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                 android.Manifest.permission.CAMERA
        };


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasPermissions(imageUploadActivity.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(imageUploadActivity.this, PERMISSIONS, PERMISSION_ALL);
                }
                else {
                    imagePicker.choosePicture(true /*show camera intents*/);

                }

            }
        });

        imagePicker = new ImagePicker(this, /* activity non null*/
                null, /* fragment nullable*/
                new OnImagePickedListener() {
                    @Override
                    public void onImagePicked(Uri imageUri) {
                        UCrop.of(imageUri,getTempUri())
                                .withAspectRatio(1, 1)
                                .start(imageUploadActivity.this);


                    }
                });

    }

    private Uri getTempUri(){

        String dir = Environment.getExternalStorageDirectory()+ File.separator+"Temp";
        File  dirfile=new File (dir);
        dirfile.mkdir();

        String file= dir+File.separator+"Temp.png";
                File tempfile=new File(file);
        try {
            tempfile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(tempfile);
    }



    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissionsList, grantResults);
        imagePicker.handlePermission(requestCode, grantResults);
        switch (requestCode) {
            case PERMISSION_ALL:{
                if (grantResults.length > 0) {
                    boolean flag = true;
                    for (int i=0;i<permissionsList.length;i++) {
                        if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                            flag=false;
                        }
                    }
                    if (flag){
                        imagePicker.choosePicture(true /*show camera intents*/);
                    }
                    // Show permissionsDenied
                 }
                return;
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode,requestCode, data);

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);

            imageView.setImageURI(null);
            upload(resultUri);


        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    void upload(Uri uri){
        loader.show();
                 final StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("Aamir/"+System.currentTimeMillis()+".png");

         riversRef.putFile(uri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(imageUploadActivity.this, "failure"+ exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {


            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...

                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        loader.dismiss();
                        imageView.setImageURI(null);
                        Picasso.get().load(uri).into(imageView);



                    }
                });
            }
        });

    }


}
