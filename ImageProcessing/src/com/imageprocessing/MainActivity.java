package com.imageprocessing;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private Button btnProc;
	private ImageView mChoiceOneIV, mChoiceTwoIV;
	private String path1, path2;
	private TextView mResultTV;
	// private Bitmap bmp;
	// OpenCV类库加载并初始化成功后的回调函数，在此我们不进行任何操作
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS: {
				System.loadLibrary("ha_facereco");
				initSDK();
			}
				break;
			default: {
				super.onManagerConnected(status);
			}
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnProc = (Button) findViewById(R.id.image_btn);
		mChoiceOneIV = (ImageView) findViewById(R.id.choice_one_iv);
		mChoiceTwoIV = (ImageView) findViewById(R.id.choice_two_iv);
		mResultTV = (TextView) findViewById(R.id.result_tv);

		mChoiceOneIV.setOnClickListener(this);
		mChoiceTwoIV.setOnClickListener(this);
		btnProc.setOnClickListener(this);

		path1 = "";
		path2 = "";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.choice_one_iv:
			startGallery(0);
			break;

		case R.id.choice_two_iv:
			startGallery(1);
			break;

		case R.id.image_btn:
			if ("".equals(path1) || "".equals(path2)) {
				Toast.makeText(this, "请选择您需要的对比图片", Toast.LENGTH_SHORT).show();
			} else {
				Date   startDate   =   new   Date(System.currentTimeMillis());
				int retid = ImageProc.compare2pictures(path1, path2);
				if (retid == 0) {
					Toast.makeText(this, "请选择含有人脸的图片进行对比", Toast.LENGTH_SHORT)
							.show();
				} else if (retid != 1) {
					Toast.makeText(this, "只有两张图片都只有一个人脸的时候才会进行对比哦",
							Toast.LENGTH_SHORT).show();
				} else {
					Date   endDate   =   new   Date(System.currentTimeMillis());
					long diff = endDate.getTime() - startDate.getTime();
					Log.e("oooooooooo", "Time= "+diff);
					mResultTV.setText("对比结果："
							+ ImageProc.comparepictures(path1, path2) * 100
							+ "%");
					Date   endDate1   =   new   Date(System.currentTimeMillis());
					long diff1 = endDate1.getTime() - startDate.getTime();
					Log.e("oooooooooo", "Time= "+diff1);
				}
			}
			break;
		default:
		}
	}

	public void startGallery(int resultID) {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
		} else {
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, resultID);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			if (data != null) {
				Uri uri = data.getData();
				path1 = getPathByUri(uri, this);
				mChoiceOneIV.setImageBitmap(BitmapFactory.decodeFile(path1));
			}
			break;

		case 1:
			if (data != null) {
				Uri uri = data.getData();
				path2 = getPathByUri(uri, this);
				mChoiceTwoIV.setImageBitmap(BitmapFactory.decodeFile(path2));
			}
			break;
		}
	}

	/**
	 * Uri--->Path
	 */
	public static String getPathByUri(Uri uri, Context context) {
		String path = null;
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(uri, projection,
				null, null, null);
		if (cursor.moveToFirst()) {
			int index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			path = cursor.getString(index);
		}
		return path;
	}

	@Override
	public void onResume() {
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this,
				mLoaderCallback);
	}

	public void initSDK() {
		File pathFile = Environment.getExternalStorageDirectory();
		String path = pathFile.getAbsolutePath() + "/bin";
		ImageProc.initLibrary(path);
	}
}