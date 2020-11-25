package com.ttsea.commonlibrary.utils;

import android.os.Handler;
import android.os.Message;

import com.ttsea.commonlibrary.debug.JLog;

/**
 * 计时器 <br>
 * <p>
 * <b>more:</b>更多请点 <a href="http://www.ttsea.com" target="_blank">这里</a> <br>
 * <b>date:</b> 2017/4/10 9:55 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
final public class MyTimer {
    private final static String TAG = "Utils.MyTimer";

    private final static long DEFAULT_FREQUENCY_MILLIS = 1 * 1000;
    private final static long DEFAULT_TIME_MILLIS = 10 * 1000;
    private final static int MESSAGE_WHAT_START = 0x002;
    private final static int MESSAGE_WHAT_RUNNING = 0x003;

    private THandler mHandler;

    private long originEndTimeMillis;

    public MyTimer() {
        this(DEFAULT_TIME_MILLIS);
    }

    public MyTimer(long endTimeMillis) {
        this(endTimeMillis, DEFAULT_FREQUENCY_MILLIS);
    }

    public MyTimer(long endTimeMillis, long frequencyMillis) {
        this(endTimeMillis, frequencyMillis, null);
    }

    public MyTimer(long endTimeMillis, long frequencyMillis, OnTimerListener listener) {
        this.originEndTimeMillis = endTimeMillis;

        mHandler = new THandler();
        setEndTimeMillis(endTimeMillis);
        setFrequencyMillis(frequencyMillis);
        setOnTimerListener(listener);
    }

    public void setEndTimeMillis(long endTimeMillis) {
        mHandler.setEndTimeMillis(endTimeMillis);
    }

    public void setFrequencyMillis(long frequencyMillis) {
        mHandler.setFrequencyMillis(frequencyMillis);
    }

    public void setOnTimerListener(OnTimerListener l) {
        mHandler.setOnTimerListener(l);
    }

    public boolean isRunning() {
        return mHandler.isRunning();
    }

    /** 开始计时 */
    public void start() {
        mHandler.start();
    }

    /** 重新开始计时 */
    public void restart() {
        mHandler.setEndTimeMillis(originEndTimeMillis);
        mHandler.start();
    }

    /** 停止计时 */
    public void stop(boolean callCallback) {
        mHandler.stop(callCallback);
    }

    private static class THandler extends Handler {

        private Runnable mStartRunnable = new Runnable() {
            @Override
            public void run() {
                sendEmptyMessage(MyTimer.MESSAGE_WHAT_START);
            }
        };

        private Runnable mRunningRunnable = new Runnable() {
            @Override
            public void run() {
                sendEmptyMessage(MyTimer.MESSAGE_WHAT_RUNNING);
            }
        };

        private OnTimerListener onTimerListener;

        /** 每次更新的频率，默认为1秒 */
        private long frequencyMillis = DEFAULT_FREQUENCY_MILLIS;
        /** 总共计时时间，毫秒 */
        private long endTimeMillis = DEFAULT_TIME_MILLIS;

        private boolean mRunning = false;

        public OnTimerListener getOnTimerListener() {
            return onTimerListener;
        }

        public void setOnTimerListener(OnTimerListener onTimerListener) {
            this.onTimerListener = onTimerListener;
        }

        public long getFrequencyMillis() {
            return frequencyMillis;
        }

        public void setFrequencyMillis(long frequencyMillis) {
            this.frequencyMillis = frequencyMillis;
            checkParam();
        }

        public long getEndTimeMillis() {
            return endTimeMillis;
        }

        public void setEndTimeMillis(long endTimeMillis) {
            this.endTimeMillis = endTimeMillis;
            checkParam();
        }

        public boolean isRunning() {
            return mRunning;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_WHAT_START:
                    if (onTimerListener != null) {
                        onTimerListener.onTimer(endTimeMillis);
                    }
                    mRunning = true;

                    sendLocalMessage(MESSAGE_WHAT_RUNNING, frequencyMillis);
                    break;

                case MESSAGE_WHAT_RUNNING:
                    endTimeMillis = endTimeMillis - frequencyMillis;
                    if (endTimeMillis > 0) {
                        if (onTimerListener != null) {
                            onTimerListener.onTimer(endTimeMillis);
                        }
                        mRunning = true;

                        if (endTimeMillis > frequencyMillis) {
                            sendLocalMessage(MESSAGE_WHAT_RUNNING, frequencyMillis);
                        } else {
                            sendLocalMessage(MESSAGE_WHAT_RUNNING, endTimeMillis);
                        }
                    } else {
                        if (onTimerListener != null) {
                            onTimerListener.onTimerEnd();
                        }
                        mRunning = false;
                    }
                    break;

                default:
                    break;
            }

        }

        /** 开始计时 */
        public void start() {
            JLog.d(TAG, "Timer started");
            clearMessage();
            sendLocalMessage(MyTimer.MESSAGE_WHAT_START, 0);
        }

        /** 停止计时 */
        public void stop(boolean callCallback) {
            JLog.d(TAG, "Timer stoped");
            clearMessage();
            if (onTimerListener != null && callCallback) {
                onTimerListener.onTimerEnd();
            }
            mRunning = false;
        }

        private void checkParam() {
            if (endTimeMillis <= 0) {
                throw new IllegalArgumentException("endTimeMillis must greater than 0");
            }

            if (frequencyMillis <= 0) {
                throw new IllegalArgumentException("frequencyMillis must greater than 0");
            }
        }

        private void clearMessage() {
            removeMessages(MESSAGE_WHAT_START);
            removeMessages(MESSAGE_WHAT_RUNNING);
            removeCallbacks(mStartRunnable);
            removeCallbacks(mRunningRunnable);
            mRunning = false;
        }

        private void sendLocalMessage(int what, long delayMillis) {
            if (what == MyTimer.MESSAGE_WHAT_START) {
                postDelayed(mStartRunnable, delayMillis);

            } else if (what == MyTimer.MESSAGE_WHAT_RUNNING) {
                postDelayed(mRunningRunnable, delayMillis);
            }
        }
    }

    public interface OnTimerListener {
        /** 计时中，residualTimeMills剩余时间 */
        void onTimer(long residualTimeMillis);

        /** 计时结束 */
        void onTimerEnd();
    }
}