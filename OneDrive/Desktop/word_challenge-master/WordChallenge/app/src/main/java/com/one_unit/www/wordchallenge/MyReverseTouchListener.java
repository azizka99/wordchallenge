package com.one_unit.www.wordchallenge;

import android.content.ClipData;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.one_unit.www.wordchallenge.Helper.MathHelper;
import com.one_unit.www.wordchallenge.UI.GameActivityController;

/**
 * Created by IbrahimovAziz.
 */

public class MyReverseTouchListener implements View.OnTouchListener {

    private GameActivityController gameActivityController;
    private float pressedX, pressedY;
    private boolean isMoved;
    private final String LOG_TAG = "REVERSE_TOUCH";
    private static final int MAX_CLICK_DISTANCE = 25;

    public  MyReverseTouchListener(GameActivityController gameActivityController){
        this.gameActivityController = gameActivityController;
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            pressedX = motionEvent.getX();
            pressedY = motionEvent.getY();
            this.gameActivityController.setReverseTouched(true);
            this.isMoved = false;
        }
        if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
            float distanceInDp = this.gameActivityController.changeFromPxToDp(MathHelper.distance(pressedX,
                    pressedY, motionEvent.getX(), motionEvent.getY()));
            if ( distanceInDp > MAX_CLICK_DISTANCE) {
                Log.d("DEBUG", "reverse touched");
                ClipData clipData = ClipData.newPlainText("letter", ((TextView)view).getText());
                View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(clipData, dragShadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                this.gameActivityController.markLastPlacedTextViewById(view.getId());
                this.isMoved = true;
            }
        }
        if(motionEvent.getAction() == MotionEvent.ACTION_UP){
            if(!this.isMoved){
                Log.d(LOG_TAG, "single touch");
                this.gameActivityController.recreateLetterByChar(((TextView)view).getText().charAt(0));
                view.setVisibility(View.INVISIBLE);
            }
        }
        return true;
    }
}
