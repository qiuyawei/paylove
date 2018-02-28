package com.ipd.paylove.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.ipd.paylove.R;
import com.ipd.paylove.adapter.AddressAdapter;
import com.ipd.paylove.base.BaseActivity;
import com.ipd.paylove.entity.EntityAddress;
import com.ipd.paylove.gloable.Constant;
import com.ipd.paylove.swipemenulistview.SwipeMenu;
import com.ipd.paylove.swipemenulistview.SwipeMenuCreator;
import com.ipd.paylove.swipemenulistview.SwipeMenuItem;
import com.ipd.paylove.swipemenulistview.SwipeMenuListView;
import com.ipd.paylove.utils.CommonUtils;
import com.ipd.paylove.utils.Constants;
import com.ipd.paylove.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**********收货地址**********/
public class Address extends BaseActivity {
    private int pages=1;
    @ViewInject(R.id.bt_addNewAddress)
    private Button bt_addNewAddress;
    @ViewInject(R.id.listViewAddress)
    private SwipeMenuListView listView;
    private AddressAdapter adapter;
    private List<EntityAddress> data=new ArrayList<>();
    /*******select=true代表是选择收货地址，false代表修改或者删除*******/
    private boolean select=false;

    //侧滑删除
    SwipeMenuCreator creator;
    @Override
    public int setLayout() {
        return R.layout.activity_address;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(CommonUtils.dp2px(getApplicationContext(),90));
                // set item title
                openItem.setTitle("编辑");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(CommonUtils.dp2px(getApplicationContext(),90));
                // set a icon
                deleteItem.setTitle("删除");
                // set item title fontsize
                deleteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);
        select=getIntent().getBooleanExtra("select",false);
        setData();
        setBack();
        setTopTitle("选择收货地址");
        adapter=new AddressAdapter(getApplication(),data);
        listView.setAdapter(adapter);

        getAddress();

    }

    @Override
    public void setListener() {
        bt_addNewAddress.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("TAG","select="+select);
                if(select){
                    intent=new Intent();
                    intent.putExtra("AddressEntity",data.get(position));
                    setResult(Constants.SELECT_ADDRESS_RESULT_CODE,intent);
                    Log.i("TAG","==");
                    //关闭
                    finish();
                }
            }
        });



        // step 2. listener item click event
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {

                switch (index) {
                    case 0:
                        //编辑
                        intent=new Intent(getApplicationContext(),AddNewAddress.class);
                        intent.putExtra("AddressEntity",data.get(position));
                        startActivity(intent);
                        break;
                    case 1:
                        // 删除
                        //delete(item);
                        deletAddress(data.get(position).aid,position);
                        break;
                }

            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_addNewAddress:
                intent=new Intent(getApplicationContext(),AddNewAddress.class);
                startActivity(intent);
                break;
        }
    }
    private  void setData(){

    }


  /*  user_token
            page
    page_size
*/
    private void getAddress(){
        dialog();
        data.clear();
        RequestParams params=new RequestParams(Constant.BASE_URL+"UserAddress/buyAddressList");
        params.addBodyParameter("user_token",Constant.USER_TOKEN);
        params.addBodyParameter("page",String.valueOf(pages));
        params.addBodyParameter("page_size",String.valueOf(20));
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject json=new JSONObject(result);
                    if(json.getString("result").equals("true")){
                        JSONArray jsonArray=json.getJSONArray("data");
                        data.addAll(JSON.parseArray(jsonArray.toString(),EntityAddress.class));
                    }else {
                        CommonUtils.loginOverrTime(json.getString("msg"),getApplicationContext(),false);
                        ToastUtils.show(getApplicationContext(),json.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("TAG","error="+ex.getMessage());
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


    @Override
    protected void onRestart() {
        super.onRestart();
        getAddress();
    }



    /*******
     * 删除收货地址
     ******/
    private void deletAddress(String aid, final int pos) {
        dialog();
        RequestParams params = new RequestParams(Constant.BASE_URL + "UserAddress/deleteUserAddress");
        params.addBodyParameter("aid", aid);
        params.addBodyParameter("user_token", Constant.USER_TOKEN);


        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("TAG", "result=" + result);
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("true")) {
                        ToastUtils.show(getApplicationContext(), "地址删除成功！");
                        data.remove(pos);
                        adapter.notifyDataSetChanged();
                    } else ToastUtils.show(getApplicationContext(), json.getString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("TAG", "onError=" + ex.getMessage());

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
}
