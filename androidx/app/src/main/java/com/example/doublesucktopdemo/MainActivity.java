package com.example.doublesucktopdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.doublesucktopdemo.adapter.HomeViewPagerAdapter;
import com.example.doublesucktopdemo.adapter.MyBannerAdapter;
import com.example.doublesucktopdemo.adapter.RecommendAdapter;
import com.example.doublesucktopdemo.grid.MyAdapter2;
import com.example.doublesucktopdemo.grid.PagerConfig;
import com.example.doublesucktopdemo.grid.PagerGridLayoutManager;
import com.example.doublesucktopdemo.widget.RecyclerViewDivider;
import com.example.doublesucktopdemo.widget.RefreshHeaderView.TodayNewsHeader;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.android.material.appbar.AppBarLayout;
import com.leaf.library.StatusBarUtil;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by weioule
 * on 2019/6/26.
 */
public class MainActivity extends FragmentActivity implements PagerGridLayoutManager
        .PageListener {
    private SmartRefreshLayout refreshView;
    private AppBarLayout mAppBarLayout;
    private RelativeLayout mFloatSearchRl;

    ///非常重要
     private RelativeLayout mHeaderLl;
    private RelativeLayout mFotterLl;
    CardView mCardView;


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

        mBanner.addBannerLifecycleObserver(this)
                .setAdapter(new MyBannerAdapter(list))
                .setIndicator(new CircleIndicator(this))
                .setIndicatorGravity(IndicatorConfig.Direction.RIGHT)
                .start();

        mHeaderLl.addView(mBannerView);
    }

    private void addRecommendView() {
        RecyclerView mRecyclerView = new RecyclerView(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 6);
        mRecyclerView.setLayoutManager(gridLayoutManager);



        RecommendAdapter adapter = new RecommendAdapter(list);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ProductBean productBean = (ProductBean) adapter.getItem(position);
                Toast.makeText(MainActivity.this, productBean.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerView.setAdapter(adapter);
        RecyclerViewDivider divider = new RecyclerViewDivider.Builder(this)
                .setStyle(RecyclerViewDivider.Style.BOTH)
                .setColor(0x00000000)
                .setOrientation(RecyclerViewDivider.GRIDE_VIW)
                .setSize(5)
                .build();
        mRecyclerView.addItemDecoration(divider);






        mHeaderLl.addView(mRecyclerView);
    }

    private void addFeaturedView() {
        View view = getLayoutInflater().inflate(R.layout.featured_layout, mHeaderLl, false);
        mHeaderLl.addView(view);
    }

    private void initViewpager() {
        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
        } else {
            fragmentList.clear();
        }

        for (int i = 0; i < titles.length; i++) {
            fragmentList.add(new HomeFragment(list));
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
        for (int i = 0; i < 10; i++) {
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
}