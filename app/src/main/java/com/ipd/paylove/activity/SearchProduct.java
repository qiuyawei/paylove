package com.ipd.paylove.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.ipd.paylove.R;
import com.ipd.paylove.adapter.HistoryAdapter;
import com.ipd.paylove.base.BaseActivity;
import com.ipd.paylove.entity.EntiyIndex;
import com.ipd.paylove.gloable.Constant;
import com.ipd.paylove.utils.CommonUtils;
import com.ipd.paylove.utils.DialogUtils;
import com.ipd.paylove.utils.SharedPreferencesUtils;
import com.ipd.paylove.utils.ToastUtils;
import com.ipd.paylove.view.MyGridView;
import com.ipd.paylove.view.PullToRefreshLayout;
import com.ipd.paylove.view.PullableListView;
import com.ipd.paylove.view.SearchView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/*******
 * 搜索商品
 ********/
public class SearchProduct extends BaseActivity {
    private int page = 1;
    private String keywords = "";//搜索关键字

    @ViewInject(R.id.seachView)
    private SearchView searchView;
    //刷新和加载
    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout ptrl;
    @ViewInject(R.id.rfList)
    private PullableListView listView;

    private static AdapterIndex adapterIndex;
    private static List<EntiyIndex> data = new ArrayList<>();
    private List<EntiyIndex> Tempdata = new ArrayList<>();

    //    历史记录
    private SharedPreferences mPref;
    public static final String KEY_SEARCH_HISTORY_KEYWORD = "key_search_history_keyword";
    private List<String> mHistoryKeywords = new ArrayList<String>();
    ;
    private HistoryAdapter historyAdapter;
    private View histoyView;
    private RelativeLayout ll_p;
    private TextView tv_clear;
    private MyGridView myGridView;
    private TextView hotOne,hotTwo,hotThree,hotFour;
//    flage 如果为true保存历史，false不保存
    private boolean flage=false;
    @Override
    public int setLayout() {
        return R.layout.activity_search;
    }



    @Override
    public void init(Bundle savedInstanceState) {
        histoyView = View.inflate(getApplicationContext(), R.layout.history, null);
        ll_p = (RelativeLayout) histoyView.findViewById(R.id.ll_p);
        tv_clear = (TextView) histoyView.findViewById(R.id.tv_clear);
        hotOne= (TextView) histoyView.findViewById(R.id.hot_one);
        hotTwo= (TextView) histoyView.findViewById(R.id.hot_two);
        hotThree= (TextView) histoyView.findViewById(R.id.hot_three);
        hotFour= (TextView) histoyView.findViewById(R.id.hot_four);
        myGridView= (MyGridView) histoyView.findViewById(R.id.myGrid);
        historyAdapter = new HistoryAdapter(getApplicationContext(), mHistoryKeywords);
        myGridView.setAdapter(historyAdapter);
        adapterIndex = new AdapterIndex(data);
        listView.addHeaderView(histoyView);
        listView.setAdapter(adapterIndex);
        initSearchHistory();
    }

    //    初始化历史记录
    public void initSearchHistory() {
        mHistoryKeywords.clear();
        String history = SharedPreferencesUtils.getSharedPreferences(getApplicationContext(), KEY_SEARCH_HISTORY_KEYWORD);
        if (!TextUtils.isEmpty(history)) {
            List<String> list = new ArrayList<String>();
            for (String o : history.split(",")) {
                if(o.length()>5){
                    o=o.substring(0,5);
                }
                list.add((String) o);
            }
            mHistoryKeywords.addAll(list);
        }
        if (mHistoryKeywords.size() > 0) {
            ll_p.setVisibility(View.VISIBLE);
        } else {
            ll_p.setVisibility(View.GONE);
        }
        historyAdapter.notifyDataSetChanged();
//        adapterIndex.notifyDataSetChanged();
    }


