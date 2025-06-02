package com.hlw.demo.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hlw.demo.R;

import java.util.Date;
import java.util.List;

public class BleServerActivity extends AppCompatActivity {

    public static void start(Context context) {
        if (context == null) return;
        Intent intent = new Intent(context, BleServerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private static final String TAG = "BleServerActivity";
    private static final int REQUEST_BT_PERMISSIONS = 101;
    private static final int REQUEST_ENABLE_BT = 102;

    // UI 组件
    private TextView tvStatus, tvLog;
    private Button btnStartServer, btnStopServer, btnSendData;
    private EditText etSendData;

    // BLE 相关变量
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGattServer mGattServer;
    private BluetoothLeAdvertiser mAdvertiser;
    private boolean mIsAdvertising = false;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_server);

        // 初始化 UI 组件
        initUI();

        // 检查并请求权限
        checkPermissions();

        // 初始化蓝牙管理器
        initBluetooth();
    }

    private void initUI() {
        tvStatus = findViewById(R.id.tvStatus);
        tvLog = findViewById(R.id.tvLog);
        btnStartServer = findViewById(R.id.btnStartServer);
        btnStopServer = findViewById(R.id.btnStopServer);
        btnSendData = findViewById(R.id.btnSendData);
        etSendData = findViewById(R.id.etSendData);

        btnStartServer.setOnClickListener(v -> startServer());
        btnStopServer.setOnClickListener(v -> stopServer());
        btnSendData.setOnClickListener(v -> sendTestData());
    }

