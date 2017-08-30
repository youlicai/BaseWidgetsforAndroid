package cn.haodian.demowidget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RefreshListView extends RelativeLayout implements SwipeRefreshLayout.OnRefreshListener {

    public static final int STATUS_READY= 0;// 准备阶段
    public static final int STATUS_LOADING = 1;// 加载中
    public static final int STATUS_NO_MORE_DATA = 2;// 没有数据
    public static final int STATUS_SUCCESS= 3;// 成功
    public static final int STATUS_ERROR = 4;// 加载失败
    public int STATUS_=STATUS_READY;

    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout loading;
    private LinearLayout loading_error;
    private ListView lv;
    private LoadMore lm;
    public RefreshListView(Context context) {
        super(context);
        this.context=context;
        init();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
    }

    private void init(){
        swipeRefreshLayout=new SwipeRefreshLayout(context);
        swipeRefreshLayout.setOnRefreshListener(this);


        lv=new ListView(context);
        lm=new LoadMore(context);
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {}
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {//滑到底部判断是不是要加载
                if ((firstVisibleItem + visibleItemCount) == totalItemCount&&(STATUS_==STATUS_SUCCESS||STATUS_==STATUS_ERROR)) {
                    View lastVisibleItemView = lv.getChildAt(lv.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == lv.getHeight()) {
                        if(mRefreshLisViewListener!=null){
                            setRefreshLisViewStatus(STATUS_LOADING);
                            mRefreshLisViewListener.OnLoadingMore();
                        }
                    }
                }
            }
        });
        lm.loadMoreClick(new LoadMore.OnLoadMoreListener() {
            @Override
            public void OnLoading() {
                if(mRefreshLisViewListener!=null){
                    setRefreshLisViewStatus(STATUS_LOADING);
                    mRefreshLisViewListener.OnLoadingMore();
                }
            }
        });

        lv.addFooterView(lm);
        swipeRefreshLayout.addView(lv);
        addView(swipeRefreshLayout);

        pulling();
    }


    //设置加载中页面
    public void setLoadingView(View loadingView){
        loading= (LinearLayout) loadingView;
    }

    //设置加载失败页面
    public void setLoadErrorView(View loadErrorView){
        loading_error= (LinearLayout) loadErrorView;
    }

    //配置adapter
    public void setAdapter(BaseAdapter adapter){
        lv.setAdapter(adapter);
    }


    private RefreshLisViewListener mRefreshLisViewListener;
    public void setOnRefreshLisViewListener(RefreshLisViewListener mRefreshLisViewListener){
        this.mRefreshLisViewListener=mRefreshLisViewListener;
    }

    @Override
    public void onRefresh() {
        if(mRefreshLisViewListener!=null){
            mRefreshLisViewListener.OnRefresh();
            setRefreshLisViewStatus(STATUS_READY);
        }
    }

    public interface RefreshLisViewListener{
        void OnLoadingMore();
        void OnRefresh();
        void OnRetry();
    }

    public void setRefreshLisViewStatus(int Status) {
        STATUS_=Status;
        switch (STATUS_){
            case STATUS_READY:
                lm.setLoadMoreStatus(LoadMore.STATUS_HIDE);
                break;
            case STATUS_LOADING:
                lm.setLoadMoreStatus(LoadMore.STATUS_LOADING);
                break;
            case STATUS_NO_MORE_DATA:
                lm.setLoadMoreStatus(LoadMore.STATUS_NO_DATA);
                break;
            case STATUS_SUCCESS:
                lm.setLoadMoreStatus(LoadMore.STATUS_MORE);
                break;
            case STATUS_ERROR:
                lm.setLoadMoreStatus(LoadMore.STATUS_ERROR);
                break;
        }
    }

    //第一次 打开页面 数据加载中
    public void pulling(){
        loading=new LinearLayout(context);
        loading.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        loading.setGravity(Gravity.CENTER);
        loading.setVisibility(INVISIBLE);
        loading.setVisibility(VISIBLE);
        TextView tv=new TextView(context);
        tv.setText("加载中.........");
        loading.addView(tv);
        if(null!=loading_error){
            removeView(loading_error);
        }
        addView(loading);
    }

    //第一次 打开页面数据加载成功
    public void pullSuccess(){
        loading.setVisibility(GONE);
        setRefreshLisViewStatus(STATUS_SUCCESS);
        swipeRefreshLayout.setRefreshing(false);
    }

    //第一次 打开页面加载成功但 数据不足分页
    public void pullSuccessNoMore(){
        loading.setVisibility(GONE);
        setRefreshLisViewStatus(STATUS_READY);
        swipeRefreshLayout.setRefreshing(false);
    }

    //第一次 打开页面加载失败
    public void pullError(){
        loading_error=new LinearLayout(context);
        loading_error.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        loading_error.setGravity(Gravity.CENTER);
        loading_error.setVisibility(INVISIBLE);
        loading_error.setVisibility(VISIBLE);
        TextView tv=new TextView(context);
        tv.setText("加载失败");
        loading_error.addView(tv);
        removeView(loading);
        addView(loading_error);

        loading_error.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mRefreshLisViewListener!=null){
                    pulling();
                    mRefreshLisViewListener.OnRetry();
                    setRefreshLisViewStatus(STATUS_READY);
                }
            }
        });
    }

    //重试成功
    public void retrySuccess(){
        pullSuccess();
    }

    //重试失败
    public void retryError(){
        pullError();
    }

    //上推-成功
    public void pushSuccess(){
        setRefreshLisViewStatus(STATUS_SUCCESS);
    }

    //上推-出现异常
    public void pushError(){
        setRefreshLisViewStatus(STATUS_ERROR);
    }

    //上推-没有数据
    public void pushNodata(){
        setRefreshLisViewStatus(STATUS_NO_MORE_DATA);
    }
}
