package com.ipd.paylove.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ipd.paylove.R;
import com.ipd.paylove.base.BaseActivity;
import com.ipd.paylove.fragment.IndexFragment;
import com.ipd.paylove.fragment.JinHuoDanFragment;
import com.ipd.paylove.fragment.PersonCenterFragment;
import com.ipd.paylove.gloable.MyApplication;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import static com.pgyersdk.update.UpdateManagerListener.startDownloadTask;

/**
 * 主页面
 */
public class TotalActivity extends BaseActivity {
    @ViewInject(R.id.radioGroup)
    private RadioGroup radioGroup;
    @ViewInject(R.id.btn_total01)
    private RadioButton btn_total01;
    @ViewInject(R.id.btn_total02)
    private RadioButton btn_total02;
    @ViewInject(R.id.btn_total03)
    private RadioButton btn_total03;
    @ViewInject(R.id.mycontiner)
    private FrameLayout container;
    private IndexFragment indexFragment;
    private JinHuoDanFragment jinHuoDanFragment;
    private PersonCenterFragment personCenterFragment;
    private FragmentManager fragmentManager;

    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        fragmentManager = getSupportFragmentManager();
        if (indexFragment == null) {
            indexFragment = new IndexFragment();
        }
        if (jinHuoDanFragment == null) {
            jinHuoDanFragment = new JinHuoDanFragment();
        }
        if (personCenterFragment == null) {
            personCenterFragment = new PersonCenterFragment();
        }
        fragmentManager.beginTransaction().add(R.id.mycontiner, indexFragment).commit();
        fragmentManager.beginTransaction().add(R.id.mycontiner, jinHuoDanFragment).commit();
        fragmentManager.beginTransaction().add(R.id.mycontiner, personCenterFragment).commit();
        setAllHidern();
        fragmentManager.beginTransaction().show(indexFragment).commit();
        showFragment(1);
        checkUpdate();

    }

    @Override
    public void setListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.btn_total01:
                        setAllHidern();
                        showFragment(1);
                        break;
                    case R.id.btn_total02:
                        setAllHidern();
                        showFragment(2);
                        break;
                    case R.id.btn_total03:
                        setAllHidern();
                        showFragment(3);
                        break;
                }
            }
        });
    }


    @Override
    public void onClick(View v) {

    }

    /*******
     * 隐藏所有Fragment
     *******/

    private void setAllHidern() {
        if (indexFragment != null) {
            fragmentManager.beginTransaction().hide(indexFragment).commit();
        }
        if (jinHuoDanFragment != null) {
            fragmentManager.beginTransaction().hide(jinHuoDanFragment).commit();
        }
        if (personCenterFragment != null) {
            fragmentManager.beginTransaction().hide(personCenterFragment).commit();
        }

    }

    /*******
     * 根据序号显示Fragment
     *******/
    private void showFragment(int number) {
        if (indexFragment != null && number == 1) {
            fragmentManager.beginTransaction().show(indexFragment).commit();
        }
        if (jinHuoDanFragment != null && number == 2) {
            fragmentManager.beginTransaction().show(jinHuoDanFragment).commit();
        }
        if (personCenterFragment != null && number == 3) {
            fragmentManager.beginTransaction().show(personCenterFragment).commit();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // super.onSaveInstanceState(outState);
        // Log.i("TAG","onSaveInstanceState");
    }


    private long exitTime = 0;

    /*********点击退出**********/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 3000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                List<Activity> activities = MyApplication.activityList;
                for (int i = 0; i < activities.size(); i++) {
                    activities.get(i).finish();
                }
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    /**
     * 强制更新
     */
    private void checkUpdate() {
        // 版本检测方式2：带更新回调监听
        PgyUpdateManager.register(TotalActivity.this, getResources().getString(R.string.app_name),
                new UpdateManagerListener() {
                    @Override
                    public void onUpdateAvailable(final String result) {
//                        upAppVersion(result);

                        String url;
                        JSONObject jsonData;
                        try {
                            jsonData = new JSONObject(
                                    result);
                            if ("0".equals(jsonData
                                    .getString("code"))) {
                                JSONObject jsonObject = jsonData
                                        .getJSONObject("data");
                                url = jsonObject
                                        .getString("downloadURL");

                                startDownloadTask(
                                        TotalActivity.this,
                                        url);

                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated
                            // catch block
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNoUpdateAvailable() {
                    }
                });
    }




    public void upAppVersion(final String result) {
        final Dialog dialog = new Dialog(TotalActivity.this, R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(TotalActivity.this);
        View viewcall = inflater.inflate(R.layout.login_dialog, null, true);
        TextView title = (TextView) viewcall.findViewById(R.id.tv_desc);
        TextView tv_cancel = (TextView) viewcall.findViewById(R.id.tv_cancel);
        TextView tv_sure = (TextView) viewcall.findViewById(R.id.tv_sure);
        tv_sure.setText("确认");
        tv_cancel.setText("取消");
        title.setText("发现新的版本，是否立即更新 ？");
//            更新
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog. dismiss();
                String url;
                JSONObject jsonData;
                try {
                    jsonData = new JSONObject(
                            result);
                    if ("0".equals(jsonData
                            .getString("code"))) {
                        JSONObject jsonObject = jsonData
                                .getJSONObject("data");
                        url = jsonObject
                                .getString("downloadURL");

                        startDownloadTask(
                                TotalActivity.this,
                                url);

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated
                    // catch block
                    e.printStackTrace();
                }
            }
        });
//            取消
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(viewcall);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

    }
}
