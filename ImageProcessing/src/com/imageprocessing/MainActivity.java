package com.imageprocessing;
import org.opencv.android.BaseLoaderCallback;  
import org.opencv.android.LoaderCallbackInterface;  
import org.opencv.android.OpenCVLoader;  
import android.os.Bundle;  
import android.app.Activity;  
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;  
import android.graphics.Bitmap.Config;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.widget.Button;  
import android.widget.ImageView;  
public class MainActivity extends Activity implements OnClickListener{  
    private Button btnProc;  
    private ImageView imageView;  
    private Bitmap bmp;  
    //OpenCV类库加载并初始化成功后的回调函数，在此我们不进行任何操作  
     private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {  
        @Override  
        public void onManagerConnected(int status) {  
            switch (status) {  
                case LoaderCallbackInterface.SUCCESS:{  
                    System.loadLibrary("ImageProc");  } break;  
                default:{  
                    super.onManagerConnected(status);  
                } break; }}};  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        btnProc = (Button) findViewById(R.id.image_btn);  
        imageView = (ImageView) findViewById(R.id.image_iv);  
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);  
        imageView.setImageBitmap(bmp);  
        btnProc.setOnClickListener(this);  
    }  
    @Override  
    public void onClick(View v) {  
        int w = bmp.getWidth();  
        int h = bmp.getHeight();  
        int[] pixels = new int[w*h];       
        bmp.getPixels(pixels, 0, w, 0, 0, w, h);  
        int[] resultInt = ImageProc.grayProc(pixels, w, h);  
        Bitmap resultImg = Bitmap.createBitmap(w, h, Config.ARGB_8888);  
        resultImg.setPixels(resultInt, 0, w, 0, 0, w, h);  
        imageView.setImageBitmap(resultImg);}  
    @Override  
    public void onResume(){  
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);  
    }  
}  