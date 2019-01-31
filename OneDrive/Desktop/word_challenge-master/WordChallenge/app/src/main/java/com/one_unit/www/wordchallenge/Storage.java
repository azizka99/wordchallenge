package com.one_unit.www.wordchallenge;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Created by Ibragimovaziz.
 */

public class Storage {
    final private int MIN_NUMBER_OF_LETTERS = 4;
    final private int MAX_NUMBER_OF_LETTERS = 8;
    final private int NUMBER_OF_DICT_FILES = MAX_NUMBER_OF_LETTERS - MIN_NUMBER_OF_LETTERS + 1;
    private int wordLength = 4;
    private int level = 0;
    private int score = 0;
    private int[] wordsUsed = {0, 0, 0, 0, 0};
    private int[] wordsUsedInSession = {0, 0, 0, 0, 0};
    private String locale = null;

    private Queue<String> savedWords;
    private Set<String> bufferWordSet = null;
    private SharedPreferences.Editor edit ;

    private boolean isSavedWords = false;
    private boolean isSavedData = false;

    private SharedPreferences settings;

    private final String INDEX_LEVEL = "Level";
    private final String INDEX_WORD_LENGTH = "Word length";
    private final String INDEX_SCORE = "Score";
    private final String INDEX_WORDS = "Words";
    private final String INDEX_LANGUAGE = "Chosen language";
    private final String INDEX_WORDS_USED = "Words used";
    private final String INDEX_SAVED_DATA = "Data saved";
    private final String INDEX_WORDS_USED_SESSION = "Words used in last session";

    public Storage(Context context) {
        this.settings = PreferenceManager.getDefaultSharedPreferences(context);

        this.edit = settings.edit();
        this.isSavedData = settings.getBoolean(INDEX_SAVED_DATA, false);
        if(isSavedData) {
            readSavedData();
        }
    }

    private void readSavedData() {
        this.wordLength = settings.getInt(INDEX_WORD_LENGTH, 0);
        if(wordLength == 0) {
            wordLength = MIN_NUMBER_OF_LETTERS;
        }

        this.level = settings.getInt(INDEX_LEVEL, 0);
        this.score = settings.getInt(INDEX_SCORE, 0);

        for(int i = 0; i < NUMBER_OF_DICT_FILES; i++){
            this.wordsUsed[i] = this.settings.getInt(INDEX_WORDS_USED + (i + MIN_NUMBER_OF_LETTERS), 0);
            this.wordsUsedInSession[i] = this.settings.getInt(INDEX_WORDS_USED_SESSION + (i + MIN_NUMBER_OF_LETTERS), 0);
        }

        this.locale = settings.getString(INDEX_LANGUAGE, null);
        this.bufferWordSet = settings.getStringSet(INDEX_WORDS + "_" + this.getLocale() + wordLength, null);
        if(this.bufferWordSet != null) {
            this.savedWords = new LinkedList<>(new ArrayList<>(bufferWordSet).
                    subList(wordsUsed[wordLength - MIN_NUMBER_OF_LETTERS] + 1, bufferWordSet.size()));
            this.isSavedWords = true;
        }
    }

    public Queue<String> getSavedWords() {
        return savedWords;
    }

    public void saveWords(Queue<String> wordQueue, int wordLength) throws IOException {
        this.wordLength = wordLength;
        this.bufferWordSet = new HashSet<String>(wordQueue);
        this.edit.putStringSet(INDEX_WORDS + "_" + this.getLocale() + wordLength, this.bufferWordSet).apply();
        this.isSavedData = true;
        this.putIsSavedData();
    }

    private void putIsSavedData() {
        this.edit.putBoolean(INDEX_SAVED_DATA, isSavedData).apply();
    }

    public void addLevel() {
        this.level++;
        this.edit.putInt(INDEX_LEVEL, level).apply();
        this.edit.putInt(INDEX_SCORE, score).apply();
    }

    public void setLevel(int level) {
        this.level = level;
        this.edit.putInt(INDEX_LEVEL, level).apply();
    }

    public void  purgeData() {
        savedWords = null;
        if(wordsUsed != null)
            Arrays.fill(wordsUsed, 0);

        this.edit.remove(INDEX_LEVEL);
        this.edit.remove(INDEX_WORD_LENGTH );
        this.edit.remove(INDEX_SCORE);
        this.edit.remove(INDEX_WORDS);
        this.edit.remove(INDEX_LANGUAGE);
        this.edit.remove(INDEX_SAVED_DATA);
        for(int i = 0; i < NUMBER_OF_DICT_FILES; i++){
            this.edit.remove(INDEX_WORDS_USED + (i + MIN_NUMBER_OF_LETTERS));
            this.edit.remove(INDEX_WORDS_USED_SESSION + (i + MIN_NUMBER_OF_LETTERS));
        }
    }

    public void setLocale(String locale) {
        this.locale = locale;
        this.edit.putString(INDEX_LANGUAGE, locale);
    }

    public int getWordsUsedInSession(int wordLength) {
        return this.wordsUsedInSession[wordLength - MIN_NUMBER_OF_LETTERS];
    }

    public void addUsedWordsCounter(int wordLength) {
        this.wordsUsed[wordLength - MIN_NUMBER_OF_LETTERS]++;
        this.edit.putInt(INDEX_WORDS_USED + wordLength, this.wordsUsed[wordLength - MIN_NUMBER_OF_LETTERS]).apply();
        this.wordsUsedInSession[wordLength - MIN_NUMBER_OF_LETTERS]++;
        this.edit.putInt(INDEX_WORDS_USED_SESSION + wordLength, this.wordsUsedInSession[wordLength - MIN_NUMBER_OF_LETTERS]).apply();
    }

    public void saveWordLength(){
        this.edit.putInt(INDEX_WORD_LENGTH, wordLength).apply();
    }

    public void addScore(int score) {
        this.score += score;
    }

    public int getWordLength() {
        return this.wordLength;
    }

    public void setWordLength(int wordLength) {
        this.wordLength = wordLength;
    }

    public int getScore() {
        return this.score;
    }

    public int getLevel() {
        return this.level;
    }

    public boolean isSavedWords() {
        return this.isSavedWords;
    }

    public String getLocale() {
        return this.locale;
    }
}