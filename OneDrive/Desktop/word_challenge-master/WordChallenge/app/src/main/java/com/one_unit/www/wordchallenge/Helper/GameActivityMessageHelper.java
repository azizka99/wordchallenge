package com.one_unit.www.wordchallenge.Helper;

import android.content.Context;
import android.os.Message;

import com.one_unit.www.wordchallenge.GameActivity;
import com.one_unit.www.wordchallenge.UI.GameActivityController;

/**
 * Created by AzizIbrahimov.
 */

public class GameActivityMessageHelper {

    private Context context;
    private GameActivityController gameActivityController;
    public final static String CHECK_TEXT_MESSAGE = "check_text";

    public GameActivityMessageHelper(Context context, GameActivityController gameActivityController){
        this.context = context;
        this.gameActivityController = gameActivityController;
    }

    /**
     * Send message to the gameactivity
     * @param msg
     */
    public void sendMessage(String msg){
        Message message = new Message();
        message.obj = "check_text";
        ((GameActivity) this.context).getHandler().sendMessage(message);
    }


}
