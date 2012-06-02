package org.fotworld.app001;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class TopActivity extends Activity{
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
			new BindData(android.R.drawable.ic_menu_agenda, R.string.news),
			new BindData(android.R.drawable.ic_menu_month, R.string.calendar),
			new BindData(android.R.drawable.ic_menu_camera, R.string.camera),
			new BindData(android.R.drawable.ic_menu_search, R.string.search),
			new BindData(android.R.drawable.ic_menu_preferences, R.string.preference),
			new BindData(android.R.drawable.ic_menu_share, R.string.share),
			new BindData(android.R.drawable.ic_menu_info_details, R.string.info),
			new BindData(android.R.drawable.ic_menu_help, R.string.help)
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
                case 0:
                	intent = new Intent(getApplication(), RssReaderActivity.class);
            		startActivity(intent);
            		break;
                case 6:
                	intent = new Intent(getApplication(), AboutActivity.class);
            		startActivity(intent);
            		break;
            	default:
                    Toast.makeText(TopActivity.this, "Sorry", Toast.LENGTH_SHORT).show();
            		
            	}
            }
        });	
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
