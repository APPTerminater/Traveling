package com.tongji.lisa1225.calendartest.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tongji.lisa1225.calendartest.adapter.DiaryAdapter;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tongji.lisa1225.calendartest.R;
import com.tongji.lisa1225.calendartest.dao.DiaryInfoDao;
import com.tongji.lisa1225.calendartest.model.DiaryInfo;
import com.tongji.lisa1225.calendartest.model.TripInfo;

public class DiaryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    List<DiaryInfo> diaryInfoList;

    String nickname;
    Intent get_intent;
    DiaryInfoDao dDao;

    private int lastVisibleItem = 0;
    private final int PAGE_COUNT = 10;
    private GridLayoutManager mLayoutManager;
    private DiaryAdapter adapter;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        get_intent = getIntent();//传来的昵称
        nickname=get_intent.getStringExtra("nickname");
        dDao=new DiaryInfoDao(DiaryActivity.this);

        initData();
        findView();
        initRefreshLayout();
        initRecyclerView();
    }

    private void initData() {
        diaryInfoList=dDao.alterData(nickname);
        DiaryInfo[] diaryInfoArray = new DiaryInfo[diaryInfoList.size()];
        diaryInfoList.toArray(diaryInfoArray);
        /*
        titleList = new ArrayList<>();
        for (int i = 0; i < diaryInfoList.size(); i++) {
            titleList.add(diaryInfoArray[i].title);
        }*/
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
        adapter = new DiaryAdapter(getDatas(0, PAGE_COUNT), this, getDatas(0, PAGE_COUNT).size() > 0 );
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

    private List<DiaryInfo> getDatas(final int firstIndex, final int lastIndex) {
        List<DiaryInfo> resList = new ArrayList<>();
        for (int i = firstIndex; i < lastIndex; i++) {
            if (i < diaryInfoList.size()) {
                resList.add(diaryInfoList.get(i));
            }
        }
        return resList;
    }

    private void updateRecyclerView(int fromIndex, int toIndex) {
        List<DiaryInfo> newDatas = getDatas(fromIndex, toIndex);
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
        Intent mainintent =new Intent(DiaryActivity.this,MainActivity.class);
        mainintent.putExtra("nickname",nickname);
        startActivity(mainintent);
    }
    public void toGaode(View view){
        Intent gaodeIntent =new Intent(DiaryActivity.this,MapActivity.class);
        gaodeIntent.putExtra("nickname",nickname);
        startActivity(gaodeIntent);
    }
    public void toAdd(View view){
        Intent addIntent =new Intent(DiaryActivity.this,AddActivity.class);
        addIntent.putExtra("nickname",nickname);
        startActivity(addIntent);
    }
    public void toTrip(View view){
        Intent tripIntent =new Intent(DiaryActivity.this,TripActivity.class);
        tripIntent.putExtra("nickname",nickname);
        startActivity(tripIntent);
    }
    //按钮点击事件结束
}
