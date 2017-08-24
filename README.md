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
```
MixDialog.getMixDialog().showWaitDialog(this, "加载中", new MixDialog.CancelDialogListener() {
    @Override
    public void OnCancel() {
		Log.e("=====","OnCancel");
    }
});
```

###### 3.信息确认弹框
```
MixDialog.getMixDialog().showTipsDialog("标题", "这是内容这是内容这是内容这是内容这是内容", "我知道了", this, new MixDialog.OnTipsDialogListener() {
    @Override
    public void OnTipsClick() {
        Log.e("=====","OnTipsClick");
    }
}
```


【XCamera】
-
### 简化获取图片方式：
###### 1.从相册获取图片
```
XCamera.takePicturePhoto(this);
```
###### 2.拍照获取图片
```
XCamera.takePictureCamera(this);
```
###### 2.截取图片
```
XCamera.cropImage(this);
```

###### 当前Activity onActivityResult处理
```
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == XCamera.REQUEST_CODE_PICK_IMAGE) {
            XCamera.cropPickImage(this,data.getData());
        } else if (requestCode == XCamera.REQUEST_CODE_CAPTURE) {
            XCamera.cropImage(this);
        }else if(requestCode==XCamera.REQUEST_CODE_CROP){
            image.setImageURI(XCamera.getUri());
        }
    }
```


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