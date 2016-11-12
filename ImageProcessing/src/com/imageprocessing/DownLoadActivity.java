package com.imageprocessing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

public class DownLoadActivity extends Activity {
	
	private Button mInstall;
	private ProgressBar mpb;
	
	public static String BASE = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/bin";
	public static String[] pathName = {"feats.dat","seeta_fa_v1.1.bin","seeta_fd_frontal_v1.0.bin","seeta_fr_v1.0.bin","opencv_manager.apk"};
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_download);
		
		initView();
	}
	private void initView() {
		mInstall = (Button) this.findViewById(R.id.install_btn);
		mpb = (ProgressBar) this.findViewById(R.id.progressBar1);
		//BASE = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/bin";
		File path = new File(BASE);
		if (!path.exists()) {// Ŀ¼���ڷ���false 
			path.mkdirs();// ����һ��Ŀ¼ 
		} 
		File file = new File(BASE + "/" + pathName[0]);
		File file1 = new File(BASE + "/" + pathName[1]);
		File file2 = new File(BASE + "/" + pathName[2]);
		File file3 = new File(BASE + "/" + pathName[3]);
		File file4 = new File(BASE + "/" + pathName[4]);
		if(!file.exists() && !file1.exists() && !file2.exists() && !file3.exists() || !file4.exists()){
			mpb.setVisibility(View.VISIBLE);
			for (int i = 0; i < pathName.length; i++) {
				assetsDataToSD(BASE+"/"+pathName[i],pathName[i]);
			}
		}
		mpb.setVisibility(View.INVISIBLE);

		mInstall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
		        if(isAppInstalled("org.opencv.engine")){
					//app installed
		        	Intent intent = new Intent(DownLoadActivity.this, MainActivity.class);
		        	startActivity(intent);
				} else {
					// ���ⲿActivity�����߿���������Ҫ����̫���ڲ�ʵ�֣�ֻ��Ҫ����һ��url��һ��context����  
			        AutoInstall.setUrl(Environment.getExternalStorageDirectory() + "/bin/opencv_manager.apk");  
			        AutoInstall.install(DownLoadActivity.this);  
				}
			}
		});
	}
	@Override
	protected void onResume() {
		super.onResume();
		if(isAppInstalled("org.opencv.engine")){
			mInstall.setText("����ң�����������аɣ�");
		}
	}
	
	
	private boolean isAppInstalled(String uri) {
		PackageManager pm = getPackageManager();
		boolean installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			installed = false;
		}
		return installed;
	}
	
	/**
	 * ��assets���ļ����Ƶ�SD����
	 */
	private void assetsDataToSD(String fileName, String openstr) {
		InputStream myInput;
		try {
			OutputStream myOutput = new FileOutputStream(fileName);
			myInput = this.getAssets().open(openstr);
			byte[] buffer = new byte[1024];
			int length = myInput.read(buffer);
			while (length > 0) {
				myOutput.write(buffer, 0, length);
				length = myInput.read(buffer);
			}
			myOutput.flush();
			myInput.close();
			myOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
