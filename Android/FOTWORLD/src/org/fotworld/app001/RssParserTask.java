package org.fotworld.app001;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Xml;
import android.widget.Toast;

public class RssParserTask extends AsyncTask<String, Integer, RssListAdapter> {
	private RssReaderActivity mActivity;
	private RssListAdapter mAdapter;
	private int return_code = 0;
	final private int NONETWORK = 1;
	final private int NOUPDATE = 2;
	
	// コンストラクタ
	public RssParserTask(RssReaderActivity activity, RssListAdapter adapter) {
		mActivity = activity;
		mAdapter = adapter;
	}

	// タスクを実行した直後にコールされる
	@Override
	protected void onPreExecute() {
    	Toast.makeText(mActivity, "Updating...", Toast.LENGTH_SHORT).show();
	}

	// バックグラウンドにおける処理を担う。タスク実行時に渡された値を引数とする
	@Override
	protected RssListAdapter doInBackground(String... params) {
		RssListAdapter result = null;
		try {
			// HTTP経由でアクセスし、InputStreamを取得する
			URL url = new URL(params[0]);
			InputStream is = url.openConnection().getInputStream();
			result = parseXml(is);
		} catch (Exception e) {
			return_code = NONETWORK;
			e.printStackTrace();
			return null;
		}
		// ここで返した値は、onPostExecuteメソッドの引数として渡される
		return result;
	}

	// メインスレッド上で実行される
	@Override
	protected void onPostExecute(RssListAdapter result) {
    	if (result == null){
    		switch (return_code){
    		case NONETWORK:
            	Toast.makeText(mActivity, "Network error", Toast.LENGTH_SHORT).show();
    			break;
    		case NOUPDATE:
            	Toast.makeText(mActivity, "No update", Toast.LENGTH_SHORT).show();
    			break;
    		}
    	}else{
        	Toast.makeText(mActivity, "Updated", Toast.LENGTH_SHORT).show();
    		//Adapterの更新
    		mActivity.setListAdapter(mAdapter);
    		mAdapter.notifyDataSetChanged();

    	}
	}

	// XMLをパースする
	public RssListAdapter parseXml(InputStream is) throws IOException, XmlPullParserException {
		
		//パーサー作成
		XmlPullParser parser = Xml.newPullParser();
		try {
//			parser.setInput(is, null);
			// Kiva JapanのフィードはUTF-8だが、ゴミ(0x3E)が含まれる為、not well-formedとなりパースに失敗する
			// ISO-8859-1としてパースすれば成功するが文字化けする為、getBytesでUTF-8に変換する
			parser.setInput(is, "ISO-8859-1");
			int eventType = parser.getEventType();
			Item currentItem = null;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tag = null;
				switch (eventType) {
					case XmlPullParser.START_TAG:
						tag = parser.getName();
						if (tag.equals("entry")) {
							currentItem = new Item();
						} else if (currentItem != null) {
							if (tag.equals("title")) {
//								currentItem.setTitle(parser.nextText());
								// ISO-8859-1としてパースすれば成功するが文字化けする為、getBytesでUTF-8に変換する
								String str = new String(parser.nextText().getBytes("ISO-8859-1"),"UTF-8");
								currentItem.setTitle(str);
							} else if (tag.equals("id")) {
								String str = new String(parser.nextText().getBytes("ISO-8859-1"),"UTF-8");
								currentItem.setId(str);
							} else if (tag.equals("updated")) {
								String str = new String(parser.nextText().getBytes("ISO-8859-1"),"UTF-8");
								currentItem.setUpdated(str);
							} else if (tag.equals("name")) {
								String str = new String(parser.nextText().getBytes("ISO-8859-1"),"UTF-8");
								currentItem.setAuthor(str);
							} else if (tag.equals("summary")) {
								parser.nextTag();
								tag = parser.getName();
								String str = new String(parser.nextText().getBytes("ISO-8859-1"),"UTF-8");
								parser.nextTag();
								tag = parser.getName();
								currentItem.setLink(parser.getAttributeValue(null,"href"));
								
								currentItem.setSummary(str);
							}
						}else if (tag.equals("updated")) {
							String str = new String(parser.nextText().getBytes("ISO-8859-1"),"UTF-8");
							Logout.v(str);
							//最終更新日を取得
							String lastUpdate = PreferenceManager.getDefaultSharedPreferences(mActivity).getString("rssLastUpdate", "");
							if (str.equals(lastUpdate)){
								//同じRSSなので処理を中断
								return_code = NOUPDATE;
								Logout.v("No update.");
								return null;
							}
							//更新日を最終更新日に設定
							PreferenceManager.getDefaultSharedPreferences(mActivity).edit().putString("rssLastUpdate", str).commit();
						}
						break;
					case XmlPullParser.END_TAG:
						tag = parser.getName();
						if (tag.equals("entry")) {
							mAdapter.add(currentItem);
						}
						break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		}
		return mAdapter;
	}

}
