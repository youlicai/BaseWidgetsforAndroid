package cn.haodian.demowidget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import java.util.List;

/**
 * Created by 立才 on 2017/8/22.
 */

public class SlidePager extends FrameLayout {

    private boolean LOOP=false;//是否自动循环播放
    private ViewPager vp;
    private LinearLayout dot_layout;//切换点区域
    private View old_dot=null;//上一个切换点
    private final int DotColor=Color.CYAN;
    private final int DotSelectColor=Color.GRAY;
    public SlidePager(Context context) {
        super(context);
        init(context);
    }

    public SlidePager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        vp=new ViewPager(context);
        addView(vp);
    }

    /*
    使用方法
     */
    public  void setView(List<View> views,Context context){
        addDots(views.size(),context);

        vp.setAdapter(new ViewPagerAdapter(views));
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeDotStatus(dot_layout.getChildAt(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if(LOOP) {
            loop(views.size());
        }
    }

    private class ViewPagerAdapter extends PagerAdapter{

        private  List<View> views;
        public ViewPagerAdapter(List<View> views) {
            this.views=views;
        }

        @Override
        public int getCount() {
            return views==null?0:views.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView(views.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if(container.getChildAt(position)!=views.get(position))
                container.addView(views.get(position));
            return views.get(position);
        }
    }


    /**
     * 设置切换点大小及背景颜色
     */
    int roundRadius = 15; // 8dp 圆角半径
    private void setDotBg(View v){
        LinearLayout.LayoutParams lp_dot= new LinearLayout.LayoutParams(20,20);//设置圆点大小
        lp_dot.setMargins(10,10,10,10);//设置圆点外边距
        v.setLayoutParams(lp_dot);

        GradientDrawable backGround_dot = new GradientDrawable();//创建drawable
        backGround_dot.setCornerRadius(roundRadius);
        backGround_dot.setColor(DotColor);
        v.setBackground(backGround_dot);
    }

    /**
     * 添加切换圆点
     * @param size
     * @param context
     */
    private void addDots(int size,Context context){
        dot_layout=new LinearLayout(context);
        dot_layout.setPadding(20,20,20,20);
        dot_layout.setGravity(Gravity.BOTTOM|Gravity.CENTER);
        dot_layout.setOrientation(LinearLayout.HORIZONTAL);

        for (int i=0;i<size;i++){
            View dot=new View(context);
            dot.setTag("dot"+i);
            setDotBg(dot);
            dot_layout.addView(dot);
        }
        changeDotStatus(dot_layout.getChildAt(0));
        addView(dot_layout);
    }


    /**
     * 切换
     * @param v
     */
    private void changeDotStatus(View v){
        GradientDrawable backGround_dot = new GradientDrawable();
        backGround_dot.setCornerRadius(roundRadius);
        backGround_dot.setColor(DotSelectColor);
        v.setBackground(backGround_dot);
        if(old_dot!=null){
            GradientDrawable backGround_select_dot = new GradientDrawable();
            backGround_select_dot.setCornerRadius(roundRadius);
            backGround_select_dot.setColor(DotColor);
            old_dot.setBackground(backGround_select_dot);
        }
        old_dot=v;
    }

    private void loop(final int size){
        new Thread(){
            int i=0;
            @Override
            public void run() {
                super.run();
                while (true){
                    vp.post(new Runnable() {
                        @Override
                        public void run() {
                            if(i>size||i==0){
                                i=0;
                                vp.setCurrentItem(0);
                            }else
                                vp.setCurrentItem(i);

                        }
                    });
                    i++;
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

}
