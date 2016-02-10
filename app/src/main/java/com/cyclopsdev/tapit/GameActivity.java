package com.cyclopsdev.tapit;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

public class GameActivity extends Activity {

	private TextView countdown;
	private int[] config = new int[2];
	private CountDownTimer countDownTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game);
		ActionBar actionBar = getActionBar();
		if (actionBar!=null)
			actionBar.hide();

		countdown = (TextView) findViewById(R.id.countdown);

		Intent intent = getIntent();
		final int[] configuration = intent.getExtras().getIntArray(
				"configuration");
		if (configuration!=null) {
			config[0] = configuration[1];
			config[1] = configuration[2];
		}

		countDownTimer = new CountDownTimer(3100, 1000) {

			public void onTick(long millisUntilFinished) {
				countdown.setText("" + millisUntilFinished / 1000);
			}

			public void onFinish() {
				if (countDownTimer != null) {
					countDownTimer.cancel();
					countDownTimer = null;
				}
				switch (configuration[0]) {
				case 1:
					Intent oneIntent = new Intent(GameActivity.this,
							OnePlayerActivity.class);
					oneIntent.putExtra("configuration", config);
					startActivity(oneIntent);
					break;
				case 2:
					Intent twoIntent = new Intent(GameActivity.this,
							TwoPlayersActivity.class);
					twoIntent.putExtra("configuration", config);
					startActivity(twoIntent);
					break;
				case 4:
					Intent fourIntent = new Intent(GameActivity.this,
							FourPlayersActivity.class);
					fourIntent.putExtra("configuration", config);
					startActivity(fourIntent);
					break;
				default:
					break;
				}
				finish();
			}
		}.start();

	}

	@Override
	public void onBackPressed() {
		if (countDownTimer != null) {
			countDownTimer.cancel();
			countDownTimer = null;
		}
		super.onBackPressed();
	}
}