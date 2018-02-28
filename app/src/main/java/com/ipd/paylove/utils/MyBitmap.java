package com.ipd.paylove.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Display;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyBitmap {

    private static MyBitmap instance;

    public MyBitmap() {
    }

    public static MyBitmap getInstance() {
        if (instance == null) {
            instance = new MyBitmap();
        }
        return instance;
    }

    public Bitmap reSizeBitmap(Activity a, File file) {
        Display display = a.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
                options);
        options.inJustDecodeBounds = false;
        int bitmapHeight = options.outHeight;
        int bitmapWidth = options.outWidth;

        if (bitmapHeight > height || bitmapWidth > width) {
            int scaleX = bitmapWidth / width;
            int scaleY = bitmapHeight / height;
            if (scaleX > scaleY) {// 按照水平方向的比例缩放
                options.inSampleSize = scaleX;
            } else {// 按照竖直方向的比例缩放
                options.inSampleSize = scaleY;
            }
        } else {// 如果图片比手机屏幕小 不去缩放了.
            options.inSampleSize = 1;
        }
        return bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
                options);
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    private boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public Bitmap reSizeBitmap(Activity a, Uri uri) {
        Display display = a.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        String path = getPath(a, uri);
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        options.inJustDecodeBounds = false;
        int bitmapHeight = options.outHeight;
        int bitmapWidth = options.outWidth;

        if (bitmapHeight > height || bitmapWidth > width) {
            int scaleX = bitmapWidth / width;
            int scaleY = bitmapHeight / height;
            if (scaleX > scaleY) {// 按照水平方向的比例缩放
                options.inSampleSize = scaleX;
            } else {// 按照竖直方向的比例缩放
                options.inSampleSize = scaleY;
            }
        } else {// 如果图片比手机屏幕小 不去缩放了.
            options.inSampleSize = 1;
        }
        return bitmap = BitmapFactory.decodeFile(path, options);
    }

    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * 上传图片文件的时候压缩
     * @param bmp
     * @param file
     */
    public static void compressBmpToFile(Bitmap bmp,String file){
//		File destDir = new File(appHome);
//		if (!destDir.exists()) {
//			destDir.mkdirs();
//		}
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;//个人喜欢从80开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***
     * 压缩bitmap
     * @param
     * @return
     */
    public static Bitmap LoadBigImg(Bitmap bitmap) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        byte[] bs=Bitmap2Bytes(bitmap);
        BitmapFactory.decodeByteArray(bs,0,bs.length);
        int bitmapWidth = options.outWidth;
        int bitmapHeight = options.outHeight;
        int scale = Math.max(bitmapWidth / 640, bitmapHeight / 640);
        // 缩放的比例
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bs,0,bs.length);
    }
    /**
     *
     * 加载大图片
     *
     *
     * @param newWidth
     *            指定分辨率
     * @param newHeight
     * @return
     */
    public static Bitmap LoadBigImg(String path, int newWidth, int newHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        int bitmapWidth = options.outWidth;
        int bitmapHeight = options.outHeight;
        int scale;

        scale = Math.max(bitmapWidth / newWidth, bitmapHeight / newHeight);

        // 缩放的比例
        options.inSampleSize = scale;

        options.inJustDecodeBounds = false;
        // 摆正
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        int degree = getExifOrientation(path);
        if (degree == 90 || degree == 180 || degree == 270) {
            // Roate preview icon according to exif orientation
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else {
            // do not need roate the icon,default
            return bitmap;
        }

    }



    /**
     * 获取图片的朝向
     *
     * @param filepath
     * @return
     */
    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            // MmsLog.e(ISMS_TAG, "getExifOrientation():", ex);
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognize a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                    default:
                        break;
                }
            }
        }

        return degree;
    }
    /**
     * bitmap转byte数组
     *
     * @param bm
     * @return
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    /**
     * 根据uri获取bitmap
     * @param context
     * @param uri
     * @return
     */
    public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        if (context == null || uri == null)
            return null;
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * Drawable 转 bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    /**
     * 存的本地
     *
     * @return
     */
    public static File saveBitmap(final Bitmap bitmap) {
        final File f = new File(Environment.getExternalStorageDirectory()
                + "/education_img.PNG");
        if (f.exists()) {
            f.delete();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//				int options = 80;//个人喜欢从80开始,
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
//				while (baos.toByteArray().length / 1024 > 240) {
//					baos.reset();
//					options -= 10;
//					bitmap.compress(Bitmap.CompressFormat.PNG, options, baos);
//				}
        try {
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    }

    public static File saveBitmap(final Bitmap bitmap,final String file){
        final File f = new File(Environment.getExternalStorageDirectory()+"/"+file);
        if (f.exists()) {
            f.delete();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,90, baos);
        try {
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

}
