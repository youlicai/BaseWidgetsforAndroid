//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.haodiana.hda_pay;

import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class SklHttp {
    public SklHttp() {
    }

    public static void sendRequest(final Handler handler, String url, final RequestBase model) {
        final String real_url = getJoinUrl(url, model);
        SklTool.printLog("request_url => " + real_url);
        final Message msg = handler.obtainMessage();
        (new Thread() {
            public void run() {
                super.run();

                try {
                    URL e = new URL(real_url);
                    HttpURLConnection conn = (HttpURLConnection)e.openConnection();
                    int code = conn.getResponseCode();
                    if(code == 200) {
                        InputStream is = conn.getInputStream();
                        String result = SklTool.convertStreamToString(is);
                        Object responseData = SklHttp.analyzingJSON(result, model);
                        if(!(responseData instanceof ResponseErr)) {
                            msg.what = 200;
                        } else {
                            msg.what = 500;
                        }

                        msg.obj = responseData;
                        SklTool.printLog("支付验证请求返回结果:" + result);
                        handler.sendMessage(msg);
                    } else {
                        msg.what = 500;
                        handler.sendMessage(msg);
                    }
                } catch (Exception var7) {
                    var7.printStackTrace();
                    SklTool.printLog("网络故障！");
                    msg.what = 500;
                    handler.sendMessage(msg);
                }

            }
        }).start();
    }

    private static String getJoinUrl(String url, RequestBase o) {
        HashMap params = new HashMap();
        SklTool.copyParam(o, params);
        Set keyset = params.keySet();
        ArrayList list = new ArrayList(keyset);
        Collections.sort(list);
        String encryptString = "";
        String requestUrlParams = "";

        for(int i = 0; i < list.size(); ++i) {
            if(encryptString.equals("")) {
                encryptString = encryptString + (String)list.get(i) + "=" + (String)params.get(list.get(i));
            } else {
                encryptString = encryptString + "&" + (String)list.get(i) + "=" + (String)params.get(list.get(i));
            }

            if(requestUrlParams.equals("")) {
                requestUrlParams = requestUrlParams + "?" + (String)list.get(i) + "=" + (String)params.get(list.get(i));
            } else {
                requestUrlParams = requestUrlParams + "&" + (String)list.get(i) + "=" + (String)params.get(list.get(i));
            }
        }

        return url + requestUrlParams;
    }

    private static Object analyzingJSON(String json, RequestBase model) {
        int code = 999;
        String msg = "服务器异常";
        if(SklTool.isRightResponseInfo(json)) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                code = jsonObject.getInt("status");
                msg = jsonObject.getString("message");
                if(code == 0) {
                    if(model instanceof RequestPayValidate) {
                        ResponsePayValidate errRes2 = new ResponsePayValidate();
                        errRes2.status = jsonObject.getString("status");
                        errRes2.trade_no = jsonObject.getString("trade_no");
                        errRes2.ds_trade_no = jsonObject.getString("ds_trade_no");
                        errRes2.trade_type = jsonObject.getString("trade_type");
                        errRes2.prepay_res = jsonObject.getString("prepay_res");
                        return errRes2;
                    }
                    ResponseTradeQuery errRes1 = new ResponseTradeQuery();
                    errRes1.status = jsonObject.getString("status");
                    errRes1.message = jsonObject.getString("message");
                    errRes1.trade_status = jsonObject.getString("trade_status");
                    errRes1.trade_no = jsonObject.getString("trade_no");
                    errRes1.ds_trade_no = jsonObject.getString("ds_trade_no");
                    errRes1.pa_trade_no = jsonObject.getString("pa_trade_no");
                    errRes1.tp_trade_no = jsonObject.getString("tp_trade_no");
                    errRes1.pay_time = jsonObject.getString("pay_time");
                    errRes1.total_fee = jsonObject.getString("total_fee");
//                    errRes1.tp_discount_fee = jsonObject.getString("tp_discount_fee");
                    errRes1.pay_fee = jsonObject.getString("pay_fee");
                    errRes1.trade_type = jsonObject.getString("trade_type");
                    return errRes1;
                }
            } catch (JSONException var6) {
                var6.printStackTrace();
                SklTool.printLog("json 解析发生错误！");
            }
        }

        ResponseErr errRes = new ResponseErr();
        errRes.message = msg;
        errRes.code = code;
        return errRes;
    }
}

