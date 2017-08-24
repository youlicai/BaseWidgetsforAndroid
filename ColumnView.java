package cn.haodian.demowidget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by 立才 on 2017/8/23.
 */

public class ColumnView extends RelativeLayout {

    private ImageView column_image;
    private TextView column_text;
    private TextView column_text_num;
    private ImageView column_image_click_tip;

    @android.support.annotation.IdRes
    int column_image_id=1000;
    @android.support.annotation.IdRes
    int column_image_click_tip_id=4004;


    public ColumnView(Context context) {
        super(context);
        init(context);
    }

    public ColumnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        setPadding(20,0,20,0);
        setBackgroundColor(Color.CYAN);
        setGravity(Gravity.CENTER_VERTICAL);


        RelativeLayout.LayoutParams column_image_params = new RelativeLayout.LayoutParams(80 ,80);
        column_image_params.addRule(RelativeLayout.CENTER_VERTICAL);
        column_image=new ImageView(context);
        column_image.setId(column_image_id);
        column_image.setLayoutParams(column_image_params);


        RelativeLayout.LayoutParams column_text_params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT ,LayoutParams.WRAP_CONTENT);
        column_text_params.addRule(RelativeLayout.RIGHT_OF,column_image_id);
        column_text_params.addRule(RelativeLayout.CENTER_VERTICAL);
        column_text_params.setMargins(40,0,0,0);
        column_text=new TextView(context);
        column_text.setTextSize(16);
        column_text.setLayoutParams(column_text_params);


        RelativeLayout.LayoutParams column_text_num_params = new RelativeLayout.LayoutParams(50 ,50);
        column_text_num_params.addRule(RelativeLayout.LEFT_OF,column_image_click_tip_id);
        column_text_num_params.addRule(RelativeLayout.CENTER_VERTICAL);
        column_text_num_params.setMargins(0,0,30,0);
        column_text_num=new TextView(context);
        column_text_num.setGravity(Gravity.CENTER);
        column_text_num.setLayoutParams(column_text_num_params);
        column_text_num.setIncludeFontPadding(false);//*****清除默认padding*****

        //背景颜色
        int roundRadius = 30; // 8dp 圆角半径
        GradientDrawable backGround = new GradientDrawable();//创建drawable
        backGround.setColor(Color.RED);
        backGround.setCornerRadius(roundRadius);
        column_text_num.setBackground(backGround);


        RelativeLayout.LayoutParams column_image_click_tip_params = new RelativeLayout.LayoutParams(50 ,50);
        column_image_click_tip_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        column_image_click_tip_params.addRule(RelativeLayout.CENTER_VERTICAL);
        column_image_click_tip=new ImageView(context);
        column_image_click_tip.setId(column_image_click_tip_id);
        column_image_click_tip.setLayoutParams(column_image_click_tip_params);

        addView(column_image);
        addView(column_text);
        addView(column_text_num);
        addView(column_image_click_tip);
    }



    public void setColumn(int resId_column_image,String text,int num,int column_image_click_tip_resId,final OnColumnListener onColumnListener){
        column_image.setImageResource(resId_column_image);
        column_text.setText(text);


        if(num==0){
            column_text_num.setText("");
        }else if(num<0){
            column_text_num.setVisibility(INVISIBLE);
        }else
            column_text_num.setText(num+"");


        if(column_image_click_tip_resId==0){
            column_image_click_tip.setVisibility(INVISIBLE);
        }else
            column_image_click_tip.setImageResource(column_image_click_tip_resId);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onColumnListener!=null)
                    onColumnListener.OnColumnClick();
            }
        });
    }

    public interface OnColumnListener{
        void OnColumnClick();
    }
}
