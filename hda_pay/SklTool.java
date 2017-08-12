//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.haodiana.hda_pay;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class SklTool {
    public static boolean DEBUG = true;

    public SklTool() {
    }

    public static void setDEBUG(boolean enable) {
        DEBUG = enable;
    }

    public static void printLog(String log) {
        if(DEBUG) {
            Log.e(Thread.currentThread().getName(), log + "   ===>> 关闭log,可调用SklPayTool.setDEBUG(false)");
        }

    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException var13) {
            var13.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException var12) {
                var12.printStackTrace();
            }

        }

        return sb.toString();
    }

    public static String getSign(RequestBase o) {
        HashMap params = new HashMap();
        copyParam(o, params);
        Set keyset = params.keySet();
        ArrayList list = new ArrayList(keyset);
        Collections.sort(list);
        String encryptString = "";

        for(int i = 0; i < list.size(); ++i) {
            if(!((String)list.get(i)).equals("sign")) {
                if(encryptString.equals("")) {
                    encryptString = encryptString + (String)list.get(i) + "=" + (String)params.get(list.get(i));
                } else {
                    encryptString = encryptString + "&" + (String)list.get(i) + "=" + (String)params.get(list.get(i));
                }
            }
        }

        return MD5(encryptString + SklStaticParams.SECRET_KEY).toUpperCase();
    }

    public static void copyParam(RequestBase model, HashMap<String, String> params) {
        if(model != null) {
            Field[] fs = model.getClass().getFields();
            if(fs != null) {
                for(int k = 0; k < fs.length; ++k) {
                    String key = fs[k].getName();
                    String value = null;

                    try {
                        Field e = fs[k];
                        Object o = e.get(model);
                        if(o != null) {
                            value = o.toString();
                        }
                    } catch (IllegalArgumentException var8) {
                        var8.printStackTrace();
                    } catch (IllegalAccessException var9) {
                        var9.printStackTrace();
                    }

                    if(value != null && !value.equals(String.valueOf(-1)) && !key.equals("serialVersionUID") && !key.equals("$change")) {
                        params.put(key, value);
                    }
                }
            }
        }

    }

    public static String MD5(String sourceStr) {
        String result = "";

        try {
            MessageDigest e = MessageDigest.getInstance("MD5");
            e.update(sourceStr.getBytes());
            byte[] b = e.digest();
            StringBuffer buf = new StringBuffer("");

            for(int offset = 0; offset < b.length; ++offset) {
                int i = b[offset];
                if(i < 0) {
                    i += 256;
                }

                if(i < 16) {
                    buf.append("0");
                }

                buf.append(Integer.toHexString(i));
            }

            result = buf.toString();
        } catch (NoSuchAlgorithmException var7) {
            System.out.println(var7);
        }

        return result;
    }

    public static boolean isRightResponseInfo(String responseinfo) {
        return !responseinfo.startsWith("<html>") && !responseinfo.contains("<html") && !responseinfo.equals("");
    }
}
