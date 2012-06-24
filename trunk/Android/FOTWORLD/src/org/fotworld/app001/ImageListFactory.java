package org.fotworld.app001;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/***
 * SDカードの読み書きを行うクラス
 * @author ge
 *
 */
public class ImageListFactory {
	//取得対象のディレクトリ
//	    private static final String APP_DIR_NAME = "/DCIM/100SHARP";
//		private static final String APP_DIR_NAME = "/sample";
//	    private static final String APP_DIR_NAME = "/DCIM/101_PANA";
	private static final String APP_DIR_NAME = "/DCIM/100ANDRO";
	//    private static final String APP_DIR_NAME = "/DCIM/Camera";
	//    private static final String APP_DIR_NAME = "/DCIM/Otayori-Photo";
	private String appDirPath;

	/***
	 * コンストラクタ
	 */
	public ImageListFactory() {
		File appDir = null;	//SDカードの場所

		//マウントされているSDカードの場所を取得する
		String status = Environment.getExternalStorageState();
		if(status.equals(Environment.MEDIA_MOUNTED)) {
			//SDカードの存在チェック
			appDir = new File(Environment.getExternalStorageDirectory() + APP_DIR_NAME);
			appDirPath = appDir.getPath();
			if(!appDir.exists()) {
				appDir.mkdir();
			}
		}
	}

	/***
	 * 指定されたパスに存在する画像ファイルを全取得
	 * @param dirPath
	 * @return
	 */
	public List<Bitmap> getBitMapList() {
		List<Bitmap> listBitmap = new ArrayList<Bitmap>();

		//指定されたパスから画像ファイルの取得
		List<String> filePathList = getFileList(appDirPath);
		for(String filePath : filePathList) {
			Logout.d(filePath);
			//OutOfMemory対策
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inSampleSize=8;
			Bitmap bitMap = BitmapFactory.decodeFile(filePath,opt);
			listBitmap.add(bitMap);
		}

		return listBitmap;
	}

	/***
	 * ファイルの一覧を取得する
	 */
	private List<String> getFileList(String dirPath) {
		List<String> fileList = new ArrayList<String>();
		File targetDir = new File(dirPath);
		//対象ディレクトリの存在チェック
		if(!targetDir.exists()) {
			targetDir.mkdir();
		} else {
			String[] targetArray = targetDir.list();
			//ディレクトリ
			for(String targetPath : targetArray) {
				fileList.add(dirPath + "/" + targetPath);
			}
			//ファイル名でソート
			Collections.sort(fileList, new Comparator<String>() {
				public int compare(String arg0, String arg1) {
					return arg1.compareTo(arg0);
				}
			});
		}
		return fileList;
	}
}
