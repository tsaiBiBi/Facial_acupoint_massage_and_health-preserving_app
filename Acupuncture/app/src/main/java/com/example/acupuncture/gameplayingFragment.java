package com.example.acupuncture;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dataclass.Que;
import com.example.webservice.Questions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class gameplayingFragment extends Fragment {
    private TextView countLabel;
    private TextView questionLabel;
    private Button answerBtn1;
    private Button answerBtn2;
    private Button answerBtn3;
    private Button answerBtn4;
    Button Nowchoies;


    private String rightAnswer;
    private int rightAnswerCount = 0;
    private int quizCount = 1;
    private List<String> wronglist = new ArrayList<>();
    private List<String> parsinglist = new ArrayList<>();
    private List<String> choice = new ArrayList<>();
    static final private int QUIZ_COUNT = 10;
    public static List<Que> questions = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.fragment_gameplaying, container, false);

        questions = (List<Que>) getArguments().getSerializable("Questions");


        countLabel = v.findViewById(R.id.countLabel);
        questionLabel = v.findViewById(R.id.questionLabel);
        answerBtn1 = v.findViewById(R.id.answerBtn1);
        answerBtn2 = v.findViewById(R.id.answerBtn2);
        answerBtn3 = v.findViewById(R.id.answerBtn3);
        answerBtn4 = v.findViewById(R.id.answerBtn4);



        answerBtn1.setOnClickListener(whichdown);
        answerBtn2.setOnClickListener(whichdown);
        answerBtn3.setOnClickListener(whichdown);
        answerBtn4.setOnClickListener(whichdown);
        Log.v("CATEGORY", questions.size() + "start33");

        showNextQuiz();
        return v;
    }


    public void showNextQuiz() {

        String[] totalanswer = new String[questions.size()];
        String[] totalques = new String[questions.size()];

        int j = 0;

        Log.v("CATEGORY", questions.size() + "start2222");

        for (int i = 0; i < questions.size(); i++) {
            if(i == (quizCount-1)){
                totalques[j] = questions.get(i).topic;
                totalanswer[j] = questions.get(i).answer;
                Log.v("CATEGORY",  "i is"+i + "ques woo");
                Log.v("CATEGORY",  "i is"+quizCount + "ques woo");
                questionLabel.setText(totalques[j]);
                //存正確答案
                rightAnswer=totalanswer[j];

                //將四個選項存進去 choice 的 array list
                choice.add(questions.get(i).answer);
                choice.add(questions.get(i).select1);
                choice.add(questions.get(i).select2);
                choice.add(questions.get(i).select3);
                Log.v("CATEGORY",  "part1"+choice + "ques woo");

            }

        }
        Log.v("CATEGORY",  "part1"+totalques[j] + "wooo");
        Log.v("CATEGORY",  "part1"+totalanswer[j] + "wooo");



        // Update quizCountLabel.
        countLabel.setText("Q" + quizCount);

        //將 choice 的選項亂數
        Collections.shuffle(choice);

        // Set choices.
        answerBtn1.setText(choice.get(0));
        answerBtn2.setText(choice.get(1));
        answerBtn3.setText(choice.get(2));
        answerBtn4.setText(choice.get(3));

        //清空 choice
        choice.clear();


    }


    private View.OnClickListener whichdown = new View.OnClickListener() {
        String fSymp = "";
        //宣告一個Handler
        Handler myHandler = new Handler();

        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.answerBtn1:
                    Nowchoies = answerBtn1;
                    fSymp = (String) answerBtn1.getText();
                    break;
                case R.id.answerBtn2:
                    Nowchoies = answerBtn2;
                    fSymp = (String) answerBtn2.getText();
                    break;
                case R.id.answerBtn3:
                    Nowchoies = answerBtn3;
                    fSymp = (String) answerBtn3.getText();
                    break;
                case R.id.answerBtn4:
                    Nowchoies = answerBtn4;
                    fSymp = (String) answerBtn4.getText();
                    break;
            }

            if (fSymp == rightAnswer ){
                //正確答案
                Nowchoies.setBackgroundResource(R.drawable.quiz_correct);
                rightAnswerCount++;
            } else {
                //錯誤
                Nowchoies.setBackgroundResource(R.drawable.quiz_wrong);
                //儲存錯誤的題目跟相對應的解析
                wronglist.add((String) questionLabel.getText());
                for (int i = 0; i < questions.size(); i++) {
                    if (i == (quizCount-1)){
                        parsinglist.add(questions.get(quizCount-1).parsing);
                    }
                }
            }



            if (quizCount == 5) {

                // Create new fragment and transaction
                Fragment newfragment = new gameresultFragment();
                Bundle bundle = new Bundle();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, newfragment);
                transaction.addToBackStack(null);
                //傳達錯的題目以及解析過去
                bundle.putInt("Integer", rightAnswerCount);
                bundle.putStringArrayList("Wronglist", (ArrayList<String>) wronglist);
                bundle.putStringArrayList("Parsinglist", (ArrayList<String>) parsinglist);
                Log.v("CATEGORY", wronglist + "2lalala2");
                Log.v("CATEGORY", parsinglist + "2lalala2");
                newfragment.setArguments(bundle);
                transaction.commit();

            } else {
                quizCount++;
                //幾秒後(delaySec)呼叫runTimerStop這個Runnable，再由這個Runnable去呼叫你想要做的事情
                myHandler.postDelayed(runTimerStop,100);
                showNextQuiz();
                Log.v("CATEGORY",  "part"+ quizCount + "qqq");

            }

        }
    };
    //主體
    private Runnable runTimerStop = new Runnable()
    {
        @Override
        public void run()
        {
            //想要做的事情
            Nowchoies.setBackgroundResource(R.drawable.quiz_nomal);
        }
    };


}