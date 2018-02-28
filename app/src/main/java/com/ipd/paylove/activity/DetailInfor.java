package com.ipd.paylove.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ipd.paylove.R;
import com.ipd.paylove.base.BaseActivity;
import com.ipd.paylove.entity.PersonInforResult;
import com.ipd.paylove.gloable.Constant;
import com.ipd.paylove.utils.CommonUtils;
import com.ipd.paylove.utils.MyBitmap;
import com.ipd.paylove.utils.SelectPicPopupWindow;
import com.ipd.paylove.view.BankPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.Arrays;

/*******
 * 详细信息
 *********/
public class DetailInfor extends BaseActivity {

    @ViewInject(R.id.tv_banlacne)
    private TextView tv_banlacne;
    private PersonInforResult personInforResult;

    @ViewInject(R.id.rl_address)
    private RelativeLayout rl_address;
    @ViewInject(R.id.tv_address)
    private TextView tv_address;
    @ViewInject(R.id.rl_bank)
    private RelativeLayout rl_bank;
    @ViewInject(R.id.tv_bank)
    private TextView tv_bank;
    @ViewInject(R.id.ivPhoto)
    private ImageView ivPhoto;
    @ViewInject(R.id.ll_parent)
    private LinearLayout ll_parent;

    @ViewInject(R.id.rl_phone)
    private RelativeLayout rl_phone;
    @ViewInject(R.id.rl_ID)
    private RelativeLayout rl_ID;
    @ViewInject(R.id.rl_birthdy)
    private RelativeLayout rl_birthdy;
    @ViewInject(R.id.rl_email)
    private RelativeLayout rl_email;
    @ViewInject(R.id.rl_code)
    private RelativeLayout rl_code;
    @ViewInject(R.id.rl_bankcount)
    private RelativeLayout rl_bankcount;
    @ViewInject(R.id.rl_khr)
    private RelativeLayout rl_khr;
    @ViewInject(R.id.rl_khhaddress)
    private RelativeLayout rl_khhaddress;
    @ViewInject(R.id.rl_ssq)
    private RelativeLayout rl_ssq;
    @ViewInject(R.id.tv_ssq)
    private TextView tv_ssq;

    @ViewInject(R.id.tv_phone)
    private TextView tv_phone;
    @ViewInject(R.id.tv_ID)
    private TextView tv_ID;
    @ViewInject(R.id.tv_birthdy)
    private TextView tv_birthdy;
    @ViewInject(R.id.tv_email)
    private TextView tv_email;
    @ViewInject(R.id.tv_code)
    private TextView tv_code;
    @ViewInject(R.id.tv_bankcount)
    private TextView tv_bankcount;
    @ViewInject(R.id.tv_khr)
    private TextView tv_khr;
    @ViewInject(R.id.tv_khhaddress)
    private TextView tv_khhaddress;
    private SelectPicPopupWindow menuWindow;
    private BankPopupWindow bankPopupWindow;
    @Override
    public int setLayout() {
        return R.layout.activity_detail_infor;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setBack();
        setTopTitle("详细信息");
        getPeronInfor();
    }