    private void initBluetooth() {
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (mBluetoothManager == null || (mBluetoothAdapter = mBluetoothManager.getAdapter()) == null) {
            showToast("设备不支持蓝牙");
            finish();
            return;
        }

        // 确保蓝牙已启用
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    private void checkPermissions() {
        String[] permissions = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION // Android 12+ 可能需要
        };

        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_BT_PERMISSIONS);
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BT_PERMISSIONS) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    showToast("权限不足，无法使用蓝牙");
                    finish();
                    return;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode != Activity.RESULT_OK) {
                showToast("必须启用蓝牙才能使用本功能");
                finish();
            }
        }
    }

    private void startServer() {
        // 创建 GATT 服务器
        mGattServer = mBluetoothManager.openGattServer(this, mGattServerCallback);
        if (mGattServer == null) {
            showToast("创建 GATT 服务器失败");
            return;
        }

        // 配置服务和特征
        setupGattService();

        // 启动广播
        startAdvertising();

        // 更新 UI 状态
        updateStatus("服务器已启动，正在广播");
        btnStartServer.setVisibility(View.GONE);
        btnStopServer.setVisibility(View.VISIBLE);
        logMessage("BLE 服务器启动成功");
    }

    private void setupGattService() {
        // 创建主服务
        BluetoothGattService service = new BluetoothGattService(
            BleConfig.SERVICE_UUID,
            BluetoothGattService.SERVICE_TYPE_PRIMARY
        );

        // 创建可读写特征（需要 MITM 配对）
        BluetoothGattCharacteristic rwChar = new BluetoothGattCharacteristic(
            BleConfig.RW_CHARACTERISTIC_UUID,
            BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_WRITE,
            BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED_MITM |
                BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED_MITM
        );

        // 创建通知特征
        BluetoothGattCharacteristic notifyChar = new BluetoothGattCharacteristic(
            BleConfig.NOTIFY_CHARACTERISTIC_UUID,
            BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
            BluetoothGattCharacteristic.PERMISSION_READ
        );

        // 添加 CCCD 描述符（允许客户端启用通知）
        BluetoothGattDescriptor cccd = new BluetoothGattDescriptor(
            BleConfig.CCCD_UUID,
            BluetoothGattDescriptor.PERMISSION_READ | BluetoothGattDescriptor.PERMISSION_WRITE
        );
        notifyChar.addDescriptor(cccd);

        // 将特征添加到服务
        service.addCharacteristic(rwChar);
        service.addCharacteristic(notifyChar);

        // 将服务添加到 GATT 服务器
        mGattServer.addService(service);
        logMessage("GATT 服务配置完成");
    }

    private void startAdvertising() {
        mAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        if (mAdvertiser == null) {
            showToast("设备不支持 BLE 广播");
            return;
        }

        AdvertiseSettings settings = new AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setConnectable(true)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
            .setTimeout(0) // 持续广播
            .build();

        AdvertiseData data = new AdvertiseData.Builder()
            .setIncludeDeviceName(true) // 显示设备名称
            .addServiceUuid(new ParcelUuid(BleConfig.SERVICE_UUID)) // 广播服务 UUID
            .build();

        mAdvertiser.startAdvertising(settings, data, mAdvertiseCallback);
        mIsAdvertising = true;
        logMessage("广播已启动");
    }

    private final AdvertiseCallback mAdvertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            mHandler.post(() -> logMessage("广播启动成功"));
        }

        @Override
        public void onStartFailure(int errorCode) {
            mHandler.post(() -> {
                logMessage("广播启动失败，错误码：" + errorCode);
                stopServer(); // 启动失败时停止服务器
            });
        }
    };

    private final BluetoothGattServerCallback mGattServerCallback = new BluetoothGattServerCallback() {
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            super.onConnectionStateChange(device, status, newState);
            mHandler.post(() -> {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    logMessage("设备连接：" + device.getName() + " (" + device.getAddress() + ")");
                    updateStatus("已连接：" + device.getName());
                    btnSendData.setVisibility(View.VISIBLE); // 连接后显示发送按钮
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    logMessage("设备断开：" + device.getName() + " (" + device.getAddress() + ")");
                    updateStatus("等待连接");
                    btnSendData.setVisibility(View.GONE); // 断开后隐藏发送按钮
                    // 重新广播以允许新连接
                    if (mGattServer != null && !mIsAdvertising) {
                        startAdvertising();
                    }
                }
            });
        }

        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
            if (characteristic.getUuid().equals(BleConfig.RW_CHARACTERISTIC_UUID)) {
                // 模拟返回当前时间
                String value = "Server Time: " + new Date().toString();
                characteristic.setValue(value.getBytes());
                mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, characteristic.getValue());
                mHandler.post(() -> logMessage("读取请求：" + device.getName() + " 读取特征 " + characteristic.getUuid()));
            }
        }

        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
            if (characteristic.getUuid().equals(BleConfig.RW_CHARACTERISTIC_UUID)) {
                String data = new String(value);
                mHandler.post(() -> {
                    logMessage("收到数据（" + device.getName() + "）：" + data);
                    if (responseNeeded) {
                        mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, null);
                    }
                    // 发送通知给客户端
                    sendNotificationToClient(device, "ACK: " + data);
                });
            }
        }

        @Override
        public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);
            if (descriptor.getUuid().equals(BleConfig.CCCD_UUID)) {
                // 处理通知订阅（客户端启用通知时）
                boolean enableNotify = (value[0] & 0x01) != 0;
                mHandler.post(() -> logMessage("客户端" + (enableNotify ? "启用" : "禁用") + "通知"));
            }

            // 处理CCCD写入（通知启用/禁用）
            if (descriptor.getUuid().equals(BleConfig.CCCD_UUID)) {
                if (value.length == 2) {
                    int flag = (value[0] & 0xFF) | ((value[1] & 0xFF) << 8);
                    boolean enableNotify = (flag & 0x0001) != 0;
                    boolean enableIndicate = (flag & 0x0002) != 0;

                    Log.i(TAG, "通知设置: Notify=" + enableNotify + ", Indicate=" + enableIndicate);

                    if (responseNeeded) {
                        mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS,
                            offset, null);
                    }
                }
            }
        }
    };


    // 向客户端发送通知
    private void sendNotificationToClient(BluetoothDevice device, String message) {
        BluetoothGattService service = mGattServer.getService(BleConfig.SERVICE_UUID);
        if (service == null) return;

        BluetoothGattCharacteristic notifyChar =
            service.getCharacteristic(BleConfig.NOTIFY_CHARACTERISTIC_UUID);
        if (notifyChar == null) return;

        notifyChar.setValue(message.getBytes());
        mGattServer.notifyCharacteristicChanged(device, notifyChar, false);
        Log.i(TAG, "已发送通知: " + message);
    }

    private void sendTestData() {
        String data = etSendData.getText().toString().trim();
        if (data.isEmpty()) {
            showToast("请输入要发送的数据");
            return;
        }

        // ❌ 旧写法（导致错误）
        // for (BluetoothDevice device : mGattServer.getConnectedDevices()) {

        // ✅ 新写法：通过 BluetoothManager 获取连接设备
        List<BluetoothDevice> connectedDevices = mBluetoothManager.getConnectedDevices(
            BluetoothProfile.GATT_SERVER // 指定为 GATT 服务器连接
        );

        for (BluetoothDevice device : connectedDevices) {
            BluetoothGattService service = mGattServer.getService(BleConfig.SERVICE_UUID);
            if (service == null) {
                logMessage("服务未找到");
                return;
            }

            BluetoothGattCharacteristic notifyChar = service.getCharacteristic(BleConfig.NOTIFY_CHARACTERISTIC_UUID);
            if (notifyChar == null) {
                logMessage("通知特征未找到");
                return;
            }

            notifyChar.setValue(data.getBytes());
            boolean success = mGattServer.notifyCharacteristicChanged(device, notifyChar, false);
            if (success) {
                logMessage("发送数据成功：" + data);
                etSendData.setText("");
            } else {
                logMessage("发送数据失败");
            }
        }
    }

    private void stopServer() {
        // 停止广播
        if (mAdvertiser != null && mIsAdvertising) {
            mAdvertiser.stopAdvertising(mAdvertiseCallback);
            mIsAdvertising = false;
        }

        // 关闭 GATT 服务器
        if (mGattServer != null) {
            mGattServer.close();
            mGattServer = null;
        }

        // 更新 UI
        updateStatus("服务器已停止");
        btnStartServer.setVisibility(View.VISIBLE);
        btnStopServer.setVisibility(View.GONE);
        btnSendData.setVisibility(View.GONE);
        logMessage("BLE 服务器已停止");
    }

    private void updateStatus(String status) {
        tvStatus.setText("状态：" + status);
    }

    private void logMessage(String message) {
        Log.i(TAG, message);
        tvLog.append(message + "\n");
    }

    private void showToast(String message) {
        Log.i(TAG, message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopServer(); // 确保销毁时停止服务器
    }
}