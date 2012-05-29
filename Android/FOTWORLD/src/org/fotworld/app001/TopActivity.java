package org.fotworld.app001;

import android.app.Activity;
import android.content.Context;
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
		String title;

		BindData(int id, String s) {
			this.iconId = id;
			this.title = s;
		}
	}

	private BindData[] mDatas = {
			new BindData(android.R.drawable.ic_menu_agenda, "Agenda"),
			new BindData(android.R.drawable.ic_menu_month, "Calendar"),
			new BindData(android.R.drawable.ic_menu_camera, "Camera"),
			new BindData(android.R.drawable.ic_menu_add, "Add"),
			new BindData(android.R.drawable.ic_menu_preferences, "Prefereces"),
			new BindData(android.R.drawable.ic_menu_share, "Share"),
			new BindData(android.R.drawable.ic_menu_info_details, "Info"),
			new BindData(android.R.drawable.ic_menu_help, "Help"), };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new MyAdapter(this, R.layout.item, mDatas));
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(TopActivity.this, "" + position+":"+id, Toast.LENGTH_SHORT).show();
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
			holder.textView.setText(data.title);
			holder.imageView.setImageResource(data.iconId);
			return convertView;
		}
	}
}
