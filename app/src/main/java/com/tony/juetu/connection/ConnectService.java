package com.tony.juetu.connection;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

public class ConnectService extends IntentService {

    private final static String  TAG = ConnectService.class.getSimpleName();
    public static final String NAME = "name";
    public static final String PASSWORD = "password";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent){
        if (intent != null)
        {
            String name = intent.getStringExtra(NAME);
            String password = intent.getStringExtra(PASSWORD);
            XmppConnection connection = new XmppConnection(this,name,password);
            try {
                connection.connect();
            }catch (IOException e)
            {
                e.fillInStackTrace();
            }catch (SmackException e1)
            {
                e1.fillInStackTrace();
            }catch (XMPPException e)
            {
                e.fillInStackTrace();
            }
            catch (InterruptedException e)
            {
                e.fillInStackTrace();
            }

        }
    }

    public ConnectService(String name) {
        super(name);
    }

    public ConnectService() {
        super("ConnectService");
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "=>onCreate");

        super.onCreate();
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
