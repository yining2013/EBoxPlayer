package com.dw.eboxplayer.player;

import java.util.ArrayList;
import java.util.List;

/**
 * 日期：2022/5/11
 * <p>
 * 作者：xudiwei
 * <p>
 * 描述：
 */
public abstract class AbstractPlayer implements IPlayer {
    private List<PlayerListener> mListeners = new ArrayList<>();

    @Override
    public void addPlayerListener(PlayerListener listener) {
        mListeners.add(listener);
    }

    @Override
    public void removePlayerListener(PlayerListener listener) {
        mListeners.remove(listener);
    }

    public void notifyOnPrepared() {
        for (PlayerListener listener : mListeners) {
            listener.onPrepared(this);
        }
    }

    public void notifyOnVideoSizeChanged(int videoWidth, int videoHeight) {
        for (PlayerListener listener : mListeners) {
            listener.onVideoSizeChanged(this, videoWidth, videoHeight);
        }
    }


    public void notifyOnInfo(int infoCode){
        for (PlayerListener listener : mListeners) {
            listener.onInfo(this, infoCode);
        }
    }

    public void notifyOnError(int errorCode){
        for (PlayerListener listener : mListeners) {
            listener.onError(this, errorCode);
        }
    }

    public void notifyOnSeekCompletion(){
        for (PlayerListener listener : mListeners) {
            listener.onSeekCompletion(this);
        }
    }

    public void notifyOnBufferingUpdate(int percent){
        for (PlayerListener listener : mListeners) {
            listener.onBufferingUpdate(this,percent);
        }
    }
    public void notifyOnCompletion(){
        for (PlayerListener listener : mListeners) {
            listener.onCompletion(this);
        }
    }

}
