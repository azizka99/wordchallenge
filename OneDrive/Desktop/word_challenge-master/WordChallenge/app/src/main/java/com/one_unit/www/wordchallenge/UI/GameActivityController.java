package com.one_unit.www.wordchallenge.UI;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jetbrains.annotations.Contract;

/**
 * Created by IbrahimliAziz.
 */

public class GameActivityController {

    private TextView[] textViews;
    private RelativeLayout[] placeHolders;
    private TextViewHandler textViewHandler;
    private LetterPlaceHolderHandler letterPlaceHolderHandler;
    private int letterCount;
    private float scale;
    private boolean dragEventResults[];
    private boolean isReverseTouched;

    public GameActivityController(TextView[] textViews, RelativeLayout[] placeHolders, int letterCount, RelativeLayout relativeLayout,
                                  RelativeLayout letterHolderRelativeLayout, float scale){
        this.textViews = new TextView[letterCount];
        this.dragEventResults = new boolean[0];
        this.placeHolders = new RelativeLayout[letterCount];
        for(int i = 0; i < letterCount; i++){
            this.textViews[i] = textViews[i];
            this.placeHolders[i] = placeHolders[i];
        }

        this.letterCount = letterCount;
        this.scale = scale;
        float dpHeight = relativeLayout.getHeight() / this.scale;
        float dpWidth = relativeLayout.getWidth() / this.scale;
        Log.d("Layout dimensions", "width = " + dpWidth + " height = " + dpHeight);
        try{
            this.textViewHandler = new TextViewHandler((int) dpWidth, (int) dpHeight, letterCount);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        this.textViewHandler.generateRandomPositions();
        this.makeUnusedTextViewsGone(textViews);

        float letterPlaceHolderWidth = letterHolderRelativeLayout.getWidth() / this.scale;
        float letterPlaceHolderHeight = letterHolderRelativeLayout.getHeight() / this.scale;
        this.letterPlaceHolderHandler = new LetterPlaceHolderHandler((int)letterPlaceHolderWidth,
                (int)letterPlaceHolderHeight, letterCount);
        this.letterPlaceHolderHandler.generatePositions();
        this.makeUnusedPlaceHoldersGone(placeHolders);
        this.makePlaceHolderTextViewsGone();
    }

    /**
     * Map the textviews according to the width and
     * height of the screen.
     */
    public void executeTextViewsDimensionMapping(){
        int width = (int) (this.textViewHandler.getWidthDp()*this.scale + 0.5f);
        int height = (int) (this.textViewHandler.getHeightDp()*this.scale + 0.5f);
        for(int i = 0; i < this.letterCount; i++){
            this.textViews[i].setWidth(width);
            this.textViews[i].setHeight(height);
        }
    }

    /**
     * Place textviews on the screen.
     */
    public void placeTextViews(){
        for (int i = 0; i < this.letterCount; i++){
            RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            int positionPx[] = changeFromDpToPx(this.textViewHandler.getPositions()[i]);
            relativeLayoutParams.setMargins(positionPx[0],
                    positionPx[1], 0, 0);
            this.textViews[i].setLayoutParams(relativeLayoutParams);
        }
    }

    /**
     * place letters in their respective text view.
     * @param word shuffled word in form of char array.
     */
    public void placeLetters(char[] word){
        for(int i = 0; i < this.letterCount; i++){
            this.textViews[i].setText(Character.toString(word[i]));
        }
    }

    /**
     * Place letter place holders.
     */
    public void placePlaceHolders(){
        for (int i = 0; i < this.letterCount; i++) {
            RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            int positionPx[] = changeFromDpToPx(this.letterPlaceHolderHandler.getPositions()[i]);
            relativeLayoutParams.setMargins(positionPx[0],
                    positionPx[1], 0, 0);
            this.placeHolders[i].setLayoutParams(relativeLayoutParams);
        }
    }

    /**
     * Place specific text view to a newPosition.
     * @param id    The id of the textview.
     * @param newPosition   new location.
     */
    public void placeTextView(int id, int[] newPosition){
        int index = 0;
        for(int i = 0; i < this.letterCount; i++){
            if(this.textViews[i].getId() == id){
                index = i;
                break;
            }
        }
        this.textViews[index].setX(newPosition[0] - this.textViews[index].getWidth()/2);
        this.textViews[index].setY(newPosition[1] - this.textViews[index].getHeight()/2);
    }

    /**
     * Convert position from dp to px.
     * @param position  Position in dp.
     * @return  Return position in px.s
     */
    @Contract(pure = true)
    private int[] changeFromDpToPx(int position[]){
        int[] result = new int[position.length];
        for(int i = 0; i < result.length; i++){
            result[i] = (int) ( position[i] * this.scale );
        }
        return result;
    }

    private int changeFromDpToPx(int dp){
        return  (int) (dp * this.scale);
    }

    public float changeFromPxToDp(float px){
        return px / this.scale;
    }

    /**
     * Hide all unused text views.
     * @param textViews Array of all textviews.
     */
    private void makeUnusedTextViewsGone(TextView[] textViews){
        for(int i = this.letterCount; i < textViews.length; i++){
            textViews[i].setVisibility(View.GONE);
        }
    }

    /**
     * Hide all unused letter place holders.
     * @param placeHolders  Array of all placeHolders.
     */
    private void makeUnusedPlaceHoldersGone(RelativeLayout placeHolders[]){
        Log.d("PlaceHolder", "Length = " + placeHolders.length);
        for(int i = this.letterCount; i < placeHolders.length; i++){
            placeHolders[i].setVisibility(View.GONE);
            for(int j = 0; j < placeHolders[i].getChildCount(); j++){
                placeHolders[i].getChildAt(j).setVisibility(View.GONE);
            }
        }
    }

    /**
     * Hide all letters in the letter place holders
     */
    private void makePlaceHolderTextViewsGone(){
        for(int i = 0; i < this.letterCount; i++){
            for(int j = 0; j < this.placeHolders[i].getChildCount(); j++){
                if(this.placeHolders[i].getChildAt(j) instanceof TextView){
                    this.placeHolders[i].getChildAt(j).setVisibility(View.GONE);
                    break;
                }
            }
        }
    }

    public void updatePositionByOffset(int id, int[] offset){
        for(int i = 0; i < this.letterCount; i++){
            if(this.textViews[i].getId() == id){
                this.textViewHandler.updatePosition(i, this.textViewHandler.getPositions()[i][0] + offset[0]
                        , this.textViewHandler.getPositions()[i][1] + offset[1]);
            }
        }
    }

    /**
     * Recreate textview that is hidden and holds specific letter
     * @param letter    The letter that hidden textview holds
     */
    public void recreateLetterByChar(char letter){
        for (TextView textView : this.textViews) {
            Log.d("Income textview", "char = " + letter);
            Log.d("Invisible textview", "x = " + textView.getX() + ", y = " + textView.getY() +
                    ", char = " + textView.getText().charAt(0) + ", visibility = " + textView.getVisibility());
            if(textView.getText().charAt(0) == letter){
                if(textView.getVisibility() == View.INVISIBLE ||
                        textView.getVisibility() == View.GONE){
                    textView.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
    }

    /**
     * Recreate letter that was marked the latest
     */
    public void recreateLastMarkedLetter(){
        int lastMarkedIndex = this.textViewHandler.getLastMarkedElementIndex();
        if(lastMarkedIndex != -1) {
            this.textViews[lastMarkedIndex].setVisibility(View.VISIBLE);
        }
    }

    /**
     * Change the letter of the last marked text view.
     * @param letter    The new letter.
     */
    public void changeLetterOfLastMarkedTextView(char letter){
        int lastMarkedIndex = this.textViewHandler.getLastMarkedElementIndex();
        if(lastMarkedIndex != -1) {
            this.textViews[lastMarkedIndex].setText(String.valueOf(letter));
        }
    }

    /**
     * Change the letter of the last marked text view place holder
     * @param letter
     */
    public void changeLetterOfLastPlacedMarkedTextView(char letter){
        int lastPlacedMarkedIndex = this.letterPlaceHolderHandler.getLastPlacedMarkedElementIndex();
        if(lastPlacedMarkedIndex != -1) {
            this.getChildTextView(this.placeHolders[lastPlacedMarkedIndex]).setText(String.valueOf(letter));
        }
    }

    public void markTextViewPlacedById(int id){
        this.textViewHandler.changeMarkedStatus(this.getTextViewIndexById(id), true);
    }

    public void unMarkTextViewPlacedById(int id){
        this.textViewHandler.changeMarkedStatus(this.getTextViewIndexById(id), false);
    }

    public void unMarkLastMarkedLetter(){
        this.textViewHandler.clearLastMarkedElementIndex();
    }

    public void markLastPlacedTextViewById(int id){
        this.letterPlaceHolderHandler.setLastPlacedMarkedElementIndex(this.getPlaceHolderTextViewIndexById(id));
    }

    public void unMarkLastPlacedTextView(){
        this.letterPlaceHolderHandler.setLastPlacedMarkedElementIndex(-1);
    }

    /**
     * Recreate last letter place holder's text view
     */
    public void recreateLastPlacedTextView(){
        int lastPlacedMarkedIndex = this.letterPlaceHolderHandler.getLastPlacedMarkedElementIndex();
        Log.d("LOG", "Last placed index = " + lastPlacedMarkedIndex);
        if(lastPlacedMarkedIndex != -1){
            this.getChildTextView(this.placeHolders[lastPlacedMarkedIndex]).setVisibility(View.VISIBLE);
        }
    }

    /**
     * Register new result for the drag event
     * @param result the new result
     */
    public void registerDragEventResult(boolean result){
        boolean[] tempResults = this.dragEventResults;
        this.dragEventResults = new boolean[this.dragEventResults.length + 1];
        for(int i = 0; i < tempResults.length; i++){
            this.dragEventResults[i] = tempResults[i];
        }
        this.dragEventResults[this.dragEventResults.length - 1] = result;
    }

    /**
     * Check if the drag event has ended.
     * This means that it has been fired n times, where n equals the amount of letters
     * @return true if equals
     */
    public boolean hasDragEventsEnded(){
        return this.dragEventResults.length == this.letterCount;
    }

    /**
     * Check if any of the drag events fired returned positive result
     * @return
     */
    public boolean shouldRecreateLastMarkedLetter(){
        boolean result = false;
        for(int i = 0; i < this.dragEventResults.length; i++){
            result = result || this.dragEventResults[i];
        }
        return !result;
    }

    public void cleanDragEventResults(){
        this.dragEventResults = new boolean[0];
    }

    /**
     * Check if all letters have been placed in all active letter place holders.
     * @return
     */
    public boolean checkIfWordIsFilled(){
        for(int i = 0; i < this.letterCount; i++){
            if(this.getChildTextView(this.placeHolders[i]).getVisibility() == View.INVISIBLE){
                return false;
            }
        }
        return true;
    }

    /**
     * Get the word that is a sequence of all letters placed in the letter place holders.
     * @return
     */
    public char[] getWord(){
        char[] result = new char[this.letterCount];
        for(int i = 0; i < this.letterCount; i++){
            result[i] = this.getChildTextView(this.placeHolders[i]).getText().charAt(0);
        }
        return result;
    }

    /**
     * Place the letter in the first empty letter place holder.
     * Empty means that the respective textview inside the placeholder is GONE or INVISIBLE
     * @param letter
     */
    public void placeLetterInFirstEmptyHolder(String letter){
        TextView textView = this.getChildTextView(this.getFirstEmptyPlaceHolder());
        textView.setText(letter);
        textView.setVisibility(View.VISIBLE);
    }

    /**
     * @return Returns the first empty place holder
     */
    @Nullable
    private View getFirstEmptyPlaceHolder(){
        for(int i = 0; i < this.letterCount; i++){
            if(this.getChildTextView(this.placeHolders[i]).getVisibility() == View.INVISIBLE
                    || this.getChildTextView(this.placeHolders[i]).getVisibility() == View.GONE){
                return placeHolders[i];
            }
        }
        return null;
    }

    /**
     * Get child text view of the parent view.
     * @param parentView
     * @return
     */
    @Nullable
    private TextView getChildTextView(View parentView){
        for(int i = 0; i < ((ViewGroup)parentView).getChildCount(); i++){
            View nextChild = ((ViewGroup)parentView).getChildAt(i);
            if(nextChild instanceof TextView ){
                return (TextView) nextChild;
            }
        }
        return null;
    }

    private int getTextViewIndexById(int id){
        for(int i =0; i < this.letterCount; i++){
            if(this.textViews[i].getId() == id){
                return i;
            }
        }
        return -1;
    }

    private int getPlaceHolderTextViewIndexById(int id){
        for(int i = 0; i < this.letterCount; i++){
            if(this.getChildTextView(this.placeHolders[i]).getId() == id){
                return i;
            }
        }
        return -1;
    }

    public int getLetterHolderMaxWidth(){
        return this.changeFromDpToPx(this.textViewHandler.getMaxFrameWidth());
    }

    public int getLetterHolderMaxHeight(){
        return this.changeFromDpToPx(this.textViewHandler.getMaxFrameHeight());
    }

    public int getTextViewWidth(){
        return this.changeFromDpToPx(this.textViewHandler.getWidthDp());
    }

    public int getTextViewHeight(){
        return this.changeFromDpToPx(this.textViewHandler.getHeightDp());
    }

    public boolean isReverseTouched() {
        return isReverseTouched;
    }

    public void setReverseTouched(boolean reverseTouched) {
        isReverseTouched = reverseTouched;
    }
}
