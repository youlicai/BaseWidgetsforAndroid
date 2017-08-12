package cn.haodiana.hda_pay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URISyntaxException;



//H5支付demo
public class SKLPAY_H5Activity extends Activity{
    private boolean PaySuccess=false; //是否支付成功 默认支付未成功
    private boolean CheckResult=false;//是否进入检查支付结果阶段  默认未进入
    private int Count=0;//记录 onPause 次数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WebView webView=new WebView(this);
        String url=getIntent().getStringExtra("url");
        if(null==url||!url.contains("http")){
            finish();
        }
        webView.loadUrl(url);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("============url:",url);
                if (url.contains("TRADE_FINISHED")) {//初步判断支付成功
                    CheckResult=true;
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
                    return false;
                } else {
                    if (url.startsWith("alipays://") || url.startsWith("intent://")) {
                        Intent intent;
                        try {
                            //调用支付宝app
                            intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                            intent.addCategory("android.intent.category.BROWSABLE");
                            intent.setComponent(null);
                            startActivity(intent);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        return true;
                    } else {
                        view.loadUrl(url);
                        return false;
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("==================","onResume");
        handler_check_pay_result.sendEmptyMessageDelayed(900,1500);
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
                case 900:
                    Count++;
                    if(Count>2&&!CheckResult&&!PaySuccess){
                        Count=0;
                        CheckResult=false;
                        ResponseErr err1=new ResponseErr();
                        err1.code=999;
                        err1.message="取消支付";
                        Intent intent1 =new Intent();
                        intent1.putExtra("payErrInfo",err1);
                        setResult(400,intent1);
                        finish();
                    }
                    break;
            }
        }
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

}