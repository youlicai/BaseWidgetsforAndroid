package cn.haodian.demowidget.fasthttp;

import android.content.Context;
import android.widget.ImageView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by 立才 on 2017/1/17.
 */

public class ImageTools {
    private static ImageTools mImageApi=new ImageTools();

    private ImageTools(){}
    public static ImageTools getImageApi(){
        return mImageApi;
    }

    public void showImage(Context context, final ImageView imageView,String imageUrl,int default_image,int failed_image,int width,int height){
        if(imageView==null||imageUrl==""){
            return;
        }
        RequestQueue mQueue = Volley.newRequestQueue(context);
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,
                default_image, failed_image);
        if(width==0||height==0){
            imageLoader.get(imageUrl,listener);
        }else
            imageLoader.get(imageUrl,listener,width,height);
    }

}
