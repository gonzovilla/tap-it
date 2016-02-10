package com.cyclopsdev.tapit.support;

/**
 * Created by gonzovilla89 on 12/03/15.
 */
public class GameProgress {


    private boolean quickReactionAchievement = false;
    private boolean rightOnTimeAchievement = false;
    private boolean aFastMinuteAchievement = false;
    private boolean fastestCowboyAchievement = false;
    private boolean cTapsAchievement = false;
    private boolean tapMasterAchievement = false;

    private long tenSecsScore = 0;
    private long thirtySecsScore = 0;
    private long sixtySecsScore = 0;
    private long fiftyTapsScore = 0;
    private long oneHundredTapsScore = 0;
    private long twoHundredTapsScore = 0;

    public boolean isQuickReactionAchievement() {
        return quickReactionAchievement;
    }

    public void setQuickReactionAchievement(boolean quickReactionAchievement) {
        this.quickReactionAchievement = quickReactionAchievement;
    }

    public boolean isRightOnTimeAchievement() {
        return rightOnTimeAchievement;
    }

    public void setRightOnTimeAchievement(boolean rightOnTimeAchievement) {
        this.rightOnTimeAchievement = rightOnTimeAchievement;
    }

    public boolean isaFastMinuteAchievement() {
        return aFastMinuteAchievement;
    }

    public void setaFastMinuteAchievement(boolean aFastMinuteAchievement) {
        this.aFastMinuteAchievement = aFastMinuteAchievement;
    }

    public boolean isFastestCowboyAchievement() {
        return fastestCowboyAchievement;
    }

    public void setFastestCowboyAchievement(boolean fastestCowboyAchievement) {
        this.fastestCowboyAchievement = fastestCowboyAchievement;
    }

    public boolean iscTapsAchievement() {
        return cTapsAchievement;
    }

    public void setcTapsAchievement(boolean cTapsAchievement) {
        this.cTapsAchievement = cTapsAchievement;
    }

    public boolean isTapMasterAchievement() {
        return tapMasterAchievement;
    }

    public void setTapMasterAchievement(boolean tapMasterAchievement) {
        this.tapMasterAchievement = tapMasterAchievement;
    }

    public long getTenSecsScore() {
        return tenSecsScore;
    }

    public void setTenSecsScore(long tenSecsScore) {
        if (tenSecsScore > this.tenSecsScore) {
            this.tenSecsScore = tenSecsScore;
        }
    }

    public long getThirtySecsScore() {
        return thirtySecsScore;
    }

    public void setThirtySecsScore(long thirtySecsScore) {
        if (thirtySecsScore > this.thirtySecsScore) {
            this.thirtySecsScore = thirtySecsScore;
        }
    }

    public long getSixtySecsScore() {
        return sixtySecsScore;
    }

    public void setSixtySecsScore(long sixtySecsScore) {
        if (sixtySecsScore > this.sixtySecsScore) {
            this.sixtySecsScore = sixtySecsScore;
        }
    }

    public long getFiftyTapsScore() {
        return fiftyTapsScore;
    }

    public void setFiftyTapsScore(long fiftyTapsScore) {
        if (this.fiftyTapsScore == 0 || fiftyTapsScore < this.fiftyTapsScore) {
            this.fiftyTapsScore = fiftyTapsScore;
        }
    }

    public long getOneHundredTapsScore() {
        return oneHundredTapsScore;
    }

    public void setOneHundredTapsScore(long oneHundredTapsScore) {
        if (this.oneHundredTapsScore == 0 || oneHundredTapsScore < this.oneHundredTapsScore) {
            this.oneHundredTapsScore = oneHundredTapsScore;
        }
    }

    public long getTwoHundredTapsScore() {
        return twoHundredTapsScore;
    }

    public void setTwoHundredTapsScore(long twoHundredTapsScore) {
        if (this.twoHundredTapsScore == 0 || twoHundredTapsScore < this.twoHundredTapsScore) {
            this.twoHundredTapsScore = twoHundredTapsScore;
        }
    }

    /*
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (quickReactionAchievement ? 1 : 0));
        dest.writeByte((byte) (rightOnTimeAchievement ? 1 : 0));
        dest.writeByte((byte) (aFastMinuteAchievement ? 1 : 0));
        dest.writeByte((byte) (fastestCowboyAchievement ? 1 : 0));
        dest.writeByte((byte) (cTapsAchievement ? 1 : 0));
        dest.writeByte((byte) (tapMasterAchievement ? 1 : 0));

        dest.writeLong(tenSecsScore);
        dest.writeLong(thirtySecsScore);
        dest.writeLong(sixtySecsScore);
        dest.writeLong(fiftyTapsScore);
        dest.writeLong(oneHundredTapsScore);
        dest.writeLong(twoHundredTapsScore);
    }

    private GameProgress (Parcel in){
        quickReactionAchievement = in.readByte() != 0;
        rightOnTimeAchievement = in.readByte() != 0;
        aFastMinuteAchievement = in.readByte() != 0;
        fastestCowboyAchievement = in.readByte() != 0;
        cTapsAchievement = in.readByte() != 0;
        tapMasterAchievement = in.readByte() != 0;

        tenSecsScore = in.readLong();
        thirtySecsScore = in.readLong();
        sixtySecsScore = in.readLong();
        fiftyTapsScore = in.readLong();
        oneHundredTapsScore = in.readLong();
        twoHundredTapsScore = in.readLong();
    }

    public static final Parcelable.Creator<GameProgress> CREATOR
            = new Parcelable.Creator<GameProgress>() {
        public GameProgress createFromParcel(Parcel in) {
            return new GameProgress(in);
        }

        public GameProgress[] newArray(int size) {
            return new GameProgress[size];
        }
    }; */

}
