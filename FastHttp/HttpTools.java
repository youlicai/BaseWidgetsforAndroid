package pw.onlyou.easy.FastHttp;

import android.content.Context;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 立才 on 2017/1/17.
 */

public class HttpTools {
    private static HttpTools httpApi=new HttpTools();
    private RequestQueue requestQueue;
    private Gson gson = new Gson();

    private HttpTools(){}
    public static HttpTools getHttpApi(){
        return httpApi;
    }

    public void sendRequest(final String url,final int method, final boolean fromCache, final RequestBase model, Class class_, Context context,FastHttpInterface fastHttp){
        if(null==requestQueue){
            requestQueue= Volley.newRequestQueue(context);
        }
        fastHttp.OnLoading();
        String real_url=getRequestUrl(url,model);
        printLog("\n 请求url:"+real_url);
        if(method==HttpConfig.GET){
            sendRequestGet(requestQueue,real_url,fromCache,class_,fastHttp);
        }else
            sendRequestPost(requestQueue,url,model,class_,fastHttp);
    }

    private void sendRequestPost(final RequestQueue requestQueue, final String url, final RequestBase model, final Class class_, final FastHttpInterface fastHttp){

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        printLog("\n 返回数据"+url+"\n response:" + result);
                        ResponseBase responseData = analyzingJSON(result,class_);
                        if (!(responseData instanceof ResponseError)) {
                            fastHttp.OnSuccess(responseData);
                        } else {
                            ResponseError err= (ResponseError) responseData;
                            fastHttp.OnFailed(err);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                printLog("网络故障或其他问题！具体详细:"+error.getMessage());
                fastHttp.OnFailed(new ResponseError());
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                copyParam(model, map);
                return map;
            }
        };
        request.setRetryPolicy( new DefaultRetryPolicy(HttpConfig.getTimeOut(),//默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT ) );
        request.setTag("REQUEST");
        requestQueue.add(request);
    }

    private  void sendRequestGet(RequestQueue requestQueue, final String url, final boolean fromCache, final Class class_, final FastHttpInterface fastHttp){
        if(fromCache){
            return;
        }
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        printLog("请求内容为:"+result);
                        ResponseBase resp = analyzingJSON(result, class_);
                        if (!(resp instanceof ResponseError)) {
                            fastHttp.OnSuccess(resp);
                        } else {
                            ResponseError err= (ResponseError) resp;
                            fastHttp.OnFailed(err);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                printLog("网络故障或其他问题！具体详细:"+error.getMessage());
                fastHttp.OnFailed(new ResponseError());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Cache-Control", "public, max-age=60");
                headers.put("Charset", "UTF-8");
                headers.put("Content-Type", "application/x-javascript");
                headers.put("Accept-Encoding", "gzip,deflate");
                headers.put("Connection", "keep-alive");
                headers.put("Keep-Alive", "4000");
                return headers;
            }

            @Override
            public Cache.Entry getCacheEntry() {
                return super.getCacheEntry();
            }

            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(HttpConfig.getTimeOut(),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        request.setShouldCache(true);
        request.setTag("REQUEST");
        requestQueue.add(request);
    }

    public void cancelAllRequests(){
        if(requestQueue!=null)
            requestQueue.cancelAll("REQUEST");
    }


    private static final int FLAG = -888;

    private static void copyParam(RequestBase model,
                                  HashMap<String, String> params) {
        if (model != null) {
             Field[] fs = model.getClass().getDeclaredFields();
//            Field[] fs = model.getClass().getFields();
            if (fs != null) {
                for (int k = 0; k < fs.length; k++) {
                    String key = fs[k].getName();
                    String value = null;
                    try {
                        Field f = fs[k];
                        Object o = f.get(model);
                        if (o != null) {
                            value = o.toString();
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    if (value != null && (!value.equals(String.valueOf(FLAG)))) {
                        if(!key.equals("serialVersionUID"))
                            params.put(key, value);
                    }
                }
            }
        }
    }

    private  boolean isRightResponseInfo(String responseinfo) {
        if (responseinfo.startsWith("<html>")||responseinfo.contains("<html")) // 获取xml或者json数据，如果获取到的数据为html，则为null
            return false;
        return true;
    }


    private ResponseBase analyzingJSON(String json, Class<ResponseBase> class_) {
        ResponseBase resp = null;

        if (isRightResponseInfo(json)) {
            try {
                resp = gson.fromJson(json, class_);
                if(resp.is_resp_ok()){
                    return resp;
                }
            } catch (Exception e) {
                e.printStackTrace();
                printLog("接口出问题！");
            }
        }
        ResponseError errRes = new ResponseError();
        errRes.message = resp.get_message();
        errRes.code=resp.get_code();
        return errRes;
    }

    private String getRequestUrl(String url, RequestBase o) {
        String requestUrlParams = "";
        HashMap<String, String> params = new HashMap<String, String>();
        copyParam(o, params);
        Collection<String> keyset = params.keySet();
        List<String> list = new ArrayList<String>(keyset);
        url=url+"?";
        int list_size=list.size();
        for (int i = 0; i < list_size; i++) {
            requestUrlParams += list.get(i) + "="
                    + params.get(list.get(i));
            if(i<list_size-1){
                requestUrlParams +="&";
            }
        }
        return url + requestUrlParams;

    }


    public interface FastHttpInterface{
        void OnSuccess(ResponseBase resp);
        void OnFailed(ResponseError err);
        void OnLoading();
    }

    //======================================== 工具 ======================================

    public static boolean DEBUG = true;
    public static void setDEBUG(boolean enable) {
        DEBUG = enable;
    }

    public static void printLog(String log) {
        if(DEBUG) {
            Log.e(Thread.currentThread().getName(), log + "   ===>> 关闭log,可调用SklPayTool.setDEBUG(false)");
        }

    }
}
