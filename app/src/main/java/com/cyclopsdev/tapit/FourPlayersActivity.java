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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class FourPlayersActivity extends Activity {
	
	protected int step;
	private RelativeLayout tlLayout, trLayout, blLayout, brLayout;
	private TextView tlText, trText, blText, brText;
	private int tlScore = 0, trScore = 0, blScore = 0, brScore = 0;
	private int tlColor = Color.GREEN, trColor= Color.RED, blColor = Color.YELLOW, brColor = Color.CYAN;
    private TextView timeText;
    private Chronometer chronometer;
    private CountDownTimer countDownTimer;
    private long startTime, endTime;
    private int[] configuration;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_four_players);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            getActionBar().hide();
        }

		Intent intent = getIntent();
		configuration = intent.getExtras().getIntArray(
				"configuration");

        step = Utils.setStep(configuration);

        timeText = (TextView) findViewById(R.id.timeText4);
        chronometer = (Chronometer) findViewById(R.id.chronometer4);

        tlLayout = (RelativeLayout) findViewById(R.id.topLeft);
		trLayout = (RelativeLayout) findViewById(R.id.topRight);
		blLayout = (RelativeLayout) findViewById(R.id.bottomLeft);
		brLayout = (RelativeLayout) findViewById(R.id.bottomRight);

		tlLayout.setBackgroundColor(tlColor);
		trLayout.setBackgroundColor(trColor);
		blLayout.setBackgroundColor(blColor);
		brLayout.setBackgroundColor(brColor);

		tlText = (TextView) findViewById(R.id.textView1);
		tlText.setText(Integer.toString(tlScore));
		
		trText = (TextView) findViewById(R.id.textView2);
		trText.setText(Integer.toString(trScore));

		blText = (TextView) findViewById(R.id.textView3);
		blText.setText(Integer.toString(blScore));
		
		brText = (TextView) findViewById(R.id.textView4);
		brText.setText(Integer.toString(brScore));

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
                    tlLayout.setClickable(false);
                    trLayout.setClickable(false);
                    blLayout.setClickable(false);
                    brLayout.setClickable(false);
                    endDialog();
                }

            }.start();
        }

		tlLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tlScore++;
				tlText.setText(Integer.toString(tlScore));

                tlColor = Utils.nextColor(tlColor, step);
				tlLayout.setBackgroundColor(tlColor);

                checkEnd(tlScore);
			}
		});
		
		trLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				trScore++;
				trText.setText(Integer.toString(trScore));

                trColor = Utils.nextColor(trColor, step);
				trLayout.setBackgroundColor(trColor);

                checkEnd(trScore);
            }
		});

		blLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				blScore++;
				blText.setText(Integer.toString(blScore));

                blColor = Utils.nextColor(blColor, step);
				blLayout.setBackgroundColor(blColor);

                checkEnd(blScore);
            }
		});

		brLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				brScore++;
				brText.setText(Integer.toString(brScore));

                brColor = Utils.nextColor(brColor, step);
				brLayout.setBackgroundColor(brColor);

                checkEnd(brScore);
			}
		});
	}

    private void checkEnd(int score){
        if (configuration[0] == 1 && score == configuration[1]) {
            chronometer.stop();
            endTime = System.currentTimeMillis();
            tlLayout.setClickable(false);
            trLayout.setClickable(false);
            blLayout.setClickable(false);
            brLayout.setClickable(false);
            endDialog();
        }
    }

    private void endDialog() {
        final String title, message, result;
        if (configuration[0] == 0) {
            title = getText(R.string.dialog_title_time).toString();
            switch (max(tlScore,trScore,blScore,brScore)){
                case 0:
                    result = getText(R.string.green_player).toString();
                    break;
                case 1:
                    result = getText(R.string.red_player).toString();
                    break;
                case 2:
                    result = getText(R.string.yellow_player).toString();
                    break;
                case 3:
                    result = getText(R.string.blue_player).toString();
                    break;
                default:
                    result = getText(R.string.tie).toString();
                    break;
            }

            message = result + "\n"
                    + getText(R.string.final_score) + "\n"
                    + tlText.getText().toString() + " - "
                    + trText.getText().toString() +"\n"
                    + blText.getText().toString() + " - "
                    + brText.getText().toString() +"\n"
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
            if (tlScore == configuration[1]){
                result = getText(R.string.green_player).toString();
            } else if (trScore == configuration[1]){
                result = getText(R.string.red_player).toString();
            } else if (blScore == configuration[1]){
                result = getText(R.string.yellow_player).toString();
            } else if (brScore == configuration[1]){
                result = getText(R.string.blue_player).toString();
            } else {
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
                                        FourPlayersActivity.this,
                                        GameActivity.class);
                                int[] config = { 4, configuration[0],
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

    private int max (int first, int second, int third, int fourth){

        List<Integer> list = Arrays.asList(first, second, third,
                fourth);
        int largest = Collections.max(list);
        int frequency = Collections.frequency(list, largest);

        if (frequency > 1){
            return 4;
        } else if (largest == first) {
            return 0;
        } else if (largest == second) {
            return 1;
        } else if (largest == third) {
            return 2;
        } else if (largest == fourth){
            return 3;
        } else {
            return 4;
        }
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
