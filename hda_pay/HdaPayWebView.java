package cn.haodiana.hda_pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.switfpass.pay.MainApplication;
import com.switfpass.pay.activity.PayPlugin;
import com.switfpass.pay.bean.RequestMsg;

public class HdaPayWebView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String prepay_res=getIntent().getStringExtra("prepay_res");
        String pay_type=getIntent().getStringExtra("pay_type");

        if(pay_type.equals("H5")){
            RequestMsg req_msg = new RequestMsg();
            req_msg.setTokenId(prepay_res);
            req_msg.setTradeType(MainApplication.PAY_WX_WAP);
            PayPlugin.unifiedH5Pay(this, req_msg);
        }else{
            RequestMsg req_msg1 = new RequestMsg();
            req_msg1.setTokenId(prepay_res);
            req_msg1.setTradeType(MainApplication.WX_APP_TYPE);
            req_msg1.setAppId(SklStaticParams.WX_APPID);//wxd3a1cdf74d0c41b3
            PayPlugin.unifiedAppPay(this, req_msg1);
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (data == null) {
            pay_failed();
            return;
        }

        String respCode = data.getExtras().getString("resultCode");
        if (!TextUtils.isEmpty(respCode) && respCode.equalsIgnoreCase("success")) {
            checkPay();
        }else {
            pay_failed();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //检查支付结果
    private void checkPay(){
        SklPay.checkPayResult();
        SklPay.setSKLPayCheckResultListener(new SklPay.SKLPayCheckResultListener() {
            @Override
            public void OnCheckResult(ResponseBase bs) {
                if(bs instanceof ResponseTradeQuery){
                    Intent intent =new Intent();
                    intent.putExtra("paySucInfo",bs);
                    setResult(200,intent);
                    finish();

                }else{
                    pay_failed();
                }
            }
        });
    }


    private void pay_failed(){
        ResponseErr err=new ResponseErr();
        err.code=999;
        err.message="支付失败";
        Intent intent =new Intent();
        intent.putExtra("payErrInfo",err);
        setResult(400,intent);
        finish();
    }
}
