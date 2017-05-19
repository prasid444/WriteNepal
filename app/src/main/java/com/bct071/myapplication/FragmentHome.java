package com.bct071.myapplication;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment {

    private String[] myquotes;
    private static final Random ran=new Random();
    int index=0;
    TextView show;
    public FragmentHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        Resources res=getResources();
        myquotes=res.getStringArray(R.array.quotes);
        index=ran.nextInt(myquotes.length);
        String q=myquotes[index];
        show=(TextView)view.findViewById(R.id.showQuotes);
        show.setText(q);


        return view;
    }

}
