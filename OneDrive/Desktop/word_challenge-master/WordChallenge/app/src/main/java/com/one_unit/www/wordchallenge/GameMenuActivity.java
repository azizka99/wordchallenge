package com.one_unit.www.wordchallenge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GameMenuActivity extends Activity {

    private Button playButton;
    private Button continueButton;
    private Button optionsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game_menu);
        long startTime = System.currentTimeMillis();
        WordReader wordReader = new WordReader();
        /*try{
            Log.d("WR DEBUG", wordReader.readFromFile(this, "test.txt"));
        }
        catch (IOException e){
            e.printStackTrace();
        }*/
        long totalTime = System.currentTimeMillis() - startTime;
        //totalTime/=1000;
        Log.d("WR DEBUG", "Execution time = " + totalTime + "ms");

        this.playButton = (Button) findViewById(R.id.main_menu_play_button);
        this.continueButton = (Button) findViewById(R.id.main_menu_continue_button);
        this.optionsButton = (Button) findViewById(R.id.main_menu_options_button);

        this.setOnClickListeners();
    }

    private void setOnClickListeners(){
        this.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameMenuActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
    }
}
