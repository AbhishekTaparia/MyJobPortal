package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


public class ApplyJobFragment extends Fragment {

    private TextView textCompany;
    private TextView textJobPosition;
    private TextView textJobDesc;
    private TextView textCompanyDesc;
    private TextView textLocation;
    private TextView textSalary;
    private Button buttonApply;

    private FirebaseAuth mAuth;



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
                    Log.d("check","profile");
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
                else {
                    Log.d("check","no profile");
                }
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

        }


        return view;
    }

}