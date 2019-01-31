package com.one_unit.www.wordchallenge;

import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;

import com.one_unit.www.wordchallenge.Helper.ViewHelper;
import com.one_unit.www.wordchallenge.UI.GameActivityController;

/**
 * Created by IbragimovAziz.
 */

public class MyReverseDragListener implements View.OnDragListener {

    private final String LOG_TAG = "DRAG_EVENT";
    private boolean isDropped;
    private Context context;

    private GameActivityController gameActivityController;

    public MyReverseDragListener(GameActivityController gameActivityController, Context context){
        this.gameActivityController = gameActivityController;
        this.context = context;
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
                this.isDropped = true;
                char letter = dragEvent.getClipData().getItemAt(0).getText().charAt(0);
                TextView child = ViewHelper.getInvisibleChildTextView(view, letter);
                if(child != null){
                    int[] position = {(int) dragEvent.getX(), (int) dragEvent.getY()};
                    this.gameActivityController.placeTextView(child.getId(), position);
                    child.setVisibility(View.VISIBLE);
                }
                Log.d(LOG_TAG, "dropped " + this.isDropped);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                if(!this.isDropped){
                    this.gameActivityController.recreateLastPlacedTextView();
                }
                this.gameActivityController.unMarkLastPlacedTextView();

                Log.d(LOG_TAG, "ended");
                break;
            default:
                break;
        }
        return true;
    }
}
