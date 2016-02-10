package com.cyclopsdev.tapit;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyclopsdev.tapit.support.GameProgress;
import com.cyclopsdev.tapit.support.Utils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class OnePlayerActivity extends Activity {

	private int step;
	private RelativeLayout mLayout;
	private TextView mText, timeText;
	private int mScore = 0;
    private long mTotalTime;
	private int mColor = Utils.setRandomColor();
	private CountDownTimer countDownTimer;
    private Chronometer chronometer;
	private long startTime, endTime;
	private int[] configuration;
    GameProgress mGameProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_one_player);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            getActionBar().hide();
        }

        mGameProgress = Utils.loadFromSharedPreferences(this);
        if (mGameProgress == null){
            mGameProgress = new GameProgress();
        }

		Intent intent = getIntent();
		configuration = intent.getExtras().getIntArray("configuration");

		mLayout = (RelativeLayout) findViewById(R.id.relativeLayout1);

		mLayout.setBackgroundColor(mColor);

        step = Utils.setStep(configuration);

		mText = (TextView) findViewById(R.id.textView);
		mText.setText(Integer.toString(mScore));

		timeText = (TextView) findViewById(R.id.timeText1);
        chronometer = (Chronometer) findViewById(R.id.chronometer1);

		if (configuration[0] == 1) {
			timeText.setVisibility(View.GONE);
			startTime = System.currentTimeMillis();
            chronometer.start();
        } else {
            chronometer.setVisibility(View.GONE);
            countDownTimer = new CountDownTimer(
					(configuration[1] * 1000) + 100, 1000) {

				public void onTick(long millisUntilFinished) {
					timeText.setText("" + millisUntilFinished / 1000);
				}

				public void onFinish() {
					if (countDownTimer != null) {
						countDownTimer.cancel();
						countDownTimer = null;
					}
					timeText.setText("0");
					mLayout.setClickable(false);
					endDialog();
				}

			}.start();
		}

		mLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mScore++;
				mText.setText(Integer.toString(mScore));

				mColor = Utils.nextColor(mColor, step);
				mLayout.setBackgroundColor(mColor);

				if (configuration[0] == 1 && mScore == configuration[1]) {
                    chronometer.stop();
					endTime = System.currentTimeMillis();
					mLayout.setClickable(false);
					endDialog();
				}
			}
		});
	}

	private void endDialog() {
		final String title, message;
		if (configuration[0] == 0) {
			title = getText(R.string.dialog_title_time).toString();
			message = getText(R.string.dialog_message_time)
					+ mText.getText().toString() + "\n"
					+ getText(R.string.try_again);
		} else {
			mTotalTime = endTime - startTime - 1;
			SimpleDateFormat sdf;
			if (mTotalTime > 60000) // at least 1 minute
				sdf = new SimpleDateFormat("m' min. 's.S", Locale.getDefault());
			else
				sdf = new SimpleDateFormat("s.S", Locale.getDefault());
			Date resultDate = new Date(mTotalTime);
			title = getText(R.string.dialog_title_taps).toString();
			message = getText(R.string.dialog_message_taps)
					+ sdf.format(resultDate) + getText(R.string.sec) + "\n"
					+ getText(R.string.try_again);
		}

        getLeaderboardsAndAchievements();

        final AlertDialog dialog = new AlertDialog.Builder(this)
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Intent gameIntent = new Intent(
										OnePlayerActivity.this,
										GameActivity.class);
								int[] config = { 1, configuration[0],
										configuration[1] };
								gameIntent.putExtra("configuration", config);
								startActivity(gameIntent);
								finish();
							}
						})
				.setNegativeButton(R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}
						}).show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.GONE);
        dialog.setCanceledOnTouchOutside(false);

        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                dialog.setMessage(Long.toString((millisUntilFinished / 1000) + 1));
            }

            @Override
            public void onFinish() {
                dialog.setMessage(message);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.VISIBLE);
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.VISIBLE);            }
        }.start();
	}

    private void getLeaderboardsAndAchievements() {
        long score;
        if (configuration[0] == 0){

            score = Long.parseLong(mText.getText().toString());

            if (configuration[1] == 10){
                mGameProgress.setTenSecsScore(score);
                if (score >= 130){
                    mGameProgress.setQuickReactionAchievement(true);
                }

            } else if (configuration [1] == 30){
                mGameProgress.setThirtySecsScore(score);
                if (score >= 270){
                    mGameProgress.setRightOnTimeAchievement(true);
                }

            } else {
                mGameProgress.setSixtySecsScore(score);
                if (score >= 400){
                    mGameProgress.setaFastMinuteAchievement(true);
                }

            }
        } else {

            score = mTotalTime;

            if (configuration[1] == 50){
                mGameProgress.setFiftyTapsScore(score);
                if (score <= 4){
                    mGameProgress.setFastestCowboyAchievement(true);
                }
            } else if (configuration [1] == 100){
                mGameProgress.setOneHundredTapsScore(score);
                if (score <= 8){
                    mGameProgress.setcTapsAchievement(true);
                }
            } else {
                mGameProgress.setTwoHundredTapsScore(score);
                if (score <= 20){
                    mGameProgress.setTapMasterAchievement(true);
                }
            }
        }

        Utils.saveToSharedPreferences(this, mGameProgress);

    }

    @Override
	public void onBackPressed() {
		if (countDownTimer != null) {
			countDownTimer.cancel();
			countDownTimer = null;
		}
        if (chronometer != null){
            chronometer.stop();
            chronometer = null;
        }
		super.onBackPressed();
	}
}
