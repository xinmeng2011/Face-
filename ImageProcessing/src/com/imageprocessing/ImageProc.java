package com.imageprocessing;

public class ImageProc {  
    public static native int[] grayProc(int[] pixels, int w, int h);  
    public static native void initLibrary(String path);
    public static native int compare2pictures(String path1, String path2);
    public static native float comparepictures(String path1, String path2);
} 