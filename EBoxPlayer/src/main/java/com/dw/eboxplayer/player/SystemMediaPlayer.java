package com.dw.eboxplayer.player;

import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.HashMap;

/**
 * 日期：2022/5/11
 * <p>
 * 作者：xudiwei
 * <p>
 * 描述：
 */
public class SystemMediaPlayer extends AbstractPlayer {
    private MediaPlayer mMediaPlayer;
    private SurfaceHolder mSurfaceHolder;
    private int mStatus;
    private int mTargetStatus;

    private void initPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mStatus = STATUS_PREPARED;
                if (mTargetStatus == STATUS_PLAYING) {
                    start();
                }
                notifyOnPrepared();
            }
        });
        mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                notifyOnVideoSizeChanged(width, height);
            }
        });

        mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                notifyOnInfo(what);
                return false;
            }
        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mStatus = STATUS_ERROR;
                notifyOnError(what);
                return false;
            }
        });
        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                notifyOnBufferingUpdate(percent);
            }
        });
        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                notifyOnSeekCompletion();
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mStatus = STATUS_COMPLETE;
                notifyOnCompletion();
            }
        });

        bindSurfaceHolderToPlayer();
        mStatus = STATUS_IDLE;
    }

    private void bindSurfaceHolderToPlayer() {
        if (null != mSurfaceHolder && null != mMediaPlayer) {
            mMediaPlayer.setDisplay(mSurfaceHolder);
        }
    }

    @Override
    public void setDisplay(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = surfaceHolder;
        bindSurfaceHolderToPlayer();
    }

    @Override
    public void setDataSource(String path) {
        try {

            initPlayer();

            mMediaPlayer.setDataSource(path);
            mStatus = STATUS_PREPARING;
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDataSource(String path, HashMap<String, String> map) {
    }

    @Override
    public void start() {
        if (mStatus == STATUS_PREPARED || mStatus == STATUS_PAUSE) {
            mMediaPlayer.start();
            mStatus = STATUS_PLAYING;
        } else {
            mTargetStatus = STATUS_PLAYING;
        }
    }

    @Override
    public void pause() {
        if (null != mMediaPlayer) {
            mMediaPlayer.pause();
            mStatus = STATUS_PAUSE;
        }
    }

    @Override
    public void stop() {
        if (null != mMediaPlayer) {
            mMediaPlayer.stop();
            mStatus = STATUS_COMPLETE;
        }
    }

    @Override
    public void release() {
        if (mStatus != STATUS_NONE) {
            mMediaPlayer.release();
            mMediaPlayer.setDisplay(null);
            mMediaPlayer = null;
            mStatus = STATUS_NONE;
            mTargetStatus = STATUS_NONE;
        }
    }

    @Override
    public int getDuration() {
        if (null != mMediaPlayer) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (null != mMediaPlayer) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void seekTo(int milliSecond) {
        if (null != mMediaPlayer) {
            mMediaPlayer.seekTo(milliSecond);
        }
    }

    @Override
    public boolean isPlaying() {
        if (null != mMediaPlayer) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public void speed(float speed) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PlaybackParams params = mMediaPlayer.getPlaybackParams();
            params.setSpeed(speed);
            mMediaPlayer.setPlaybackParams(params);
        } else {
            //todo
        }

    }
}
