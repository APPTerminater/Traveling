package com.tongji.lisa1225.calendartest.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tongji.lisa1225.calendartest.R;
import com.tongji.lisa1225.calendartest.adapter.DiaryAdapter;
import com.tongji.lisa1225.calendartest.adapter.TripAdapter;
import com.tongji.lisa1225.calendartest.dao.DiaryInfoDao;
import com.tongji.lisa1225.calendartest.dao.TripInfoDao;
import com.tongji.lisa1225.calendartest.model.DiaryInfo;
import com.tongji.lisa1225.calendartest.model.TripInfo;

import java.util.ArrayList;
import java.util.List;

public class TripActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    List<TripInfo> tripInfoList;

    String nickname;
    Intent get_intent;
    TripInfoDao tDao;

    private int lastVisibleItem = 0;
    private final int PAGE_COUNT = 10;
    private GridLayoutManager mLayoutManager;
    private TripAdapter adapter;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        get_intent = getIntent();//传来的昵称
        nickname=get_intent.getStringExtra("nickname");
        tDao=new TripInfoDao(TripActivity.this);

        initData();
        findView();
        initRefreshLayout();
        initRecyclerView();
    }
    private void initData() {
        tripInfoList = tDao.alterData(nickname);
        //[] diaryInfoArray = new DiaryInfo[diaryInfoList.size()];
        //diaryInfoList.toArray(diaryInfoArray);
    }
    private void findView() {
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

    }
    private void initRefreshLayout() {
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        refreshLayout.setOnRefreshListener(this);
    }
    private void initRecyclerView() {
        adapter = new TripAdapter(getDatas(0, PAGE_COUNT), this, getDatas(0, PAGE_COUNT).size() > 0 );
        mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!adapter.isFadeTips() && lastVisibleItem + 1 == adapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateRecyclerView(adapter.getRealLastPosition(), adapter.getRealLastPosition() + PAGE_COUNT);
                            }
                        }, 500);
                    }

                    if (adapter.isFadeTips() && lastVisibleItem + 2 == adapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateRecyclerView(adapter.getRealLastPosition(), adapter.getRealLastPosition() + PAGE_COUNT);
                            }
                        }, 500);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private List<TripInfo> getDatas(final int firstIndex, final int lastIndex) {
        List<TripInfo> resList = new ArrayList<>();
        for (int i = firstIndex; i < lastIndex; i++) {
            if (i < tripInfoList.size()) {
                resList.add(tripInfoList.get(i));
            }
        }
        return resList;
    }

    private void updateRecyclerView(int fromIndex, int toIndex) {
        List<TripInfo> newDatas = getDatas(fromIndex, toIndex);
        if (newDatas.size() > 0) {
            adapter.updateList(newDatas, true);
        } else {
            adapter.updateList(null, false);
        }
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        adapter.resetDatas();
        updateRecyclerView(0, PAGE_COUNT);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        }, 1000);
    }


    //几个按钮点击事件
    public void tomain(View view) {
        Intent mainintent =new Intent(TripActivity.this,MainActivity.class);
        mainintent.putExtra("nickname",nickname);
        startActivity(mainintent);
    }
    public void toGaode(View view){
        Intent gaodeIntent =new Intent(TripActivity.this,MapActivity.class);
        gaodeIntent.putExtra("nickname",nickname);
        startActivity(gaodeIntent);
    }
    public void toAdd(View view){
        Intent addIntent =new Intent(TripActivity.this,AddActivity.class);
        addIntent.putExtra("nickname",nickname);
        startActivity(addIntent);
    }
    public void toDiary(View view){
        Intent diaryIntent =new Intent(TripActivity.this,DiaryActivity.class);
        diaryIntent.putExtra("nickname",nickname);
        startActivity(diaryIntent);
    }
    public void tocomment(View view){
        Intent commentIntent=new Intent(TripActivity.this,CommentActivity.class);
        commentIntent.putExtra("nickname",nickname);
        startActivity(commentIntent);

    }
    //按钮点击事件结束
}
