package com.example.dataclass;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import com.example.acupuncture.R;

import java.io.IOException;

public class MusicService extends Service {
    public MusicService() {
    }

    private MediaPlayer mediaPlayer = null;
    private boolean isReady = false;

    @Override
    public void onCreate() {
        //onCreate在Service的生命週期中只會呼叫一次
        super.onCreate();
        //初始化媒體播放器
        mediaPlayer = MediaPlayer.create(this, R.raw.song);
        if (mediaPlayer == null) {
            return;
        }
        mediaPlayer.stop();
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mp.release();
                stopSelf();
                return false;
            }
        });
        try {
            mediaPlayer.prepare();
            isReady = true;
        } catch (IOException e) {
            e.printStackTrace();
            isReady = false;
        }
        if (isReady) {
            //將背景音樂設定為迴圈播放
            mediaPlayer.setLooping(true);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //每次呼叫Context的startService都會觸發onStartCommand回撥方法
        //所以onStartCommand在Service的生命週期中可能會被呼叫多次
        if (isReady && !mediaPlayer.isPlaying()) {
            //播放背景音樂
            mediaPlayer.start();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //該Service中不支援bindService方法，所以此處直接返回null
        return null;
    }

    @Override
    public void onDestroy() {
        //當呼叫Context的stopService或Service內部執行stopSelf方法時就會觸發onDestroy回撥方法
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                //停止播放音樂
                mediaPlayer.stop();
            }
            //釋放媒體播放器資源
            mediaPlayer.release();
        }
    }
}
