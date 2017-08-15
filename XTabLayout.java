package cn.haodian.demowidget.tblayout;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


/**
 * 底部tab
 */
public class XTabLayout extends LinearLayout {

    private TabLayout tb;
    private ViewPager vp;
    private ContentPagerAdapter contentAdapter;

    private List<Fragment> tabFragments=new ArrayList<>();
    private String[] tab_strings;
    private int tab_string_color=Color.BLACK;
    private int tab_string_select_color=Color.GREEN;

    private int[] tab_img_redIds;
    private int[] tab_img_select_redIds;

    private final int[] tab_img_size=new int[]{80,80};//wh
    private final int[] tab_text_size=new int[]{80,LayoutParams.WRAP_CONTENT};//wh
    private final int[] tab_size=new int[]{LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT};//wh

    private int tab_bg_color=Color.WHITE;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public XTabLayout(Context context, AttributeSet attrs){
        super(context,attrs);

    }


    /*=============================添加底部tab start===============================*/
    /**
     * 对外调用方法
     * @param fs fragments
     * @param resIds 默认图片
     * @param select_resIds  选中是图片
     * @param strings tab 文字
     * @param fa  当前context
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setBottomTab(List<Fragment> fs, int[] resIds, int[] select_resIds,String[] strings, FragmentActivity fa){
        if((fs.size()==resIds.length)&&(resIds.length==strings.length)){
            initBottomTab(fa);
            tabFragments.addAll(fs);
            tab_strings=strings;
            tab_img_redIds=resIds;
            tab_img_select_redIds=select_resIds;
            contentAdapter = new ContentPagerAdapter(fa.getSupportFragmentManager());
            vp.setAdapter(contentAdapter);
            tb.setupWithViewPager(vp);
            addBottomTab(resIds,strings,fa);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void  initBottomTab(Context context){
        setOrientation(LinearLayout.VERTICAL);

        vp=new ViewPager(context);
        vp.setId(View.generateViewId());
        addView(vp);
        vp.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));

        tb=new TabLayout(context);
        tb.setLayoutParams(new LayoutParams(tab_size[0],tab_size[1]));
        tb.setBackgroundColor(tab_bg_color);
        tb.setSelectedTabIndicatorHeight(0);

        addView(tb);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void addBottomTab(int[] resIds,String[] strings,Context context){
        for(int i=0;i<resIds.length;i++) {
            LinearLayout tab_ = new LinearLayout(context);
            tab_.setOrientation(LinearLayout.VERTICAL);
            tab_.setGravity(Gravity.CENTER);

            ImageView tab_image = new ImageView(context);
            tab_image.setImageResource(resIds[i]);
            tab_image.setLayoutParams(new LayoutParams(tab_img_size[0],tab_img_size[1]));

            TextView tab_text = new TextView(context);
            tab_text.setLayoutParams(new LayoutParams(tab_text_size[0],tab_text_size[1]));
            tab_text.setText(strings[i]);
            tab_text.setTextSize(12);

            tab_.addView(tab_image);
            tab_.addView(tab_text);

            TabLayout.Tab tab = tb.getTabAt(i);
            tab.setCustomView(tab_);
            if(i==0)
                setFirstTab(tab);
        }

        tb.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateTab(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                restoreTab(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    /*=============================添加底部tab  end===============================*/


    /*=============================添加顶部tab  start===============================*/

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setTopTab(List<Fragment> fs,String[] strings, FragmentActivity fa) {
        if (fs.size() == strings.length) {

            initTopTab(fa);
            tabFragments.addAll(fs);
            tab_strings = strings;
            contentAdapter = new ContentPagerAdapter(fa.getSupportFragmentManager());
            vp.setAdapter(contentAdapter);
            tb.setupWithViewPager(vp);
            addTopTab(strings, fa);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void  initTopTab(Context context){
        setOrientation(LinearLayout.VERTICAL);

        tb=new TabLayout(context);
        tb.setLayoutParams(new LayoutParams(tab_size[0],tab_size[1]));
        tb.setTabMode(TabLayout.MODE_SCROLLABLE);
//        tb.setSelectedTabIndicatorHeight(0);
        tb.setBackgroundColor(tab_bg_color);
        addView(tb);

        vp=new ViewPager(context);
        vp.setId(View.generateViewId());
        addView(vp);
        vp.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void addTopTab(String[] strings,Context context){

        for(int i=0;i<strings.length;i++) {
            TextView tab_text = new TextView(context);
            tab_text.setLayoutParams(new LayoutParams(tab_text_size[0],tab_text_size[1]));
            tab_text.setText(strings[i]);
            tab_text.setTextSize(12);

            TabLayout.Tab tab = tb.getTabAt(i);
            tab.setCustomView(tab_text);
            if(i==0)
                setFirstTopTab(tab);
        }

        tb.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateTopTab(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                restoreTopTab(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /*=============================添加顶部tab  end===============================*/

    class ContentPagerAdapter extends FragmentPagerAdapter {
        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.get(position);
        }

        @Override
        public int getCount() {
            return tabFragments.size();
        }

    }

    /**
     * 更新tab为选中状态
     * @param tab
     */
    private void updateTab(TabLayout.Tab tab){
        ViewGroup view= (ViewGroup) tab.getCustomView();
        ImageView iv= (ImageView) view.getChildAt(0);
        TextView tv= (TextView) view.getChildAt(1);
        for (int i=0;i<tab_strings.length;i++){
            if(tb.getTabAt(i)==tab){
                tv.setTextColor(tab_string_select_color);
                iv.setImageResource(tab_img_select_redIds[i]);
                return;
            }
        }
    }

    /**
     * 恢复tab状态
     * @param tab
     */
    private void restoreTab(TabLayout.Tab tab){
        ViewGroup view= (ViewGroup) tab.getCustomView();
        ImageView iv= (ImageView) view.getChildAt(0);
        TextView tv= (TextView) view.getChildAt(1);
        for (int i=0;i<tab_strings.length;i++){
            if(tb.getTabAt(i)==tab){
                tv.setTextColor(tab_string_color);
                iv.setImageResource(tab_img_redIds[i]);
                return;
            }
        }
    }

    /**
     * 初始化第一个tab
     * @param tab
     */
    private void setFirstTab(TabLayout.Tab tab){
        ViewGroup view= (ViewGroup) tab.getCustomView();
        ImageView iv= (ImageView) view.getChildAt(0);
        TextView tv= (TextView) view.getChildAt(1);

        tv.setTextColor(tab_string_select_color);
        iv.setImageResource(tab_img_select_redIds[0]);
    }

/*==============================================================================*/


    /**
     * 更新tab为选中状态
     * @param tab
     */
    private void updateTopTab(TabLayout.Tab tab){
        TextView tv= (TextView) tab.getCustomView();
        for (int i=0;i<tab_strings.length;i++){
            if(tb.getTabAt(i)==tab){
                tv.setTextColor(tab_string_select_color);
                return;
            }
        }
    }

    /**
     * 恢复tab状态
     * @param tab
     */
    private void restoreTopTab(TabLayout.Tab tab){
        TextView tv= (TextView) tab.getCustomView();
        for (int i=0;i<tab_strings.length;i++){
            if(tb.getTabAt(i)==tab){
                tv.setTextColor(tab_string_color);
                return;
            }
        }
    }


    /**
     * 初始化第一个tab
     * @param tab
     */
    private void setFirstTopTab(TabLayout.Tab tab){
        TextView tv= (TextView) tab.getCustomView();
        tv.setTextColor(tab_string_select_color);

    }
}
