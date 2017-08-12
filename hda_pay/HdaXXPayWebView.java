package cn.haodiana.hda_pay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URISyntaxException;


//H5支付demo
public class HdaXXPayWebView extends Activity{
    private boolean PaySuccess=false; //是否支付成功 默认支付未成功
    private boolean CheckResult=false;//是否进入检查支付结果阶段  默认未进入
    private int Count=0;//记录 onPause 次数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WebView webView=new WebView(this);
        String url=getIntent().getStringExtra("prepay_res");
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
//                Log.e("============url:",url);
                    CheckResult=true;
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
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(CheckResult&&Count>0){
            checkPay();
        }
        Count++;
    }


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
                    ResponseErr err=new ResponseErr();
                    err.code=999;
                    err.message="支付失败";
                    Intent intent =new Intent();
                    intent.putExtra("payErrInfo",err);
                    setResult(400,intent);
                    finish();

                }
            }
        });
    }

}