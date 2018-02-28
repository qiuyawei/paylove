package com.ipd.paylove.utils;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ipd.paylove.R;
import com.ipd.paylove.activity.Login;
import com.ipd.paylove.activity.SearchProduct;
import com.ipd.paylove.activity.SplitOrder;
import com.ipd.paylove.adapter.SimpleListAdapter;
import com.ipd.paylove.entity.EntiyIndex;
import com.ipd.paylove.entity.OrderEntityInner;
import com.ipd.paylove.entity.ShopCarEntity;
import com.ipd.paylove.fragment.IndexFragment;
import com.ipd.paylove.fragment.JinHuoDanFragment;
import com.ipd.paylove.gloable.Constant;
import com.ipd.paylove.view.MyWebView;
import com.ipd.paylove.widget.HKDialogLoading;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Arrays;
import java.util.List;

import static com.ipd.paylove.R.id.tv_cancel;


/**
 * Created by Administrator on 2015/12/22.
 */
public class DialogUtils {
    //    查看明细   查看发货情况
    public static void showFaHuo(final Context context, final String ordrl, String bz) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fahuo_dialog, null, true);
        final ListView myListView = (ListView) view.findViewById(R.id.myList);
        final TextView beizhu = (TextView) view.findViewById(R.id.tv_beizhu);
        final SimpleListAdapter adapter=new SimpleListAdapter(context, Arrays.asList(ordrl.split("\n")));
        myListView.setAdapter(adapter);


        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText((String)adapter.getItem(position));
                ToastUtils.show(context,"复制成功！");
                dialog.dismiss();
                return false;
            }
        });
/*//        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        order.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(ordrl);
                Log.i("TAG","ordrl="+ordrl);
                ToastUtils.show(context,"复制成功！");
                return false;
            }
        });
        order.setText(ordrl);*/
        beizhu.setText(bz);
        dialog.setContentView(view);
        dialog.show();
    }

    //    首页弹出框
    public static void showIndex(final Context context, String url) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.index_dialog, null, true);
        final MyWebView webView = (MyWebView) view.findViewById(R.id.webView);
        final ImageView iv_cancell = (ImageView) view.findViewById(R.id.iv_cancell);
//        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        iv_cancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        webView.loadUrl(url);
        dialog.setContentView(view);
        dialog.show();
    }


    public static void login(final Context context) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.login_dialog, null, true);
        final TextView tv_sure = (TextView) view.findViewById(R.id.tv_sure);
        final TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
