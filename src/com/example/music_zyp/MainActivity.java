package com.example.music_zyp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class MainActivity extends Activity {
	private ImageView imageView;
	private ListView listView;
	private ImageButton imageButton;
//	private Button btn_play,btn_pre,btn_next;
	private TextView music_name,player;
	private List<Mp3> songs;// 歌曲集合
//	private List<String> singers;// 歌手集合
//	private List<String> al_playlist;// 播放列表集合
	private SimpleAdapter adapter;
	public static final int SONGS_LIST = 2;//适配器加载的数据是歌曲列表
	private MusicPlayService mService;
	private MyApplication application;
	
	private int cur_pos = 5;// 当前显示的一行

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		application = (MyApplication) getApplication();///*****
		initView();
		allsongsView();//显示所有的歌曲
		
		imageButton.setBackgroundResource(R.drawable.pause);			

		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);// 一定要设置这个属性，否则ListView不会刷新
				
		listView.setOnItemClickListener(new OnItemClickListener() {//listView条目点击调准
			Intent it = new Intent();
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					
					if(position == cur_pos){
						listView.setBackgroundColor(Color.RED);						
					}
				
					if(null == mService){
						mService = application.getmService();
					}
					mService.setCurrentListItme(position);
					mService.setSongs(songs);
					mService.playMusic(songs.get(position).getUrl());
					

					music_name.setText(mService.getSongName());
					music_name.setHorizontallyScrolling(true);
//					music_name.setFocusable(true);
					player.setText(mService.getSingerName());
					
					it.setClass(MainActivity.this, MusicPlayActivity.class);
					Log.i("zyp", "startActivity to MusicPlayActivity\n");
					startActivity(it);
			}
		});
		
		imageView.setOnClickListener(new OnClickListener(){//主页图标点击跳转			
			Intent it = new Intent();

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub	
				if(null == mService){//第一次打开时就点击图标，显示第一条目录
					mService = application.getmService();
					mService.setCurrentListItme(1);
					mService.setSongs(songs);
					mService.playMusic(songs.get(1).getUrl());
				}
				
				music_name.setText(mService.getSongName());
				player.setText(mService.getSingerName());
				
				it.setClass(MainActivity.this, MusicPlayActivity.class);
				Log.i("zyp", "startActivity to MusicPlayActivity\n");
				startActivity(it);
			}
		});
		
		imageButton.setOnClickListener(new OnClickListener(){//主页播放暂停按钮
			@Override
			public void onClick(View view) {				
				mService.pausePlay();
				if (mService.isPlay()) {
					imageButton.setBackgroundResource(R.drawable.pause);
				} else {
					imageButton.setBackgroundResource(R.drawable.play);
				}
			}			
		});
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		listView = (ListView) this.findViewById(R.id.listView1);
		player = (TextView) this.findViewById(R.id.textView1);
		music_name = (TextView) this.findViewById(R.id.textView2);
		imageView = (ImageView) this.findViewById(R.id.imageView1);
		imageButton = (ImageButton) this.findViewById(R.id.imageButton1);
	}
	/*
	 * 显示所有的歌曲
	 */
	private void allsongsView() {
		// TODO Auto-generated method stub
		songs = MusicUtils.getAllSongs(MainActivity.this);
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < songs.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", songs.get(i).getSqlId());
			map.put("songName", songs.get(i).getName());
			if (songs.get(i).getSingerName().equals("<unknown>")) {
				map.put("singerName", "----");
			} else {
				map.put("singerName", songs.get(i).getSingerName());
			}
			listItems.add(map);
		}
		adapter = new SimpleAdapter(MainActivity.this, listItems, 
									R.layout.item4music_main_activity, 
									new String[] { "id", "songName", "singerName" }, 
									new int[] { R.id.tv_id,
									R.id.tv_songName, R.id.tv_singerName });
		listView.setAdapter(adapter);
	}
	

}
