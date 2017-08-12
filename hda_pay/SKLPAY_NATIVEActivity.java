package cn.haodiana.hda_pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import java.util.Map;


//原生支付demo
public class SKLPAY_NATIVEActivity extends Activity {
    private boolean PaySuccess=false; //是否支付成功 默认支付未成功
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        final String orderInfo =getIntent().getStringExtra("url");
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(SKLPAY_NATIVEActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo,true);

                Message msg = new Message();
                msg.what = 200;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 200: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        Toast.makeText(SKLPAY_NATIVEActivity.this, "支付成功"+resultInfo, Toast.LENGTH_LONG).show();
                        new Thread(){
                            int i=0;
                            @Override
                            public void run() {
                                super.run();
                                while (i<5&&!PaySuccess) {//轮询5次，如果5次还没支付成功，则判定支付失败
                                    handler_check_pay_result.sendEmptyMessage(200);
                                    try {
                                        sleep(4000);//4秒一次
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    i++;
                                }
                                if(!PaySuccess)
                                    handler_check_pay_result.sendEmptyMessage(500);
                            }
                        }.start();

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(SKLPAY_NATIVEActivity.this, "支付失败"+resultInfo, Toast.LENGTH_LONG).show();
                        handler_check_pay_result.sendEmptyMessage(500);

                    }
                    break;
                }

                default:
                    break;
            }

//            finish();
        };
    };



    //检查支付结果
    private void checkPay(){
        SklPay.checkPayResult();
        SklPay.setSKLPayCheckResultListener(new SklPay.SKLPayCheckResultListener() {
            @Override
            public void OnCheckResult(ResponseBase bs) {
                if(bs instanceof ResponseTradeQuery){
                    PaySuccess=true;
                    Intent intent =new Intent();
                    intent.putExtra("paySucInfo",bs);
                    setResult(200,intent);
                    finish();

                }else{
                    Intent intent2 =new Intent();
                    intent2.putExtra("payErrInfo",bs);
                    setResult(400,intent2);
                    finish();
                }
            }
        });
    }

    private Handler handler_check_pay_result=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    checkPay();
                    break;
                case 500:
                    ResponseErr err=new ResponseErr();
                    err.code=999;
                    err.message="支付失败";
                    Intent intent =new Intent();
                    intent.putExtra("payErrInfo",err);
                    setResult(400,intent);
                    finish();
                    break;
            }
        }
    };

}
