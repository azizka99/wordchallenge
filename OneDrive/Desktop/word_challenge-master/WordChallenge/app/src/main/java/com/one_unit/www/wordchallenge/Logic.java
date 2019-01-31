package com.one_unit.www.wordchallenge;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;

class Logic {
    final private int MIN_NUMBER_OF_LETTERS = 4;
    final private int MAX_NUMBER_OF_LETTERS = 8;

    private Map<String, int[]> numberOfLines = generateArrayMap();
    private Map<String, int[]> generateArrayMap() {
        Map<String, int[]> arrayMap = new ArrayMap<String, int[]>();
        arrayMap.put("de", new int[]{428, 752, 1064, 984, 1025});
        arrayMap.put("en", new int[]{442, 452, 468,  454, 379});
        arrayMap.put("es", new int[]{299, 493, 526,  569, 520});
        arrayMap.put("ru", new int[]{475, 792, 826,  855, 680});
        arrayMap.put("sv", new int[]{493, 523, 449,  408, 327});
        return arrayMap;
    }

    private String locale;
    private int[] numberOfLinesLocale = new int[MAX_NUMBER_OF_LETTERS - MIN_NUMBER_OF_LETTERS + 1];

    private Queue<String> wordQueue;

    private java.util.Random RandNumGenerate = new java.util.Random();

    private int wordLength = 4;
    private Storage storage;
    private WordReader wordReader;
    private char[] shuffledWord;
    private String[] origWords;
    final private int[] WORDS_PER_LEVEL = {30, 50, 80, 100};
    Context context;

    private final long MILLISECONDS_IN_SECOND = 1000;
    private final long MAX_TIME_TO_SOLVE = 15 * MILLISECONDS_IN_SECOND;
    private final long TIME_FOR_GAINING_MAX_SCORE = 1 * MILLISECONDS_IN_SECOND;

    Logic(Context context) throws IOException {
        this.context = context;
        this.storage = new Storage(context);
        this.wordReader = new WordReader();

        locale = storage.getLocale();
        if (locale == null){
            this.getSystemLocale();
        }
        numberOfLinesLocale = numberOfLines.get(locale);

        this.wordLength = getWordLength();
        Log.d("WordLength", String.valueOf(wordLength));
        if(this.storage.isSavedWords()) {
            wordQueue = storage.getSavedWords();
        } else {
            this.readAllWords(this.wordLength);
        }
    }

    public void readWords (int wordLength, int numberOfWords) throws IOException {
        String[] words = this.wordReader.readMultipleWords(wordLength, numberOfWords,
                this.numberOfLinesLocale[wordLength - MIN_NUMBER_OF_LETTERS], this.locale, this.context);
        this.wordQueue = new LinkedList<>(Arrays.asList(words));
        this.storage.saveWords(wordQueue, wordLength);
    }

    public void readAllWords (int wordLength) throws IOException {
        this.readWords(wordLength, this.numberOfLinesLocale[wordLength - MIN_NUMBER_OF_LETTERS]);
    }

    public char[] getWord() throws IOException {
        if(wordLength <= MAX_NUMBER_OF_LETTERS){
            if(storage.getWordsUsedInSession(wordLength) > WORDS_PER_LEVEL[wordLength - MIN_NUMBER_OF_LETTERS]) {
                setWordLength(++wordLength);
                storage.saveWordLength();
                readAllWords(wordLength);
            }
        }
        this.processNextWord();

        return this.shuffledWord;
    }

    public void processNextWord() {
        this.origWords = wordQueue.poll().split("\t");
        do {
            this.shuffledWord = this.shuffleWord(origWords[0]);
        } while (compareWords(shuffledWord));

        storage.addUsedWordsCounter(wordLength);
    }

    @NonNull
    private char[] shuffleWord(String origWord) {
        char[] origWordArray = origWord.toCharArray();
        char[] randWordArray = new char[this.wordLength];
        int randNum;

        for(int j = 0; j < this.wordLength; j++) {
            randNum = this.RandNumGenerate.nextInt(j + 1);
            if(randNum != j) {
                randWordArray[j] = randWordArray[randNum];
            }
            randWordArray[randNum] = origWordArray[j];
        }
        return randWordArray;
    }

    public boolean compareWords(char[] word) {
        for(int i = 0; i < this.origWords.length; i++){
            if(Arrays.equals(this.origWords[i].toCharArray(), word)){
                return true;
            }
        }
        return false;
    }

    public int calculateScore(long timeToSolveLeft) {
        int scores;

        scores = (1 + storage.getLevel() / 4) * (((MAX_TIME_TO_SOLVE - timeToSolveLeft) <
                TIME_FOR_GAINING_MAX_SCORE) ? 1 : (int)(MAX_TIME_TO_SOLVE -
                TIME_FOR_GAINING_MAX_SCORE) / (int)timeToSolveLeft) * 100;

        return scores;
    }

    public void getSystemLocale() {
        Locale current = context.getResources().getConfiguration().locale;
        locale = String.valueOf(current).substring(0, 2);
        storage.setLocale(locale);
    }

    public String[] getOrigWords() {
        return this.origWords;
    }

    public char[] getShuffledWord() {
        return this.shuffledWord;
    }

    public void setWordLength(int wordLength) {
        this.storage.setWordLength(wordLength);
    }

    public int getWordLength() {
        return this.storage.getWordLength();
    }

    public void addScore(int score) {
        this.storage.addScore(score);
    }

    public int getScore() {
        return this.storage.getScore();
    }

    public int getLevel() {
        return this.storage.getLevel();
    }

    public boolean isSavedData() {
        return this.storage.isSavedWords();
    }

    public String getLocale() {
        return this.storage.getLocale();
    }

    public void setLocale(String locale) {
        this.storage.setLocale(locale);
    }

    public int getWordsUsedInSession() {
        return this.storage.getWordsUsedInSession(wordLength);
    }
}