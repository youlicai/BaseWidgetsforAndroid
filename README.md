# 安卓开发各种常用自定义控件整理

【XToast】
--
### 自定义Toast样式，使用方式：
```
XToast.show(this,"显示文字");
```
【MixDialog】
----
### 自定义Dialog，MixDialog有三个功能
###### 1.确认取消弹框，使用方式：
```
MixDialog.getMixDialog().showConfirmDialog(this,"标题","内容",new OnConfirmDialogListener(){
	@Override
    public void OnConfirm() {
        Log.e("=====","OnConfirm");
    }

    @Override
    public void OnCancel() {
        Log.e("=====","OnCancel");
    }
})
```
###### 2.加载等待框
###### 3.信息确认弹框


【XCamera】
-
【TopBar】
-
【SlidePager】
-
【Permission】
-
【Dimens】
-
【ColumnView】
-
【XTabLayout】
-
【RefreshListView】
-
【MixDialog】
-
【FastHttp】
-