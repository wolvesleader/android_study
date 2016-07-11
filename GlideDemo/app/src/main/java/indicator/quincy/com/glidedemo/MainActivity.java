package indicator.quincy.com.glidedemo;

import android.app.Activity;

import android.os.Bundle;
import android.widget.Toast;


import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * 官方地址
 * https://github.com/koral--/android-gif-drawable
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GifImageView ivImgGlide = (GifImageView)findViewById(R.id.ivImgGlide);
        //ivImgGlide.setImageResource(R.mipmap.gifanimation2);


        try {
           final  GifDrawable gifFromAssets = new GifDrawable(getResources(), R.mipmap.gifanimation2 );

            ivImgGlide.setBackground(gifFromAssets);
            int fram4 = gifFromAssets.getCurrentFrameIndex();
            int temp = gifFromAssets.getCurrentPosition();
            System.out.println(temp + "==============");
            //gifFromAssets.seekTo(1000);
           int time =  gifFromAssets.getDuration();
            int loop = gifFromAssets.getLoopCount();
            System.out.println(time + "--------------");
            System.out.println(loop + "$$$$$$$$$$$$$$");

            gifFromAssets.setLoopCount(1);// 设置动画循环播放几次




            int fram1 = gifFromAssets.getCurrentFrameIndex();
            int temp1 = gifFromAssets.getCurrentPosition();
//            if (temp1 == 26){
//                gifFromAssets.stop();
//            }
            int fram = gifFromAssets.getCurrentFrameIndex();
            System.out.println(temp1 + "==============  " + fram1 + " 000 " + fram + "hh" + fram4);


            gifFromAssets.addAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationCompleted(int loopNumber) {
                    System.out.println(loopNumber + "loopppppp");
                    //if (loopNumber == 2){
                       // gifFromAssets.reset();
                    //}
                    if (0== loopNumber){
                        Toast.makeText(MainActivity.this,"donghuanwanle ",Toast.LENGTH_SHORT).show();
                        gifFromAssets.seekToFrame(0);//  设置到第几正动画
                        //gifFromAssets.reset(); ／／从新开始动画
                        gifFromAssets.stop();
                    }
                }


            });


        }catch (Exception e){
            e.printStackTrace();
        }








    }
}