    @Override
    public void setListener() {
        hotOne.setOnClickListener(this);
        hotTwo.setOnClickListener(this);
        hotThree.setOnClickListener(this);
        hotFour.setOnClickListener(this);
        searchView.setOnTextChangeListener(new SearchView.OnTextChangeListener() {
            @Override
            public void onTextChange(String s) {
                if (!TextUtils.isEmpty(s.replace(" ",""))){
                    flage=false;
                    keywords=s.replace(" ","");
                    getData("");
                }else {
                    data.clear();
                    adapterIndex.notifyDataSetChanged();
                    ToastUtils.show(getApplicationContext(),"搜索内容不能为空！");
                }

            }
        });

        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout PullToRefreshLayout2) {

                // 下拉刷新操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 千万别忘了告诉控件刷新完毕了哦！
                        flage=true;
                        page = 1;
                        getData("refesh");
                        ptrl.refreshFinish(PullToRefreshLayout2.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 3000);

            }

            @Override
            public void onLoadMore(final PullToRefreshLayout PullToRefreshLayout2) {
                // 加载操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 千万别忘了告诉控件加载完毕了哦！
                        flage=true;
                        page++;
                        getData("loadMore");
                        ptrl.loadmoreFinish(PullToRefreshLayout2.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 3000);

            }
        });

        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanHistory();
            }
        });
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                flage=true;
                page=0;
                keywords=mHistoryKeywords.get(position);
                getData("");
            }
        });
    }


    @Override
    public void onClick(View v) {
        page=1;
        flage=true;
        switch (v.getId()){
            case R.id.hot_one:
                keywords="柚色";
                break;
            case R.id.hot_two:
                keywords="FUYIME";
                break;
            case R.id.hot_three:
                keywords="姿生本草";
                break;
            case R.id.hot_four:
                keywords="乐呵宝";
                break;
        }
        getData("");
    }

    /* keywords	搜索关键字（获取所有商品时传空或不传）
         page	页码（默认为1）
         page_size	每页显示数量（默认为10）*/
    private void getData(String canRoate) {
        Log.i("TAG", "keywords=" + keywords);
        Tempdata.clear();
        if (page == 1) {
            data.clear();
        }
        if (TextUtils.isEmpty(canRoate))
            dialog();
        RequestParams params = new RequestParams(Constant.BASE_URL + "Goods/index");
        params.addBodyParameter("keywords", keywords);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("page_size", Constant.SIZE);
        params.addBodyParameter("user_token", Constant.USER_TOKEN);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("TAG", "res=" + result);
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("true")) {
                        Tempdata.addAll(JSON.parseArray(json.getString("data"), EntiyIndex.class));
                        data.addAll(Tempdata);
                    } else {
                        CommonUtils.loginOverrTime(json.getString("msg"),SearchProduct.this,true);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                adapterIndex.notifyDataSetChanged();
                if(!flage)
                save();
                dismiss();
            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });

    }


    public class AdapterIndex extends BaseAdapter {
        private List<EntiyIndex> data;
        private AdapterIndex adapter;
        ViewHolder vh = null;

        public AdapterIndex(List<EntiyIndex> mdata) {
            this.data = mdata;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            //  if (convertView == null) {
            vh = new ViewHolder();
            convertView = View.inflate(getApplicationContext(), R.layout.item_index, null);
            vh.pro_photo = (ImageView) convertView.findViewById(R.id.iv_indexPhoto);
            vh.pro_name = (TextView) convertView.findViewById(R.id.tv_indexName);
            vh.pro_price = (TextView) convertView.findViewById(R.id.tv_indexPrice);
            vh.pro_number = (TextView) convertView.findViewById(R.id.et_indexNumber);
            vh.add_car = (Button) convertView.findViewById(R.id.bt_indexAddcar);
            convertView.setTag(vh);
          /*  } else {
                vh = (ViewHolder) convertView.getTag();
            }*/
            Glide.with(getApplicationContext()).load(data.get(position).goods_img).placeholder(R.drawable.erropic).error(R.drawable.erropic).centerCrop().into(vh.pro_photo);
            vh.pro_name.setText(data.get(position).goods_name);
            vh.pro_price.setText("¥" + data.get(position).price);
            vh.pro_number.setText(data.get(position).number + "");


            vh.add_car.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CommonUtils.goLogin(SearchProduct.this)) {
                        return;
                    }
                    if (data.get(position).number.equals("0") || TextUtils.isEmpty(data.get(position).number)) {
                        ToastUtils.show(getApplicationContext(), "数量最小为1哦！");
                        return;
                    } else
                        addShopCar(data.get(position).goods_id, data.get(position).number);
                }
            });

            vh.pro_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CommonUtils.goLogin(SearchProduct.this)) {
                        return;
                    }
                    DialogUtils.ShowSeach(SearchProduct.this, vh.pro_number, vh.pro_number.getText().toString().trim(), adapterIndex, data, position);
                }
            });

