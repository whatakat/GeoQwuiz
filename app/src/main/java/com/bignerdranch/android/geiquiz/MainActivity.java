package com.bignerdranch.android.geiquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG ="MainActivity";
    private static final String KEY_INDEX="index";
    private static final int REQUEST_CODE_CHEAT = 0;
    private boolean mIsCheater;


    private int resultS =0;
    private int resultT =0;
    private int resultF =0;

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private Button mCheatButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, false),
            new Question(R.string.question_asia, true)
    };
    private int mCurrentIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle)called");
        setContentView(R.layout.activity_main);

        if(savedInstanceState!=null){
            mCurrentIndex=savedInstanceState.getInt(KEY_INDEX,0);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);


        mTrueButton = (Button) findViewById(R.id.true_button);


        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);

            }
        });
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater=false;
                updateQuestion();
                mTrueButton.setEnabled(true);
                mFalseButton.setEnabled(true);
                resultS++;
                if(resultS==5){
                    mNextButton.setEnabled(false);
                    mPrevButton.setEnabled(false);

                }
            }
        });
        mCheatButton=(Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
               Intent intent = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
               startActivityForResult(intent,REQUEST_CODE_CHEAT);
            }
        });


        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                updateQuestion();
                mTrueButton.setEnabled(false);
                mFalseButton.setEnabled(false);
            }
        });
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if (requestCode==REQUEST_CODE_CHEAT){
            if(data==null){
                return;
            }
            mIsCheater=CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSavedInstanceState");
        savedInstanceState.putInt(KEY_INDEX,mCurrentIndex);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion() {
       // Log.d(TAG, "Updating question text", new Exception());//add our exception for looking Logcat
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }
    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId =0;
        if(mIsCheater){
            messageResId= R.string.judgment_toast;
        }else {
            if (userPressedTrue == answerIsTrue) {
                resultT++;
                messageResId = R.string.correct_toast;

            } else {
                resultF++;
                messageResId = R.string.incorrect_toast;

            }
        }
            if (resultS == 5) {
                Toast.makeText(this, " false: " + resultF + " true: " + resultT, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
            }



        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
    }
}
