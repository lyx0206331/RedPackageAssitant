package com.adrian.redpackageassitant;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.adrian.redpackageassitant.tools.CommUtil;
import com.adrian.redpackageassitant.tools.LogUtil;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Setting;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button mBootBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBootBtn = findViewById(R.id.boot_btn);

        requestPermission(Permission.READ_PHONE_STATE);

        mBootBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOnAccessibilityService();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isAccessibilitySettingsOn(this)) {
            mBootBtn.setText("已开启");
        } else {
            mBootBtn.setText("未开启");
        }
    }

    private void turnOnAccessibilityService() {
//        if (!isAccessibilitySettingsOn(getApplicationContext())) {
        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
//        }
    }

    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = getPackageName() + "/" + RedPackageService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            LogUtil.LogE(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            LogUtil.LogE(TAG, "Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            LogUtil.LogE(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    LogUtil.LogE(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        LogUtil.LogE(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            LogUtil.LogE(TAG, "***ACCESSIBILITY IS DISABLED***");
        }

        return false;
    }

    /**
     * Request permissions.
     */
    private void requestPermission(String... permissions) {
        AndPermission.with(this)
                .runtime()
                .permission(permissions)
                .rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        toast(R.string.successfully);
                        MyApplication.getInstance().setImei(CommUtil.getIMEI(MainActivity.this));
                        Log.e("MAIN", "IMEI:" + MyApplication.getInstance().getImei());
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        toast(R.string.failure);
                        if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, permissions)) {
                            showSettingDialog(MainActivity.this, permissions);
                        }
                    }
                })
                .start();
    }

    /**
     * Display setting dialog.
     */
    public void showSettingDialog(Context context, final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames));

        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPermission();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    /**
     * Set permissions.
     */
    private void setPermission() {
        AndPermission.with(this)
                .runtime()
                .setting()
                .onComeback(new Setting.Action() {
                    @Override
                    public void onAction() {
                        Toast.makeText(MainActivity.this, R.string.message_setting_comeback, Toast.LENGTH_SHORT).show();
                    }
                })
                .start();
    }

    protected void toast(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
