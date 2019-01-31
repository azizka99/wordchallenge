package com.one_unit.www.wordchallenge.Helper;

import android.util.Log;

import com.one_unit.www.wordchallenge.UI.GameActivityController;

/**
 * Created by IbrahimovAziz.
 */

public final class MathHelper {

    public final static boolean checkIfCollidesWithLeftWall(int nextX){
        if(nextX < 0){
            return true;
        }
        return false;
    }

    public static final boolean checkIfCollidesWithRightWall(int nextX, GameActivityController gameActivityController){
        if(nextX + gameActivityController.getTextViewWidth() > gameActivityController.getLetterHolderMaxWidth()){
            return true;
        }
        return false;
    }

    public static final boolean checkIfCollidesWithTopWall(int nextY){
        if(nextY < 0){
            return true;
        }
        return false;
    }

    public static final boolean checkIfCollidesWithBottomWall(int nextY, GameActivityController gameActivityController){
        int nextYPlusHeight = nextY + gameActivityController.getTextViewHeight();
        Log.d("Collision", "nextY = " + nextYPlusHeight + ", maxHeight = " +
                gameActivityController.getLetterHolderMaxHeight());
        if(nextY + gameActivityController.getTextViewHeight()*2 > gameActivityController.getLetterHolderMaxHeight()){
            return true;
        }
        return false;
    }

    /**
     * Calculate the distance between two points
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static final float distance(float x1, float y1, float x2, float y2) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        float distanceInPx = (float) Math.sqrt(dx * dx + dy * dy);
        return distanceInPx;
    }
}
