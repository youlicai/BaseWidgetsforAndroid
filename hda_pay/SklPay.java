
package cn.haodiana.hda_pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;


public class SklPay {
    private static SKLPayValidateListener mSKLPayListener;
    private static SKLPayCheckResultListener mSKLPayCheckListener;

    public SklPay() {
    }

    public static void startSkl(String ds_id, String mch_id, String secret) {
        SklStaticParams.TRADE_NO="";
        SklStaticParams.DS_ID = ds_id;
        SklStaticParams.MCH_ID = mch_id;
        SklStaticParams.SECRET_KEY = secret;
        SklStaticParams.HAS_CONFIG = true;
    }

    public static void setDsTradeNo(String ds_trade_no) {
        SklStaticParams.DS_TRADE_NO = ds_trade_no;
    }

    public static void setReferer(String referer_url){
        SklStaticParams.PAY_REFERER=referer_url;
    }

    public  static void  setAppId(String appid){
        SklStaticParams.WX_APPID=appid;
    }

    public static void pay(String pay_fee, final Activity current_activity) {
        if(!SklStaticParams.HAS_CONFIG) {
            SklTool.printLog("支付未配置参数，先调用SKLPay.init_sklpay()");
        } else {
            String url;
            url = SklStaticParams.PAY_URL_QC;

            RequestPayValidate req = new RequestPayValidate();
            req.pay_fee = pay_fee;

            req.sign = SklTool.getSign(req);
            SklHttp.sendRequest(new Handler() {
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch(msg.what) {
                        case 200:
                            ResponsePayValidate rep = (ResponsePayValidate)msg.obj;
                            if(rep == null) {
                                ResponseErr intent1 = (ResponseErr)msg.obj;
                                SklTool.printLog("支付验证失败!code:" + intent1.code + " message:" + intent1.message);
                                if(SklPay.mSKLPayListener != null) {
                                    SklPay.mSKLPayListener.onValidateFail("支付验证失败!code:" + intent1.code + " message:" + intent1.message);
                                }
                                return;
                            }
                            SklStaticParams.TRADE_NO = rep.trade_no;
                            Intent intent = new Intent(current_activity, HdaXXPayWebView.class);
                            intent.putExtra("prepay_res", rep.prepay_res);
                            current_activity.startActivityForResult(intent, 200);
                            break;
                        case 500:
                            if(msg.obj != null) {
                                ResponseErr rep_err = (ResponseErr)msg.obj;
                                SklTool.printLog("支付验证失败!code:" + rep_err.code + " message:" + rep_err.message);
                                if(SklPay.mSKLPayListener != null) {
                                    SklPay.mSKLPayListener.onValidateFail("支付验证失败!code:" + rep_err.code + " message:" + rep_err.message);
                                }
                            } else {
                                SklTool.printLog("支付验证失败!code:");
                                if(SklPay.mSKLPayListener != null) {
                                    SklPay.mSKLPayListener.onValidateFail("支付验证失败!code:");
                                }
                            }
                    }

                }
            }, url, req);
        }
    }


    public static void pay(String pay_fee, final Activity current_activity,String url,final String type) {
        if(!SklStaticParams.HAS_CONFIG) {
            SklTool.printLog("支付未配置参数，先调用SKLPay.init_sklpay()");
        } else {
            RequestPayValidate req = new RequestPayValidate();
            req.pay_fee = pay_fee;
            req.sign = SklTool.getSign(req);
            SklHttp.sendRequest(new Handler() {
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch(msg.what) {
                        case 200:
                            ResponsePayValidate rep = (ResponsePayValidate)msg.obj;
                            if(rep == null) {
                                ResponseErr intent1 = (ResponseErr)msg.obj;
                                SklTool.printLog("支付验证失败!code:" + intent1.code + " message:" + intent1.message);
                                if(SklPay.mSKLPayListener != null) {
                                    SklPay.mSKLPayListener.onValidateFail("支付验证失败!code:" + intent1.code + " message:" + intent1.message);
                                }
                                return;
                            }
                            SklStaticParams.TRADE_NO = rep.trade_no;
                            switch (type){
                                case "WX_H5":

                                    Intent intent = new Intent(current_activity, HdaWXPayWebView.class);
                                    intent.putExtra("prepay_res", rep.prepay_res);
                                    intent.putExtra("pay_type", "H5");
                                    current_activity.startActivityForResult(intent, 200);

//                                    Intent intent = new Intent(current_activity, HdaXXPayWebView.class);
//                                    intent.putExtra("prepay_res", rep.prepay_res);
//                                    current_activity.startActivityForResult(intent, 200);
                                    break;
                                case "WX_APP":

                                    Intent intent2 = new Intent(current_activity, HdaWXPayWebView.class);
                                    intent2.putExtra("prepay_res", rep.prepay_res);
                                    intent2.putExtra("pay_type", "NV");
                                    current_activity.startActivityForResult(intent2, 200);
                                    break;
                                case "ZFB_H5":
                                    Intent intent3 = new Intent(current_activity,SKLPAY_H5Activity.class);
                                    intent3.putExtra("prepay_res", rep.prepay_res);
                                    current_activity.startActivityForResult(intent3, 200);

                                    break;
                                case "ZFB_APP":
                                    Intent intent4 = new Intent(current_activity, HdaXXPayWebView.class);
                                    intent4.putExtra("prepay_res", rep.prepay_res);
                                    current_activity.startActivityForResult(intent4, 200);

                                    break;
                                case "ZFB_XX":
                                    Intent intent5 = new Intent(current_activity, HdaXXPayWebView.class);
                                    intent5.putExtra("prepay_res", rep.prepay_res);
                                    current_activity.startActivityForResult(intent5, 200);
                                    break;
                            }
                            break;
                        case 500:
                            if(msg.obj != null) {
                                ResponseErr rep_err = (ResponseErr)msg.obj;
                                SklTool.printLog("支付验证失败!code:" + rep_err.code + " message:" + rep_err.message);
                                if(SklPay.mSKLPayListener != null) {
                                    SklPay.mSKLPayListener.onValidateFail("支付验证失败!code:" + rep_err.code + " message:" + rep_err.message);
                                }
                            } else {
                                SklTool.printLog("支付验证失败!code:");
                                if(SklPay.mSKLPayListener != null) {
                                    SklPay.mSKLPayListener.onValidateFail("支付验证失败!code:");
                                }
                            }
                    }

                }
            }, url, req);
        }
    }

    public static void checkPayResult() {
        if(!SklStaticParams.HAS_CONFIG) {
            SklTool.printLog("支付未配置参数，方法调用SKLPay.init_sklpay()");
        } else if(SklStaticParams.TRADE_NO == null) {
            ResponseErr req1 = new ResponseErr();
            req1.code = 999;
            req1.message = "支付没有返回trade_no";
            SklTool.printLog("支付没有返回trade_no");
            if(mSKLPayCheckListener != null) {
                mSKLPayCheckListener.OnCheckResult(req1);
            }

        } else {
            RequestTradeQuery req = new RequestTradeQuery();
            req.ds_trade_no = SklStaticParams.DS_TRADE_NO;
            req.trade_no = SklStaticParams.TRADE_NO;
            req.sign = SklTool.getSign(req);
            SklHttp.sendRequest(new Handler() {
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    ResponseErr err;
                    switch(msg.what) {
                        case 200:
                            ResponseTradeQuery res = (ResponseTradeQuery)msg.obj;
                            if(res == null || !res.status.equals("0")) {
                                err = new ResponseErr();
                                err.code = Integer.parseInt(res.status);
                                err.message = res.message;
                                if(SklPay.mSKLPayCheckListener != null) {
                                    SklPay.mSKLPayCheckListener.OnCheckResult(err);
                                }

                                return;
                            }

                            if(SklPay.mSKLPayCheckListener != null) {
                                SklPay.mSKLPayCheckListener.OnCheckResult(res);
                            }
                            break;
                        case 500:
                            err = (ResponseErr)msg.obj;
                            if(SklPay.mSKLPayCheckListener != null) {
                                SklPay.mSKLPayCheckListener.OnCheckResult(err);
                            }

                            SklTool.printLog("查询失败" + err.message);
                    }

                }
            }, SklStaticParams.CHECK_PAY_URL, req);
        }
    }

    public static void endSkl() {
        SklTool.printLog("销毁支付");
        SklStaticParams.DS_ID = null;
        SklStaticParams.MCH_ID = null;
        SklStaticParams.VERSION = null;
        SklStaticParams.PAY_TYPE = null;
        SklStaticParams.SECRET_KEY = null;
        SklStaticParams.PAY_STYLE = null;
        SklStaticParams.HAS_CONFIG = false;
    }

    public static void setOnSKLPayValidateListener(SKLPayValidateListener sKLPayListener) {
        mSKLPayListener = sKLPayListener;
    }

    public static void setSKLPayCheckResultListener(SKLPayCheckResultListener sKLPayCheckListener) {
        mSKLPayCheckListener = sKLPayCheckListener;
    }

    public interface SKLPayCheckResultListener {
        void OnCheckResult(ResponseBase var1);
    }

    public interface SKLPayValidateListener {
        void onValidateFail(String var1);
    }



    public static void unifiedWxAppPay(String fee, Activity current_activity){
        SklStaticParams.PAY_TYPE="WX";
        pay(fee,current_activity,SklStaticParams.PAY_URL_H5,"WX_APP");
    }
    public static void unifiedWxH5Pay(String fee, Activity current_activity){
        if(SklStaticParams.PAY_REFERER==null){
            SklTool.printLog("需要设置referer,先调用SKLPay.setReferer()");
            return;
        }
        SklStaticParams.PAY_TYPE="WX";
        pay(fee,current_activity,SklStaticParams.PAY_URL_H5,"WX_H5");

    }
    public static void unifiedZfbAppPay(String fee, Activity current_activity){
        SklStaticParams.PAY_TYPE="AP";
        pay(fee,current_activity,SklStaticParams.PAY_URL_H5,"ZFB_APP");
    }
    public static void unifiedZfbH5Pay(String fee, Activity current_activity){
        SklStaticParams.PAY_TYPE="AP";
        pay(fee,current_activity,SklStaticParams.PAY_URL_H5,"ZFB_H5");
    }

    public static void unifiedZfbXXPay(String fee, Activity current_activity){
        SklStaticParams.PAY_TYPE="AP";
        pay(fee,current_activity,SklStaticParams.PAY_URL_QC,"ZFB_XX");
    }




}