    @Override
    public void setListener() {
 /*       rl_bank.setOnClickListener(this);
//        ivPhoto.setOnClickListener(this);
        rl_address.setOnClickListener(this);
        rl_phone.setOnClickListener(this);
        rl_ID.setOnClickListener(this);
        rl_birthdy.setOnClickListener(this);
        rl_email.setOnClickListener(this);
        rl_code.setOnClickListener(this);
        rl_bankcount.setOnClickListener(this);
        rl_khr.setOnClickListener(this);
        rl_khhaddress.setOnClickListener(this);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_bank:
                bankPopupWindow=new BankPopupWindow(DetailInfor.this, Arrays.asList(personInforResult.bank),tv_bank);
                bankPopupWindow.showAtLocation(ll_parent, Gravity.BOTTOM, 0, 0);
                break;

            case R.id.ivPhoto:
                //上传身份证照片,暂时没有
                menuWindow = new SelectPicPopupWindow(this, itemsOnClick);
                menuWindow.showAtLocation(ll_parent, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.rl_address:
                //选取收货地址
                intent = new Intent(getApplicationContext(), Address.class);
                startActivity(intent);
                break;
            case R.id.rl_phone:
                intent = new Intent(getApplicationContext(), Reset.class);
                intent.putExtra("reset", "resetPhone");
                startActivity(intent);
                break;
            case R.id.rl_ID:
                intent = new Intent(getApplicationContext(), Reset.class);
                intent.putExtra("reset", "resetID");
                startActivity(intent);
                break;
            case R.id.rl_birthdy:
                intent = new Intent(getApplicationContext(), Reset.class);
                intent.putExtra("reset", "resetBirthdy");
                startActivity(intent);
                break;
            case R.id.rl_email:
                intent = new Intent(getApplicationContext(), Reset.class);
                intent.putExtra("reset", "resetEmail");
                startActivity(intent);
                break;
            case R.id.rl_code:
                intent = new Intent(getApplicationContext(), Reset.class);
                intent.putExtra("reset", "resetCode");
                startActivity(intent);
                break;
            case R.id.rl_bankcount:
                intent = new Intent(getApplicationContext(), Reset.class);
                intent.putExtra("reset", "resetBankcount");
                startActivity(intent);
                break;
            case R.id.rl_khr:
                intent = new Intent(getApplicationContext(), Reset.class);
                intent.putExtra("reset", "resetKhr");
                startActivity(intent);
                break;
            case R.id.rl_khhaddress:
                intent = new Intent(getApplicationContext(), Reset.class);
                intent.putExtra("reset", "resetKhhaddress");
                startActivity(intent);
                break;
        }
    }


    private String photoSaveName;
    public String photoSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/paylove/";
    public static final int PHOTOTAKE = 123, PHOTOZOOM = 124;
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            // 隐藏弹出窗口
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.takePhotoBtn:// 拍照
                    // 相机
                    photoSaveName = String.valueOf(System.currentTimeMillis()) + ".png";
                    Uri imageUri = null;
                    File file = new File(photoSavePath);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    imageUri = Uri.fromFile(new File(photoSavePath, photoSaveName));
                    openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(openCameraIntent, PHOTOTAKE);
                    break;
                case R.id.pickPhotoBtn:// 相册选择图片
                    Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    openAlbumIntent.setType("image/*");
                    startActivityForResult(openAlbumIntent, PHOTOZOOM);
                    break;
                case R.id.cancelBtn:// 取消
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTOZOOM && resultCode == RESULT_OK) {
            String path = MyBitmap.getInstance().getPath(this, data.getData());
            File bitmapFile = new File(path);
            // file= bitmapFile;
            if (bitmapFile.exists()) {
                Bitmap bitmap = MyBitmap.getInstance().reSizeBitmap(this, bitmapFile);
                //  Bitmap2InputStream(bitmap,60);
                //  upLoadPhoto(Bitmap2InputStream(bitmap,30));
                // logo=new FileInputStream(bitmapFile);
                //   Log.i("TAG","logo="+logo);
                // upLoadPhoto(logo);
                // submit();
                ivPhoto.setImageBitmap(bitmap);
            }
        } else if (requestCode == PHOTOTAKE && resultCode == RESULT_OK) {
            String path = photoSavePath + photoSaveName;
            File bitmapFile = new File(path);


            //  file= bitmapFile;
            if (bitmapFile.exists()) {
                Bitmap bitmap = MyBitmap.getInstance().reSizeBitmap(this, bitmapFile);
                //  upLoadPhoto(Bitmap2InputStream(bitmap,30));

                // submit();
                ivPhoto.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getPeronInfor();
    }


    /*******
     * 获取用户详细信息
     ******/
    private void getPeronInfor() {
        dialog();
        RequestParams params = new RequestParams(Constant.BASE_URL + "User/getUserInfo");
        params.addBodyParameter("user_token", Constant.USER_TOKEN);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("TAG","result="+result);
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("true")) {
                        JSONObject json1=json.getJSONObject("data");
                        personInforResult = JSON.parseObject(json1.toString(), PersonInforResult.class);

                        setData();
                    } else {
                        CommonUtils.loginOverrTime(json.getString("msg"),DetailInfor.this,true);
                    }
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




    private void setData() {
        tv_banlacne.setText(personInforResult.balance);
        tv_bank.setText(personInforResult.bank_name);
        tv_phone.setText(personInforResult.phone);
        tv_ID.setText(personInforResult.idcard);
        tv_birthdy.setText(personInforResult.birthday);
        tv_email.setText(personInforResult.email);
        tv_code.setText(personInforResult.zipcord);
        tv_bankcount.setText(personInforResult.bank_account);
        tv_khr.setText(personInforResult.account_holder);
        tv_khhaddress.setText(personInforResult.bank_addr);
        if(personInforResult.provinces.contains(personInforResult.city.substring(0,2))){
            tv_ssq.setText(personInforResult.city+personInforResult.district);
        }else {
            tv_ssq.setText(personInforResult.provinces+personInforResult.city+personInforResult.district);
        }

    }




}
