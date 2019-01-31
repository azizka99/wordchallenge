package com.one_unit.www.wordchallenge.UI;

/**
 * Created by IbrahimliAziz.
 */

public class LetterPlaceHolderHandler {

    private final int OFFSET = 10;

    private int currentLetterNumber;
    private int maxFrameWidth;
    private int maxFrameHeight;
    private int[][] positions;
    private boolean isPlacedArr[];
    private int heightDp;
    private int widthDp;
    private int lastPlacedMarkedElementIndex;



    public LetterPlaceHolderHandler(int maxFrameWidth, int maxFrameHeight, int currentLetterNumber){
        this.currentLetterNumber = currentLetterNumber;
        this.maxFrameWidth = maxFrameWidth;
        this.maxFrameHeight = maxFrameHeight;

        this.positions = new int[this.currentLetterNumber][2];
        this.isPlacedArr = new boolean[this.currentLetterNumber];

        for(int i = 0; i < this.currentLetterNumber; i++){
            this.isPlacedArr[i] = false;
        }

        this.heightDp = this.widthDp = this.calculateWidth();

        this.lastPlacedMarkedElementIndex = -1; //default value
    }

    /**
     * Generate the position of the placeholders.
     */
    public void generatePositions(){

        for(int i =0 ; i < this.currentLetterNumber; i++){
            this.positions[i][0] = (this.OFFSET + this.widthDp)*i;
            this.positions[i][1] = (this.maxFrameHeight - this.heightDp)/2;
        }
    }

    public int[][] getPositions(){
        return this.positions;
    }

    private int calculateWidth(){
        return (int)Math.floor(maxFrameWidth/8) + 4;
    }
    public int getWidthDp(){
        return this.widthDp;
    }

    public int getHeightDp(){
        return this.heightDp;
    }

    public int getMaxFrameWidth(){
        return this.maxFrameWidth;
    }

    public int getMaxFrameHeight(){
        return this.maxFrameHeight;
    }

    public int getLastPlacedMarkedElementIndex() {
        return lastPlacedMarkedElementIndex;
    }

    public void setLastPlacedMarkedElementIndex(int lastPlacedMarkedElementIndex) {
        this.lastPlacedMarkedElementIndex = lastPlacedMarkedElementIndex;
    }

}
