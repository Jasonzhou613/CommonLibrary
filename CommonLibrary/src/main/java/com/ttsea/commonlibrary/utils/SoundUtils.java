package com.ttsea.commonlibrary.utils;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.ttsea.commonlibrary.debug.JLog;

import java.io.IOException;

final public class SoundUtils {
    private final static String TAG = "Utils.SoundUtils";

    /**
     * 播放声音提示
     *
     * @param activity 上下文
     * @param resId    声音资源Id
     * @param listener 播放完成监听
     */
    public static void playSound(Activity activity, int resId, OnCompletionListener listener) {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        MediaPlayer player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnCompletionListener(listener);

        try {
            AssetFileDescriptor file = activity.getResources().openRawResourceFd(resId);
            player.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            file.close();
            player.setVolume(0.5f, 0.5f);
            player.prepare();
            player.start();

        } catch (IOException e) {
            JLog.e(TAG, "IOException e:" + e.getMessage());
            e.printStackTrace();
            player = null;
        }
    }
}
