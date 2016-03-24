package com.jsqix.dq.shoop.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 模仿咻一咻
 *
 * @author LGL
 */
public class ShoopView extends View {

    private Paint paint;
    private int maxWidth = 255;
    // 是否运行
    private boolean isStarting = false;
    private List<Integer> alphaList = new ArrayList<Integer>();
    private List<Integer> startWidthList = new ArrayList<Integer>();

    public ShoopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ShoopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    public ShoopView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    private void init() {
        paint = new Paint();
        // 设置博文的颜色
        paint.setColor(0x0059ccf5);
        alphaList.add(255);// 圆心的不透明度
        startWidthList.add(0);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        maxWidth=getWidth()/2-200;
        setBackgroundColor(Color.TRANSPARENT);// 颜色：完全透明
        // 依次绘制 同心圆
        for (int i = 0; i < alphaList.size(); i++) {
            int alpha = alphaList.get(i);
            // 圆半径
            int startWidth = startWidthList.get(i);
            paint.setAlpha(alpha);
            // 这个半径决定你想要多大的扩散面积
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, startWidth + 200,
                    paint);
            // 同心圆扩散
            if (isStarting && alpha > 0 && startWidth < maxWidth) {
                alphaList.set(i, alpha - 1);
                startWidthList.set(i, startWidth +2);
            }
        }
        if (isStarting
                && startWidthList.get(startWidthList.size() - 1) == maxWidth / 5) {
            alphaList.add(255);
            startWidthList.add(0);
        }
        // 同心圆数量达到10个，删除最外层圆
        if (isStarting && startWidthList.size() == 7) {
            startWidthList.remove(0);
            alphaList.remove(0);
        }
        // 刷新界面
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