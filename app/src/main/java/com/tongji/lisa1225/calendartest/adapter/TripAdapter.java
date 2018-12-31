package com.tongji.lisa1225.calendartest.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tongji.lisa1225.calendartest.R;
import com.tongji.lisa1225.calendartest.model.TripInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TripInfo> datas;
    private Context context;
    private int normalType = 0;
    private int footType = 1;
    private boolean hasMore = true;
    private boolean fadeTips = false;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    public TripAdapter(List<TripInfo> datas, Context context, boolean hasMore) {
        this.datas = datas;
        this.context = context;
        this.hasMore = hasMore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == normalType) {
            return new TripAdapter.NormalHolder(LayoutInflater.from(context).inflate(R.layout.trip_item, null));
        } else {
            return new TripAdapter.FootHolder(LayoutInflater.from(context).inflate(R.layout.footview, null));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TripAdapter.NormalHolder) {//todo

            ((TripAdapter.NormalHolder) holder).destination.setText(datas.get(position).destination);
            ((NormalHolder) holder).duration.setText(getDateToString(datas.get(position).start_time)+"-"+getDateToString(datas.get(position).end_time));
            ((NormalHolder) holder).briefInfo.setText(datas.get(position).brief_info);

        } else {
            ((TripAdapter.FootHolder) holder).tips.setVisibility(View.VISIBLE);
            if (hasMore == true) {
                fadeTips = false;
                if (datas.size() > 0) {
                    ((TripAdapter.FootHolder) holder).tips.setText("正在加载更多...");
                }
            } else {
                if (datas.size() > 0) {
                    ((TripAdapter.FootHolder) holder).tips.setText("没有更多数据了");
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((TripAdapter.FootHolder) holder).tips.setVisibility(View.GONE);
                            fadeTips = true;
                            hasMore = true;
                        }
                    }, 500);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return datas.size() + 1;
    }

    public int getRealLastPosition() {
        return datas.size();
    }


    public void updateList(List<TripInfo> newDatas, boolean hasMore) {
        if (newDatas != null) {
            datas.addAll(newDatas);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }

    class NormalHolder extends RecyclerView.ViewHolder {//todo
        private TextView textView;
        private TextView destination;
        private TextView duration;
        private TextView briefInfo;


        public NormalHolder(View itemView) {
            super(itemView);
            destination=(TextView)itemView.findViewById(R.id.destination);
            duration=(TextView)itemView.findViewById(R.id.duration);
            briefInfo=(TextView)itemView.findViewById(R.id.brief_info);

            //textView = (TextView) itemView.findViewById(R.id.tv);
        }
    }

    class FootHolder extends RecyclerView.ViewHolder {
        private TextView tips;

        public FootHolder(View itemView) {
            super(itemView);
            tips = (TextView) itemView.findViewById(R.id.tips);
        }
    }

    public boolean isFadeTips() {
        return fadeTips;
    }

    public void resetDatas() {
        datas = new ArrayList<>();
    }
    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return footType;
        } else {
            return normalType;
        }
    }
}
