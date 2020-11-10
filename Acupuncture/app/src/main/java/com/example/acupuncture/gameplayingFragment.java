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
    private int test = 0;
    static final private int QUIZ_COUNT = 5;



    public static List<Que> questions = new ArrayList<>();

    ArrayList<ArrayList<String>> quizArray = new ArrayList<>();
    String quizData[][] = {
            // {"Country", "Right Answer", "Choice1", "Choice2", "Choice3"}
            {"請問哪一個穴道可以眼睛保健?", "承泣穴", "印堂穴", "下關穴", "絲竹空"},
            {"請問哪一個穴道無法 \n達到臉部美容的效果?", "水溝穴", "顴髎穴", "陽白穴", "瞳子膠"},
            {"Indonesia", "Jakarta", "Manila", "New Delhi", "Kuala Lumpur"},
            {"Japan", "Tokyo", "Bangkok", "Taipei", "Jakarta"},
            {"Thailand", "Bangkok", "Berlin", "Havana", "Kingston"},
            {"Brazil", "Brasilia", "Havana", "Bangkok", "Copenhagen"},
            {"Canada", "Ottawa", "Bern", "Copenhagen", "Jakarta"},
            {"Cuba", "Havana", "Bern", "London", "Mexico City"},
            {"Mexico", "Mexico City", "Ottawa", "Berlin", "Santiago"},
            {"United States", "Washington D.C.", "San Jose", "Buenos Aires", "Kuala Lumpur"},
            {"France", "Paris", "Ottawa", "Copenhagen", "Tokyo"},
            {"Germany", "Berlin", "Copenhagen", "Bangkok", "Santiago"},
            {"Italy", "Rome", "London", "Paris", "Athens"},
            {"Spain", "Madrid", "Mexico City", "Jakarta", "Havana"},
            {"United Kingdom", "London", "Rome", "Paris", "Singapore"}
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.fragment_gameplaying, container, false);

        if (!Questions.queIsGotten) {
            Questions.que(getActivity());
        }

        countLabel = v.findViewById(R.id.countLabel);
        questionLabel = v.findViewById(R.id.questionLabel);
        answerBtn1 = v.findViewById(R.id.answerBtn1);
        answerBtn2 = v.findViewById(R.id.answerBtn2);
        answerBtn3 = v.findViewById(R.id.answerBtn3);
        answerBtn4 = v.findViewById(R.id.answerBtn4);


        for (int i = 0; i < quizData.length; i++) {

            // Prepare array.
            ArrayList<String> tmpArray = new ArrayList<>();
            tmpArray.add(quizData[i][0]); // Country
            tmpArray.add(quizData[i][1]); // Right Answer
            tmpArray.add(quizData[i][2]); // Choice1
            tmpArray.add(quizData[i][3]); // Choice2
            tmpArray.add(quizData[i][4]); // Choice3

            // Add tmpArray to quizArray.
            quizArray.add(tmpArray);
            Log.v("CATEGORY", tmpArray + "123");
        }

        answerBtn1.setOnClickListener(whichdown);
        answerBtn2.setOnClickListener(whichdown);
        answerBtn3.setOnClickListener(whichdown);
        answerBtn4.setOnClickListener(whichdown);

        showNextQuiz();

        return v;
    }
    public static void quesMap(int num, String topic,String answer,String select1,String select2,String select3,String parsing) {
        // 將資料以穴道為單位分類
        Que nowQue = new Que(num, topic, answer, select1, select2, select3, parsing);
        questions.add(nowQue);

    }


    public void showNextQuiz() {

        String[] totalques = new String[questions.size()];
        String[] totalanswer = new String[questions.size()];

        int j = 0;


        Log.v("CATEGORY", test + "testwooo");
        Log.v("CATEGORY", questions.size() + "quizsizewooo");

        for (int i = 0; i < questions.size(); i++) {
            Log.v("CATEGORY",  "i is"+ i + "wooo");
            if(i == test){
                totalques[j] = questions.get(i).topic;
                totalanswer[j] = questions.get(i).answer;
                Log.v("CATEGORY",  "part1"+totalques[j] + "wooo");
                questionLabel.setText(totalques[j]);
                rightAnswer=totalanswer[j];
                answerBtn2.setText(totalanswer[j]);
            }

        }
        test++;

        // Update quizCountLabel.
        countLabel.setText("Q" + quizCount);


        // Generate random number between 0 and 14 (quizArray's size - 1)
        Random random = new Random();
        int randomNum = random.nextInt(quizArray.size());

        // Pick one quiz set.
        ArrayList<String> quiz = quizArray.get(randomNum);

//        questionLabel.setText(quiz.get(0));
//        rightAnswer = quiz.get(1);


        // Remove "Country" from quiz and Shuffle choices.
        quiz.remove(0);
        Collections.shuffle(quiz);

        // Set choices.
        answerBtn1.setText(quiz.get(0));
//        answerBtn2.setText(quiz.get(1));
        answerBtn3.setText(quiz.get(2));
        answerBtn4.setText(quiz.get(3));



        quizArray.remove(randomNum);

        
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
                    //Toast.makeText(getActivity(), fSymp, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.answerBtn2:
                    Nowchoies = answerBtn2;
                    fSymp = (String) answerBtn2.getText();
                    //Toast.makeText(getActivity(), fSymp, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.answerBtn3:
                    Nowchoies = answerBtn3;
                    fSymp = (String) answerBtn3.getText();
                    //Toast.makeText(getActivity(), fSymp, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.answerBtn4:
                    Nowchoies = answerBtn4;
                    fSymp = (String) answerBtn4.getText();
                    //Toast.makeText(getActivity(), fSymp, Toast.LENGTH_SHORT).show();
                    break;
            }


            if (fSymp == rightAnswer ){
                Toast.makeText(getActivity(),"答對囉", Toast.LENGTH_SHORT).show();
                Nowchoies.setBackgroundResource(R.drawable.quiz_correct);

                rightAnswerCount++;
            } else {
                Toast.makeText(getActivity(),"錯了", Toast.LENGTH_SHORT).show();
                Nowchoies.setBackgroundResource(R.drawable.quiz_wrong);


            }



            if (quizCount == QUIZ_COUNT) {

                // Create new fragment and transaction
                Fragment newfragment = new gameresultFragment();
                Bundle bundle = new Bundle();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, newfragment);
                transaction.addToBackStack(null);
                bundle.putInt("Integer", rightAnswerCount);
                newfragment.setArguments(bundle);
                transaction.commit();

            } else {
                quizCount++;
                //Log.v("CATEGORY", rightAnswerCount + "qazqaz");
                //幾秒後(delaySec)呼叫runTimerStop這個Runnable，再由這個Runnable去呼叫你想要做的事情
                myHandler.postDelayed(runTimerStop,100);
                showNextQuiz();
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