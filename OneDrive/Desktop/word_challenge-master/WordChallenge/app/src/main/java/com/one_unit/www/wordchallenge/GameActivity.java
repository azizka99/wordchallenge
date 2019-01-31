package com.one_unit.www.wordchallenge;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.one_unit.www.wordchallenge.Helper.ViewHelper;
import com.one_unit.www.wordchallenge.UI.GameActivityController;

import java.io.IOException;


/**
 * Created by IbrahimovAziz.
 */

public class GameActivity extends Activity {

    private TextView[] textViews;
    private TextView countDownTimerTextView;
    private final String COUNT_DOWN_TIMER_START = "00:";
    private final String COUNT_DOWN_TIMER_ZERO_SUFFIX = "0";
    private final String COUND_DOWN_TIMER_FINISHED = "00:00";
    private final int COUNT_DOWN_MAX_TIME_IN_MILLIS = 30000;
    private RelativeLayout letterPlaceholderRelativeLayout[];
    private RelativeLayout letterPlaceHolderParent;
    private RelativeLayout letterRelativeLayout;
    private DisplayMetrics displayMetrics;
    private GameActivityController gameActivityController;
    private int numberOfActiveLetters;
    private boolean gameHasStarted = false;
    private Logic logic;

    private Handler handler;

    private char[] word;

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        this.countDownTimerTextView = (TextView) findViewById(R.id.count_down_time_text_view);

        this.textViews = new TextView[10];

        this.textViews[0] = (TextView) findViewById(R.id.text_view_letter_1);
        this.textViews[1] = (TextView) findViewById(R.id.text_view_letter_2);
        this.textViews[2] = (TextView) findViewById(R.id.text_view_letter_3);
        this.textViews[3] = (TextView) findViewById(R.id.text_view_letter_4);
        this.textViews[4] = (TextView) findViewById(R.id.text_view_letter_5);
        this.textViews[5] = (TextView) findViewById(R.id.text_view_letter_6);
        this.textViews[6] = (TextView) findViewById(R.id.text_view_letter_7);
        this.textViews[7] = (TextView) findViewById(R.id.text_view_letter_8);
        this.textViews[8] = (TextView) findViewById(R.id.text_view_letter_9);
        this.textViews[9] = (TextView) findViewById(R.id.text_view_letter_10);



        this.letterPlaceholderRelativeLayout = new RelativeLayout[10];

        this.letterPlaceholderRelativeLayout[0] = (RelativeLayout) findViewById(R.id.relative_layout_placeholder_0);
        this.letterPlaceholderRelativeLayout[1] = (RelativeLayout) findViewById(R.id.relative_layout_placeholder_1);
        this.letterPlaceholderRelativeLayout[2] = (RelativeLayout) findViewById(R.id.relative_layout_placeholder_2);
        this.letterPlaceholderRelativeLayout[3] = (RelativeLayout) findViewById(R.id.relative_layout_placeholder_3);
        this.letterPlaceholderRelativeLayout[4] = (RelativeLayout) findViewById(R.id.relative_layout_placeholder_4);
        this.letterPlaceholderRelativeLayout[5] = (RelativeLayout) findViewById(R.id.relative_layout_placeholder_5);
        this.letterPlaceholderRelativeLayout[6] = (RelativeLayout) findViewById(R.id.relative_layout_placeholder_6);
        this.letterPlaceholderRelativeLayout[7] = (RelativeLayout) findViewById(R.id.relative_layout_placeholder_7);
        this.letterPlaceholderRelativeLayout[8] = (RelativeLayout) findViewById(R.id.relative_layout_placeholder_8);
        this.letterPlaceholderRelativeLayout[9] = (RelativeLayout) findViewById(R.id.relative_layout_placeholder_9);


        this.letterRelativeLayout = (RelativeLayout) findViewById(R.id.activity_game_letters_relative_layout);

        this.letterPlaceHolderParent = (RelativeLayout) findViewById(R.id.placeholder_letters_relative_layout);
        this.logic = null;
        try{
            this.logic = new Logic(this);
            this.word = this.logic.getWord();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        this.numberOfActiveLetters = this.word.length;

        this.registerMessageHandler();
        this.registerTimer();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        this.generateContent();
    }

    private void generateContent(){
        if(gameHasStarted){
            return;
        }
        gameHasStarted = true;
        this.displayMetrics = this.getResources().getDisplayMetrics();
        this.gameActivityController = new GameActivityController(this.textViews, this.letterPlaceholderRelativeLayout, this.numberOfActiveLetters,
                this.letterRelativeLayout, this.letterPlaceHolderParent, this.displayMetrics.density);

        this.gameActivityController.placeLetters(this.word);
        this.gameActivityController.executeTextViewsDimensionMapping();
        this.gameActivityController.placeTextViews();
        this.gameActivityController.placePlaceHolders();
        this.letterRelativeLayout.setOnDragListener(new MyReverseDragListener(this.gameActivityController, this));
        for(int i = 0; i < this.numberOfActiveLetters; i++){
            this.textViews[i].setOnTouchListener(new MyTouchListener(this.gameActivityController, this));
        }

        for(int j = 0; j < this.numberOfActiveLetters; j++){
            ViewHelper.getChildTextView(this.letterPlaceholderRelativeLayout[j]).setOnTouchListener(
                    new MyReverseTouchListener(this.gameActivityController)
            );
            this.letterPlaceholderRelativeLayout[j].setOnDragListener(new MyDragListener(this.gameActivityController, this));
        }
    }

    private void registerMessageHandler(){

        this.handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Log.d("DEBUG", "Message arrived");
                String message = (String) msg.obj;
                if(message.equals("check_text")){
                    if(gameActivityController.checkIfWordIsFilled()){
                        if(logic.compareWords(gameActivityController.getWord())){
                            Toast.makeText(GameActivity.this, "Correct", Toast.LENGTH_SHORT).show();
                            showLevelFinishedAlertDialog();
                        }
                        else{
                            String word = new String(gameActivityController.getWord());
                            Toast.makeText(GameActivity.this, "Wrong = " + word,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        };
    }

    private void registerTimer(){
        this.countDownTimer = new CountDownTimer(COUNT_DOWN_MAX_TIME_IN_MILLIS, 1000) {
            @Override
            public void onTick(long millis) {
                if(millis / 1000 >= 10){
                    countDownTimerTextView.setText(COUNT_DOWN_TIMER_START + millis / 1000);
                }
                else{
                    countDownTimerTextView.setText(COUNT_DOWN_TIMER_START
                            + COUNT_DOWN_TIMER_ZERO_SUFFIX + millis / 1000);
                }
            }

            @Override
            public void onFinish() {
                countDownTimerTextView.setText(COUND_DOWN_TIMER_FINISHED);
            }
        };
        this.countDownTimer.start();
    }

    public Handler getHandler(){
        return this.handler;
    }

    private void showLevelFinishedAlertDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Success");
        alertDialog.setMessage("Go to next level");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(getIntent());
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }




}
