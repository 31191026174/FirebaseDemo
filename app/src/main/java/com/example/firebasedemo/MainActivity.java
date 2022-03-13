package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    private Button logout;
    private EditText edit;
    private Button add;
    private ListView listView;
    private Uri imageUri;

    private static final int IMAGE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout = findViewById(R.id.logout);
        edit = findViewById(R.id.edit);
        add = findViewById(R.id.add);
        listView = findViewById(R.id.listview);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Logger out", Toast.LENGTH_SHORT).show();
                startActivity((new Intent(MainActivity.this, StartActivity.class)));
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });
    }
    private void openImage(){
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST && requestCode == RESULT_OK){
            imageUri = data.getData();
            uploadImage();
        }
    }

    private String getFileExtension (Uri uri){
        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap =  MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if(imageUri != null){
            StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("upload")
                    .child(System.currentTimeMillis()+"."+ getFileExtension(imageUri));
            fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            Log.d("DownloadURl",url);
                            pd.dismiss();
                            Toast.makeText(MainActivity.this,"Upload Successfull",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

    }












        //ADD AND GET DATA WITH CLOUD FIRESTORE
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String txt_name = edit.getText().toString();
//                if( txt_name.isEmpty()){
//                    Toast.makeText(MainActivity.this, "No name entered!", Toast.LENGTH_SHORT).show();
//                }else {
//                    FirebaseDatabase.getInstance().getReference().child("Xuanphongdeptrai").child("Android").setValue("abc");
//                }
//            }
//        });

//        final ArrayList<String> list = new ArrayList<>();
//        final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_item, list);
//        listView.setAdapter(adapter);
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Languages");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                list.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    list.add(snapshot.getValue().toString());
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        DocumentReference ref = FirebaseFirestore.getInstance().collection("cities").document("JSR");
//        ref.update("capital", true);
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Map<String, Object> city = new HashMap<>();
//        city.put("Name", "Phng");
//        city.put("Age", "20");
//
//        db.collection("cities").document("JSR").set(city).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//                    Toast.makeText(MainActivity.this, "Values added",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        Map<String , Object> data = new HashMap<>();
//        data.put("capital", false);
//        db.collection("cities").document("JSR").set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//                    Toast.makeText(MainActivity.this, "Merg Successful", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        data.put("name", "tokyo");
//        data.put("capital", "japan");
//        db.collection("cities").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentReference> task) {
//                if(task.isSuccessful()){
//                    Toast.makeText(MainActivity.this, "Merg Successful", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });





}