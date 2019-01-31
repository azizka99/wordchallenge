package com.one_unit.www.wordchallenge;

import android.content.ClipData;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.one_unit.www.wordchallenge.Helper.GameActivityMessageHelper;
import com.one_unit.www.wordchallenge.Helper.MathHelper;
import com.one_unit.www.wordchallenge.UI.GameActivityController;

/**
 * Created by IbrahimovAziz.
 */

public class MyTouchListener implements View.OnTouchListener {

    private GameActivityController gameActivityController;
    private float dX, dY;
    private boolean isMoved;
    private Context context;
    private GameActivityMessageHelper gameActivityMessageHelper;

    public  MyTouchListener(GameActivityController gameActivityController, Context context){
        this.gameActivityController = gameActivityController;
        this.context = context;
        this.gameActivityMessageHelper = new GameActivityMessageHelper(context, gameActivityController);
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            this.dX = view.getX() - motionEvent.getRawX();
            this.dY = view.getY() - motionEvent.getRawY();
            this.isMoved = false;
        }
        if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
            float nextX = motionEvent.getRawX() + dX;
            float nextY = motionEvent.getRawY() + dY;
            if(MathHelper.checkIfCollidesWithLeftWall( (int) nextX)){
                nextX = 0;
            }
            else if(MathHelper.checkIfCollidesWithRightWall((int) nextX, this.gameActivityController)){
                nextX = this.gameActivityController.getLetterHolderMaxWidth() -
                        this.gameActivityController.getTextViewWidth();
            }

            if(MathHelper.checkIfCollidesWithTopWall((int) nextY)){
                nextY = 0;
            }


            if(MathHelper.checkIfCollidesWithBottomWall((int) nextY, this.gameActivityController)){
                ClipData clipData = ClipData.newPlainText("letter", ((TextView)view).getText());
                View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(clipData, dragShadowBuilder, view, 0);
                this.gameActivityController.markTextViewPlacedById(view.getId());
                view.setVisibility(View.INVISIBLE);
            }
            else{
                view.animate()
                        .x(nextX)
                        .y(nextY)
                        .setDuration(0)
                        .start();
                int[] offset = {(int)dX, (int)dY};
                gameActivityController.updatePositionByOffset(view.getId(), offset);
            }

            this.isMoved = true;

        }
        if(motionEvent.getAction() == MotionEvent.ACTION_UP){
            if(!this.isMoved){
                this.gameActivityController.placeLetterInFirstEmptyHolder(String.valueOf(((TextView)view).getText()));
                this.gameActivityMessageHelper.sendMessage(GameActivityMessageHelper.CHECK_TEXT_MESSAGE);
                this.gameActivityController.unMarkLastMarkedLetter();
                view.setVisibility(View.INVISIBLE);
            }
        }
        return true;
    }


}
