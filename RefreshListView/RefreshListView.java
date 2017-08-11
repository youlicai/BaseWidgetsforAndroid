package cn.haodian.demowidget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class RefreshListView extends SwipeRefreshLayout  implements SwipeRefreshLayout.OnRefreshListener {

    public static final int STATUS_READY= 0;// 准备阶段
    public static final int STATUS_LOADING = 1;// 加载中
    public static final int STATUS_NO_DATA = 2;// 没有数据
    public static final int STATUS_SUCCESS= 3;// 成功
    public static final int STATUS_ERROR = 4;// 加载失败


    public int STATUS_=STATUS_READY;

    private Context context;
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
                            mRefreshLisViewListener.OnLoading();
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
                    mRefreshLisViewListener.OnLoading();
                }
            }
        });
        lv.addFooterView(lm);
        addView(lv);
    }


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
            setRefreshLisViewStatus(STATUS_READY);
            mRefreshLisViewListener.OnRefresh();
        }
    }

    public interface RefreshLisViewListener{
        void OnLoading();
        void OnRefresh();
    }

    public void setRefreshLisViewStatus(int Status) {
        STATUS_=Status;
        switch (STATUS_){
            case STATUS_READY:
                lm.setLoadMoreStatus(LoadMore.STATUS_HIDE);
                setRefreshing(false);
                break;
            case STATUS_LOADING:
                lm.setLoadMoreStatus(LoadMore.STATUS_LOADING);
                break;
            case STATUS_NO_DATA:
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

}
