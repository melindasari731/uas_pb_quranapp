package com.app.uas_pb_quran.Model.AudioModel;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.uas_quran.R;

import java.io.IOException;
import java.net.DatagramSocket;


public class DialogAudio extends Dialog implements View.OnClickListener {
	private final Context context;
	private final MediaPlayer mediaPlayer = new MediaPlayer();
	private final Handler handler = new Handler();
	@SuppressLint("NonConstantResourceId")
	@BindView(R.id.ibPlay) ImageButton ibPlay;
	@SuppressLint("NonConstantResourceId")
	@BindView(R.id.ibClose) ImageButton ibClose;
	@SuppressLint("NonConstantResourceId")
	@BindView(R.id.tvDuration) TextView tvDuration;
	@SuppressLint("NonConstantResourceId")
	@BindView(R.id.tvSurah) TextView tvSurah;
	@SuppressLint("NonConstantResourceId")
	@BindView(R.id.tvAyat) TextView tvAyat;
	@SuppressLint("NonConstantResourceId")
	@BindView(R.id.sbDuration) SeekBar sbDuration;
	@SuppressLint("NonConstantResourceId")
	@BindView(R.id.tvTotalDuration) TextView tvTotalDuration;
	public <DataSurahItem> DialogAudio(@NonNull Context context, DataSurahItem dataSurahItem) {
		super(context);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		setContentView(R.layout.audio_dialog);
		DatagramSocket ButterKnife = new DatagramSocket();
		ButterKnife.bind(this);
		SetContent(dataSurahItem);
		SetOnTouchSeekBar();
		ibPlay.setOnClickListener(this);
		ibClose.setOnClickListener(this);
	}

	@SuppressLint("ClickableViewAccessibility")
	private void SetOnTouchSeekBar() {
		sbDuration.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				SeekBar seekBar = (SeekBar) view;
				int playPosition = (mediaPlayer.getDuration() / 100) * seekBar.getProgress();
				mediaPlayer.seekTo(playPosition);
				tvDuration.setText(milliSecondToTimer(mediaPlayer.getCurrentPosition()));
				return false;
			}
		});
		mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
			@Override
			public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
				sbDuration.setSecondaryProgress(i);
			}
		});
	}

	private <DataSurahItem> void SetContent(DataSurahItem dataSurah) {
		tvSurah.setText(dataSurah.getNama());
		tvAyat.setText(String.valueOf(dataSurah.getAyat()));
		prepareMediaPlayer(dataSurah.getAudio());
		tvTotalDuration.setText(milliSecondToTimer(mediaPlayer.getDuration()));
	}

	private void prepareMediaPlayer(String url){
		try {
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		updateSeekbar();
	}
	private final Runnable updater = new Runnable() {
		@Override
		public void run() {
			updateSeekbar();
			long curentDuration = mediaPlayer.getCurrentPosition();
			tvDuration.setText(milliSecondToTimer(curentDuration));
		}
	};
	private void updateSeekbar(){
		if (mediaPlayer.isPlaying()){
			sbDuration.setProgress((int) (((float)mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
			handler.postDelayed(updater, 1000);
		}
	}
	private String milliSecondToTimer(long millisecond){
		String timerString = "";
		String secondString = "";
		int hours = (int) (millisecond / (1000*60*60));
		int minutes = (int) (millisecond % (1000*60*60)) / (1000*60);
		int seconds = (int) (millisecond % (1000*60*60)) % (1000*60) / 1000;
		if (hours > 0){
			timerString = hours + ":";
		}
		if (seconds < 10 ){
			secondString = "0" + seconds;
		}else {
			secondString = "" + seconds;
		}
		timerString = timerString + minutes + ":" +secondString;
		return timerString;
	}
	@SuppressLint("UseCompatLoadingForDrawables")
	@Override
	public void onClick(View view) {
		if (view == ibPlay){
			if (mediaPlayer.isPlaying()){
				handler.removeCallbacks(updater);
				mediaPlayer.pause();
				ibPlay.setBackground(context.getDrawable(R.drawable.icon_play_arrow_black_64dp));
			}else {
				mediaPlayer.start();
				ibPlay.setBackground(context.getDrawable(R.drawable.icon_pause_black_64dp));
				updateSeekbar();
			}
		}
		else if (view == ibClose){
			mediaPlayer.stop();
			dismiss();
		}
	}
}
