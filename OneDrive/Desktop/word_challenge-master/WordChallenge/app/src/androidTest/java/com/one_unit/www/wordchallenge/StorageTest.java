package com.one_unit.www.wordchallenge;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by IbrahimovAziz.
 */
@RunWith(AndroidJUnit4.class)
public class StorageTest  {
    @Test
    public void shit() throws IOException {
        Storage storage = new Storage(InstrumentationRegistry.getTargetContext());
        storage.readWords(4, 100);
        storage.saveWords();
        Set <String> someShit = storage.readWordsFromStorage();
        int counter = 0;
        for(String word : someShit){
            Log.i("",word + String.valueOf(counter++));
        }
        storage.addLevel();
        storage.readLevel();
        storage.addLevel();
        storage.readLevel();
        storage.addLevel();
        storage.readLevel();
        storage.addLevel();
        storage.readLevel();
        storage.addLevel();
        storage.readLevel();

        Log.i("", String.valueOf(storage.getLevel()));
    }
}