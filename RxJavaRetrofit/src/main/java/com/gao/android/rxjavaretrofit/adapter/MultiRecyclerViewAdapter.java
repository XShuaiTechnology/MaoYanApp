package com.gao.android.rxjavaretrofit.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gao.android.rxjavaretrofit.R;
import com.gao.android.rxjavaretrofit.model.USListBean;
import com.gao.android.rxjavaretrofit.model.WaitExpctBean;
import com.gao.android.rxjavaretrofit.model.WaitListBean;
import com.gao.android.util.ListUtils;
import com.orhanobut.logger.Logger;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by GaoMatrix on 2016/10/22.
 */
public class MultiRecyclerViewAdapter extends RecyclerView.Adapter
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
    //预告片
    public static List<USListBean.DataBean.ComingBean> mRecomDataList = new ArrayList<>();

    //近期最受期待
    public static List<WaitExpctBean.DataBean.MoviesBean> mExpectDataList = new ArrayList<>();

    //下部分
    public static List<WaitListBean.DataBean.ComingBean> mListDataList = new ArrayList<>();
    private RecomAdapter recomAdapter;

    public MultiRecyclerViewAdapter(List<USListBean.DataBean.ComingBean> recomDataList,
                                    List<WaitExpctBean.DataBean.MoviesBean> expectDataList,
                                    List<WaitListBean.DataBean.ComingBean> listDataList) {
        mRecomDataList = recomDataList;
        mExpectDataList = expectDataList;
        mListDataList = listDataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multi_recyclerview_search, parent, false);
            return new SearchViewHolder(view);
        } else if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multi_recyclerview_recommand, parent, false);
            return new RecommandViewHolder(view);
        } else if (viewType == 2) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multi_recyclerview_except, parent, false);
            return new ExpectViewHolder(view);
        } else if (viewType == 3) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multi_recyclerview_list, parent, false);
            return new ListViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 3;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0 || position == 1 || position == 2) {
            return;
        }

        if (holder instanceof ListViewHolder) {
            if (mListDataList != null && mListDataList.size() > 0) {
                // position-3 因为前面有三个item。
                WaitListBean.DataBean.ComingBean itemData = mListDataList.get(position - 3);
                Logger.d(itemData);
                ((ListViewHolder) holder).tv_nm.setText(itemData.getNm());
                ((ListViewHolder) holder).tv_scm.setText(itemData.getScm());
                ((ListViewHolder) holder).tv_desc.setText(itemData.getDesc());
                ((ListViewHolder) holder).tv_wish.setText(itemData.getWish() + "");

                // 这个是每个Item下面还有一个RecyclerView的情况，现在返回数据没有这个，不会走到
                if (itemData.getHeadLinesVO() != null & itemData.getHeadLinesVO().size() > 0) {
                    ((ListViewHolder) holder).recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL, false));
                    ((ListViewHolder) holder).recyclerView.setAdapter(new WaitSpecialAdapter(holder.itemView.getContext(), itemData.getHeadLinesVO()));
                }


               /* //是否显示标题
                String rt = itemData.getRt();
                for (int i = 0; i < comingData.indexOf(itemData); i++) {
                    if (rt.equals(comingData.get(i).getRt())) {
                        ((ContentHolder) holder).tv_data.setVisibility(View.GONE);
                        break;
                    }
                    ((ContentHolder) holder).tv_data.setVisibility(View.VISIBLE);
                }
                if (comingData.indexOf(itemData) == 0) {
                    ((ContentHolder) holder).tv_data.setVisibility(View.VISIBLE);
                }
                //显示日期
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("MM月dd日 E");
                Date date = null;//提取格式中的日期
                try {
                    date = sdf1.parse(rt);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                rt = sdf2.format(date);

                ((ContentHolder) holder).tv_data.setText(rt);*/


                String imaUrl = itemData.getImg();
                imaUrl = imaUrl.replace("w.h", "165.220");
                Glide.with(holder.itemView.getContext()).load(imaUrl).into(((ListViewHolder) holder).iv_img);
            }

        }
    }

    @Override
    public long getHeaderId(int position) {
        if(mListDataList!=null&&mListDataList.size()>0) {
//            if (position == 0) {
//                return -1;
//            }else if(position==1) {
//                return -1;
//            }else if(position==2) {
//                return -1;
//            }
            if (position >= 3) {
                return parseDate(mListDataList.get(position - 3).getRt());
            }
            return -1;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_header, parent, false);
        return new HeaderViewHolder(view);
    }

    private String testDate;
    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getHeaderId(position) == parseDate(mListDataList.get(position - 3).getRt())) {
            commonTitle.setText(testDate);//
        } else if (getHeaderId(position) == -1) {// 不是底部的Adapter就不显示
            commonTitle.setVisibility(View.GONE);
        }
