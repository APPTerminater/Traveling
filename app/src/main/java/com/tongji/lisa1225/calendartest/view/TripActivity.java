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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tongji.lisa1225.calendartest.R;
import com.tongji.lisa1225.calendartest.adapter.TripAdapter;
import com.tongji.lisa1225.calendartest.controllor.RateController;
import com.tongji.lisa1225.calendartest.dao.DiaryInfoDao;
import com.tongji.lisa1225.calendartest.dao.TripInfoDao;
import com.tongji.lisa1225.calendartest.dao.UserInfoDao;
import com.tongji.lisa1225.calendartest.model.DiaryInfo;
import com.tongji.lisa1225.calendartest.model.TripInfo;

import java.util.ArrayList;
import java.util.List;

public class TripActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    List<TripInfo> tripInfoList;
    List<DiaryInfo> diaryInfoList;

    Toolbar topbar;
    //侧边栏部分
    private RelativeLayout layout;
    private LinearLayout infoLayout;
    private TextView nicknameTextview;
    private TextView birthdayTextview;
    private TextView walkTextview;
    private CheckBox modechange;
    //底边栏部分
    private Toolbar bottombar;
    private Button addBtn;
    private Button homepage;
    private Button searchBtn;
    //数据部分
    String nickname;
    Intent get_intent;
    TripInfoDao tDao;
    DiaryInfoDao dDao;
    UserInfoDao mDao;
    RateController rateController;

    int walk_daily;

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
        //初始化
        tDao=new TripInfoDao(TripActivity.this);
        dDao=new DiaryInfoDao(TripActivity.this);
        mDao=new UserInfoDao(TripActivity.this);

        layout=(RelativeLayout)findViewById(R.id.layout);
        infoLayout=(LinearLayout)findViewById(R.id.userinfo);
        nicknameTextview=(TextView)findViewById(R.id.nickname);
        birthdayTextview=(TextView)findViewById(R.id.birthday);
        walkTextview=(TextView)findViewById(R.id.walk_daily);
        modechange=(CheckBox)findViewById(R.id.changemode);
        addBtn=(Button)findViewById(R.id.addButton);
        homepage=(Button)findViewById(R.id.shouye);
        searchBtn=(Button)findViewById(R.id.searchButton);

        topbar=(Toolbar)findViewById(R.id.activity_toolbar);
        bottombar=(Toolbar)findViewById(R.id.bottom_tool_bar);

        walk_daily=mDao.alterWalk(nickname);
        initData();
        findView();
        initRefreshLayout();
        initRecyclerView();

        whichMode();//夜间模式
    }
    private void initData() {
        int num;//有多少天记录了
        tripInfoList = tDao.alterData(nickname);//最后传入的数据
        //评分
        diaryInfoList=dDao.alterData(nickname);
        TripInfo[] tripInfoArray=new TripInfo[tripInfoList.size()];
        tripInfoList.toArray(tripInfoArray);
        DiaryInfo[] diaryInfoArray=new DiaryInfo[diaryInfoList.size()];
        diaryInfoList.toArray(diaryInfoArray);
        for (int i=0;i<tripInfoArray.length;i++)
        {
            tripInfoArray[i].total_cost=0;
            tripInfoArray[i].total_walk=0;
            num=0;
            for(int j=0;j<diaryInfoArray.length;j++)
            {
                if(diaryInfoArray[j].time > (tripInfoArray[i].start_time-1000 * 60 * 60 * 24L)
                        && diaryInfoArray[j].time < tripInfoArray[i].end_time){
                    tripInfoArray[i].total_cost+=diaryInfoArray[j].cost;
                    tripInfoArray[i].total_walk+=diaryInfoArray[j].step;
                    num++;
                }
            }
            rateController=new RateController(tripInfoArray[i],walk_daily,num);
            tDao.updateRate(rateController.getRate());
        }

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
        adapter.setOnItemClickListener(new TripAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //tripInfoList.get(postion)
                Toast.makeText(TripActivity.this,"onItemClick : " + String.valueOf(position), Toast.LENGTH_SHORT).show();
                Intent commentIntent=new Intent(TripActivity.this,CommentActivity.class);
                commentIntent.putExtra("nickname",nickname);
                commentIntent.putExtra("position",position);
                startActivity(commentIntent);

            }
        });

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
    public void seemore(View view){
        infoLayout.setVisibility(View.VISIBLE);
        nicknameTextview.setText(nickname);
        birthdayTextview.setText(mDao.alterBirthday(nickname));
        walkTextview.setText(mDao.alterWalk(nickname)+"步");
    }
    public void seeless(View view){
        infoLayout.setVisibility(View.INVISIBLE);
    }
    public void toEditInfo(View view){
        Intent editinfoIntent = new Intent(TripActivity.this, EditInfoActivity.class);
        editinfoIntent.putExtra("nickname",nickname);
        //启动
        startActivity(editinfoIntent);
    }
    public void changeMode(View view){
        switch (mDao.alterMode(nickname)){
            case "day":
                mDao.updateMode(nickname,"night");
                whichMode();
                break;
            case "night":
                mDao.updateMode(nickname,"day");
                layout.setBackgroundColor(getResources().getColor(R.color.white));
                topbar.setBackgroundColor(getResources().getColor(R.color.tool_bar));
                infoLayout.setBackgroundColor(getResources().getColor(R.color.tool_bar));
                addBtn.setBackground(getResources().getDrawable(R.drawable.addst));
                homepage.setTextColor(getResources().getColor(R.color.tool_bar));
                searchBtn.setTextColor(getResources().getColor(R.color.danxiaqu2));
                bottombar.setBackgroundColor(getResources().getColor(R.color.zi));
                break;
        }

    }
    //按钮点击事件结束
    public void whichMode(){
        if(mDao.alterMode(nickname).equals("night")) {
            layout.setBackground(getResources().getDrawable(R.drawable.bg_xk));
            topbar.setBackgroundColor(getResources().getColor(R.color.night_toolbar));
            infoLayout.setBackgroundColor(getResources().getColor(R.color.night_toolbar));
            addBtn.setBackground(getResources().getDrawable(R.drawable.addst1));
            homepage.setTextColor(getResources().getColor(R.color.night_toolbar));
            searchBtn.setTextColor(getResources().getColor(R.color.night_danxiaqu));
            bottombar.setBackgroundColor(getResources().getColor(R.color.night_buttombar));
            modechange.setChecked(true);
        }
    }

}
