package com.cyclopsdev.tapit.support;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.Random;

/**
 * Created by gonzovilla89 on 01/03/15.
 */
public class Utils {

    public static int setStep (int[] configuration) {

        int step = 0;

        if (configuration[0] == 0) {
            switch (configuration[1]) {
                case 10:
                    step = 2;
                    break;
                case 30:
                    step = 1;
                    break;
                case 60:
                    step = 0;
                    break;
                default:
                    break;
            }
        } else {
            switch (configuration[1]) {
                case 50:
                    step = 5;
                    break;
                case 100:
                    step = 2;
                    break;
                case 200:
                    step = 1;
                    break;
                default:
                    break;
            }
        }
        return step;
    }

    public static int setRandomColor() {

        int randomNum;

        Random rand = new Random();

        randomNum = rand.nextInt(4) + 1;

        switch (randomNum) {
            case 1:
                return Color.RED;
            case 2:
                return Color.YELLOW;
            case 3:
                return Color.GREEN;
            default:
                return Color.CYAN;
        }
    }

    public static int nextColor(int currentColor, int step) {
        int color;
        if (currentColor > Color.CYAN - 255 && currentColor <= Color.CYAN) {
            color = currentColor - step;
        } else {
            color = currentColor + step;
        }
        return color;
    }


    public static void saveToSharedPreferences(Context context, GameProgress gameProgress){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(gameProgress);
        prefsEditor.putString("GameProgress", json);
        prefsEditor.commit();

    }

    public static GameProgress loadFromSharedPreferences(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = preferences.getString("GameProgress", "");
        if (json != "") {
            GameProgress gameProgress = gson.fromJson(json, GameProgress.class);
            return gameProgress;
        } else{
            return null;
        }

    }

}
