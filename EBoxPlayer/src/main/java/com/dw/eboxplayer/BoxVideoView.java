package com.dw.eboxplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dw.eboxplayer.panel.IPanel;
import com.dw.eboxplayer.player.IPlayer;

/**
 * 日期：2022/5/11
 * <p>
 * 作者：xudiwei
 * <p>
 * 描述：
 */
public class BoxVideoView extends FrameLayout {
    private static final String TAG = "BoxVideoView";
    private IPlayer mPlayer;
    private IPanel mPanel;
    private VideoSurfaceView mVideoSurfaceView;
    private boolean mSurfaceCreated = false;

    public BoxVideoView(@NonNull Context context) {
        this(context, null);
    }

    public BoxVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BoxVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        mVideoSurfaceView = new VideoSurfaceView(getContext());
        FrameLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        addView(mVideoSurfaceView, params);
        mVideoSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                mSurfaceCreated = true;
                bindSurfaceToPlayer();
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

            }
        });
    }

    private void bindSurfaceToPlayer() {
        if (null != mPlayer && mSurfaceCreated) {
            mPlayer.setDisplay(mVideoSurfaceView.getHolder());
        }
    }

    /**
     * 设置播放器
     *
     * @param player
     */
    public void setPlayer(IPlayer player) {
        this.mPlayer = player;
        setPlayerToPanel();
        bindSurfaceToPlayer();
        if (null != mPlayer) {
            mPlayer.addPlayerListener(new OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(IPlayer player, int videoWidth, int videoHeight) {
                    mVideoSurfaceView.setVideoSize(videoWidth, videoHeight);
                }
            });
        }
    }

    /**
     * 设置控件面板
     *
     * @param panel
     */
    public void setPanel(IPanel panel) {
        if (!(panel instanceof View)) {
            throw new IllegalArgumentException("panel must is View subclass and implements IPanel");
        }
        this.mPanel = panel;
        setPlayerToPanel();

        FrameLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView((View) mPanel, params);
    }

    private void setPlayerToPanel() {
        if (null != mPlayer && null != mPanel) {
            mPanel.setPlayer(mPlayer);
        }
    }


}
