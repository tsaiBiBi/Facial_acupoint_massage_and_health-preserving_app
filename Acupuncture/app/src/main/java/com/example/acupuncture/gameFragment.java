package com.example.acupuncture;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dataclass.Que;
import com.example.webservice.Questions;
import com.example.webservice.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class gameFragment extends Fragment {
    Button start;
    public static List<Que> questions = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.fragment_game, container, false);


        if (!Questions.queIsGotten) {
            Questions.que(getActivity());
        }
        start = v.findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new gameplayingFragment();
                Bundle bundle = new Bundle();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, fragment);
                //將 資料表整個傳過去下一個 fragment
                bundle.putSerializable("Questions", (Serializable)questions);
                fragment.setArguments(bundle);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
        return v;
    }
    public static void quesMap(int num,int id, String topic,String answer,String select1,String select2,String select3,String parsing) {
        // 將資料以穴道為單位分類
        Que nowQue = new Que(num,id, topic, answer, select1, select2, select3, parsing);
        questions.add(nowQue);
//        for(int i = 0; i< questions.size();i++){
//            Log.e("www-i", String.valueOf(i));
//            Log.v("www",questions.get(i).topic);
//        }
    }
    @Override
    public void onResume() {
//        Log.e("DEBUG", "onResume of HomeFragment");
        super.onResume();
        Questions.que(getActivity());
    }

}