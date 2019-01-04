package com.tongji.lisa1225.calendartest.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tongji.lisa1225.calendartest.R;
import com.tongji.lisa1225.calendartest.model.DiaryInfo;

import java.util.ArrayList;
import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<DiaryInfo> datas;
    private Context context;
    private int normalType = 0;
    private int footType = 1;
    private boolean hasMore = true;
    private boolean fadeTips = false;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public DiaryAdapter(List<DiaryInfo> datas, Context context, boolean hasMore) {
        this.datas = datas;
        this.context = context;
        this.hasMore = hasMore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == normalType) {
            return new DiaryAdapter.NormalHolder(LayoutInflater.from(context).inflate(R.layout.item, null));
        } else {
            return new DiaryAdapter.FootHolder(LayoutInflater.from(context).inflate(R.layout.footview, null));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof DiaryAdapter.NormalHolder) {
            ((NormalHolder) holder).title.setText(datas.get(position).title);
            ((NormalHolder) holder).destination.setText(datas.get(position).destination);
            ((NormalHolder) holder).temperature.setText(String.valueOf(datas.get(position).temperature)+"℃");
            ((NormalHolder) holder).text.setText(datas.get(position).text);
            ((NormalHolder) holder).step.setText(String.valueOf(datas.get(position).step)+"步");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //触发自定义监听的单击事件
                    onItemClickListener.onItemClick(holder.itemView,position);
                }
            });

        } else {
            ((DiaryAdapter.FootHolder) holder).tips.setVisibility(View.VISIBLE);
            if (hasMore == true) {
                fadeTips = false;
                if (datas.size() > 0) {
                    ((DiaryAdapter.FootHolder) holder).tips.setText("正在加载更多...");
                }
            } else {
                if (datas.size() > 0) {
                    ((DiaryAdapter.FootHolder) holder).tips.setText("没有更多数据了");
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((DiaryAdapter.FootHolder) holder).tips.setVisibility(View.GONE);
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


    public void updateList(List<DiaryInfo> newDatas, boolean hasMore) {
        if (newDatas != null) {
            datas.addAll(newDatas);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }

    class NormalHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView destination;
        private TextView temperature;
        private TextView text;
        private TextView step;

        public NormalHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.title);
            destination=(TextView)itemView.findViewById(R.id.destination);
            temperature=(TextView)itemView.findViewById(R.id.temperature);
            text=(TextView)itemView.findViewById(R.id.text);
            step=(TextView)itemView.findViewById(R.id.step);
        }
    }

    public void setOnItemClickListener(DiaryAdapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    private DiaryAdapter.OnItemClickListener onItemClickListener;

    /**
     * 自定义监听回调，RecyclerView 的 单击事件
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
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

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return footType;
        } else {
            return normalType;
        }
    }
}
