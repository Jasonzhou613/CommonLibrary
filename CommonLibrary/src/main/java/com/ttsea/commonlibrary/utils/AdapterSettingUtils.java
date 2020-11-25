package com.ttsea.commonlibrary.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.ttsea.commonlibrary.debug.JLog;


/**
 * 跳转到app详细设置页面（如：权限设置） <br>
 * <br>
 * <b>more:</b> 更多请点 <a href="http://www.ttsea.com" target="_blank">这里</a><br>
 * <b>date:</b> 2020/6/28 17:04 <br>
 * <b>author:</b> Jason <br>
 * <b>version:</b> 1.0 <br>
 */
public class AdapterSettingUtils {
    private final static String TAG = "Utils.AdapterSettingUtils";

    /**
     * 跳转到app权限设置页面（PSP是PermissionSettingPage的缩写）
     *
     * @param context       上下文
     * @param applicationId 包名，参考 BuildConfig.APPLICATION_ID
     * @throws Exception 打开失败的时候会抛出一些异常
     */
    public static void gotoPSP(Context context, String applicationId) throws Exception {
        Intent intent = null;

        if (RomUtil.isMiui()) {//小米
            intent = getMiuiPermissionIntent(context);

        } else if (RomUtil.isFlyme()) {//魅族
            intent = getFlymePermissionIntent(applicationId);

        } else if (RomUtil.isEmui()) {//华为
            intent = getEmuiPermissionIntent();
        }

        if (intent != null) {
            try {
                context.startActivity(intent);
                //启动成功，直接返回
                return;

            } catch (Exception e) {
                //表示启动失败，这里可与主动报告给服务端
                JLog.e(TAG, "Exception e:" + e.getMessage());
            }
        }

        //以上启动失败，则默认跳转到详细设置页面
        intent = getAppDetailSettingIntent(context);
        context.startActivity(intent);
    }

    /**
     * 跳转到权限设置界面
     */
    private static Intent getAppDetailSettingIntent(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }

        return intent;
    }

    /**
     * 获取跳转到 miui 权限管理页面的Intent
     */
    private static Intent getMiuiPermissionIntent(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        ComponentName componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        intent.setComponent(componentName);
        intent.putExtra("extra_pkgname", context.getPackageName());

        return intent;
    }


    /**
     * 获取跳转到 魅族 权限管理页面的Intent
     *
     * @param applicationId 包名，参考 BuildConfig.APPLICATION_ID
     */
    private static Intent getFlymePermissionIntent(String applicationId) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("packageName", applicationId);

        return intent;
    }


    /**
     * 获取跳转到 华为 权限管理页面的Intent
     */
    private static Intent getEmuiPermissionIntent() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
        intent.setComponent(comp);

        return intent;
    }
}
