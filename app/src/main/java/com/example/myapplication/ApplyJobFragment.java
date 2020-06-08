package com.example.myapplication;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class ApplyJobFragment extends Fragment {

    private TextView textCompany;
    private TextView textJobPosition;
    private TextView textJobDesc;
    private TextView textCompanyDesc;
    private TextView textLocation;
    private TextView textSalary;
    private Button buttonApply;

    private String key;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;

    private User user;



    public ApplyJobFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_apply_job, container, false);
        mAuth=FirebaseAuth.getInstance();




        textCompany=(TextView)view.findViewById(R.id.textCompany);
        textCompanyDesc=(TextView)view.findViewById(R.id.textCompanyDescription);
        textJobPosition=(TextView)view.findViewById(R.id.textJobPosition);
        textJobDesc=(TextView)view.findViewById(R.id.textJobDescription);
        textSalary=(TextView)view.findViewById(R.id.textSalaryAmount);
        textLocation=(TextView)view.findViewById(R.id.textLocation);

        buttonApply=(Button)view.findViewById(R.id.buttonApply2);

        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=mAuth.getInstance().getCurrentUser().getDisplayName();
                if (name == null || name.isEmpty()) {
                    ProfileFragment profileFragment = new ProfileFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("profileAdded",false);
                    profileFragment.setArguments(bundle);
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction().replace(R.id.frame, profileFragment)
                            .commit();
                    return ;
                }
                Map<String,Object> updateValues=new HashMap<>();
                updateValues.put("/"+mAuth.getCurrentUser().getUid()+"/jobApplied",user.getJobApplied().toString()+","+key);
                mDatabaseRef.updateChildren(updateValues);
            }
        });

        Bundle bundle=getArguments();
        if(bundle!=null){
            String company=bundle.getString("company");
            Log.d("In Profile",company);
            textLocation.setText(bundle.getString("location"));
            textCompany.setText(bundle.getString("company"));
            textCompanyDesc.setText(bundle.getString("companyDesc"));
            textJobPosition.setText(bundle.getString("jobPosition"));
            textJobDesc.setText(bundle.getString("jobDesc"));
            textSalary.setText("Rs. "+bundle.getString("salary"));
            key=bundle.getString("key");
            Log.d("key",key);

        }

//        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
//        String x=mDatabaseRef.child(mAuth.getCurrentUser().getUid().toString()).child("jobApplied").push().get;
//        Log.d("Jobs Applied",);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    if (snap.getKey().equals(mAuth.getCurrentUser().getUid())) {
                        user = snap.getValue(User.class);
                        String appliedJobs[]=user.getJobApplied().toString().split(",");
                        if(Arrays.asList(appliedJobs).contains(key)){
                            if(isAdded()) {
                                buttonApply.setBackgroundColor(getResources().getColor(R.color.lightblue));
                            }
                            buttonApply.setText("Applied");
                            buttonApply.setEnabled(false);

                        }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        return view;
    }

}