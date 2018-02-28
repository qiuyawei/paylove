package com.ipd.paylove.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ipd.paylove.R;
import com.ipd.paylove.activity.AdapterName;
import com.ipd.paylove.adapter.AdapterBank;
import com.ipd.paylove.entity.BankEntity;
import com.ipd.paylove.entity.EntityName;
import com.ipd.paylove.gloable.Constant;
import com.ipd.paylove.utils.ToastUtils;
import com.ipd.paylove.widget.HKDialogLoading;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;


/**
 * All rights Reserved, Designed By GeofferySun
 *
 * @Description:从底部弹出或滑出选择菜单或窗口
 */
public class BankPopupWindow extends PopupWindow {
    private Context context;
    private MyListView myListView;
    private View mMenuView;
    private List<String> entityNames;
    private AdapterBank adapterBank;
    private TextView textView;
    private List<String> names;
    private HKDialogLoading hkDialogLoading;
    @SuppressLint("InflateParams")
    public BankPopupWindow(Context context, final List<String> names, final TextView textView) {
        super(context);
        this.context=context;
        this.textView = textView;
        this.names = names;
        hkDialogLoading=new HKDialogLoading(context,R.style.HKDialog);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.dialog_select_bank, null);
        myListView = (MyListView) mMenuView.findViewById(R.id.myList);
        adapterBank = new AdapterBank(context, names);
        myListView.setAdapter(adapterBank);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    resetPersonInfor(names.get(position));
                    dismiss();
            }
        });


        // 设置按钮监听
        //cancelBtn.setOnClickListener(itemsOnClick);
        //pickPhotoBtn.setOnClickListener(itemsOnClick);
        //takePhotoBtn.setOnClickListener(itemsOnClick);

        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x80000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {

            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });


    }

    /*******
     * 修改用户其他信息（详细信息）
     ******/

  /*  user_token	登录时返回的user_token
    phone	手机
    id_card	身份证号
    birthday	生日
    email	邮箱
    zipcord	邮编
    bank_name	开户行名称
    bank_account	银行账号
    account_holder	开户人姓名
    bank_addr	开户行地址*/
    private void resetPersonInfor(final String bankName) {
        hkDialogLoading.show();
        RequestParams params = new RequestParams(Constant.BASE_URL + "UserUpdate/updateOtherInfo");
        params.addBodyParameter("user_token", Constant.USER_TOKEN);
        params.addBodyParameter("phone", "");
        params.addBodyParameter("id_card", "");
        params.addBodyParameter("birthday", "");
        params.addBodyParameter("email", "");
        params.addBodyParameter("zipcord", "");
        params.addBodyParameter("bank_name", bankName);
        params.addBodyParameter("bank_account", "");
        params.addBodyParameter("account_holder", "");
        params.addBodyParameter("bank_addr", "");

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("true")) {
                        ToastUtils.show(context, "收款银行修改成功！");
                        textView.setText(bankName);
                    } else ToastUtils.show(context, json.getString("msg"));
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
                hkDialogLoading.dismiss();
            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });

    }
}
