package com.example.myapplication;

import android.app.DatePickerDialog;
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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private EditText editTextName,editTextMobile,editTextAddress,editTextPin,editTextDOB,editTextCurrentCity,editTextDegree;
    private Button buttonUpdate;
    private RadioButton radioMale,radioFemale;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private String gender;



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

        radioMale=(RadioButton)view.findViewById(R.id.radioMale);
        radioFemale=(RadioButton)view.findViewById(R.id.radioFemale);


        buttonUpdate=(Button)view.findViewById(R.id.buttonUpdate);

        progressBar=(ProgressBar)view.findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                        if(user.getGender().equals("Male")){
                            radioMale.setChecked(true);
                        }else{
                            radioFemale.setChecked(true);
                        }


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
                editTextDegree.getText().toString(),editTextCurrentCity.getText().toString(),gender,editTextDOB.getText().toString());
        String uploadId = mDatabaseRef.push().getKey();
        mDatabaseRef.child(user.getUid()).setValue(user_);
    }

}