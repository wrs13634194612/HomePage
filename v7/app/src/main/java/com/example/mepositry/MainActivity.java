package com.example.mepositry;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.example.mepositry.adapter.HomeViewPagerAdapter;
import com.example.mepositry.adapter.MyBannerAdapter;
import com.example.mepositry.adapter.MyLoader;
import com.example.mepositry.grid.MyAdapter2;
import com.example.mepositry.grid.PagerConfig;
import com.example.mepositry.widget.RecyclerViewDivider;
import com.example.mepositry.widget.refreshheaderview.TodayNewsHeader;

import java.util.ArrayList;
import java.util.List;
import com.example.mepositry.widget.refreshheaderview.PagerGridLayoutManager;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;


/**
 * Created by weioule
 * on 2019/6/26.
 */
public class MainActivity extends FragmentActivity implements PagerGridLayoutManager
        .PageListener , OnBannerListener {
    private SmartRefreshLayout refreshView;
    private AppBarLayout mAppBarLayout;
    private RelativeLayout mFloatSearchRl;


    ///非常重要
    private RelativeLayout mHeaderLl;
    private RelativeLayout mFotterLl;
    CardView mCardView;
    CoordinatorLayout mCoordinatorLayout;
    AppBarLayout mAsppBarLayout;
    //AppBarLayoutBehavior mAppBarLayoutBehavior;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    Toolbar mToolbar;
    ViewPager mViewPasger;




    private SlidingTabLayout mTabView;
    private ViewPager mViewPager;

    private int totalScrollRange, oldVerticalOffset = -1;
    private HomeViewPagerAdapter fragmentAdapter;
    private String[] titles = {"首页", "手机", "食品", "生鲜", "休闲零食", "家居厨具", "美妆"};
    private ArrayList<ProductBean> list = new ArrayList<>();
    private List<Fragment> fragmentList;
    private int mRows = 1;  //设置行数
    private int mColumns = 4;  //设置列数
    private RecyclerView mRecyclerView;
    private MyAdapter2 mAdapter;
    private PagerGridLayoutManager mLayoutManager;
    private RelativeLayout lineParent;
    private int mTotal = 0;
    private int mCurrent = 0;
    private View lineChild;
    private ImageView iv_icon_bottom;
    private RelativeLayout rl_cancel_bottom;
    private ArrayList<String> urls;


    private String[] names = {"多多果园","九块九特卖","多多爱消除","天天领现金"
            ,"行家帮你选","限时秒杀","断码清仓","跟着好评买"
            ,"充值中心","医药馆","签到","多多赚大钱"
            ,"砍价免费拿","多多精灵","省钱月卡","现金大转盘"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        addListener();
    }

    private void initView() {
        // StatusBarUtil.setColor(this, getColor(R.color.theme_color));
        refreshView = findViewById(R.id.refresh_view);
        mFloatSearchRl = findViewById(R.id.rl_float_search);
        mAppBarLayout = findViewById(R.id.app_bar);
        mTabView = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        mHeaderLl = findViewById(R.id.ll_header);
        mFotterLl = findViewById(R.id.ll_fotter);
        refreshView.setRefreshHeader(new TodayNewsHeader(this));
        addProductBeans();
        getData();
    }


    private void addListener() {
        mAppBarLayout.addOnOffsetChangedListener(offsetChangedListener);
        refreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });
    }

    private void loadData() {
        refreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshView.finishRefresh();
                refreshView.setEnabled(true);
            }
        }, 800);
    }

    // 这里的处理是当AppBarLayout处于最顶部也就是完全打开状态verticalOffse=0时才允许刷新控件可用.
    private AppBarLayout.OnOffsetChangedListener offsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffse) {
            totalScrollRange = appBarLayout.getTotalScrollRange();

            if (oldVerticalOffset == verticalOffse) return;

            if (verticalOffse == 0) {
                refreshView.setEnabled(true);
            } else {
                refreshView.setEnabled(false);
            }

            if (verticalOffse <= -Utils.dp2px(40)) {
                mFloatSearchRl.setVisibility(View.VISIBLE);
            } else {
                mFloatSearchRl.setVisibility(View.GONE);
            }

            if (Math.abs(verticalOffse) >= totalScrollRange && totalScrollRange != 0) {
                mTabView.setBackgroundColor(getResources().getColor(R.color.white_color));
            } else {
                mTabView.setBackgroundColor(getResources().getColor(R.color.background_color));
            }

            oldVerticalOffset = verticalOffse;
        }
    };

    public void getData() {
        mHeaderLl.removeAllViews();
        mFotterLl.removeAllViews();
        addTopView();
        addBannerView();

        addBannerTab();

        addBottom();


        //  addRecommendView();
        //  addFeaturedView();  图片
        initViewpager();
    }

    private void addBannerTab() {
        View mTopView = getLayoutInflater().inflate(R.layout.activity_two, mHeaderLl, false);
        lineParent = mTopView.findViewById(R.id.rl_line_parent);
        lineChild = mTopView.findViewById(R.id.view_line_child);
        mLayoutManager = new PagerGridLayoutManager(mRows, mColumns, PagerGridLayoutManager
                .HORIZONTAL);
        // 系统带的 RecyclerView，无需自定义
        mRecyclerView =mTopView.findViewById(R.id.recycler_view);
        // 水平分页布局管理器
        mLayoutManager.setPageListener(this);    // 设置页面变化监听器
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 如果需要查看调试日志可以设置为true，一般情况忽略即可
        PagerConfig.setShowLog(true);
        initData();

        mHeaderLl.addView(mTopView);
    }

    private void initData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            list.add(names[i]);
        }
        //   mAdapter.refreshDataList(list);
        // 使用原生的 Adapter 即可
        mAdapter = new MyAdapter2(MainActivity.this, list);
        mRecyclerView.setAdapter(mAdapter);
    }


    private void addTopView() {
        View mTopView = getLayoutInflater().inflate(R.layout.top_view_layout, mHeaderLl, false);


        mHeaderLl.addView(mTopView);
    }

    private void addBottom() {
        View mTopView = getLayoutInflater().inflate(R.layout.bottom_layout, mFotterLl, false);
        iv_icon_bottom = mTopView.findViewById(R.id.iv_icon_bottom);
        rl_cancel_bottom= mTopView.findViewById(R.id.rl_cancel_bottom);
        iv_icon_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TAG","click iv_icon_bottom ");
            }
        });
        rl_cancel_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TAG","click cancel");
            }
        });


        mFotterLl.addView(mTopView);
    }

    private void addBannerView() {
        View mBannerView = getLayoutInflater().inflate(R.layout.banner_layout, mHeaderLl, false);
        Banner mBanner = mBannerView.findViewById(R.id.banner);

        ArrayList<ProductBean> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(new ProductBean("三会一课","2","会议",R.mipmap.ic_launcher));
        }

      /*  mBanner.addBannerLifecycleObserver(this)
                .setAdapter(new MyBannerAdapter(list))
                .setIndicator(new CircleIndicator(this))
                .setIndicatorGravity(IndicatorConfig.Direction.RIGHT)
                .start();*/

        urls = new ArrayList<>();
        urls.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1382278421,846715889&fm=26&gp=0.jpg");
        urls.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=243326707,792860696&fm=26&gp=0.jpg");
        urls.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2508242520,2274367858&fm=26&gp=0.jpg");



        playBanner(mBanner,urls);


        mHeaderLl.addView(mBannerView);
    }

    private void playBanner(Banner banner, ArrayList<String> urls) {
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        // banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        banner.setImages(urls);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        // banner.setBannerTitles(list_title);

        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.LEFT)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(this)
                //必须最后调用的方法，启动轮播图。
                .start();


    }

    private void addRecommendView() {

    }

    private void addFeaturedView() {

    }

    private void initViewpager() {
        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
        } else {
            fragmentList.clear();
        }

        for (int i = 0; i < titles.length; i++) {
            //这个地方传递值给fragment
            fragmentList.add(ZnzbFragment.newInstance(list));

        }

        fragmentAdapter = new HomeViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(fragmentAdapter);
        mTabView.setOnTabSelectListener(onTabSelectListener);
        mViewPager.addOnPageChangeListener(onPageChangeListener);

        mTabView.setViewPager(mViewPager, titles);
    }

    private OnTabSelectListener onTabSelectListener = new OnTabSelectListener() {
        @Override
        public void onTabSelect(int position) {
            mTabView.setCurrentTab(position);
        }

        @Override
        public void onTabReselect(int position) {
        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mTabView.setCurrentTab(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    /**
     * 控制appbar的滑动
     *
     * @param isScroll true 允许滑动   false 禁止滑动
     */
    private void banAppBarScroll(boolean isScroll) {
        View mAppBarChildAt = mAppBarLayout.getChildAt(0);
        AppBarLayout.LayoutParams mAppBarParams = (AppBarLayout.LayoutParams) mAppBarChildAt.getLayoutParams();
        if (isScroll && mAppBarParams.getScrollFlags() == 0) {
            mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
            mAppBarChildAt.setLayoutParams(mAppBarParams);
        } else if (mAppBarParams.getScrollFlags() != 0)
            mAppBarParams.setScrollFlags(0);
    }

    /**
     * 控制appbar的开关
     * 需要手动开关时调用
     *
     * @param open true 打开   false 关闭
     */
    public void switchAppbarLayout(boolean open) {
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams()).getBehavior();
        if (behavior instanceof AppBarLayout.Behavior) {
            AppBarLayout.Behavior appBarLayoutBehavior = (AppBarLayout.Behavior) behavior;
            int topAndBottomOffset = appBarLayoutBehavior.getTopAndBottomOffset();
            if (open && topAndBottomOffset != 0) {
                //打开
                appBarLayoutBehavior.setTopAndBottomOffset(0);
            } else if (!open) {
                //关闭
                if (totalScrollRange == 0) totalScrollRange = mAppBarLayout.getHeight();
                appBarLayoutBehavior.setTopAndBottomOffset(-totalScrollRange);
            }
        }
    }


    private void addProductBeans() {
        for (int i = 0; i < 20; i++) {
            list.add(new ProductBean("智慧美食","2","美食",R.mipmap.ic_launcher));
        }
    }

    @Override
    public void onPageSizeChanged(int pageSize) {
        mTotal = pageSize;
        Log.e("TAG", "总页数 = " + pageSize);
    }

    @Override
    public void onPageSelect(int pageIndex, int pageSize) {
        mCurrent = pageIndex;
        Log.e("TAG", "选中页码 = " + pageIndex + "\t" + pageSize);
        //计算滚动条宽度
        float proportion = (float) ((pageIndex + 1) / pageSize);
        float transMaxRange = lineParent.getWidth() - lineChild.getWidth();
        //设置滚动条移动
        lineChild.setTranslationX(transMaxRange * proportion);
    }

    @Override
    public void OnBannerClick(int position) {
        Log.e("TAG", "banner = " + position );
    }
}