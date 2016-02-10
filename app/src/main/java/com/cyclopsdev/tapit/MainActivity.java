package com.cyclopsdev.tapit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyclopsdev.tapit.support.GameProgress;
import com.cyclopsdev.tapit.support.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

	private TextView players, mode, options;
	private Button onePlayer, twoPlayers, fourPlayers;
	private Button timeAttack, tapGo;
	private Button option1, option2, option3;
	private Button play;
	private LinearLayout optionsLayout;
	private RelativeLayout mProgressLayout;
	private ProgressBar mProgress1, mProgress2, mProgress3;
    private Menu mOptionsMenu;
    private SignInButton mSignInButton;

    private GoogleApiClient mGoogleApiClient;
    private GameProgress mGameProgress;

    boolean mExplicitSignOut = false;
    boolean mInSignInFlow = false;

    private boolean mResolvingConnectionFailure = false;
    private boolean mSignInClicked = false;
    private boolean mAutoStartSignInFlow = true;

    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;

    private boolean isSignedIn() {
        return (mGoogleApiClient != null && mGoogleApiClient.isConnected());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mInSignInFlow && !mExplicitSignOut) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isNetworkAvailable()) {
            checkPlayServices();
        }

        mGameProgress = Utils.loadFromSharedPreferences(this);
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
		
		optionsLayout = (LinearLayout) findViewById(R.id.optionsLayout);
		optionsLayout.setVisibility(View.GONE);
		
		setUpMiniGame();	// Progress bars mini-game

		players = (TextView) findViewById(R.id.textView1);
		mode = (TextView) findViewById(R.id.textView2);
		options = (TextView) findViewById(R.id.textView3);

		players.setText(getText(R.string.players));
		mode.setText(getText(R.string.mode));

		onePlayer = (Button) findViewById(R.id.button1);
		twoPlayers = (Button) findViewById(R.id.button2);
		fourPlayers = (Button) findViewById(R.id.button3);

		onePlayer.setPressed(true);

		timeAttack = (Button) findViewById(R.id.button4);
		tapGo = (Button) findViewById(R.id.button5);

		timeAttack.setText(getText(R.string.time_attack));
		tapGo.setText(getText(R.string.tap_go));

		option1 = (Button) findViewById(R.id.button6);
		option2 = (Button) findViewById(R.id.button7);
		option3 = (Button) findViewById(R.id.button8);

		play = (Button) findViewById(R.id.button9);
		play.setText(getText(R.string.play));

		// Keep a button pressed
		onePlayer.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				onePlayer.setPressed(true);
				twoPlayers.setPressed(false);
				fourPlayers.setPressed(false);
				return true;
			}
		});

		twoPlayers.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				onePlayer.setPressed(false);
				twoPlayers.setPressed(true);
				fourPlayers.setPressed(false);
				return true;
			}
		});

		fourPlayers.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				onePlayer.setPressed(false);
				twoPlayers.setPressed(false);
				fourPlayers.setPressed(true);
				return true;
			}
		});

		timeAttack.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				timeAttack.setPressed(true);
				tapGo.setPressed(false);
				options.setText(getText(R.string.seconds));
				option1.setText("10");
				option2.setText("30");
				option3.setText("60");
				optionsLayout.setVisibility(View.VISIBLE);
				return true;
			}
		});

		tapGo.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				timeAttack.setPressed(false);
				tapGo.setPressed(true);
				options.setText(getText(R.string.taps));
				option1.setText("50");
				option2.setText("100");
				option3.setText("200");
				optionsLayout.setVisibility(View.VISIBLE);
				return true;
			}
		});

		option1.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				option1.setPressed(true);
				option2.setPressed(false);
				option3.setPressed(false);
				return true;
			}
		});

		option2.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				option1.setPressed(false);
				option2.setPressed(true);
				option3.setPressed(false);
				return true;
			}
		});

		option3.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				option1.setPressed(false);
				option2.setPressed(false);
				option3.setPressed(true);
				return true;
			}
		});

		play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				boolean ready1 = true, ready2 = true, ready3 = true;

				Intent gameIntent = new Intent(MainActivity.this,
						GameActivity.class);

				int[] configuration = new int[3];

				if (onePlayer.isPressed())
					configuration[0] = 1;
				else if (twoPlayers.isPressed())
					configuration[0] = 2;
				else if (fourPlayers.isPressed())
					configuration[0] = 4;
				else {
					ready1 = false;
					Toast.makeText(getApplicationContext(),
							getText(R.string.players_missing),
							Toast.LENGTH_SHORT).show();
				}

				if (timeAttack.isPressed())
					configuration[1] = 0;
				else if (tapGo.isPressed())
					configuration[1] = 1;
				else {
					ready2 = false;
					Toast.makeText(getApplicationContext(),
							getText(R.string.mode_missing), Toast.LENGTH_SHORT)
							.show();
				}

				if (option1.isPressed())
					configuration[2] = Integer.parseInt(option1.getText()
							.toString());
				else if (option2.isPressed())
					configuration[2] = Integer.parseInt(option2.getText()
							.toString());
				else if (option3.isPressed())
					configuration[2] = Integer.parseInt(option3.getText()
							.toString());
				else if (ready2) {
					ready3 = false;
					if (configuration[1] == 0) {
						Toast.makeText(getApplicationContext(),
								getText(R.string.seconds_missing),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(),
								getText(R.string.taps_missing),
								Toast.LENGTH_SHORT).show();

					}
				}

				if (ready1 && ready2 && ready3) {
					gameIntent.putExtra("configuration", configuration);
					startActivity(gameIntent);
					optionsLayout.setVisibility(View.GONE);
				}
			}
		});

        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                signInClicked();
            }
        });
	}

	private void setUpMiniGame() {
		mProgressLayout = (RelativeLayout) findViewById(R.id.progressLayout);
		mProgress1 = (ProgressBar) findViewById(R.id.progressBar1);
		mProgress2 = (ProgressBar) findViewById(R.id.progressBar2);
		mProgress3 = (ProgressBar) findViewById(R.id.progressBar3);
		
		mProgress1.getIndeterminateDrawable().setColorFilter(0xFF66ccFF,
                android.graphics.PorterDuff.Mode.MULTIPLY);
		mProgress2.getIndeterminateDrawable().setColorFilter(0xFFcc0000,
                android.graphics.PorterDuff.Mode.MULTIPLY);
		mProgress3.getIndeterminateDrawable().setColorFilter(0xFFFFcc00,
                android.graphics.PorterDuff.Mode.MULTIPLY);
		mProgress2.setRotation(180);
		mProgressLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean p1 = (mProgress1.getVisibility() == View.VISIBLE);
				boolean p2 = (mProgress2.getVisibility() == View.VISIBLE);
				boolean p3 = (mProgress3.getVisibility() == View.VISIBLE);
				if (p1 && p2 && p3){
					mProgress2.setVisibility(View.INVISIBLE);
				} else if (p1 && !p2 && p3){
					mProgress2.setVisibility(View.VISIBLE);
					mProgress3.setVisibility(View.INVISIBLE);
				} else if (p1 && p2 && !p3){
					mProgress3.setVisibility(View.VISIBLE);
					mProgress1.setVisibility(View.INVISIBLE);
				} else if (!p1 && p2 && p3){
					mProgress1.setVisibility(View.VISIBLE);
				}
			}
		});
	}


    private void pushGameData(GameProgress gameProgress){
        if (gameProgress.isQuickReactionAchievement()){
            unlockAchievement(getString(R.string.achievement_quick_reaction));
        }
        if (gameProgress.isRightOnTimeAchievement()){
            unlockAchievement(getString(R.string.achievement_right_on_time));
        }
        if (gameProgress.isaFastMinuteAchievement()){
            unlockAchievement(getString(R.string.achievement_a_fast_minute));
        }
        if (gameProgress.isFastestCowboyAchievement()){
            unlockAchievement(getString(R.string.achievement_fastest_cowboy));
        }
        if (gameProgress.iscTapsAchievement()){
            unlockAchievement(getString(R.string.achievement_c_taps));
        }
        if (gameProgress.isTapMasterAchievement()){
            unlockAchievement(getString(R.string.achievement_tap_master));
        }
        if (gameProgress.getTenSecsScore()>0){
            submitScore(getString(R.string.leaderboard_maximum_taps_in_10_seconds), gameProgress.getTenSecsScore());
        }
        if (gameProgress.getThirtySecsScore()>0){
            submitScore(getString(R.string.leaderboard_maximum_taps_in_30_seconds), gameProgress.getThirtySecsScore());
        }
        if (gameProgress.getSixtySecsScore()>0){
            submitScore(getString(R.string.leaderboard_maximum_taps_in_60_seconds), gameProgress.getSixtySecsScore());
        }
        if (gameProgress.getFiftyTapsScore()>0){
            submitScore(getString(R.string.leaderboard_fastest_50_taps), gameProgress.getFiftyTapsScore());
        }
        if (gameProgress.getOneHundredTapsScore()>0){
            submitScore(getString(R.string.leaderboard_fastest_100_taps), gameProgress.getOneHundredTapsScore());
        }
        if (gameProgress.getTwoHundredTapsScore()>0){
            submitScore(getString(R.string.leaderboard_fastest_200_taps), gameProgress.getTwoHundredTapsScore());
        }
    }

    public void unlockAchievement(String achievementId){
        Games.Achievements.unlock(mGoogleApiClient, achievementId);
    }

    public void submitScore(String leaderBoardId, long score){
        Games.Leaderboards.submitScore(mGoogleApiClient, leaderBoardId, score);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        mOptionsMenu = menu;

        if (mSignInClicked){
            mSignInButton.setVisibility(View.GONE);
        } else {
            menu.findItem(R.id.action_logout).setVisible(false);
        }
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_achievements:
                onShowAchievementsRequested();
                return true;
            case R.id.action_leaderboard:
                onShowLeaderboardsRequested();
                return true;
            case R.id.action_logout:
                signOutclicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager CManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NInfo = CManager.getActiveNetworkInfo();
        return (NInfo != null && NInfo.isConnectedOrConnecting());

    }

	private boolean checkPlayServices() {
		GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
		int result = googleAPI.isGooglePlayServicesAvailable(this);
		if(result != ConnectionResult.SUCCESS) {
			int requestCode = 10;
			if(googleAPI.isUserResolvableError(result)) {
				googleAPI.getErrorDialog(this, result,
						requestCode).show();
			}

			return false;
		}

		return true;
	}

    private void signInClicked() {
        mInSignInFlow = true;
        mSignInClicked = true;
        mGoogleApiClient.connect();
    }

    private void signOutclicked() {

        mSignInButton.setVisibility(View.VISIBLE);

        if (mOptionsMenu != null) {
            mOptionsMenu.findItem(R.id.action_logout).setVisible(false);
        }

        mSignInClicked = false;
        mExplicitSignOut = true;

        if (isSignedIn()) {
            Games.signOut(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
    }

    public void onShowAchievementsRequested() {
        if (isSignedIn()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient),
                    RC_UNUSED);
        } else {
            BaseGameUtils.makeSimpleDialog(this, getString(R.string.achievements_not_available)).show();
        }
    }

    public void onShowLeaderboardsRequested() {
        if (isSignedIn()) {
            startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(mGoogleApiClient),
                    RC_UNUSED);
        } else {
            BaseGameUtils.makeSimpleDialog(this, getString(R.string.leaderboards_not_available)).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RC_SIGN_IN) {
            mInSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                BaseGameUtils.showActivityResultError(this, requestCode, resultCode, R.string.signin_other_error);
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        mInSignInFlow = false;
        mSignInButton.setVisibility(View.GONE);

        if (mOptionsMenu != null) {
            mOptionsMenu.findItem(R.id.action_logout).setVisible(true);
        }

        if (mGameProgress != null && isSignedIn()){
            Log.wtf("ABOUT TO", "PUSH DATA");
            pushGameData(mGameProgress);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            return;
        }

        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mInSignInFlow = false;
            mResolvingConnectionFailure = true;
            if (!BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, getString(R.string.signin_other_error))) {
                mResolvingConnectionFailure = false;
            }
        }

        mSignInButton.setVisibility(View.VISIBLE);

        if (mOptionsMenu != null) {
            mOptionsMenu.findItem(R.id.action_logout).setVisible(false);
        }

    }
}