//未登录不显示价格
            if(TextUtils.isEmpty(SharedPreferencesUtils.getSharedPreferences(getApplicationContext(),"user_token"))){
                vh.pro_price.setVisibility(View.GONE);
            }else vh.pro_price.setVisibility(View.VISIBLE);


            return convertView;
        }


        private class ViewHolder {
            ImageView pro_photo;
            TextView pro_name;
            TextView pro_price;
            TextView pro_number;
            Button add_car;
        }


    }

    private void addShopCar(String goods_id, String num) {
        dialog();
        RequestParams params = new RequestParams(Constant.BASE_URL + "Cart/add");
        params.addBodyParameter("user_token", Constant.USER_TOKEN);
        params.addBodyParameter("goods_id", goods_id);
        params.addBodyParameter("num", num);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("200")) {
                        ToastUtils.show(getApplicationContext(), json.getString("msg"));
                    } else {
                        ToastUtils.show(getApplicationContext(),json.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dismiss();
            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }
    List<String> list = new ArrayList<String>();
//    blage=false说明没有重复
    boolean blage=false;
    String final_history="";
    //    保存历史记录,最多保存5条，多了替换

    public void save()
    {
            list.clear();
            blage=false;
            String oldText = SharedPreferencesUtils.getSharedPreferences(getApplicationContext(), KEY_SEARCH_HISTORY_KEYWORD);
           //判断是否搜索的关键字已经存在，存在不再保存,如果已经存了5条，替换
            if (!TextUtils.isEmpty(oldText))
            {
                    for (String o : oldText.split(","))
                    {
                        list.add(o);
                        if (keywords.equals(o))
                        {
                            blage = true;
                            keywords="";
                        }
                     }


//            如果已经存了5条并且不重复，替换,重复就不往里添加了
            if(!blage&&list.size()==5)
            {
                oldText="";
               for(int i=0;i<5;i++)
               {
                   if(i!=4)
                   {
                       if(oldText.equals("")){
                           oldText=list.get(i);
                       }else
                       oldText=oldText+","+list.get(i);
                   }

               }
//               判断搜索字段长度，自动截取前5五位保存
                if(keywords.length()>5){
                    keywords=keywords.substring(0,5);
                }
                SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), KEY_SEARCH_HISTORY_KEYWORD,keywords+","+ oldText);
            }else if(list.size()<5&&!blage){
                SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), KEY_SEARCH_HISTORY_KEYWORD,keywords+","+ oldText);
            }
        }
        else
            {
                if(keywords.length()>5){
                    keywords=keywords.substring(0,5);
                }
                SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), KEY_SEARCH_HISTORY_KEYWORD,keywords);
            }


               initSearchHistory();
    }

    //清除历史记录
    public void cleanHistory() {
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), KEY_SEARCH_HISTORY_KEYWORD, "");
        initSearchHistory();
    }

  /*  @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("REFRESH");
        intentFilter.addAction("Cancel_price");
        registerReceiver(myReciver,intentFilter);
    }
    BroadcastReceiver myReciver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("REFRESH")||intent.getAction().equals("Cancel_price")){
                page=1;
                getData("");
            }
        }
    };*/


}
