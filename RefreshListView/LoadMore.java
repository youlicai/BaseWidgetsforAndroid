package cn.haodian.demowidget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class LoadMore extends TextView {
	public static final int STATUS_HIDE = 0;// 隐藏
	public static final int STATUS_MORE = 1;// 加载更多
	public static final int STATUS_LOADING = 2;// 加载中
	public static final int STATUS_NO_DATA = 4;// 没有数据
	public static final int STATUS_ERROR = 3;// 加载失败

	public static final String STATUS_MORE_TEXT = "加载更多";
	public static final String STATUS_LOADING_TEXT = "加载中";
	public static final String STATUS_NO_DATA_TEXT = "没有数据";
	public static final String STATUS_ERROR_TEXT = "加载失败";

	private int initStatus = STATUS_MORE;

	public LoadMore(Context context) {
		super(context);
		init();
	}

	private void init() {
		setStyle();
		setLoadMoreStatus(STATUS_MORE);
	}


	private void setStyle(){
		setGravity(Gravity.CENTER);
		setPadding(30,30,30,30);
		setTextSize(16);
	}


	public void loadMoreClick(final OnLoadMoreListener loadmoreListener){
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(initStatus==STATUS_LOADING||initStatus==STATUS_NO_DATA) {
					return;
				}
				setLoadMoreStatus(STATUS_LOADING);
				loadmoreListener.OnLoading();
			}
		});
	}

	public interface OnLoadMoreListener {
		void OnLoading();
	}

	public void setLoadMoreStatus(int Status) {
		initStatus = Status;
		switch (Status) {
		case STATUS_HIDE:
			this.setVisibility(View.GONE);
			break;
		case STATUS_MORE:
			setText(STATUS_MORE_TEXT);
			this.setVisibility(View.VISIBLE);
			break;
		case STATUS_LOADING:
			setText(STATUS_LOADING_TEXT);
			this.setVisibility(View.VISIBLE);
			break;
		case STATUS_ERROR:
			setText(STATUS_ERROR_TEXT);
			this.setVisibility(View.VISIBLE);
			break;
		case STATUS_NO_DATA:
			setText(STATUS_NO_DATA_TEXT);
			this.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	public int getLoadMoreStatus() {
		return initStatus;
	}
}
