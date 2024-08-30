package com.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyHomeCCTV extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private Thread threadSView;
    private boolean threadRunning = true;
    private final SurfaceHolder holder;
    private Bitmap currentFrame;
    private String urlString;

    public Bitmap getCurrentFrame() {
        return currentFrame;
    }

    public MyHomeCCTV(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
    }

    public void clearCurrentFrame() {
        if (currentFrame != null && !currentFrame.isRecycled()) {
            currentFrame.recycle();
            currentFrame = null;
        }
    }

    public void setUrl(String urlString) {
        this.urlString = urlString;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        threadRunning = true;
        if (threadSView == null || !threadSView.isAlive()) {
            threadSView = new Thread(this);
            threadSView.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Surface 크기나 포맷이 변경되었을 때 처리할 내용이 있다면 추가
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopThread();
    }

    public void stopThread() {
        threadRunning = false;
        if (threadSView != null && threadSView.isAlive()) {
            try {
                threadSView.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        threadSView = null;
    }

    @Override
    public void run() {
        final int maxImgSize = 1000000;
        byte[] arr = new byte[maxImgSize];
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(con.getInputStream());

            while (threadRunning) {
                int i = 0;
                while (i < 1000) {
                    int b = in.read();
                    if (b == 0xff) {
                        int b2 = in.read();
                        if (b2 == 0xd8) break;
                    }
                    i++;
                }
                if (i >= 1000) {
                    Log.e("MyHomeCCTV", "Bad head!");
                    continue;
                }

                arr[0] = (byte) 0xff;
                arr[1] = (byte) 0xd8;
                i = 2;
                while (i < maxImgSize) {
                    int b = in.read();
                    if (b == -1) break;
                    arr[i++] = (byte) b;
                    if (b == 0xff) {
                        int b2 = in.read();
                        if (b2 == -1) break;
                        arr[i++] = (byte) b2;
                        if (b2 == 0xd9) break;
                    }
                }

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arr, 0, i);
                Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
                byteArrayInputStream.close();

                if (bitmap != null) {
                    // SurfaceView 크기에 맞게 비트맵 조정
                    Canvas canvas = holder.lockCanvas();
                    if (canvas != null) {
                        synchronized (holder) {
                            canvas.drawColor(Color.BLACK);
                            float scaleWidth = (float) getWidth() / bitmap.getWidth();
                            float scaleHeight = (float) getHeight() / bitmap.getHeight();
                            float scale = Math.min(scaleWidth, scaleHeight);

                            int scaledWidth = (int) (bitmap.getWidth() * scale);
                            int scaledHeight = (int) (bitmap.getHeight() * scale);
                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);

                            canvas.drawBitmap(scaledBitmap, 0, 0, null);
                            scaledBitmap.recycle(); // scale bitmap을 해제합니다.
                        }
                        holder.unlockCanvasAndPost(canvas);
                    }
                } else {
                    Log.e("MyHomeCCTV", "Failed to decode bitmap");
                }
            }
        } catch (Exception e) {
            Log.e("MyHomeCCTV", "Error: " + e.toString());
        }
    }

}
