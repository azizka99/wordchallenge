package com.one_unit.www.wordchallenge;

import android.app.Application;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;
import android.util.Log;
import android.test.AndroidTestCase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.support.test.InstrumentationRegistry;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(AndroidJUnit4.class)
public class WordReaderTest {
    @Test
    public void shit() throws IOException {
        WordReader wordReader = new WordReader();
        String[] words = wordReader.readMultipleWords(4, 100, InstrumentationRegistry.getTargetContext());
        int counter = 0;
        for(String num : words){
            counter++;
            Log.i("",counter + ") " + String.valueOf(num) + "\n");
        }

        AssetManager assetPath = InstrumentationRegistry.getTargetContext().getAssets();
        InputStream dictFile = assetPath.open(4 + ".txt");
        InputStreamReader dictReader = new InputStreamReader(dictFile);
        BufferedReader bReader = new BufferedReader(dictReader);

        int[] numberOfLines = {0, 0, 0, 0, 536, 679, 725, 650, 500};
        String[] allWords = new String[numberOfLines[4]];
        int[] intAllWords = new int[numberOfLines[4]];

        for (int i = 0; i < numberOfLines[4]; i++) {
            allWords[i] = bReader.readLine();
        }

        int counter2 = 0;
        for(String bigWord : allWords) {
            for(String smallWord : words) {
                if(bigWord.equals(smallWord)){
                    intAllWords[counter2]++;
                    break;
                }
            }
            counter2++;
        }
        for(int i = 0; i < numberOfLines[4]; i++){
            if(intAllWords[i] > 0)
                Log.i("",i + ") " + allWords[i] + "\n");
        }
    }
}

