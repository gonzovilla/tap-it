package com.cyclopsdev.tapit;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyclopsdev.tapit.support.Utils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TwoPlayersActivity extends Activity {

	private int step;
	private RelativeLayout tLayout, bLayout;
	private TextView tText, bText;
	private int tScore = 0, bScore = 0;
	private int tColor = Color.RED, bColor = Color.GREEN;
    private TextView timeText;
    private Chronometer chronometer;
    private CountDownTimer countDownTimer;
    private long startTime, endTime;
    private int[] configuration;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_two_players);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            getActionBar().hide();
        }

		Intent intent = getIntent();
		configuration = intent.getExtras().getIntArray(
				"configuration");

        step = Utils.setStep(configuration);

        timeText = (TextView) findViewById(R.id.timeText2);
        chronometer = (Chronometer) findViewById(R.id.chronometer2);

        tLayout = (RelativeLayout) findViewById(R.id.top);
        bLayout = (RelativeLayout) findViewById(R.id.bottom);

        tLayout.setBackgroundColor(tColor);
        bLayout.setBackgroundColor(bColor);

        tText = (TextView) findViewById(R.id.textView1);
        tText.setText(Integer.toString(tScore));

        bText = (TextView) findViewById(R.id.textView2);
        bText.setText(Integer.toString(bScore));

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
                    tLayout.setClickable(false);
                    bLayout.setClickable(false);
                    endDialog();
                }

            }.start();
        }

		tLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tScore++;
				tText.setText(Integer.toString(tScore));

                tColor = Utils.nextColor(tColor, step);
				tLayout.setBackgroundColor(tColor);

                checkEnd(tScore);
			}
		});

		bLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				bScore++;
				bText.setText(Integer.toString(bScore));

                bColor = Utils.nextColor(bColor, step);
                bLayout.setBackgroundColor(bColor);

                checkEnd(bScore);
			}
		});

	}

    private void checkEnd(int score){
        if (configuration[0] == 1 && score == configuration[1]) {
            chronometer.stop();
            endTime = System.currentTimeMillis();
            tLayout.setClickable(false);
            bLayout.setClickable(false);
            endDialog();
        }
    }

    private void endDialog() {
        final String title, message, result;
        if (configuration[0] == 0) {
            title = getText(R.string.dialog_title_time).toString();
            if(tScore>bScore) {
                result = getText(R.string.red_player).toString();
            } else if (tScore<bScore){
                result = getText(R.string.green_player).toString();
            } else{
                result = getText(R.string.tie).toString();
            }
            message = result + "\n"
                    + getText(R.string.final_score) + " "
                    + tText.getText().toString() + " - "
                    + bText.getText().toString() +"\n"
                    + getText(R.string.play_again);
        } else {
            Long totalTime = endTime - startTime - 1;
            SimpleDateFormat sdf;
            if (totalTime > 60000) // at least 1 minute
                sdf = new SimpleDateFormat("m' min. 's.S", Locale.getDefault());
            else
                sdf = new SimpleDateFormat("s.S", Locale.getDefault());
            Date resultDate = new Date(totalTime);
            title = getText(R.string.dialog_title_taps).toString();
            if(tScore == configuration[1]){
                result = getText(R.string.red_player).toString();
            } else if (bScore == configuration[1]){
                result = getText(R.string.green_player).toString();
            } else{
                result = getText(R.string.tie).toString();
            }
            message = result + "\n"
                    + getText(R.string.dialog_message_taps)
                    + sdf.format(resultDate) + getText(R.string.sec) + "\n"
                    + getText(R.string.play_again);
        }

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent gameIntent = new Intent(
                                        TwoPlayersActivity.this,
                                        GameActivity.class);
                                int[] config = { 2, configuration[0],
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
