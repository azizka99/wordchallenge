package com.one_unit.www.wordchallenge;

import com.one_unit.www.wordchallenge.UI.TextViewHandler;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created by IbrahimovAziz.
 */

public class TextViewHandlerTest {

    @Test
    public void generateRandomPositionsTest(){
        TextViewHandler textViewHandler = null;
        try{
            textViewHandler = new TextViewHandler(600, 400, 8);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        textViewHandler.generateRandomPositions();
        int[][] positions = textViewHandler.getPositions();
        for(int i = 0; i < positions.length; i++){
            System.out.println(Arrays.toString(positions[i]));
        }

    }
}
