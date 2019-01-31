package com.one_unit.www.wordchallenge;

import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;

import com.one_unit.www.wordchallenge.Helper.GameActivityMessageHelper;
import com.one_unit.www.wordchallenge.Helper.ViewHelper;
import com.one_unit.www.wordchallenge.UI.GameActivityController;

/**
 * Created by IbrahimovAziz.
 */

public class MyDragListener implements View.OnDragListener {

    private final String LOG_TAG = "DRAG_EVENT";
    private boolean isDropped;
    private Context context;

    private GameActivityController gameActivityController;
    private GameActivityMessageHelper gameActivityMessageHelper;

    public MyDragListener(GameActivityController gameActivityController, Context context){
        this.gameActivityController = gameActivityController;
        this.context = context;
        this.gameActivityMessageHelper = new GameActivityMessageHelper(context, gameActivityController);
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {

        int action = dragEvent.getAction();
        //ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        switch (action){
            case DragEvent.ACTION_DRAG_STARTED:
                this.isDropped = false;
                Log.d(LOG_TAG, "started");
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                Log.d(LOG_TAG, "entered");
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                Log.d(LOG_TAG, "exited");
                break;
            case DragEvent.ACTION_DROP:
                char letter = dragEvent.getClipData().getItemAt(0).getText().charAt(0);
                if(ViewHelper.getChildTextView(view).getVisibility() != View.VISIBLE){
                    this.isDropped = true;
                    ViewHelper.placeCharacter(letter, view);
                    this.gameActivityMessageHelper.sendMessage(GameActivityMessageHelper.CHECK_TEXT_MESSAGE);
                    this.gameActivityController.unMarkLastMarkedLetter();
                }
                else{
                    char oldLetter = ViewHelper.getChildTextView(view).getText().charAt(0);
                    this.gameActivityController.changeLetterOfLastMarkedTextView(oldLetter);
                    this.gameActivityController.changeLetterOfLastPlacedMarkedTextView(oldLetter);
                    this.gameActivityController.recreateLastMarkedLetter();
                    ViewHelper.getChildTextView(view).setText(Character.toString(letter));
                }
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                    this.gameActivityController.registerDragEventResult(this.isDropped);
                    if(this.gameActivityController.hasDragEventsEnded()){
                        if(this.gameActivityController.shouldRecreateLastMarkedLetter()){
                            this.gameActivityController.recreateLastMarkedLetter();
                            Log.d(LOG_TAG, "recreated");
                        }
                        this.gameActivityController.cleanDragEventResults();
                        this.isDropped = false;
                    }
                Log.d(LOG_TAG, "ended");
                break;
            default:
                break;
        }
        return true;
    }
}
