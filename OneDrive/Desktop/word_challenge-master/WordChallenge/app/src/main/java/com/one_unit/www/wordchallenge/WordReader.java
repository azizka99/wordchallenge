package com.one_unit.www.wordchallenge;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class WordReader {

    private java.util.Random RandNumGenerate = new java.util.Random();
    private BufferedReader bReader;
    private int counter = 0;

    private String readFromFile(int numberOfLine) throws IOException {
        for (; this.counter < numberOfLine; this.counter++) {
            this.bReader.readLine();
        }
        this.counter++;

        return this.bReader.readLine();
    }

    private int[] selectRandWords(int numberOfLetters, int numberOfRandWords, int numberOfLines) {
        int[] numbersOfLines = new int[numberOfLines];
        int randNum;
        int bufNum;

        for(int i = 0; i < numberOfLines; i++) {
            numbersOfLines[i] = i;
        }

        for (int i = numberOfLines - 1; i > 0; i--) {
            randNum = this.RandNumGenerate.nextInt(i);
            bufNum = numbersOfLines[i];
            numbersOfLines[i] = numbersOfLines[randNum];
            numbersOfLines[randNum] = bufNum;
        }

        int[] trunkedSortedNumbersOfLines;
        if(numberOfRandWords != numberOfLines){
            trunkedSortedNumbersOfLines = new int [numberOfRandWords];
            System.arraycopy(numbersOfLines, 0, trunkedSortedNumbersOfLines, 0, numberOfRandWords);
        }
        else {
            trunkedSortedNumbersOfLines = numbersOfLines;
        }
        Arrays.sort(trunkedSortedNumbersOfLines);

        return trunkedSortedNumbersOfLines;
    }

    public String[] readMultipleWords(int numberOfLetters, int numberOfWords, int numberOfLines,
                                      String locale, Context context) throws IOException {
        if (numberOfLetters < 4 || numberOfLetters > 8) {
            throw new IllegalArgumentException("Number of letters should in range from 4 to 8");
        }
        if (numberOfWords < 1 || numberOfWords > numberOfLines) {
            throw new IllegalArgumentException("Illegal number of words");
        }

        String[] randReadedWords = new String[numberOfWords];
        AssetManager assetPath = context.getAssets();

        int[] numbers = this.selectRandWords(numberOfLetters, numberOfWords, numberOfLines);


        InputStream dictFile = assetPath.open(locale + '/' +locale + String.valueOf(numberOfLetters) + ".txt");
        if (dictFile != null) {
            InputStreamReader dictReader = new InputStreamReader(dictFile);
            this.bReader = new BufferedReader(dictReader);

            for (int i = 0; i < numberOfWords; i++) {
                randReadedWords[i] = readFromFile(numbers[i]);
            }

            dictFile.close();
            bReader.close();
        }

        this.shuffleWords(randReadedWords);

        return randReadedWords;
    }

    private void shuffleWords(String[] words) {
        int randNum;
        String bufString;
        for (int i = words.length - 1; i > 0; i--) {
            randNum = this.RandNumGenerate.nextInt(i);
            bufString = words[i];
            words[i] = words[randNum];
            words[randNum] = bufString;
        }
    }
}