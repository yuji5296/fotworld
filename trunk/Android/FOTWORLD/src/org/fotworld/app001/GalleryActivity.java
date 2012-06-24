package org.fotworld.app001;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;


public class GalleryActivity extends Activity implements AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory {

	private ImageSwitcher mSwitcher;
//  private TextSwitcher tSwitcher;
	private ImageAdapter galleryAdapter;
	private GestureDetector myGestureDetector;
    public static final int THRESHOLD = 200;
    private int position = 0;
    private Gallery gallery;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.image_switcher);

        //SDカードの画像リストを取得する
        ImageListFactory factory = new ImageListFactory();
        List<Bitmap> galleryBitmap = factory.getBitMapList();

        myGestureDetector = new GestureDetector(this, new OnGestureListener() {
        	private boolean event = true;
        	public boolean onSingleTapUp(MotionEvent e) {
        		Logout.d("onSingletapUp");
        		event = true;
        		return false;
        	}

        	public void onShowPress(MotionEvent e) {
        		Logout.d("onShowPress");
				showNext();
        	}

        	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
        			float distanceY) {
        		Logout.d("onScroll");
        		event = true;
        		return false;
        	}

        	public void onLongPress(MotionEvent e) {
        		// do nothing
        		Logout.d("onLongPress");
        	}

        	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
        			float velocityY) {
        		Logout.d("onFling:velocityX="+velocityX);
        		if (Math.abs(e2.getX() - e1.getX()) > THRESHOLD) {
        			if (velocityX < 0) {
        				showNext();
        			} else {
        				showPrevious();
        			}
        			event = true;
        			return true;
        		}
        		return false;
        	}

        	public boolean onDown(MotionEvent e) {
        		Logout.d("onDown");
        		if (event) {
        			event = false;
        			return true;
        		} else {
        			return false;
        		}
        	}
        });
        
        //ImageSwitterのアダプターを設定
        mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
        mSwitcher.setFactory(this);
        mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
        mSwitcher.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return myGestureDetector.onTouchEvent(event);
            }
        });
        
        //Galleryのアダプターの生成、設定
        galleryAdapter = new ImageAdapter(this, galleryBitmap);
        gallery = (Gallery) findViewById(R.id.gallery);
        gallery.setAdapter(galleryAdapter);
        gallery.setOnItemSelectedListener(this);

//        tSwitcher = (TextSwitcher) findViewById(R.id.TextSwitcher01);
//        tSwitcher.setFactory(this);
//
//        Animation in = AnimationUtils.loadAnimation(this,
//                android.R.anim.fade_in);
//        Animation out = AnimationUtils.loadAnimation(this,
//                android.R.anim.fade_out);
//        tSwitcher.setInAnimation(in);
//        tSwitcher.setOutAnimation(out);


    }
    /**
     * 現在位置の１つ次の画像を表示する
     */
    private void showPrevious() {
    	position -= 1;
    	if (position < 0) {
    		position = galleryAdapter.getCount() - 1;
    	}

    	mSwitcher.setImageDrawable(galleryAdapter.getItemDrawable(position));
    	gallery.setSelection(position,true);
//    	gallery.onFling(null, null, 1000, 0);

    }
 
//     public void onShowPrevious(View view) {
//    	 showPrevious();
//     }
 
     /**
     * 現在位置の一つ前の画像を表示する
     */
     private void showNext() {
    	 position += 1;
    	 if (position >= galleryAdapter.getCount()) {
    		 position = 0;
    	 }
    	 mSwitcher.setImageDrawable(galleryAdapter.getItemDrawable(position));
    	 gallery.setSelection(position,true);
//    	 gallery.onFling(null, null, -1000, 0);
     }
     
     //Galleryで画像が選択された時の動作
     public void onItemSelected(AdapterView parent, View v, int position, long id) {
    	 //ImageAdapterの同じpositionのBitmapをDrawableに変換して表示
//  	 mSwitcher.setImageResource(mImageIds[position]);
    	 mSwitcher.setImageDrawable(galleryAdapter.getItemDrawable(position));
    	 //tSwitcher.setText(String.valueOf(position));
    	 this.position = position;
    }

    public void onNothingSelected(AdapterView parent) {
    }

    public View makeView() {
        ImageView i = new ImageView(this);
        i.setBackgroundColor(0xFF000000);
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
        return i;
    }
	private static final int REQUEST_IMAGE_CAPTURE = 0;
    public void onClick_button1(View v){
    	//カメラを起動
    	Intent intent = new Intent();
    	intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//    	intent.addCategory(Intent.CATEGORY_DEFAULT);
    	//カメラで撮影した画像のファイル名を取得するための処理
//    	mTmpFile = new File(Environment.getExternalStorageDirectory()+"sample", "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
//		Logout.d(mTmpFile.getPath());
//    	intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));                	// インテントのインスタンス生成
    	startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Logout.d("requestCode="+requestCode);
		Logout.d("resultCode="+resultCode);
		if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			try {
//				Intent mailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:info@fotworld.org"));
//				mailIntent.putExtra(Intent.EXTRA_SUBJECT, "FOTWORLD");
//				mailIntent.putExtra(Intent.EXTRA_TEXT, "Attach photo from Android");
//				Uri attachments = Uri.parse("file://" + Uri.fromFile(mTmpFile));
//				Logout.d(attachments.toString());
//				mailIntent.putExtra(Intent.EXTRA_STREAM, attachments);
//				startActivity(mailIntent);
				Uri uri = data.getData();
				Logout.d("onActivityResult:data="+uri.toString());
				//ファイルパスを取得したい場合は下記のようにContentResolverを活用する
				//ContentResolver経由でファイルパスを取得
				ContentResolver cr = getContentResolver();
				String[] columns = {MediaStore.Images.Media.DATA };
				Cursor c = cr.query(uri, columns, null, null, null);
				c.moveToFirst();
				File picture = new File(c.getString(0));
				Logout.d("onActivityResult:data="+picture.getPath());
				Bitmap bitmap = (Bitmap) data.getExtras().get("data");
				galleryAdapter.addBitmap(bitmap);
			} catch (Exception e) {
				Logout.d("Exception:"+e.toString());
			}
		}
	}

    public void onClick_button2(View v){
    }
    public void onClick_button3(View v){
    }

}
