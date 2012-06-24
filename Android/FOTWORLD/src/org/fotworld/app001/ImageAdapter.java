package org.fotworld.app001;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Gallery.LayoutParams;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	private List<Bitmap> listImageBitmap = new ArrayList<Bitmap>();

	//コンストラクタ1
	public ImageAdapter(Context context) {
		this(context, null);
	}

	//コンストラクタ2
	public ImageAdapter(Context context, List<Bitmap> listImageBitmap) {
		mContext = context;
		this.listImageBitmap = listImageBitmap;
	}

	public int getCount() {
		//return mThumbIds.length;
		return listImageBitmap.size();
	}

	public Object getItem(int position) {
		return position;
	}

	//指定のpositionのBitmapをDrawableに変換して返す
	public Drawable getItemDrawable(int position) {
		Drawable drawable = new BitmapDrawable(listImageBitmap.get(position));
		return drawable;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = new ImageView(mContext);
		imageView.setImageBitmap(listImageBitmap.get(position));
		imageView.setAdjustViewBounds(true);
		imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		return imageView;
	}

	//セッター
	public void setListImageBitmap(List<Bitmap> listImageBitmap) {
		this.listImageBitmap = listImageBitmap;
	}
	
	//ビットマップリストにビットマップを追加
	public void addBitmap(Bitmap bmp){
		this.listImageBitmap.add(0,bmp);
	}
}
