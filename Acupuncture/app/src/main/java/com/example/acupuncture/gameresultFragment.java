package com.example.acupuncture;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class gameresultFragment extends Fragment {
    TextView textView1;
    TextView textView2;
    LinearLayout gameend;
    Button back;
    private List<String> wronglist = new ArrayList<>();
    private List<String> parsinglist = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.fragment_gameresult, container, false);

        textView1 = v.findViewById(R.id.textView1);
        textView2 = v.findViewById(R.id.textView2);
        gameend = v.findViewById(R.id.gameend);
        back = v.findViewById(R.id.back);

        int x  =   getArguments().getInt("Integer");//Integer value
        textView1.setText("總分為:"+x+" /10");

        wronglist = getArguments().getStringArrayList("Wronglist");
        parsinglist = getArguments().getStringArrayList("Parsinglist");

        //印出解析
        for (int i = 0; i < wronglist.size(); i++) {
            TextView textView2 = new TextView(getContext());
            textView2.setText(wronglist.get(i));
            textView2.append(":"+parsinglist.get(i));
            gameend.addView(textView2);
        }

        //回到首頁
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new gameFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        return v;
    }

}