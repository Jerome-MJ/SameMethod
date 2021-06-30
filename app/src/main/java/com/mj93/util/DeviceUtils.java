package com.mj93.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * @author liumengjie
 * @version V1.0
 * @Title:
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/6/29 17:22
 */
class DeviceUtils {

    private static String VERSION_NAME = "";
    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    /**
     * 获取版本名称
     *
     * @return 当前应用的版本名称
     */
    public static synchronized String getVersionName() {
        if (TextUtils.isEmpty(VERSION_NAME)) {
            try {
                PackageManager manager = DYLibUtilsConfig.getAppCtx().getPackageManager();
                PackageInfo info = manager.getPackageInfo(DYLibUtilsConfig.getAppCtx().getPackageName(), 0);
                VERSION_NAME = info.versionName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return VERSION_NAME;
    }



}
