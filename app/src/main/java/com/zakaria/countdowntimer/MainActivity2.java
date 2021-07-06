package com.zakaria.countdowntimer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    //2 calss is working good ===========================================================================================================================================================


        private static final long START_TIME_IN_MILLIS = 15000;
        private TextView mTextViewCountDown;
        private CountDownTimer mCountDownTimer;
        private boolean mTimerRunning;
        private long mTimeLeftInMillis;
        private long mEndTime;
        private long remainingTimeInMillis;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);

            mTextViewCountDown = findViewById(R.id.texview_countdown);
        }

        private void startTimer() {
            mCountDownTimer = new CountDownTimer(remainingTimeInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    remainingTimeInMillis = millisUntilFinished;
                    mTimeLeftInMillis = millisUntilFinished;
                    updateCountDownText();
                }

                @Override
                public void onFinish() {
                    //mTimerRunning = false;
                    //updateButtons();

                    updateCountDownText();
                    resetTimer();
                    startTimer();

                }
            }.start();

            //mTimerRunning = true;

        }


        private void resetTimer() {
            remainingTimeInMillis = START_TIME_IN_MILLIS;
            updateCountDownText();

        }

        private void updateCountDownText() {


            int minutes = (int) (remainingTimeInMillis / 1000) / 60;
            int seconds = (int) (remainingTimeInMillis / 1000) % 60;

            String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

            mTextViewCountDown.setText(timeLeftFormatted);
        }


        @Override
        protected void onStop() {
            super.onStop();

            SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putLong("millisLeft", mTimeLeftInMillis);
            editor.putBoolean("timerRunning", mTimerRunning);
            editor.putLong("endTime", System.currentTimeMillis());
            editor.apply();

        }

        @Override
        protected void onStart() {
            super.onStart();

            SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

            mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
            mTimerRunning = prefs.getBoolean("timerRunning", false);
            mEndTime = prefs.getLong("endTime", 0);
            if (mEndTime == 0L) {
                remainingTimeInMillis = (mTimeLeftInMillis);
            } else {
                Long timeDiff = (mEndTime - System.currentTimeMillis());
                //to convert into positive number
                timeDiff = Math.abs(timeDiff);

                long timeDiffInSeconds = (timeDiff / 1000) % 60;
                long timeDiffInMillis = timeDiffInSeconds * 1000;
                Long timeDiffInMillisPlusTimerRemaining = remainingTimeInMillis = mTimeLeftInMillis - timeDiffInMillis;

                if (timeDiffInMillisPlusTimerRemaining < 0) {
                    timeDiffInMillisPlusTimerRemaining = Math.abs(timeDiffInMillisPlusTimerRemaining);
                    remainingTimeInMillis = START_TIME_IN_MILLIS - timeDiffInMillisPlusTimerRemaining;
                }
            }
            updateCountDownText();
            startTimer();
        }
    }
}