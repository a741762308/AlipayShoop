package com.jsqix.dq.shoop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.jsqix.dq.shoop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dq on 2016/3/23.
 */
public class WhewView extends View {
    private Context context;
    private Paint paint = null; //  画笔
    private Bitmap bitmap = null;   //  图片位图
    private Bitmap bitmapDisplay = null;
    private Matrix matrix = null;
    private int nBitmapWidth = 0;   //  图片的宽度
    private int nBitmapHeight = 0;  //  图片的高度
    private int nPosX = 120;    //  图片所在的位置X
    private int nPosY = 10; //  图片所在的位置Y
    private float fAngle = 0.0f;    //  图片旋转
    private float fScale = 1.0f;    //  图片缩放 1.0表示为原图

    private int maxWidth = 255;
    // 是否运行
    private boolean isStarting = false;
    private List<Integer> alphaList = new ArrayList<Integer>();
    private List<Integer> startWidthList = new ArrayList<Integer>();

    public WhewView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public WhewView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {

        paint = new Paint();
//        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        //  加载需要操作的图片
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.aqm);
        bitmapDisplay = bitmap;

        matrix = new Matrix();
        //  获取图片高度和宽度
        nBitmapWidth = bitmap.getWidth();
        nBitmapHeight = bitmap.getHeight();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics sm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(sm);
        nPosX = sm.widthPixels / 2;
        nPosY = sm.heightPixels / 2;

        alphaList.add(255);// 圆心的不透明度
        startWidthList.add(0);
    }

    //  向左旋转
    public void setRotationLeft() {
        fAngle--;
        setAngle();
    }

    //  向右旋转
    public void setRotationRight() {
        fAngle++;
        setAngle();
    }

    //  图片放大
    public void setEnlarge() {
        if (fScale < 2) {
            fScale += 0.5f;
            setScale();
        }
    }

    //  图片缩小
    public void setNarrow() {
        if (fScale > 0.5) {
            fScale -= 0.1f;
            setScale();
        }
    }

    //  设置旋转比例
    private void setAngle() {
        matrix.reset();
        matrix.setRotate(fAngle);
        bitmapDisplay = Bitmap.createBitmap(bitmap, 0, 0, nBitmapWidth, nBitmapHeight, matrix, true);
    }

    //  设置缩放比例
    private void setScale() {
        matrix.reset();
        matrix.setScale(fScale, fScale, nPosX, nPosY);
        bitmapDisplay = Bitmap.createBitmap(bitmap, 0, 0, nBitmapWidth, nBitmapHeight, matrix, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundColor(Color.TRANSPARENT);// 颜色：完全透明
        int width = bitmapDisplay.getWidth();
        int height = bitmapDisplay.getHeight();
        // 依次绘制 同心圆
        for (int i = 0; i < alphaList.size(); i++) {
            int alpha = alphaList.get(i);
            // 圆半径
            int startWidth = startWidthList.get(i);
            paint.setAlpha(alpha);
            canvas.drawBitmap(bitmapDisplay, nPosX - width / 2, nPosY - height/2, paint);

            // 同心圆扩散
        if (isStarting && alpha > 0 && startWidth < maxWidth) {
            alphaList.set(i, alpha - 1);
            startWidthList.set(i, startWidth + 1);
//                setEnlarge();
                setRotationRight();
        }
    }
        if (isStarting
                && startWidthList.get(startWidthList.size() - 1) == maxWidth / 5) {
            alphaList.add(255);
            startWidthList.add(0);
        }
        // 同心圆数量达到10个，删除最外层圆
        if (isStarting && startWidthList.size() == 10) {
            startWidthList.remove(0);
            alphaList.remove(0);
        }
        invalidate();
    }

    // 执行动画
    public void start() {
        isStarting = true;
    }

    // 停止动画
    public void stop() {
        isStarting = false;
    }

    // 判断是都在不在执行
    public boolean isStarting() {
        return isStarting;
    }

}
