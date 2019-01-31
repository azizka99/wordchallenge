package com.one_unit.www.wordchallenge;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

/**
 * Created by  IbrahimovAziz.
 */
@RunWith(AndroidJUnit4.class)
public class LogicTest {
    @Test
    public void readWords() throws Exception {
        Logic logic = new Logic(InstrumentationRegistry.getTargetContext());
        char[] kaka = logic.getWord();
        System.out.println("SHIT 700");
        System.out.println(Arrays.toString(kaka));
        System.out.println(kaka.length);
    }

}