//        else if(getHeaderId(position)==-2) {
//            commonTitle.setVisibility(View.VISIBLE);
//            commonTitle.setText("预告片推荐11");
//        }else if(getHeaderId(position)==-3) {
//            commonTitle.setVisibility(View.VISIBLE);
//            commonTitle.setText("近期最受期待22");
//        }
    }

    private TextView commonTitle;
    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
            commonTitle = (TextView) view;
        }
    }

    private int parseDate(String strDate) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM月dd日 E");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMMdd");
        Date date = null;//提取格式中的日期
        try {
            date = sdf1.parse(strDate);
            String strDate1 = sdf3.format(date);
            int intDate = Integer.parseInt(strDate1);
            testDate = sdf2.format(date);
            return intDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        if (!ListUtils.isEmpty(mListDataList)) {
            return mListDataList.size() + 3;
        }
        return 0;
    }

    public void setData(List<USListBean.DataBean.ComingBean> recomDataList,
                        List<WaitExpctBean.DataBean.MoviesBean> expectDataList,
                        List<WaitListBean.DataBean.ComingBean> listDataList) {
        mRecomDataList = recomDataList;
        mExpectDataList = expectDataList;
        mListDataList = listDataList;
        notifyDataSetChanged();
    }

    /**
     * 搜索框Item
     */
    class SearchViewHolder extends RecyclerView.ViewHolder {

        public SearchViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 横向预告片推荐
     */
    class RecommandViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recom_recyclerview;

        public RecommandViewHolder(View itemView) {
            super(itemView);
            recomAdapter = new RecomAdapter(itemView.getContext(), mRecomDataList);
            recom_recyclerview = (RecyclerView) itemView.findViewById(R.id.recomRecyclerView);
            recom_recyclerview.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            recom_recyclerview.setAdapter(recomAdapter);
        }
    }

    /**
     * 横向最受欢迎
     */
    class ExpectViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView expct_recyclerview;

        public ExpectViewHolder(View itemView) {
            super(itemView);
            expct_recyclerview = (RecyclerView) itemView.findViewById(R.id.expctRecyclerView);
//            if (moviesData != null && moviesData.size() > 0) {
            expct_recyclerview.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            expct_recyclerview.setAdapter(new ExpctAdapter(itemView.getContext(), mExpectDataList));
//            }
        }
    }


    /**
     * 竖向recycler
     */
    public class ListViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_img;
        public TextView tv_nm, tv_3d, tv_imax, tv_scm, tv_desc, tv_wish, tv_state, tv_data;
        private Button btn_bay, btn_per;
        private RecyclerView recyclerView;

        public ListViewHolder(View itemView) {
            super(itemView);
            btn_per = (Button) itemView.findViewById(R.id.btn_per);
            tv_nm = (TextView) itemView.findViewById(R.id.tv_nm);
            tv_3d = (TextView) itemView.findViewById(R.id.tv_3d);
            tv_imax = (TextView) itemView.findViewById(R.id.tv_imax);
            tv_scm = (TextView) itemView.findViewById(R.id.tv_scm);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);
            tv_wish = (TextView) itemView.findViewById(R.id.tv_wish);
            tv_state = (TextView) itemView.findViewById(R.id.tv_state);
            btn_bay = (Button) itemView.findViewById(R.id.btn_bay);
            btn_per = (Button) itemView.findViewById(R.id.btn_per);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
