package cn.haodian.demowidget;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.TextView;
import android.widget.Toast;

public class MyToast {

	private static long lastTime = 0;
	private static Toast toast;

	public static void showToast(Context context, String text) {
		if (showTime()) {
			TextView textView=new TextView(context);

			int roundRadius = 15; //圆角半径
			GradientDrawable backGround = new GradientDrawable();//创建drawable
			backGround.setColor(Color.BLACK);
			backGround.setCornerRadius(roundRadius);
			backGround.setStroke(5,Color.BLACK);
			backGround.setAlpha(180);
			textView.setBackground(backGround);

			textView.setTextColor(Color.WHITE);
			textView.setPadding(50,30,50,30);
			toast = new Toast(context);
			toast.setView(textView);
			textView.setText(text);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private static boolean showTime() {
		//1.5s内只能弹出一次
		long nowTime = System.currentTimeMillis();
		if ((nowTime - lastTime) > 1500) {
			lastTime = nowTime;
			return true;
		} else {
			return false;
		}
	}
}
