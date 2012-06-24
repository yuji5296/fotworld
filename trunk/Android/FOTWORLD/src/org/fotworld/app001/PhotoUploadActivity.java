package org.fotworld.app001;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class PhotoUploadActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.upload);

	}

	private static final int REQUEST_IMAGE_CAPTURE = 0;
	private static final int REQUEST_IMAGE_PICK = 1;
	// private static final int REQUEST_GET_CONTENT = 2;
	private Uri mImageUri;

	public void onClick_button1(View v) {
		// カメラを起動
		ContentValues values = new ContentValues();
		// String photoPath = "test001.jpg";
		// values.put(MediaStore.Images.Media.TITLE, photoPath);
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		mImageUri = getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

		Intent intent = new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.addCategory(Intent.CATEGORY_DEFAULT);

		// カメラで撮影した画像のファイル名を取得するための処理
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);

		startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
	}

	public void onClick_button2(View v) {
		// ACTION_PICK と ACTION_GET_CONTENT の違いは以下に。
		// データを取得するアプリを開く。
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, REQUEST_IMAGE_PICK);
		// データを取得するアプリ全てを開く。ファイルエクスプローラーなども含む。
		// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		// startActivityForResult(intent, REQUEST_GET_CONTENT);
	}

	public void onClick_button3(View v) {
		// Share
		Intent intent = new Intent(Intent.ACTION_SEND);
		if (mImageUri == null) {
			intent.setType("text/plain");
		} else {
			intent.setType("image/jpeg");
			intent.putExtra(Intent.EXTRA_STREAM, mImageUri);
		}
		intent.putExtra(Intent.EXTRA_EMAIL,
				new String[] { "info@fotworld.org" });
		intent.putExtra(Intent.EXTRA_SUBJECT, "FOTWORLD Report");
		EditText editText1 = (EditText) findViewById(R.id.editText1);
		intent.putExtra(Intent.EXTRA_TEXT, editText1.getText());
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Logout.d("requestCode=" + requestCode);
		Logout.d("resultCode=" + resultCode);
		ImageView imageview = (ImageView) findViewById(R.id.imageView1);
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			try {
				// Bitmapを取得（サムネイル程度の大きさ）
				// Bitmap bitmap = (Bitmap) data.getExtras().get("data");
				// imageview.setImageBitmap(bitmap);
				// Uri指定して画像を取得
				imageview.setImageURI(mImageUri);
			} catch (Exception e) {
				Logout.d("Exception:" + e.toString());
			}
		} else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
			try {
				mImageUri = data.getData();
				imageview.setImageURI(mImageUri);
				Toast.makeText(this, "Get image", Toast.LENGTH_LONG);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(this, "requestCode=" + requestCode + ",resultCode="
					+ resultCode, Toast.LENGTH_LONG);
		}
	}

}
