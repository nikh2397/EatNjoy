package com.example.nikhil.eatnjoy.fragment;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nikhil.eatnjoy.HomeActivity;
import com.example.nikhil.eatnjoy.R;
import com.example.nikhil.eatnjoy.helper.Constant;
import com.example.nikhil.eatnjoy.model.Bean;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddImagesFragment extends Fragment implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 0;

    private EditText e1, e2, e3, e4;
    private Button choosee, uploadd, showw;
    private ImageView i;
    private Uri filePath;
    private StorageReference storageReference;
    private DatabaseReference mDatabase;


    public AddImagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_images, container, false);
        e1 = v.findViewById(R.id.pic);
        e2 = v.findViewById(R.id.price);
        e3 = v.findViewById(R.id.quanty);
        e4 = v.findViewById(R.id.des);
        choosee = v.findViewById(R.id.choose);
        uploadd = v.findViewById(R.id.upload);
        showw = v.findViewById(R.id.show);
        i = v.findViewById(R.id.imageView);
        storageReference = FirebaseStorage.getInstance().getReference();

        mDatabase = FirebaseDatabase.getInstance().getReference(Constant.DATABASE_PATH_UPLOADS);

        choosee.setOnClickListener(this);
        uploadd.setOnClickListener(this);
        showw.setOnClickListener(this);
        return v;
    }



    @Override
    public void onResume() {
        super.onResume();

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onClick(View v) {
        if (v == choosee) {
            showFileChooser();
        } else if (v == uploadd) {
            uploadFile();
        } else if (v == showw) {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

    private void showFileChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select picture"), PICK_IMAGE_REQUEST);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                i.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(getActivity(), "Select picture...", Toast.LENGTH_SHORT).show();
        }

    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void uploadFile() {
        //checking if file is available

        if (filePath != null) {
            //displaying progress dialog while image is uploading...
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            //getting the storage reference...
            StorageReference sRef = storageReference.child(Constant.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "," + getFileExtension(filePath));

            //adding the file to reference...

            sRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //dismissing the progress dialog...

                    progressDialog.dismiss();

                    //displaying the success Toast...

                    Toast.makeText(getActivity(), "File uploaded...", Toast.LENGTH_SHORT).show();

                    //creating the Bean object to store uploaded image details...

                    Bean bean = new Bean(e1.getText().toString().trim(),taskSnapshot.getDownloadUrl().toString(), e2.getText().toString().trim(), e4.getText().toString().trim(), e3.getText().toString().trim());


                    //adding an Bean to firebase database...

                    String uploadId = mDatabase.push().getKey();
                    bean.setId(uploadId);
                    mDatabase.child(uploadId).setValue(bean);

                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), e.getMessage() + "", Toast.LENGTH_SHORT).show();
                }

            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            //displaying the upload progress

                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            progressDialog.setMessage("uploading... " + ((int) progress) + " %");

                        }
                    });

        } else {

         //   Snackbar snackbar=Snackbar.make(v,"You did not select any pc yet",Snackbar.LENGTH_LONG).show();
            //display an error if no file is selected...
        }

    }
}

