package br.com.devgeek.cartolaparciais.helper;

import android.util.Log;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

/**
 * Created by geovannefduarte
 */
public class NotificationHandler extends NotificationExtenderService {

    private static final String TAG = "NotificationHandler";

    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult notification){

        String groupMessage = notification.payload.groupMessage;

        Log.d(TAG, "onNotificationProcessing: groupMessage -> "+groupMessage);
        return false;
    }
}