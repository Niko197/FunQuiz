package com.kresic.nikola.funquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String SCORE_KEY = "SCORE";
    private static final String INDEX_KEY = "INDEX";

    private TextView tvQuestion;
    private TextView tvQuizStat;
    private ProgressBar pbQuiz;
    private Button btnCorrect;
    private Button btnWrong;
    private int mQuestionIndex;
    private int mQuizQuestion;

    private int mUserScore;

    private QuizModel [] questionCollection = new QuizModel[]{
            new QuizModel(R.string.q1, true),
            new QuizModel(R.string.q2, true),
            new QuizModel(R.string.q3, false),
            new QuizModel(R.string.q4, true),
            new QuizModel(R.string.q5, false),
            new QuizModel(R.string.q6, true),
            new QuizModel(R.string.q7, true),
            new QuizModel(R.string.q8, false),
            new QuizModel(R.string.q9, true),
            new QuizModel(R.string.q10, false),
    };

    final int USER_PROGRESS = (int) Math.round(100.0 / questionCollection.length);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tvQuestion = findViewById(R.id.tv_question);
        tvQuizStat = findViewById(R.id.tv_quiz_stat);

        if (savedInstanceState != null){
            mUserScore = savedInstanceState.getInt(SCORE_KEY);
            mQuestionIndex = savedInstanceState.getInt(INDEX_KEY);
            tvQuizStat.setText(String.valueOf(mUserScore));
        } else {
            mUserScore = 0;
            mQuestionIndex = 0;
        }

        pbQuiz = findViewById(R.id.pb_quiz);

        btnCorrect = findViewById(R.id.btn_correct);
        btnCorrect.setOnClickListener(this);
        btnWrong = findViewById(R.id.btn_wrong);
        btnWrong.setOnClickListener(this);

        QuizModel q1 = questionCollection[mQuestionIndex];
        mQuizQuestion = questionCollection[mQuestionIndex].getQuestion();
        tvQuestion.setText(mQuizQuestion);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_correct:
                evaluateUsersAnswer(true);
                changeQuestionOnButtonClicked();
                break;

            case R.id.btn_wrong:
                evaluateUsersAnswer(false);
                changeQuestionOnButtonClicked();
                break;


        }

    }

    private void changeQuestionOnButtonClicked(){
        mQuestionIndex = (mQuestionIndex + 1) % questionCollection.length;

        if (mQuestionIndex == 0){
            AlertDialog.Builder quizFinishedAlert = new AlertDialog.Builder(this);
            quizFinishedAlert.setTitle(getString(R.string.alert_title))
                    .setCancelable(false)
                    .setMessage(getString(R.string.alert_message_part_1) + mUserScore + "/" + getString(R.string.alert_message_part_2))
                    .setPositiveButton(getString(R.string.positive_button_text), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            quizFinishedAlert.show();
        }

        mQuizQuestion = questionCollection[mQuestionIndex].getQuestion();
        tvQuestion.setText(mQuizQuestion);
        YoYo.with(Techniques.BounceIn)
                .duration(2000)
                .repeat(0)
                .playOn(tvQuestion);

        pbQuiz.incrementProgressBy(USER_PROGRESS);
    }

    private void evaluateUsersAnswer(boolean usersGuess){
        boolean currentQuestionAnswer = questionCollection[mQuestionIndex].isAnswer();
        if (currentQuestionAnswer == usersGuess){
            mUserScore = mUserScore + 1;
            tvQuizStat.setText(String.valueOf(mUserScore));


            YoYo.with(Techniques.FadeIn)
                    .duration(500)
                    .repeat(0)
                    .playOn(tvQuizStat);

        }else
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .repeat(0)
                    .playOn(tvQuizStat);



    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCORE_KEY, mUserScore);
        outState.putInt(INDEX_KEY, mQuestionIndex);

    }
}