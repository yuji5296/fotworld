package org.fotworld.app001;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class FacebookActivity extends Activity {
	// 引数は Facebook から取得した App ID
	Facebook facebook = new Facebook("135741723160602");

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		facebook.authorize(this, new DialogListener() {
			public void onComplete(Bundle values) {
			}

			public void onFacebookError(FacebookError error) {
			}

			public void onError(DialogError e) {
			}

			public void onCancel() {
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		facebook.authorizeCallback(requestCode, resultCode, data);
	}
}
