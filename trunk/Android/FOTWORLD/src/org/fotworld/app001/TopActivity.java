package org.fotworld.app001;

import java.io.File;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TopActivity extends Activity {
	private static final int REQUEST_IMAGE_CAPTURE = 0;
	private static File mTmpFile;
	/** Called when the activity is first created. */
	public class BindData {
		int iconId;
		int title;

		BindData(int id, int s) {
			this.iconId = id;
			this.title = s;
		}
	}

	private BindData[] mDatas = {
			new BindData(android.R.drawable.ic_menu_recent_history, R.string.news),
			new BindData(android.R.drawable.ic_menu_month, R.string.calendar),
			new BindData(android.R.drawable.ic_menu_gallery, R.string.gallery),
			new BindData(android.R.drawable.ic_menu_mapmode, R.string.map),
			new BindData(android.R.drawable.ic_menu_upload, R.string.upload),
			new BindData(android.R.drawable.ic_menu_share, R.string.share),
//			new BindData(android.R.drawable.ic_menu_search, R.string.search),
//			new BindData(android.R.drawable.ic_menu_preferences, R.string.preference),
			new BindData(android.R.drawable.ic_menu_info_details, R.string.info),
			new BindData(android.R.drawable.ic_menu_help, R.string.help),
			new BindData(R.drawable.facebook, R.string.facebook),
	};
	
	GoogleAnalyticsTracker tracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		tracker = GoogleAnalyticsTracker.getInstance();  
        tracker.startNewSession("UA-28921874-4", 60, this); 
        tracker.trackPageView("/TopActivity");

        super.onCreate(savedInstanceState);
		setContentView(R.layout.top);

		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new MyAdapter(this, R.layout.item, mDatas));
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent;
                Uri uri;
				switch (position){
                case 0: //News
                    tracker.trackPageView("/TopActivity/rss");
                	//RSSリーダを開く
                	intent = new Intent(getApplication(), RssReaderActivity.class);
            		startActivity(intent);
            		break;
                case 1: //Calender
                    tracker.trackPageView("/TopActivity/calendar");
                	//活動予定のページを開く
//    				Uri uri = Uri.parse("http://www.fotworld.org/about/calendar");
                	//adminのカレンダーをブラウザで開く
    				uri = Uri.parse("https://www.google.com/calendar/embed?src=6427dlhbj4jq0kh91smvr8rnjc@group.calendar.google.com");
    				intent = new Intent(Intent.ACTION_VIEW,uri);
    				//Android標準のカレンダーアプリを起動
//    				intent = new Intent(Intent.ACTION_VIEW);
//    				intent.setClassName("com.android.calendar", "com.android.calendar.LaunchActivity");
    				startActivity(intent);
    				break;
                case 2: //Photo
                    tracker.trackPageView("/TopActivity/picasa");
                	//Piacasaのアルバムを開く
                	uri = Uri.parse("https://picasaweb.google.com/116766722328185923795/TASUKi_record");
                	intent = new Intent(Intent.ACTION_VIEW, uri);  
                	startActivity(intent);     	
                	break;
                case 3: //Maps
                    tracker.trackPageView("/TopActivity/map");
                	//寄贈先の地図を開く
                	uri = Uri.parse("https://maps.google.com/maps/ms?msid=204531763720450638157.0004c284ead4abf0ced26&msa=0");
                	intent = new Intent();
                	intent.setAction(Intent.ACTION_VIEW);
                	intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
                	intent.setData(uri);
                	startActivity(intent);     	
                	break;
                case 4: //Upload
                    tracker.trackPageView("/TopActivity/upload");
                	intent = new Intent(getApplication(), PhotoUploadActivity.class);
            		startActivity(intent);
            		break;
                case 5: //Share
                    tracker.trackPageView("/TopActivity/share");
        			intent = new Intent(Intent.ACTION_SEND);
        			intent.setType("text/plain");
        			intent.putExtra(Intent.EXTRA_TEXT, "http://www.fotworld.org/");
        			intent.putExtra(Intent.EXTRA_SUBJECT, "FOTWORLD+α");
        			startActivity(intent);        			
            		break;                	
                case 6: //Info
                    tracker.trackPageView("/TopActivity/info");
                	//バージョン情報などを表示
                	intent = new Intent(getApplication(), AboutActivity.class);
            		startActivity(intent);
            		break;
                case 7: //Help
                    tracker.trackPageView("/TopActivity/help");
                	//FOTWORLDとは何かを説明
                	//このアプリの操作方法を説明
                	intent = new Intent(getApplication(), HelpActivity.class);
            		startActivity(intent);
                	break;
                case 8: //Facebook
                    tracker.trackPageView("/TopActivity/facebook");
                    //Facebookページを開く
                    try {
                        uri = Uri.parse("fb://page/167295640049952");
                    	intent = new Intent(Intent.ACTION_VIEW, uri);  
    					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    	startActivity(intent);
                    }
                    catch (ActivityNotFoundException e) {
                        uri = Uri.parse("http://www.facebook.com/fotworld/");
                    	intent = new Intent(Intent.ACTION_VIEW, uri);  
                    	startActivity(intent);
                    }
                	break;
//                case 6: //検索
//                case 7: //設定
            	default:
                    tracker.trackPageView("/TopActivity/others");
                    Toast.makeText(TopActivity.this, "Sorry, this feature will be coming soon.", Toast.LENGTH_SHORT).show();
            		
            	}
            }

        });	
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Logout.d("requestCode="+requestCode);
		Logout.d("resultCode="+resultCode);
		if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			try {
				Intent mailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:info@fotworld.org"));
				mailIntent.putExtra(Intent.EXTRA_SUBJECT, "FOTWORLD");
				mailIntent.putExtra(Intent.EXTRA_TEXT, "Attach photo from Android");
				Uri attachments = Uri.parse("file://" + Uri.fromFile(mTmpFile));
				Logout.d(attachments.toString());
				mailIntent.putExtra(Intent.EXTRA_STREAM, attachments);
				startActivity(mailIntent);
			} catch (Exception e) {
				Logout.d("Exception:"+e.toString());
			}
		}
	}
	static class ViewHolder {
		TextView textView;
		ImageView imageView;
	}

	public class MyAdapter extends ArrayAdapter<BindData> {
		private LayoutInflater inflater;
		private int layoutId;

		public MyAdapter(Context context, int layoutId, BindData[] objects) {
			super(context, 0, objects);
			this.inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.layoutId = layoutId;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = inflater.inflate(layoutId, parent, false);
				holder = new ViewHolder();
				holder.textView = (TextView) convertView
						.findViewById(R.id.textview);
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.imageview);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			BindData data = getItem(position);
			holder.textView.setText(getString(data.title));
			holder.imageView.setImageResource(data.iconId);
			return convertView;
		}
	}
}
