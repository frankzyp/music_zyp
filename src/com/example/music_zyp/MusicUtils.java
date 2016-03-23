package com.example.music_zyp;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;

public class MusicUtils {
	public static Cursor query(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		return query(context, uri, projection, selection, selectionArgs, sortOrder, 0);
	}

	public static Cursor query(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, int limit) {
		try {
			ContentResolver resolver = context.getContentResolver();
			if (resolver == null) {
				return null;
			}
			if (limit > 0) {
				uri = uri.buildUpon().appendQueryParameter("limit", "" + limit).build();
			}
			return resolver.query(uri, projection, selection, selectionArgs, sortOrder);
		} catch (UnsupportedOperationException ex) {
			return null;
		}
	}
	/**
	 * 得到媒体库中的全部歌曲
	 */
	public static ArrayList<Mp3> getAllSongs(Context context) {
		Cursor c = query(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST,MediaColumns.DATA },
				MediaStore.Audio.Media.IS_MUSIC + "=1", null, null);
		try {
			if (c == null || c.getCount() == 0) {
				return null;
			}
			int len = c.getCount();

			ArrayList<Mp3> list = new ArrayList<Mp3>();

			int id = c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
			int title = c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
			int name = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
			int url = c.getColumnIndex(MediaColumns.DATA);
			for (int i = 0; i < len; i++) {
				Mp3 mp3 = new Mp3();
				c.moveToNext();
				mp3.setSqlId(Integer.parseInt(c.getLong(id) + ""));
				mp3.setName(c.getString(title));
				mp3.setSingerName(c.getString(name));
				mp3.setUrl(c.getString(url));
				list.add(mp3);
			}

			return list;
		} finally {
			if (c != null) {
				c.close();
			}
		}
	}

}
