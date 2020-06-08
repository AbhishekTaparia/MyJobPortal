package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;

public class JobAdapter extends ArrayAdapter<Job>  {

    private ArrayList<Job> jobs;
    private Context context;

    public JobAdapter(@NonNull Context context, int resource, ArrayList<Job> jobs) {
        super(context, 0,jobs);
        this.jobs = jobs;
        this.context=context;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        Job job=jobs.get(i);
        if(view==null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
            view=layoutInflater.inflate(R.layout.job_item, null);
            TextView textJobPosition=view.findViewById(R.id.textJobPosition);
            TextView textCompany=view.findViewById(R.id.textCompany);
            TextView textLocation=view.findViewById(R.id.textLocation);
            Button buttonApply=view.findViewById(R.id.buttonApply);
            textCompany.setText(job.getCompany());
            textJobPosition.setText(job.getJobPosition());
            textLocation.setText(job.getLocation());
            buttonApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ApplyJobFragment applyJobFragment =new ApplyJobFragment();
                    Bundle bundle = new Bundle();
//                    int pos=(Integer) view.getTag();
//                    Object object=getItem(pos);
//                    Job job=(Job)object;
                    Job job=jobs.get(i);
                    bundle.putString("company",job.getCompany());
                    bundle.putString("location",job.getLocation());
                    bundle.putString("jobPosition",job.getJobPosition());
                    bundle.putString("jobDesc",job.getJobDesc());
                    bundle.putString("companyDesc",job.getCompanyDesc());
                    bundle.putString("salary",job.getSalary());
                    bundle.putString("key",job.getKey());

                    applyJobFragment.setArguments(bundle);

                    ((FragmentActivity) context)
                            .getSupportFragmentManager()
                            .beginTransaction().replace(R.id.frame, applyJobFragment)
                            .addToBackStack(null).commit();
                }
            });
        }
        return view;
    }


}
