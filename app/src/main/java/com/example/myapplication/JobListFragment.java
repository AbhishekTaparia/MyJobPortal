package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class JobListFragment extends Fragment {

    private ListView listView;
    private ProgressBar progressBar;
    private ArrayList<Job> jobs;

    private DatabaseReference mRef;

    private JobAdapter adapter;

    public JobListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_job_list, container, false);
        listView=(ListView)view.findViewById(R.id.listView);


        jobs=new ArrayList<>();

        progressBar=view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        adapter=new JobAdapter(getContext(),R.layout.job_item,jobs);
        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Job job=jobs.get(i);
//                Log.d("dataOnClick",job.getJobPosition());
//                Snackbar.make(view,job.getJobPosition(),Snackbar.LENGTH_LONG)
//                FirebaseAuth.getInstance().signOut();
//                finish();
//                startActivity(new Intent(getContext(), LoginActivity.class));
//                break;
//            }
//        });

        mRef= FirebaseDatabase.getInstance().getReference("jobs");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                jobs.clear();
                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    Job job=snap.getValue(Job.class);
                    job.setKey(snap.getKey());
                    jobs.add(job);
                }
                adapter.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}