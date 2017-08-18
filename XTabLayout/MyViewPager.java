package cn.haodian.demowidget.tblayout;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 立才 on 2017/8/18.
 */

public class MyViewPager extends ViewPager {
    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        return true;
//    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }


//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        return true;
//    }
//

}
