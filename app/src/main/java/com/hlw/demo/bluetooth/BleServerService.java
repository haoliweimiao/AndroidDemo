package com.hlw.demo.bluetooth;

import android.app.Service;
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
import android.os.IBinder;
import android.os.ParcelUuid;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Date;

public class BleServerService extends Service {

    public static void start(Context context) {
        if (context == null) return;
        Intent intent = new Intent(context, BleServerService.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(intent);
        Log.i(TAG, "开始启动服务");
    }

    private static final String TAG = "BleServerService";
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGattServer mGattServer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "BleServerService 服务启动");
        initBleServer();
    }

    private void initBleServer() {
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        // 检查BLE支持
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Log.e(TAG, "蓝牙未启用或不支持BLE");
            return;
        }

        // 创建GATT服务器
        try {
            mGattServer = mBluetoothManager.openGattServer(this, mGattServerCallback);
            setupGattService();
            startAdvertising();
        } catch (Exception e) {
            Log.e(TAG, "创建GATT服务器失败: " + e.getMessage());
        }
    }

    // 设置GATT服务
    private void setupGattService() {
        // 创建主服务
        BluetoothGattService service = new BluetoothGattService(
            BleConfig.SERVICE_UUID,
            BluetoothGattService.SERVICE_TYPE_PRIMARY);

        // 创建可读写特征（需要MITM配对）
        BluetoothGattCharacteristic rwChar = new BluetoothGattCharacteristic(
            BleConfig.RW_CHARACTERISTIC_UUID,
            BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_WRITE,
            BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED_MITM |
                BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED_MITM);

        // 创建通知特征
        BluetoothGattCharacteristic notifyChar = new BluetoothGattCharacteristic(
            BleConfig.NOTIFY_CHARACTERISTIC_UUID,
            BluetoothGattCharacteristic.PROPERTY_READ |
                BluetoothGattCharacteristic.PROPERTY_NOTIFY,
            BluetoothGattCharacteristic.PERMISSION_READ);

        // 添加CCCD描述符到通知特征
        BluetoothGattDescriptor cccd = new BluetoothGattDescriptor(
            BleConfig.CCCD_UUID,
            BluetoothGattDescriptor.PERMISSION_READ | BluetoothGattDescriptor.PERMISSION_WRITE);
        notifyChar.addDescriptor(cccd);

        // 添加特征到服务
        service.addCharacteristic(rwChar);
        service.addCharacteristic(notifyChar);

        // 添加服务到GATT服务器
        mGattServer.addService(service);
        Log.i(TAG, "GATT服务创建完成");
    }

    // 开始广播
    private void startAdvertising() {
        BluetoothLeAdvertiser advertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        if (advertiser == null) {
            Log.e(TAG, "设备不支持BLE广播");
            return;
        }

        AdvertiseSettings settings = new AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setConnectable(true)
            .setTimeout(0)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
            .build();

        AdvertiseData data = new AdvertiseData.Builder()
            .setIncludeDeviceName(true)
            .addServiceUuid(new ParcelUuid(BleConfig.SERVICE_UUID))
            .build();

        advertiser.startAdvertising(settings, data, mAdvertiseCallback);
    }

    private final AdvertiseCallback mAdvertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            Log.i(TAG, "BLE广播已启动");
        }

        @Override
        public void onStartFailure(int errorCode) {
            Log.e(TAG, "启动广播失败，错误码: " + errorCode);
        }
    };

    // GATT服务器回调
    private final BluetoothGattServerCallback mGattServerCallback = new BluetoothGattServerCallback() {
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            super.onConnectionStateChange(device, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i(TAG, "设备已连接: " + device.getName());
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i(TAG, "设备已断开: " + device.getName());
                // 可以尝试重新广播
                startAdvertising();
            }
        }

        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId,
                                                int offset, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicReadRequest(device, requestId, offset, characteristic);

            // 处理特征读取请求
            if (characteristic.getUuid().equals(BleConfig.RW_CHARACTERISTIC_UUID)) {
                // 返回当前时间作为示例数据
                String value = "Time: " + new Date().toString();
                characteristic.setValue(value.getBytes());
                mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS,
                    offset, characteristic.getValue());
                Log.i(TAG, "已响应读取请求");
            }
        }

        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId,
                                                 BluetoothGattCharacteristic characteristic, boolean preparedWrite,
                                                 boolean responseNeeded, int offset, byte[] value) {
            super.onCharacteristicWriteRequest(device, requestId, characteristic,
                preparedWrite, responseNeeded, offset, value);

            // 处理特征写入请求
            if (characteristic.getUuid().equals(BleConfig.RW_CHARACTERISTIC_UUID)) {
                String received = new String(value);
                Log.i(TAG, "收到客户端数据: " + received);

                if (responseNeeded) {
                    mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS,
                        offset, null);
                }

                // 发送通知给客户端
                sendNotificationToClient(device, "ACK: " + received);
            }
        }

        @Override
        public void onDescriptorWriteRequest(BluetoothDevice device, int requestId,
                                             BluetoothGattDescriptor descriptor, boolean preparedWrite,
                                             boolean responseNeeded, int offset, byte[] value) {
            super.onDescriptorWriteRequest(device, requestId, descriptor,
                preparedWrite, responseNeeded, offset, value);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopServer();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void stopServer() {
        if (mGattServer != null) {
            mGattServer.close();
            mGattServer = null;
        }

        if (mBluetoothAdapter != null) {
            BluetoothLeAdvertiser advertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
            if (advertiser != null && mAdvertiseCallback != null) {
                advertiser.stopAdvertising(mAdvertiseCallback);
            }
        }
        Log.i(TAG, "BLE服务已停止");
    }
}
