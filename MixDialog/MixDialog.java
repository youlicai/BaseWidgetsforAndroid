package cn.haodian.demowidget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 立才 on 2017/1/19.
 */

//dialog  确认框+加载框
public class MixDialog {

    private static MixDialog maxDialog=new MixDialog();
    private Dialog dialog;

    //点击颜色
    private int ACTION_DOWN_COLOR=Color.BLUE;
    private int ACTION_UP_COLOR=Color.WHITE;

    //标题颜色
    private int TITLE_COLOR=Color.BLUE;

    //内容颜色
    private int CONTENT_COLOR=Color.BLUE;

    //按钮文字颜色
    private int BUTTON_COLOR=Color.BLUE;

    //对话框宽度
    private int DIALOG_WIDTH=700;

    private final String CONFIRM_TEXT="确认";
    private final String CANCEL_TEXT="取消";


    public static MixDialog getMixDialog(){
        return maxDialog;
    }



    //=================================================  confirm dialog  start   ===============================================
    /**
     * @des  显示确认框
     * @param context
     * @param title
     * @param content
     * @param maxDialogListener
     */
    public void showConfirmDialog(final Context context, String title, String content, final ConfirmDialogListener maxDialogListener ){

        dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();

        //大容器
        LinearLayout dialogLayout = new LinearLayout(context);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setPadding(0,0,0,0);


        //标题
        TextView titleView = new TextView(context);
        titleView.setTextColor(TITLE_COLOR);
        titleView.setPadding(10,10,10,10);
        titleView.setTextSize(18);
        titleView.setGravity(Gravity.CENTER);
        titleView.setText(title);

        //内容
        TextView contentView = new TextView(context);
        contentView.setPadding(20,30,20,40);
        contentView.setTextColor(CONTENT_COLOR);
        contentView.setTextSize(15);
        contentView.setText(content);
        contentView.setGravity(Gravity.CENTER);

        //横线
        View line_horizontal=new View(context);
        LinearLayout.LayoutParams line_params = new LinearLayout.LayoutParams(DIALOG_WIDTH,
                1);
        line_horizontal.setLayoutParams(line_params);
        line_horizontal.setBackgroundColor(Color.LTGRAY);
        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textParam.weight = 1;

        //左边按钮--确定
        final TextView confirm = new TextView(context);
        confirm.setTextColor(BUTTON_COLOR);
        confirm.setTextSize(15);
        confirm.setText(CONFIRM_TEXT);
        confirm.setGravity(Gravity.CENTER);
        confirm.setPadding(20,30,20,30);
        confirm.setLayoutParams(textParam);
        confirm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        confirm.setBackgroundColor(ACTION_DOWN_COLOR);
                        break;
                    case MotionEvent.ACTION_UP:
                        confirm.setBackgroundColor(ACTION_UP_COLOR);
                        break;
                }

                return false;
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maxDialogListener.OnConfirm();
                dialog.dismiss();
            }
        });

        //中间竖线
        View line_vertical=new View(context);
        LinearLayout.LayoutParams line_vertical_params = new LinearLayout.LayoutParams(1,
                ViewGroup.LayoutParams.MATCH_PARENT);
        line_vertical.setLayoutParams(line_vertical_params);
        line_vertical.setBackgroundColor(Color.LTGRAY);


        //右边按钮--取消
        final TextView cancel = new TextView(context);
        cancel.setTextSize(15);
        cancel.setTextColor(BUTTON_COLOR);
        cancel.setText(CANCEL_TEXT);
        cancel.setGravity(Gravity.CENTER);
        cancel.setPadding(20,30,20,30);
        cancel.setLayoutParams(textParam);
        cancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        cancel.setBackgroundColor(ACTION_DOWN_COLOR);
                        break;
                    case MotionEvent.ACTION_UP:
                        cancel.setBackgroundColor(ACTION_UP_COLOR);
                        break;
                }

                return false;
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maxDialogListener.OnCancel();
                dialog.dismiss();
            }
        });

        //底部容器
        LinearLayout bottomLayout = new LinearLayout(context);
        bottomLayout.setOrientation(LinearLayout.HORIZONTAL);
        bottomLayout.addView(confirm);
        bottomLayout.addView(line_vertical);
        bottomLayout.addView(cancel);

        dialogLayout.addView(titleView);
        dialogLayout.addView(contentView);
        dialogLayout.addView(line_horizontal);
        dialogLayout.addView(bottomLayout);


        //背景颜色
        int roundRadius = 15; // 8dp 圆角半径
        GradientDrawable backGround = new GradientDrawable();//创建drawable
        backGround.setColor(Color.WHITE);
        backGround.setCornerRadius(roundRadius);
        backGround.setStroke(5,Color.WHITE);

        window.setContentView(dialogLayout);
        window.setBackgroundDrawable(backGround);
        dialog.show();
    }

    public interface ConfirmDialogListener{
        void OnConfirm();
        void OnCancel();
    }

    //=============================================  confirm dialog  end   ===============================================



    //=============================================  wait dialog start  ==================================================
    private AnimImageView anim=new AnimImageView();
    private List<Integer> resIds= new ArrayList<Integer>();
    //加载中 弹框
    public void showWaitDialog(final Context context, String tips, final CancelDialogListener cancelDialogListener){
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        LinearLayout.LayoutParams dialog_params = new LinearLayout.LayoutParams(230,
                230);
        //大容器
        LinearLayout dialogLayout = new LinearLayout(context);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setGravity(Gravity.CENTER);
        dialogLayout.setLayoutParams(dialog_params);
        ImageView iv=new ImageView(context);

        if(resIds.size()==0) {
            resIds.add(R.mipmap.loading01);
            resIds.add(R.mipmap.loading02);
            resIds.add(R.mipmap.loading03);
            resIds.add(R.mipmap.loading04);
            resIds.add(R.mipmap.loading05);
            resIds.add(R.mipmap.loading06);
            resIds.add(R.mipmap.loading07);
            resIds.add(R.mipmap.loading08);
            resIds.add(R.mipmap.loading09);
            resIds.add(R.mipmap.loading10);
            resIds.add(R.mipmap.loading11);
        }
        anim.setAnimation(iv,resIds);
        anim.start(true,100);
        //文字
        TextView tv_content= new TextView(context);
        tv_content.setTextSize(13);
        tv_content.setPadding(10,20,10,0);
        tv_content.setText(tips);
        tv_content.setTextColor(Color.WHITE);
        tv_content.setGravity(Gravity.CENTER);

        dialogLayout.addView(iv);
        dialogLayout.addView(tv_content);
        dialog.addContentView(dialogLayout,dialog_params);

        int roundRadius = 12; // 8dp 圆角半径
        GradientDrawable backGround = new GradientDrawable();//创建drawable
        backGround.setColor(Color.argb(140,0,0,0));
        backGround.setCornerRadius(roundRadius);

        window.setBackgroundDrawable(backGround);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(anim!=null)
                    anim.stop();
                anim=null;
                cancelDialogListener.OnCancel();
            }
        });
        dialog.show();
    }

    public void dismissWaitDialog(){
        if(dialog!=null&&dialog.isShowing())
            dialog.dismiss();

    }

    interface  CancelDialogListener{
        void OnCancel();
    }

    //=============================================  wait dialog end  ==================================================
}
