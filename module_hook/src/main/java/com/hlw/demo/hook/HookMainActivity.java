package com.hlw.demo.hook;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

import dalvik.system.DexClassLoader;

public class HookMainActivity extends AppCompatActivity {

    // 这个方法比onCreate调用早; 在这里Hook比较好.
    @Override
    protected void attachBaseContext(Context newBase) {
        HookHelper.hookActivityManager();
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        Button tv = new Button(this);
        tv.setText("测试界面");

        setContentView(tv);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hookAMS();

                hookApkMethod();

                hookResource();
            }
        });

        requestSDCardPermission();
    }

    private void hookResource() {
        DexClassLoader dexClassLoader = createDexClassLoader();
        // 修改test_hook_string文本内容，打包推送至APK_PATH，再修改test_hook_string文本内容，运行APK，得到的是第一次打包的文本
        // 说明resource已经被重定向了
        ChangeApkContextWrapper contextWrapper = new ChangeApkContextWrapper(this, APK_PATH, dexClassLoader);
        String msg = contextWrapper.getResources().getString(R.string.test_hook_string);
        Log.i("!!!!!!", msg);
    }

    private void hookAMS() {
        // 测试AMS HOOK (调用其相关方法)
        Uri uri = Uri.parse("http://www.baidu.com");
        Intent t = new Intent(Intent.ACTION_VIEW);
        t.setData(uri);
        startActivity(t);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void requestSDCardPermission() {
        String[] permission = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> needRequestPermission = new ArrayList<>();
            for (String per : permission) {
                if (ActivityCompat.checkSelfPermission(this, permission[0]) != 0) {
                    needRequestPermission.add(per);
                }
            }
            if (needRequestPermission.size() > 0)
                ActivityCompat.requestPermissions(this, permission, 99);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                startActivity(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION));
            }
        }
    }

    private static final String WHITE_LIST_CLASS_NAME = "com.hlw.demo.hook.HookConstants";
    private static final String WHITE_LIST_FIELD_NAME = "mHookStrings";

    private void hookApkMethod() {
        DexClassLoader dexClassLoader = createDexClassLoader();

        String[] mHookStrings = null;
        try {
            Class<?> whiteListClass = dexClassLoader.loadClass(WHITE_LIST_CLASS_NAME);
            Field whiteListField = whiteListClass.getDeclaredField(WHITE_LIST_FIELD_NAME);
            Object o = whiteListField.get(null);
            mHookStrings = (String[]) o;
            for (String test : mHookStrings) {
                Log.e("!!!!!!!!", "test " + test);
            }
        } catch (ClassNotFoundException ignored) {
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String APK_PATH = "/sdcard/hook.apk";

    private DexClassLoader createDexClassLoader() {
        File root = new File(getApplication().getFilesDir(), "ManagerImplLoader");
        File odexDir = new File(root, "10086");
        odexDir.mkdirs();
        // 需要把 module_hook 打包，并推送到 /sdcard/hook.apk
        DexClassLoader dexClassLoader = new DexClassLoader(
                APK_PATH,
                odexDir.getAbsolutePath(),
                null,
                getClass().getClassLoader()
        );
        return dexClassLoader;
    }
}