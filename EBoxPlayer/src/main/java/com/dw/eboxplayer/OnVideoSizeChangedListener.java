package com.dw.eboxplayer;

import com.dw.eboxplayer.player.IPlayer;

/**
 * 日期：2022/5/11
 * <p>
 * 作者：xudiwei
 * <p>
 * 描述：
 */
public abstract class OnVideoSizeChangedListener implements IPlayer.PlayerListener {
    @Override
    public void onPrepared(IPlayer player) {

    }

    @Override
    public abstract void onVideoSizeChanged(IPlayer player, int videoWidth, int videoHeight);

    @Override
    public void onBufferingUpdate(IPlayer player, int percent) {

    }

    @Override
    public void onInfo(IPlayer player, int infoCode) {

    }

    @Override
    public void onError(IPlayer player, int errorCode) {

    }

    @Override
    public void onSeekCompletion(IPlayer player) {

    }

    @Override
    public void onCompletion(IPlayer player) {

    }
}
