package com.example.xiaomiaibypass;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {

    private static final String TAG = "XiaoAiBypass: ";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        String pkg = lpparam.packageName;

        // 1. 嚴格作用域隔離：非目標 App 立即跳出，絕對不干擾 Android Auto / Gemini / FCM
        if (!"com.miui.personalassistant".equals(pkg) &&
            !"com.miui.voiceassist".equals(pkg) &&
            !"com.xiaomi.aicr".equals(pkg)) {
            return;
        }

        XposedBridge.log(TAG + "Injecting into target process: " + pkg);

        // 2. 精準 Hook AICR 引擎的判斷類 (解決 isInternationalDevice=true 引起的 need cta 循環)
        try {
            Class<?> deviceUtilsClass = XposedHelpers.findClassIfExists(
                "com.xiaomi.aicr.aireco.intergrator.AiCrEngine_DeviceUtils",
                lpparam.classLoader
            );
            if (deviceUtilsClass != null) {
                // 強制將 isInternationalDevice 方法的傳回值置為 false
                XposedBridge.hookAllMethods(deviceUtilsClass, "isInternationalDevice", XC_MethodReplacement.returnConstant(false));
                XposedBridge.log(TAG + "Hooked AiCrEngine_DeviceUtils.isInternationalDevice -> false");
            }
        } catch (Throwable t) {
            XposedBridge.log(TAG + "Failed to hook AiCrEngine_DeviceUtils: " + t.getMessage());
        }

        // 3. 備用 Hook：攔截 AIRC_Utils 類中的國際版判定
        try {
            Class<?> aircUtilsClass = XposedHelpers.findClassIfExists("AIRC_Utils", lpparam.classLoader);
            if (aircUtilsClass != null) {
                XposedBridge.hookAllMethods(aircUtilsClass, "isInternationalDevice", XC_MethodReplacement.returnConstant(false));
            }
        } catch (Throwable t) {
            // ignore
        }

        // 4. 強制將此進程記憶體中的 miui.os.Build.IS_INTERNATIONAL_BUILD 靜態常數改為 false
        try {
            Class<?> miuiBuildClass = XposedHelpers.findClassIfExists("miui.os.Build", lpparam.classLoader);
            if (miuiBuildClass != null) {
                XposedHelpers.setStaticBooleanField(miuiBuildClass, "IS_INTERNATIONAL_BUILD", false);
                XposedBridge.log(TAG + "Set miui.os.Build.IS_INTERNATIONAL_BUILD = false in " + pkg);
            }
        } catch (Throwable t) {
            // ignore
        }

        // 5. 局部進程 SystemProperties 攔截：僅在本進程呼叫 SystemProperties.get 時回傳 CN 屬性
        try {
            Class<?> sysPropClass = XposedHelpers.findClassIfExists("android.os.SystemProperties", lpparam.classLoader);
            if (sysPropClass != null) {
                XposedHelpers.findAndHookMethod(sysPropClass, "get", String.class, String.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        String key = (String) param.args[0];
                        if ("ro.miui.build.region".equals(key) || "ro.miui.region".equals(key)) {
                            param.setResult("CN");
                        } else if ("ro.product.mod_device".equals(key)) {
                            String val = (String) param.getResult();
                            if (val != null && val.contains("_global")) {
                                param.setResult(val.replace("_global", ""));
                            }
                        }
                    }
                });
            }
        } catch (Throwable t) {
            // ignore
        }
    }
}
