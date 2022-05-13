package com.dw.eboxplayer.player;

import android.view.SurfaceHolder;

import java.util.HashMap;

/**
 * 日期：2022/5/11
 * <p>
 * 作者：xudiwei
 * <p>
 * 描述：播放器的抽象接口，声明了播放器的功能状态。
 */
public interface IPlayer {
    //未初始化
    int STATUS_NONE = 0;
    //空闲
    int STATUS_IDLE = 1;
    //准备中
    int STATUS_PREPARING = 2;
    //准备完成
    int STATUS_PREPARED = 3;
    //播放中
    int STATUS_PLAYING = 4;
    //暂时中
    int STATUS_PAUSE = 5;
    //出错
    int STATUS_ERROR = 6;
    //播放完成
    int STATUS_COMPLETE = 7;

    void setDisplay(SurfaceHolder surfaceHolder);

    void setDataSource(String path);

    void setDataSource(String path, HashMap<String, String> map);

    void start();

    void pause();

    void stop();

    void release();

    int getDuration();

    int getCurrentPosition();

    void seekTo(int milliSecond);

    boolean isPlaying();

    void speed(float speed);

    void addPlayerListener(PlayerListener listener);

    void removePlayerListener(PlayerListener listener);


    interface PlayerListener {

        /**
         * 播放器准备完成回调
         *
         * @param player
         */
        void onPrepared(IPlayer player);

        /**
         * 播放源的视频大小发生改变时回调
         *
         * @param player
         * @param videoWidth  视频的宽
         * @param videoHeight 视频的高
         */
        void onVideoSizeChanged(IPlayer player, int videoWidth, int videoHeight);

        /**
         * 视频加载缓冲更新回调
         *
         * @param player
         * @param percent
         */
        void onBufferingUpdate(IPlayer player, int percent);

        /**
         * 视频信息获取回调
         *
         * @param player
         * @param infoCode
         */
        void onInfo(IPlayer player, int infoCode);

        /**
         * 播放出错时回调
         *
         * @param player
         * @param errorCode
         */
        void onError(IPlayer player, int errorCode);

        /**
         * 播放进度拖动改变时回调
         *
         * @param player
         */
        void onSeekCompletion(IPlayer player);

        /**
         * 播放完成回调
         *
         * @param player
         */
        void onCompletion(IPlayer player);
    }

}
