package org.fotworld.app001;

import java.io.File;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
			new BindData(android.R.drawable.ic_menu_camera, R.string.camera),
			new BindData(android.R.drawable.ic_menu_search, R.string.search),
			new BindData(android.R.drawable.ic_menu_preferences, R.string.preference),
			new BindData(android.R.drawable.ic_menu_share, R.string.share),
			new BindData(android.R.drawable.ic_menu_info_details, R.string.info),
			new BindData(android.R.drawable.ic_menu_help, R.string.help),
			new BindData(android.R.drawable.ic_menu_gallery, R.string.gallery),
			new BindData(android.R.drawable.ic_menu_mapmode, R.string.map),
};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.top);

		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new MyAdapter(this, R.layout.item, mDatas));
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent;
				switch (position){
                case 0: //最新情報
                	//RSSリーダを開く
                	intent = new Intent(getApplication(), RssReaderActivity.class);
            		startActivity(intent);
            		break;
                case 1: //活動予定
                	//活動予定のページを開く
    				Uri uri = Uri.parse("http://www.fotworld.org/about/calendar");
    				intent = new Intent(Intent.ACTION_VIEW,uri);
    				//Android標準のカレンダーアプリを起動
//    				intent = new Intent(Intent.ACTION_VIEW);
//    				intent.setClassName("com.android.calendar", "com.android.calendar.LaunchActivity");
    				startActivity(intent);
    				break;
                case 2: //カメラ起動
                	intent = new Intent();
                	intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                	intent.addCategory(Intent.CATEGORY_DEFAULT);
                	//カメラで撮影した画像のファイル名を取得するための処理
                	mTmpFile = new File(Environment.getExternalStorageDirectory(), "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                	intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));                	// インテントのインスタンス生成
                	startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            		break;
                case 5: //共有
        			intent = new Intent(Intent.ACTION_SEND);
        			intent.setType("text/plain");
        			intent.putExtra(Intent.EXTRA_TEXT, "http://www.fotworld.org/");
        			intent.putExtra(Intent.EXTRA_SUBJECT, "FOTWORLD+α");
        			startActivity(intent);        			
            		break;                	
                case 6: //アプリ情報
                	//バージョン情報などを表示
                	intent = new Intent(getApplication(), AboutActivity.class);
            		startActivity(intent);
            		break;
                case 7: //ヘルプ
                	//FOTWORLDとは何かを説明
                	//このアプリの操作方法を説明
                	intent = new Intent(getApplication(), HelpActivity.class);
            		startActivity(intent);
                	break;
                case 3: //検索
                case 4: //設定
            	default:
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
