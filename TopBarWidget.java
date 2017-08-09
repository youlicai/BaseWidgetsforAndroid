package cn.haodian.demowidget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TopBarWidget extends RelativeLayout {

    private Context context;

    private RelativeLayout left,center,right;

    public TopBarWidget(Context context, AttributeSet attrs){
        super(context,attrs);
        this.context=context;

        left=new RelativeLayout(context);
        center=new RelativeLayout(context);
        right=new RelativeLayout(context);
        initTopBar(0,0);
    }

    public void initTopBar(int width,int height){
        RelativeLayout.LayoutParams left_params = new RelativeLayout.LayoutParams(width == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : width, height == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : height);
        left_params.addRule(RelativeLayout.CENTER_VERTICAL);
        left.setLayoutParams(left_params);

        RelativeLayout.LayoutParams center_params = new RelativeLayout.LayoutParams(width == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : width, height == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : height);
        center_params.addRule(RelativeLayout.CENTER_VERTICAL);
        center.setLayoutParams(left_params);


        RelativeLayout.LayoutParams right_params = new RelativeLayout.LayoutParams(width == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : width, height == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : height);
        right_params.addRule(RelativeLayout.CENTER_VERTICAL);
        right.setLayoutParams(left_params);

        addView(left);
        addView(center);
        addView(right);
    }

    //topbar左边
    public void setLeftText(String text,int textColorId,int textSize){
        TextView tv=new TextView(context);
        tv.setText(text);
        tv.setTextColor(textColorId);
        tv.setTextSize(textSize);
        left.addView(tv);
    }

    public void setLeftImage(int resId){

    }

    public void setLeftImage_Text(int resId,String text,int textColorId,int textSize){

        ImageView iv=new ImageView(context);
        iv.setBackgroundResource(resId);
        left.addView(iv);
        TextView tv=new TextView(context);
        tv.setText(text);
        tv.setTextColor(textColorId);
        tv.setTextSize(textSize);
        left.addView(tv);
    }

    //topbar中央
    public void setCenterText(String text,int textColorId,int textSize){

    }

    //topbar右边
    public void setRightText(){

    }

    public void setRightImage(){

    }


    public void setTopbarBgColor(int resId){
        this.setBackgroundColor(resId);
    }

    public interface  OnTopbarListener{
        void OnLeftClick();
        void OnCenterClick();
        void OnRightClick();
    }



}

