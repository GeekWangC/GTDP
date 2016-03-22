package com.glodon.easymedicalapptosvn.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Toast统一管理类
 * <p/>
 * Created by shirr on 2015/12/4.
 */
public class ToastUtils {
    /**
     * 为了能够在程序内部用同一个实例，及时更换提示内容
     */
    private static Toast mToast;
    /**
     * Toast是否显示的开关
     */
    public static boolean isShow = true;
    private static int GRAVITY = Gravity.CENTER;

    private ToastUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 取消显示Toast
     *
     * @param context
     */
    public static void cancelShow(Context context) {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, String message) {
        showNormal(context, message, Toast.LENGTH_SHORT);
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param messageId
     */
    public static void showShort(Context context, int messageId) {
        showNormal(context, messageId, Toast.LENGTH_SHORT);
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, String message) {
        showNormal(context, message, Toast.LENGTH_LONG);
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param messageId
     */
    public static void showLong(Context context, int messageId) {
        showNormal(context, messageId, Toast.LENGTH_LONG);
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void showNormal(Context context, String message, int duration) {
        if (isShow) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(context, message, duration);
            mToast.show();
        }
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param messageId
     * @param duration
     */
    public static void showNormal(Context context, int messageId, int duration) {
        if (isShow) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(context, messageId, duration);
            mToast.show();
        }
    }

    /**
     * 屏幕中央位置显示自定义时间Toast
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void showCenter(Context context, String message, int duration) {
        if (isShow) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(context, message, duration);
            mToast.setGravity(GRAVITY, 0, 0);
            mToast.show();
        }
    }

    /**
     * 屏幕中央位置显示自定义时间Toast
     *
     * @param context
     * @param messageId
     * @param duration
     */
    public static void showCenter(Context context, int messageId, int duration) {
        if (isShow) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(context, messageId, duration);
            mToast.setGravity(GRAVITY, 0, 0);
            mToast.show();
        }
    }

    /**
     * 屏幕自定义位置显示自定义时间Toast
     *
     * @param context
     * @param message
     * @param duration
     * @param xOffset
     * @param yOffset
     */
    public static void showCenter(Context context, String message, int duration, int xOffset, int yOffset) {
        if (isShow) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(context, message, duration);
            mToast.setGravity(GRAVITY, xOffset, yOffset);
            mToast.show();
        }
    }

    /**
     * 屏幕自定义位置显示自定义时间Toast
     *
     * @param context
     * @param duration
     * @param xOffset
     * @param yOffset
     */
    public static void showCenter(Context context, int textId, int duration, int xOffset, int yOffset) {
        if (isShow) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(context, textId, duration);
            mToast.setGravity(GRAVITY, xOffset, yOffset);
            mToast.show();
        }
    }

    /**
     * 显示成功的Toast
     *
     * @param context
     * @param messageId
     * @param duration
     */
    public static void showSuccess(Context context, int messageId, int duration) {
        //需成功的图标和颜色值
//        showIconToast(context, messageId, R.drawable.ic_success, R.color.holo_blue, duration);
    }

    /**
     * 显示失败的Toast
     *
     * @param context
     * @param messageId
     * @param duration
     */
    public static void showFailure(Context context, int messageId, int duration) {
        //需失败的图标和颜色值
//        showIconToast(context, messageId, R.drawable.ic_failure, R.color.warn, duration);
    }

    /**
     * 显示自定义图标的Toast
     *
     * @param context
     * @param messageId
     * @param iconId
     * @param colorId
     * @param duration
     */
    public static void showIconToast(Context context, int messageId, int iconId, int colorId, int duration) {
/*        if (isShow){
            if (mToast != null) {
                mToast.cancel();
            }
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //需自定义layout
            View _layout = inflater.inflate(R.layout.toast, null);
            ((TextView) _layout).setText(messageId);
            ((TextView) _layout).setTextColor(context.getResources().getColor(colorId));
            ((TextView) _layout).setCompoundDrawablesWithIntrinsicBounds(iconId, 0, 0, 0);
            mToast = new Toast(context);
            mToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            mToast.setDuration(duration);
            mToast.setView(_layout);
            mToast.show();
        }*/
    }
}