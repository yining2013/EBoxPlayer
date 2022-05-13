package com.dw.eboxplayer.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dw.eboxplayer.R;
import com.dw.eboxplayer.player.IPlayer;
import com.dw.eboxplayer.utils.TimeUtil;

/**
 * 日期：2022/5/11
 * <p>
 * 作者：xudiwei
 * <p>
 * 描述：
 */
public class SimpleUIPanel extends FrameLayout implements IPanel, IPlayer.PlayerListener {
    private static final String TAG = "SimpleUIPanel";
    private SeekBar mSeekBar;
    private ImageView mIvIcon;
    private TextView mTvProcess;
    private TextView mTvTotal;

    private IPlayer mPlayer;

    public SimpleUIPanel(@NonNull Context context) {
        this(context, null);
    }

    public SimpleUIPanel(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleUIPanel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_simple_ui_panel, this);
        mSeekBar = findViewById(R.id.seek_bar);
        mIvIcon = findViewById(R.id.iv_player);
        mTvProcess = findViewById(R.id.tv_process);
        mTvTotal = findViewById(R.id.tv_total);

        mIvIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer.isPlaying()) {
                    play(false);
                } else {
                    play(true);
                }
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener());

    }

    private void play(boolean play) {
        if (play) {
            mPlayer.start();
            mIvIcon.setImageResource(R.drawable.ic_pause);
            post(mUpdateProcessRunnable);
        } else {
            mPlayer.pause();
            mIvIcon.setImageResource(R.drawable.ic_play);
            removeCallbacks(mUpdateProcessRunnable);
        }
    }

    @Override
    public void setPlayer(IPlayer player) {
        this.mPlayer = player;
        mPlayer.addPlayerListener(this);
    }

    public void setData(String url) {
        mPlayer.setDataSource(url);
    }

    private final Runnable mUpdateProcessRunnable = new Runnable() {
        @Override
        public void run() {
            updateProcess();
            postDelayed(mUpdateProcessRunnable, 1000);
        }
    };

    private void updateProcess() {
        mTvProcess.setText(TimeUtil.getVideoTime(mPlayer.getCurrentPosition()));
        mSeekBar.setProgress(TimeUtil.toSecond(mPlayer.getCurrentPosition()));
    }


    @Override
    public void onPrepared(IPlayer player) {
        mTvTotal.setText(TimeUtil.getVideoTime(mPlayer.getDuration()));
        mTvProcess.setText(TimeUtil.getVideoTime(mPlayer.getCurrentPosition()));
        mSeekBar.setMax(TimeUtil.toSecond(mPlayer.getDuration()));
        Log.d(TAG, "onPrepared: ");
    }

    @Override
    public void onVideoSizeChanged(IPlayer player, int videoWidth, int videoHeight) {

    }

    @Override
    public void onBufferingUpdate(IPlayer player, int percent) {
        int secondProcess = mSeekBar.getMax() * percent / 100;
        mSeekBar.setSecondaryProgress(secondProcess);
        Log.d(TAG, "onBufferingUpdate: secondProcess: " + secondProcess);
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

    private class OnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                Log.d(TAG, "onProgressChanged: "+progress);
               mTvProcess.setText(TimeUtil.getVideoTime(progress*1000));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            removeCallbacks(mUpdateProcessRunnable);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            post(mUpdateProcessRunnable);
            int progress = seekBar.getProgress();
            mPlayer.seekTo(progress * 1000);

        }
    }
}
