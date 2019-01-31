package com.one_unit.www.wordchallenge.UI;

/**
 * Created by IbrahimliAziz.
 */

public class TextViewHandler {

    private final int MAX_TEXT_VIEWS = 10;
    private final int MIN_TEXT_VIEWS = 4;
    private final String[] TEXT_VIEW_IDS = {"text_view_1", "text_view_2", "text_view_3", "text_view_4", "text_view_5",
            "text_view_6", "text_view_7", "text_view_8", "text_view_9", "text_view_10"};


    private int widthDp;
    private int heightDp;
    private int maxFrameWidth;
    private int maxFrameHeight;
    private int currentTextViewNumber;
    private int[][] positions;
    private boolean[] isPlacedArr;
    private int lastMarkedElementIndex;

    public TextViewHandler(int maxFrameWidth, int maxFrameHeight, int currentTextViewNumber) throws Exception{

        if(currentTextViewNumber < this.MIN_TEXT_VIEWS || currentTextViewNumber > this.MAX_TEXT_VIEWS){
            throw new Exception("Number of text views shall be in a range from " +
                    this.MIN_TEXT_VIEWS + " to " + this.MAX_TEXT_VIEWS);
        }
        this.currentTextViewNumber = currentTextViewNumber;
        this.maxFrameWidth = maxFrameWidth;
        this.maxFrameHeight = maxFrameHeight;

        this.positions = new int[this.currentTextViewNumber][2];
        this.isPlacedArr = new boolean[this.currentTextViewNumber];

        for(int i = 0; i < this.currentTextViewNumber; i++){
            this.isPlacedArr[i] = false;
        }

        this.heightDp = this.widthDp = this.calculateWidth();
    }

    /**
     * Generate random positions for textviews.
     * The column (or y element) is not calculated randomly.
     */
    public void generateRandomPositions(){

        for(int i = 0; i < this.currentTextViewNumber; i++){
            int x = (int) (((this.maxFrameWidth - this.widthDp) * ((float)i/(float)this.currentTextViewNumber))
                    + this.widthDp/2);
            int y = (int) ( Math.random() * (this.maxFrameHeight - this.heightDp*2));
            this.positions[i][0] = x;
            this.positions[i][1] = y;
            for(int j = 0; j < i; j++){
                if(this.checkCollision(x, y, this.positions[j][0], this.positions[j][1])){
                    i--;
                    break;
                }
            }

        }

    }

    public int[][] getPositions(){
        return this.positions;
    }

    /**
     * Calculate the width of the textview according to the maximum screen width
     * @return
     */
    private int calculateWidth(){
        return (int)Math.floor(maxFrameWidth/8);
    }

    /**
     * Check collision between two elements with first element (x1, y1) and second element (x2, y2).
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    @org.jetbrains.annotations.Contract(pure = true)
    private boolean checkCollision(int x1, int y1, int x2, int y2){
        if(x1 < x2 + this.widthDp && x1 + this.widthDp > x2 &&
                y1 < y2 + this.heightDp && y1 + this.heightDp > y2){
            return true;
        }
        return false;
    }

    /**
     * Change status of the text view.
     * The status is whether it is placed in the placeholder or not.
     * @param index     The index of the textview.
     * @param status    The new status.
     */
    public void changeMarkedStatus(int index, boolean status){
        this.isPlacedArr[index] = status;
        if(status){
            this.lastMarkedElementIndex = index;
        }
    }

    public int getLastMarkedElementIndex(){
        return this.lastMarkedElementIndex;
    }

    public void clearLastMarkedElementIndex(){
        this.lastMarkedElementIndex = -1;
    }

    public int getWidthDp(){
        return this.widthDp;
    }

    public int getHeightDp(){
        return this.heightDp;
    }

    /**
     * Update the location of textview.
     * @param index     The index of the textview.
     * @param newX      New x position.
     * @param newY      New y position.
     */
    public void updatePosition(int index, int newX, int newY){
        this.positions[index][0] = newX;
        this.positions[index][1] = newY;
    }

    public int getMaxFrameWidth(){
        return this.maxFrameWidth;
    }

    public int getMaxFrameHeight(){
        return this.maxFrameHeight;
    }
}
