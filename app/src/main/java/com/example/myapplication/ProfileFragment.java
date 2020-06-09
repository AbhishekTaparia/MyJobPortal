package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private static final int PICK_PDF_REQUEST = 1;
    private static final int PICK_IMAGE_REQUEST = 2;

    private Uri pdfUri,imageUri;

    private EditText editTextName,editTextMobile,editTextAddress,editTextPin,editTextDOB,editTextCurrentCity,editTextDegree;
    private Button buttonUpdate,buttonUploadCV;
    private RadioButton radioMale,radioFemale;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private String gender,jobApplied;
    private ImageView imageViewDP;

    private String cv,imageURL;



    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = getArguments();
        if (bundle != null && !bundle.getBoolean("profileAdded")) {
            Toast.makeText(getActivity(), "Complete your profile first", Toast.LENGTH_SHORT).show();
            return;
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        editTextName=(EditText)view.findViewById(R.id.editTextFullName);
        editTextMobile=(EditText)view.findViewById(R.id.editTextMobile);
        editTextAddress=(EditText)view.findViewById(R.id.editTextAddress);
        editTextPin=(EditText)view.findViewById(R.id.editTextPincode);
        editTextDOB=(EditText)view.findViewById(R.id.editTextDOB);
        editTextCurrentCity=(EditText)view.findViewById(R.id.editTextCurrentCity);
        editTextDegree=(EditText)view.findViewById(R.id.editTextDegree);
        gender=null;
        jobApplied="";
        cv="";
        imageURL="";

        radioMale=(RadioButton)view.findViewById(R.id.radioMale);
        radioFemale=(RadioButton)view.findViewById(R.id.radioFemale);


        buttonUpdate=(Button)view.findViewById(R.id.buttonUpdate);
        buttonUploadCV=(Button)view.findViewById(R.id.buttonUploadCV);

        progressBar=(ProgressBar)view.findViewById(R.id.progressbar);

        imageViewDP=(ImageView)view.findViewById(R.id.imageView);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    Log.d("snap Key",snap.getKey());
                    if (snap.getKey().equals(mAuth.getCurrentUser().getUid())) {
                        User user = snap.getValue(User.class);
                        editTextAddress.setText(user.getAddress());
                        editTextCurrentCity.setText(user.getCurrentCity());
                        editTextName.setText(user.getName());
                        editTextDegree.setText(user.getDegree());
                        editTextDOB.setText(user.getDob());
                        editTextPin.setText(user.getPinCode());
                        editTextMobile.setText(user.getMobile());
                        jobApplied=user.getJobApplied();
                        if(user.getGender().equals("Male")){
                            radioMale.setChecked(true);
                        }else{
                            radioFemale.setChecked(true);
                        }
                        cv=user.getCv();
                        imageURL=user.getImageURL();
                        Picasso.with(getActivity()).load(imageURL).into(imageViewDP);
                        Log.d("imageURL",user.getImageURL());

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {

            if (user.getDisplayName() != null) {
                String displayName = user.getDisplayName();
                editTextName.setText(displayName);
            }

        }

        editTextDOB.setOnClickListener(this);
        buttonUpdate.setOnClickListener(this);
        buttonUploadCV.setOnClickListener(this);
        imageViewDP.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.editTextDOB:{
                dateOfBirth();
                return;
            }

            case R.id.buttonUpdate:{
                addData();
                return;
            }
            case R.id.buttonUploadCV:{
                openFileChooser();
                return;
            }
            case R.id.imageView:{
                openImageChooser();
                return;
            }
        }
    }

    private void dateOfBirth() {

        Calendar c= Calendar.getInstance();
        final int dd,mm,yy;
        dd=c.get(Calendar.DAY_OF_MONTH);
        mm=c.get(Calendar.MONTH);
        yy=c.get(Calendar.YEAR);

        DatePickerDialog dpd=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                editTextDOB.setText(dayOfMonth+"/"+month+"/"+year);
            }
        },dd,mm,yy);

        dpd.updateDate(1980,0,1);
        dpd.show();
    }

    private void addData(){
        final FirebaseUser user = mAuth.getCurrentUser();
        String displayName=editTextName.getText().toString().trim();

        if(displayName.isEmpty()){
            editTextName.setError("Name Required");
            editTextName.requestFocus();
            return;
        }
        if(user!=null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(displayName).build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            });
        }
        boolean bol=radioMale.isChecked();
        if(bol)
            gender="Male";
        else
            gender="Female";


        User user_=new User(editTextName.getText().toString(),editTextMobile.getText().toString(),
                editTextAddress.getText().toString(),editTextPin.getText().toString(),
                editTextDegree.getText().toString(),editTextCurrentCity.getText().toString(),gender,editTextDOB.getText().toString(),jobApplied,cv,imageURL);
        mDatabaseRef.child(user.getUid()).setValue(user_);
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(intent,"Upload your CV"), PICK_PDF_REQUEST);
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Upload your profile pic"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            pdfUri = data.getData();
            Log.d("pdfUri",pdfUri.getPath());
            String fileName=mAuth.getCurrentUser().getEmail();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            storageReference.child("uploadCV").child(fileName).putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                            Map<String,Object> updateValues=new HashMap<>();
                            updateValues.put("/"+mAuth.getCurrentUser().getUid()+"/cv",uri.toString());
                            databaseReference.updateChildren(updateValues);
                            Toast.makeText(getContext(),"CV Upload Success",Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),"CV Upload Failed",Toast.LENGTH_SHORT).show();
                }
            });

        }
        else if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri=data.getData();
            String fileName=mAuth.getCurrentUser().getEmail();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            storageReference.child("uploadProfilePic").child(fileName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                            Map<String,Object> updateValues=new HashMap<>();
                            updateValues.put("/"+mAuth.getCurrentUser().getUid()+"/imageURL",uri.toString());
                            databaseReference.updateChildren(updateValues);
                            Picasso.with(getActivity()).load(imageUri).into(imageViewDP);
                            Toast.makeText(getContext(),"Pic Upload Success",Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),"Pic Upload Failed",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}