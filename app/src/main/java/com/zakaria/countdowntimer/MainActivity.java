package com.zakaria.countdowntimer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static  final  long START_TIME_IN_MILLITS = 1800000;

    private TextView mTextViewTimeDownCounter;
    private Button mStartBtn;
    private Button mResetBtn;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLITS;
    private  long endTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewTimeDownCounter = findViewById(R.id.texview_countdown);
        mStartBtn = findViewById(R.id.button_start_id);
        mResetBtn = findViewById(R.id.button_reset_id);

        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTimerRunning){
                    pauseTimer();
                }else {
                    startTimer();
                }

            }
        });


        mResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();

            }
        });

        updateCountDownText();

    }


    private void pauseTimer() {

        mCountDownTimer.cancel();
        mTimerRunning = false;
        mStartBtn.setText("Start");
        updateButtons();
    }



    //start and pause method
    private void startTimer() {
        endTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mStartBtn.setText("Start");
                updateButtons();


            }
        }.start();

        mTimerRunning = true;
        updateButtons();
    }


    //reset timer
    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLITS;
        updateCountDownText();
        updateButtons();
    }

    private void updateCountDownText() {
        int minuites = (int) (mTimeLeftInMillis / 1000) / 60;
        int second = (int)(mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormated = String.format(Locale.getDefault(), "%02d:%02d", minuites, second);
        mTextViewTimeDownCounter.setText(timeLeftFormated);


    }

    private  void updateButtons(){
//        if(mTimerRunning){
//            mResetBtn.setVisibility(View.INVISIBLE);
//            mStartBtn.setText("Pause");
//        }
//        else {
//            mStartBtn.setText("Start");
//            if(mTimeLeftInMillis < 1000){
//                mStartBtn.setVisibility(View.INVISIBLE);
//            }else {
//                mStartBtn.setVisibility(View.VISIBLE);
//            }
//            if(mTimeLeftInMillis < START_TIME_IN_MILLITS){
//                mResetBtn.setVisibility(View.VISIBLE);
//            }else {
//                mResetBtn.setVisibility(View.INVISIBLE);
//            }
//        }
    }




    //for save state data rotatate data store
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putLong("MillisLeft", mTimeLeftInMillis);
        outState.putBoolean("timerRunning", mTimerRunning);
        outState.putLong("endTime", endTime);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mTimeLeftInMillis = savedInstanceState.getLong("MillisLeft");
        mTimerRunning = savedInstanceState.getBoolean("timerRunning");
        updateCountDownText();
        updateButtons();
        if(mTimerRunning){
            endTime = savedInstanceState.getLong("endTime");
            mTimeLeftInMillis = endTime - System.currentTimeMillis();
            startTimer();

        }
    }
    //end for save state data rotatate data store



    //save data for close activity
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("MillisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", endTime);

        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        mTimeLeftInMillis = prefs.getLong("MillisLeft", START_TIME_IN_MILLITS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        updateButtons();

        if(mTimerRunning){
            endTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = endTime - System.currentTimeMillis();
            if(mTimeLeftInMillis < 0){
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateButtons();
                updateCountDownText();
            }
            else {
                startTimer();
            }
        }
    }

    //save data after close activity
}