package cn.haodian.demowidget;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


public class Permission {
    public static boolean checkPermission(Context context, Activity activity,String permissionCode, int resultCode){
        if (Build.VERSION.SDK_INT >= 23) {
            //检查权限
            if (ContextCompat.checkSelfPermission(context, permissionCode)
                    != PackageManager.PERMISSION_GRANTED) {
                //进入到这里代表没有权限.
                ActivityCompat.requestPermissions(activity, new String[]{permissionCode}, resultCode);
                return false;
            }
            return true;
		}
        return true;
    }
}
