package com.example.music_zyp;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;


public class MusicPlayActivity extends Activity {
	private Button mFrontImageButton, mPauseImageButton, mNextImageButton;
	private TextView tv_songName, tv_singerName, tv_curcentTime, tv_allTime;
	private SeekBar seekBar1;// 播放进度条
	private MusicPlayService mService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_play);
		MyApplication application = (MyApplication) getApplication();
		mService = application.getmService();
		initView();
		setListener();
	}

	private void initView() {
		mFrontImageButton = (Button) findViewById(R.id.button3);
		mPauseImageButton = (Button) findViewById(R.id.button2);
		mNextImageButton = (Button) findViewById(R.id.button1);
		tv_songName = (TextView) findViewById(R.id.tv_songName);
		tv_singerName = (TextView) findViewById(R.id.tv_singerName);
		tv_curcentTime = (TextView) findViewById(R.id.textView1);
		tv_allTime = (TextView) findViewById(R.id.textView2);
		seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
		// 启动
		handler.post(updateThread);
	}

	Handler handler = new Handler();
	Runnable updateThread = new Runnable() {
		public void run() {
			// 获得歌曲的长度并设置成播放进度条的最大值
			seekBar1.setMax(mService.getDuration());
			// 获得歌曲现在播放位置并设置成播放进度条的值
			seekBar1.setProgress(mService.getCurrent());

			tv_songName.setText(mService.getSongName());
			tv_singerName.setText(mService.getSingerName());
			tv_curcentTime.setText(formatTime(mService.getCurrent()));
			tv_allTime.setText(formatTime(mService.getDuration()));
			// 每次延迟100毫秒再启动线程
			handler.postDelayed(updateThread, 100);
		}
	};

	private void setListener() {
		// 暂停or开始
		mPauseImageButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				mService.pausePlay();
				if (mService.isPlay()) {
					mPauseImageButton.setBackgroundResource(R.drawable.pause);
				} else {
					mPauseImageButton.setBackgroundResource(R.drawable.play);
				}
			}
		});

		// 下一首
		mNextImageButton.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mService.nextMusic();
			}
		});
		// 上一首
		mFrontImageButton.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mService.frontMusic();
			}
		});
		seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// fromUser判断是用户改变的滑块的值
				if (fromUser == true) {
					mService.movePlay(progress);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});
	}

	/**
	 * 格式化时间，将其变成00:00的形式
	 */
	public String formatTime(int time) {
		int secondSum = time / 1000;
		int minute = secondSum / 60;
		int second = secondSum % 60;

		String result = "";
		if (minute < 10)
			result = "0";
		result = result + minute + ":";
		if (second < 10)
			result = result + "0";
		result = result + second;

		return result;
	}
	// public boolean onKeyDown(int keyCode, KeyEvent event)
	// {
	// if ( keyCode == KeyEvent.KEYCODE_BACK)
	// {
	// mMediaPlayer.stop();
	// mMediaPlayer.release();
	// this.finish();
	// return true;
	// }
	// return super.onKeyDown(keyCode, event);
	// }
}