//            tv_data = (TextView) itemView.findViewById(R.id.tv_data);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);

        }
    }

    /**
     * 下部分数据
     */
    class WaitSpecialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final Context mContext;
        private final List<WaitListBean.DataBean.HeadLinesVO> headLinesVO;

        public WaitSpecialAdapter(Context mContext, List<WaitListBean.DataBean.HeadLinesVO> headLinesVO) {
            this.mContext = mContext;
            this.headLinesVO = headLinesVO;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SpeciaHolder(LayoutInflater.from(mContext).inflate(R.layout.item_special, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            if (headLinesVO != null && headLinesVO.size() > 0) {
                WaitListBean.DataBean.HeadLinesVO headLinesVO = this.headLinesVO.get(position);
                ((SpeciaHolder) holder).tv_special_title.setText(headLinesVO.getType());
                ((SpeciaHolder) holder).tv_special_content.setText(headLinesVO.getTitle());
            }

        }

        @Override
        public int getItemCount() {
            if (headLinesVO != null && headLinesVO.size() > 0) {
                return headLinesVO.size();
            }
            return 0;
        }

        class SpeciaHolder extends RecyclerView.ViewHolder {
            private TextView tv_special_title, tv_special_content;

            public SpeciaHolder(View itemView) {
                super(itemView);
                tv_special_title = (TextView) itemView.findViewById(R.id.tv_special_title);
                tv_special_content = (TextView) itemView.findViewById(R.id.tv_special_content);
            }
        }
    }


    /**
     * 近期最受期待
     */
    class ExpctAdapter extends RecyclerView.Adapter<ExpctAdapter.ExpctHolder> {
        private final Context context;
        private final List<WaitExpctBean.DataBean.MoviesBean> moviesData;

        public ExpctAdapter(Context mContext, List<WaitExpctBean.DataBean.MoviesBean> moviesData) {
            this.context = mContext;
            this.moviesData = moviesData;
        }

        @Override
        public ExpctHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ExpctHolder(LayoutInflater.from(context).inflate(R.layout.item_expct, parent, false));
        }

        @Override
        public void onBindViewHolder(ExpctHolder holder, int position) {
            if (moviesData != null && moviesData.size() > 0) {
                WaitExpctBean.DataBean.MoviesBean moviesList = moviesData.get(position);
                holder.tv_rt.setText(moviesList.getRt());
                holder.tv_nm.setText(moviesList.getNm());
                holder.tv_wish.setText(moviesList.getWish() + "想看");
                String img = moviesList.getImg();
                img = img.replace("w.h", "165.220");
                Glide.with(context).load(img).into(holder.iv_img);
            }
        }

        @Override
        public int getItemCount() {
            if (moviesData != null && moviesData.size() > 0) {
                return moviesData.size();
            }
            return 0;
        }

        class ExpctHolder extends RecyclerView.ViewHolder {
            private TextView tv_rt, tv_nm, tv_wish;
            private ImageView iv_img, iv_islike;

            public ExpctHolder(View itemView) {
                super(itemView);
                tv_rt = (TextView) itemView.findViewById(R.id.tv_rt);
                tv_nm = (TextView) itemView.findViewById(R.id.tv_nm);
                tv_wish = (TextView) itemView.findViewById(R.id.tv_wish);
                iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
                iv_islike = (ImageView) itemView.findViewById(R.id.iv_islike);
            }
        }
    }

    /**
     * 预告片adapter
     */
    class RecomAdapter extends RecyclerView.Adapter<RecomAdapter.RecomItem> {

        private final Context context;
        private List<USListBean.DataBean.ComingBean> recomData;

        public RecomAdapter(Context mContext, List<USListBean.DataBean.ComingBean> recomData) {
            this.context = mContext;
            this.recomData = recomData;
        }


        @Override
        public RecomItem onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecomItem(LayoutInflater.from(context).inflate(R.layout.item_recom, parent, false));
        }

        @Override
        public void onBindViewHolder(RecomItem holder, int position) {
            if (recomData != null && recomData.size() > 0) {
                USListBean.DataBean.ComingBean recomList = recomData.get(position);
                holder.tv_nm.setText(recomList.getNm());
                holder.tv_videoName.setText(recomList.getVideoName());
                String img = recomList.getImg();
                img = img.replace("w.h", "165.220");
                Glide.with(context).load(img).into(holder.iv_icon);
            }
        }

        @Override
        public int getItemCount() {
            if (recomData != null && recomData.size() > 0) {
                return recomData.size();
            }
            return 0;
        }

        class RecomItem extends RecyclerView.ViewHolder {

            private ImageView iv_icon;
            private TextView tv_nm, tv_videoName;

            public RecomItem(View itemView) {
                super(itemView);
                iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
                tv_nm = (TextView) itemView.findViewById(R.id.tv_nm);
                tv_videoName = (TextView) itemView.findViewById(R.id.tv_videoName);
            }
        }
    }
}