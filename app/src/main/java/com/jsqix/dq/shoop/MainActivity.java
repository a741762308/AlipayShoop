package com.jsqix.dq.shoop;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jsqix.dq.shoop.view.ShoopView;
import com.makeramen.roundedimageview.RoundedImageView;

public class MainActivity extends AppCompatActivity {
    private ShoopView shoopView;
    private RoundedImageView roundedImageView;
    private SoundPool soundPool;
    private int music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                // 每隔5s响一次
                handler.sendEmptyMessageDelayed(1, 5000);
                soundPool.play(music, 1, 1, 0, 0, 1);
            }
        }
    };

    private void initView() {
        // 第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        // 把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        music = soundPool.load(this, R.raw.shoop, 1);
        shoopView = (ShoopView) findViewById(R.id.ShoopView);
        roundedImageView = (RoundedImageView) findViewById(R.id.my_photo);
//        roundedImageView.setVisibility(View.INVISIBLE);
//        shoopView.start();
        roundedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shoopView.isStarting()) {
                    //如果动画正在运行就停止，否则就继续执行
                    shoopView.stop();
                    //结束进程
                    handler.removeMessages(1);
                } else {
                    // 执行动画
                    shoopView.start();
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (shoopView.isStarting()) {
            //如果动画正在运行就停止，否则就继续执行
            shoopView.stop();
            //结束进程
            handler.removeMessages(1);
        }
    }
}