//        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(context, Login.class);
                intent.putExtra("needLogin", true);
                context.startActivity(intent);
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }


    public static void ShopCarshow(final int pos, final List<ShopCarEntity> data, final JinHuoDanFragment.AdapterMiddle adapterMiddle, final Context context, final String number, final HKDialogLoading dialogLoading, final TextView tv_number, final String cart_id) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_shopcar, null, true);
        Button bt_add = (Button) view.findViewById(R.id.ib_add);//增加
        Button bt_desc = (Button) view.findViewById(R.id.ib_desc);//减少
        Button bt_cancel = (Button) view.findViewById(R.id.tv_commit);//减少
        Button bt_sure = (Button) view.findViewById(tv_cancel);//减少

        final EditText et_number = (EditText) view.findViewById(R.id.et_middle);
        et_number.setText(number);

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = Integer.parseInt(et_number.getText().toString().trim()) + 1;
                et_number.setText(String.valueOf(temp));
            }
        });
        bt_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(et_number.getText().toString().trim()) == 1) {
                    ToastUtils.show(context, "数量最小为1！");
                    return;
                }
                int temp = Integer.parseInt(et_number.getText().toString().trim()) - 1;
                et_number.setText(String.valueOf(temp));
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_number.getText().toString().trim().equals("0") || et_number.getText().toString().trim().equals("") || Integer.parseInt(et_number.getText().toString().trim()) < 0) {
                    ToastUtils.show(context, "数量最小为1！");
                    et_number.setText("1");
                    return;
                }
                dialog.dismiss();
                if (!et_number.getText().toString().trim().equals(number)) {
                    //和原来数量一致不用调接口
                    resetShopCarNumber(pos, data, adapterMiddle, dialogLoading, context, cart_id, et_number.getText().toString().trim(), tv_number);
                }
            }
        });
        dialog.setContentView(view);

        dialog.show();
    }


    public static void ShowIndex(final Context context, final TextView tv_number, String number, final IndexFragment.AdapterIndex adapterIndex, final List<EntiyIndex> data, final int pos) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_shopcar, null, true);
        Button bt_add = (Button) view.findViewById(R.id.ib_add);//增加
        Button bt_desc = (Button) view.findViewById(R.id.ib_desc);//减少
        Button bt_cancel = (Button) view.findViewById(R.id.tv_commit);//减少
        Button bt_sure = (Button) view.findViewById(tv_cancel);//减少

        final EditText et_number = (EditText) view.findViewById(R.id.et_middle);
        et_number.setText(number);

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = Integer.parseInt(et_number.getText().toString().trim()) + 1;
                et_number.setText(String.valueOf(temp));
            }
        });
        bt_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(et_number.getText().toString().trim()) == 1) {
                    ToastUtils.show(context, "数量最小为1！");
                    return;
                }
                int temp = Integer.parseInt(et_number.getText().toString().trim()) - 1;
                et_number.setText(String.valueOf(temp));
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_number.getText().toString().trim().equals("0") || et_number.getText().toString().trim().equals("")) {
                    ToastUtils.show(context, "数量最小为1！");
                    et_number.setText("1");
                    return;
                } else {
                    data.get(pos).number = et_number.getText().toString().trim();
                    adapterIndex.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    public static void ShowSeach(final Context context, final TextView tv_number, String number, final SearchProduct.AdapterIndex adapterIndex, final List<EntiyIndex> data, final int pos) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_shopcar, null, true);
        Button bt_add = (Button) view.findViewById(R.id.ib_add);//增加
        Button bt_desc = (Button) view.findViewById(R.id.ib_desc);//减少
        Button bt_cancel = (Button) view.findViewById(R.id.tv_commit);//减少
        Button bt_sure = (Button) view.findViewById(tv_cancel);//减少

        final EditText et_number = (EditText) view.findViewById(R.id.et_middle);
        et_number.setText(number);

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = Integer.parseInt(et_number.getText().toString().trim()) + 1;
                et_number.setText(String.valueOf(temp));
            }
        });
        bt_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(et_number.getText().toString().trim()) == 1) {
                    ToastUtils.show(context, "数量最小为1！");
                    return;
                }
                int temp = Integer.parseInt(et_number.getText().toString().trim()) - 1;
                et_number.setText(String.valueOf(temp));
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_number.getText().toString().trim().equals("0") || et_number.getText().toString().trim().equals("")) {
                    ToastUtils.show(context, "数量最小为1！");
                    et_number.setText("1");
                    return;
                } else {
                    data.get(pos).number = et_number.getText().toString().trim();
                    adapterIndex.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }


    public static void show(Context context, String title, String msg, String flag, final onDialogClickListener listener) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.chedan_dialog, null, true);
        final TextView tv_commit = (TextView) view.findViewById(R.id.tv_sure);
        final TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);

        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onCommit(view, tv_commit.getText().toString());
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onCancel(view, tv_cancel.getText().toString());
                }
            }
        });

        dialog.setContentView(view);
        dialog.show();


    }

    /*******
     * 根据需要添加监听事件
     ******/
    public interface onDialogClickListener {
        public void onCommit(View view, String s);

        public void onCancel(View view, String s);

    }


    //修改购物车商品数量
    private static void resetShopCarNumber(final int pos, final List<ShopCarEntity> data, final JinHuoDanFragment.AdapterMiddle adapterMiddle, final HKDialogLoading dialogLoading, final Context context, String car_id, final String num, final TextView tv) {
        dialogLoading.show();
        while (num.startsWith("0")) {
            // num=num.replace("")
        }
        RequestParams params = new RequestParams(Constant.BASE_URL + "Cart/setCartNum");
        params.addBodyParameter("user_token", Constant.USER_TOKEN);
        params.addBodyParameter("car_id", car_id);
        params.addBodyParameter("num", num);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("TAGS", "result=" + result);
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("true")) {
                        ToastUtils.show(context, json.getString("msg"));
                        data.get(pos).buy_count = num;
                        adapterMiddle.notifyDataSetChanged();

                    } else {
                        CommonUtils.loginOverrTime(json.getString("msg"), context, false);
                        ToastUtils.show(context, json.getString("msg"));
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

                dialogLoading.dismiss();

            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });


    }


//    拆分订单输入数字


    public static void SplitOrderShow(final int pos, final List<OrderEntityInner> data, final SplitOrder.SplitAdapter adapterMiddle, final Context context) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_shopcar, null, true);
        Button bt_add = (Button) view.findViewById(R.id.ib_add);//增加
        Button bt_desc = (Button) view.findViewById(R.id.ib_desc);//减少
        Button bt_cancel = (Button) view.findViewById(R.id.tv_commit);//减少
        Button bt_sure = (Button) view.findViewById(tv_cancel);//减少

        final EditText et_number = (EditText) view.findViewById(R.id.et_middle);

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = Integer.parseInt(et_number.getText().toString().trim()) + 1;
                if (Integer.parseInt(data.get(pos).buy_count) >= temp)
                    et_number.setText(String.valueOf(temp));
                else ToastUtils.show(context, "不能超过拆分产品的总数！");
            }
        });
        bt_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(et_number.getText().toString().trim()) == 1) {
                    ToastUtils.show(context, "数量最小为1！");
                    return;
                }
                int temp = Integer.parseInt(et_number.getText().toString().trim()) - 1;
                et_number.setText(String.valueOf(temp));
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_number.getText().toString().trim().equals("0") || et_number.getText().toString().trim().equals("") || Integer.parseInt(et_number.getText().toString().trim()) < 0) {
                    ToastUtils.show(context, "数量最小为1！");
                    et_number.setText("1");
                    return;
                }

                dialog.dismiss();
                if (Integer.parseInt(data.get(pos).buy_count) > Integer.parseInt(et_number.getText().toString().trim()))
                    data.get(pos).orginNumber = et_number.getText().toString().trim();
                else
                    data.get(pos).orginNumber = data.get(pos).buy_count;
                adapterMiddle.notifyDataSetChanged();
            }
        });
        dialog.setContentView(view);

        dialog.show();
    }


}
