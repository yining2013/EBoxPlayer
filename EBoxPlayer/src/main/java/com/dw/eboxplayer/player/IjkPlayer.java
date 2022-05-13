package com.dw.eboxplayer.player;

import android.media.AudioManager;
import android.util.Log;
import android.view.SurfaceHolder;

import androidx.appcompat.view.menu.MenuPresenter;

import java.io.IOException;
import java.util.HashMap;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 日期：2022/5/12
 * <p>
 * 作者：xudiwei
 * <p>
 * 描述：
 */
public class IjkPlayer extends AbstractPlayer {
    private static final String TAG = "IjkPlayer";
    private IjkMediaPlayer mPlayer;
    private SurfaceHolder mSurfaceHolder;
    private int mStatus;
    private int mTargetStatus;

    private void initPlayer() {
        mPlayer = new IjkMediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
        mPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                mStatus = STATUS_PREPARED;
                if(mTargetStatus==STATUS_PLAYING){
                    start();
                }
                notifyOnPrepared();
                Log.d(TAG, "onPrepared: ");
            }
        });
        mPlayer.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                notifyOnInfo(i);
                return false;
            }
        });
        mPlayer.setOnVideoSizeChangedListener(new IMediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
                notifyOnVideoSizeChanged(i, i1);
            }
        });
        mPlayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                notifyOnError(i);
                mStatus = STATUS_ERROR;
                return false;
            }
        });
        mPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                notifyOnCompletion();
                mStatus = STATUS_COMPLETE;
            }
        });
        mPlayer.setOnSeekCompleteListener(new IMediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {
                notifyOnSeekCompletion();
            }
        });
        mPlayer.setOnBufferingUpdateListener(new IMediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
                notifyOnBufferingUpdate(i);
            }
        });

        bindSurfaceToPlayer();
        mStatus = STATUS_IDLE;
    }

    private void bindSurfaceToPlayer() {
        if (null != mPlayer && null != mSurfaceHolder) {
            mPlayer.setDisplay(mSurfaceHolder);
        }
    }

    @Override
    public void setDisplay(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = surfaceHolder;
        bindSurfaceToPlayer();
    }

    @Override
    public void setDataSource(String path) {
        try {
            if (mStatus != STATUS_NONE) {
                return;
            }
            initPlayer();
            mPlayer.setDataSource(path);
            mPlayer.prepareAsync();
            mStatus = STATUS_PLAYING;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDataSource(String path, HashMap<String, String> map) {
        // todo
    }

    @Override
    public void start() {
        if (mStatus == STATUS_PREPARED || mStatus == STATUS_PAUSE) {
            mPlayer.start();
            mStatus = STATUS_PLAYING;
        } else {
            mTargetStatus = STATUS_PLAYING;
        }
    }

    @Override
    public void pause() {
        mPlayer.pause();
        mStatus = STATUS_PAUSE;
    }

    @Override
    public void stop() {
        mPlayer.stop();
        mStatus = STATUS_COMPLETE;
    }

    @Override
    public void release() {
        mPlayer.release();
        mPlayer.setDisplay(null);
        mPlayer = null;
        mTargetStatus = mStatus = STATUS_NONE;

    }

    @Override
    public int getDuration() {
        if (null != mPlayer) {
            return (int) mPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (null != mPlayer) {
            return (int) mPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void seekTo(int milliSecond) {
        if (null != mPlayer) {
            mPlayer.seekTo(milliSecond);
        }
    }

    @Override
    public boolean isPlaying() {
        if (null != mPlayer) {
            return mPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public void speed(float speed) {
        if (null != mPlayer) {
            mPlayer.setSpeed(speed);
        }
    }
}
