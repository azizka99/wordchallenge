package com.one_unit.www.wordchallenge.Helper;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by IbrahimovAziz.
 */

public final class ViewHelper {

    /**
     * Get an invisible text view that has specific letter in it
     * @param parentView
     * @param letter
     * @return
     */
    @Nullable
    public final static TextView getInvisibleChildTextView(View parentView, char letter){
        for(int i = 0; i < ((ViewGroup)parentView).getChildCount(); i++){
            View nextChild = ((ViewGroup)parentView).getChildAt(i);
            if(nextChild instanceof TextView ){
                if(((TextView)nextChild).getText().charAt(0) == letter &&
                        (nextChild.getVisibility() == View.INVISIBLE || nextChild.getVisibility() == View.GONE)){
                    return (TextView) nextChild;
                }
            }
        }
        return null;
    }

    @Nullable
    public final static TextView getChildTextView(View parentView){
        for(int i = 0; i < ((ViewGroup)parentView).getChildCount(); i++){
            View nextChild = ((ViewGroup)parentView).getChildAt(i);
            if(nextChild instanceof TextView ){
                return (TextView) nextChild;
            }
        }
        return null;
    }

    /**
     * Place character in the child text view
     * @param letter
     * @param view
     */
    public final static void placeCharacter(char letter, View view){
        Log.d("ClipData", "char = " + letter);
        ViewHelper.getChildTextView(view).setText(Character.toString(letter));
        ViewHelper.getChildTextView(view).setVisibility(View.VISIBLE);
    }
}
