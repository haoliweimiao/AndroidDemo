package com.hlw.demo.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hlw.demo.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BleClientActivity extends AppCompatActivity {

    public static void start(Context context) {
        if (context == null) return;
        Intent intent = new Intent(context, BleClientActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private static final String TAG = "BleClientActivity";
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private BluetoothGatt mBluetoothGatt;
    private Handler mHandler = new Handler();

    // 与服务端相同的UUID
    private static final UUID SERVICE_UUID = UUID.fromString("0000ABCD-0000-1000-8000-00805F9B34FB");
    private static final UUID RW_CHARACTERISTIC_UUID = UUID.fromString("0000BEEF-0000-1000-8000-00805F9B34FB");
    private static final UUID NOTIFY_CHARACTERISTIC_UUID = UUID.fromString("0000DEAD-0000-1000-8000-00805F9B34FB");
    private static final UUID CCCD_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    private Button btnScan, btnConnect, btnDisconnect, btnSend;
    private TextView tvStatus, tvMessages;
    private String mDeviceAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_client);

        // 初始化UI组件
        btnScan = findViewById(R.id.btnScan);
        btnConnect = findViewById(R.id.btnConnect);
        btnDisconnect = findViewById(R.id.btnDisconnect);
        btnSend = findViewById(R.id.btnSend);
        tvStatus = findViewById(R.id.tvStatus);
        tvMessages = findViewById(R.id.tvMessages);

        // 初始化BLE适配器
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // 检查权限
        checkPermissions();

        // 设置按钮事件
        btnScan.setOnClickListener(v -> scanLeDevice(true));
        btnConnect.setOnClickListener(v -> connectToDevice(mDeviceAddress));
        btnDisconnect.setOnClickListener(v -> disconnectDevice());
        btnSend.setOnClickListener(v -> sendDataToServer("Hello from Client!"));

        // 初始状态
        updateStatus("准备扫描设备");
    }

    // 检查并请求必要权限
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                }, 1);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            }
        }
    }

    // 扫描BLE设备
    private void scanLeDevice(boolean enable) {
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            updateStatus("蓝牙未启用");
            return;
        }

        if (enable) {
            updateStatus("扫描中...");
            mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

            // 设置扫描参数
            ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();

            // 过滤服务UUID
            List<ScanFilter> filters = new ArrayList<>();
            filters.add(new ScanFilter.Builder()
                .setServiceUuid(new ParcelUuid(SERVICE_UUID))
                .build());

            // 开始扫描
            mBluetoothLeScanner.startScan(filters, settings, mScanCallback);

            // 10秒后停止扫描
            mHandler.postDelayed(() -> scanLeDevice(false), 10000);
        } else {
            if (mBluetoothLeScanner != null) {
                mBluetoothLeScanner.stopScan(mScanCallback);
                updateStatus("扫描已停止");
            }
        }
    }

    // 扫描回调
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice device = result.getDevice();
            if (device != null) {
                mDeviceAddress = device.getAddress();
                updateStatus("发现设备: " + device.getName());
                // 自动连接
                connectToDevice(mDeviceAddress);
                // 停止扫描
                scanLeDevice(false);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            updateStatus("扫描失败，错误码: " + errorCode);
        }
    };

    // 连接到设备
    private void connectToDevice(String deviceAddress) {
        if (deviceAddress == null) {
            updateStatus("未发现设备");
            return;
        }

        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(deviceAddress);
        if (device == null) {
            updateStatus("设备不可用");
            return;
        }

        updateStatus("连接中...");
        // 使用TRANSPORT_LE自动选择BLE传输
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mBluetoothGatt = device.connectGatt(this, false, mGattCallback, BluetoothDevice.TRANSPORT_LE);
        } else {
            mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        }
    }

    // GATT客户端回调
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                updateStatus("已连接");
                // 发现服务
                runOnUiThread(() -> {
                    mBluetoothGatt.discoverServices();
                    updateStatus("发现服务中...");
                });
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                updateStatus("已断开");
                // 尝试重新连接
                // reconnect();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                updateStatus("服务已发现");
                // 设置通知
                setupCharacteristicNotification();
                checkGattPermission(gatt);
//                checkGattPermissionWithReflection(gatt);
                // 读取特征值
                readCharacteristic();
            } else {
                updateStatus("服务发现失败: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                String value = new String(characteristic.getValue());
                addMessage("读取: " + value);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                addMessage("写入成功");
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            // 收到通知
            String value = new String(characteristic.getValue());
            addMessage("通知: " + value);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (descriptor.getUuid().equals(CCCD_UUID)) {
                    addMessage("通知已启用");
                }
            }
        }

        private void checkGattPermission(BluetoothGatt gatt) {
            Log.i(TAG, "start checkGattPermission");
            // 遍历所有服务和特征，检查其权限
            List<BluetoothGattService> services = gatt.getServices();
            for (BluetoothGattService service : services) {
                List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                for (BluetoothGattCharacteristic characteristic : characteristics) {
                    int properties = characteristic.getProperties();
                    int permissions = characteristic.getPermissions();
                    Log.i(TAG, "Characteristic " + characteristic.getUuid() + " permissions:" + permissions);
                    // 检查特征是否需要ENCRYPTED_MITM权限
                    if ((permissions & BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED_MITM) != 0 ||
                        (permissions & BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED_MITM) != 0) {
                        Log.i(TAG, "Characteristic " + characteristic.getUuid() + " requires ENCRYPTED_MITM");
                        // 此时可以判断服务端具有ENCRYPTED_MITM权限
                        // 你可以在这里决定是否需要发起配对
                    }
                }
            }
        }


//        private void checkGattPermissionWithReflection(BluetoothGatt gatt) {
//            Log.i(TAG, "start checkGattPermissionWithReflection");
//            List<BluetoothGattService> services = gatt.getServices();
//            for (BluetoothGattService service : services) {
//                List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
//                for (BluetoothGattCharacteristic characteristic : characteristics) {
//                    int properties = characteristic.getProperties();
//                    int permissions = characteristic.getPermissions();
//
//                    Log.i(TAG, "Characteristic " + characteristic.getUuid() +
//                        " standard permissions:" + permissions);
//
//                    // 尝试使用反射获取更多信息
//                    try {
//                        Field field = characteristic.getClass().getDeclaredField("mDescriptorPermissions");
//                        field.setAccessible(true);
//                        int descriptorPermissions = (int) field.get(characteristic);
//
//                        Log.i(TAG, "Characteristic " + characteristic.getUuid() +
//                            " descriptor permissions:" + descriptorPermissions);
//                    } catch (Exception e) {
//                        Log.e(TAG, "Failed to get descriptor permissions via reflection", e);
//                    }
//
//                    // 检查标准权限
//                    if ((permissions & BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED_MITM) != 0 ||
//                        (permissions & BluetoothGattCharacteristic.PERMISSION_WRITE_ENCRYPTED_MITM) != 0) {
//                        Log.i(TAG, "Characteristic " + characteristic.getUuid() + " requires ENCRYPTED_MITM");
//                    }
//                }
//            }
//        }
    };

    // 设置特征通知
    private void setupCharacteristicNotification() {
        BluetoothGattService service = mBluetoothGatt.getService(SERVICE_UUID);
        if (service == null) {
            updateStatus("服务未找到");
            return;
        }

        BluetoothGattCharacteristic notifyChar =
            service.getCharacteristic(NOTIFY_CHARACTERISTIC_UUID);
        if (notifyChar == null) {
            updateStatus("通知特征未找到");
            return;
        }

        // 启用特征通知
        mBluetoothGatt.setCharacteristicNotification(notifyChar, true);

        // 获取并写入CCCD描述符以启用通知
        BluetoothGattDescriptor descriptor = notifyChar.getDescriptor(CCCD_UUID);
        if (descriptor != null) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    // 读取特征值
    private void readCharacteristic() {
        BluetoothGattService service = mBluetoothGatt.getService(SERVICE_UUID);
        if (service == null) return;

        BluetoothGattCharacteristic charRead =
            service.getCharacteristic(RW_CHARACTERISTIC_UUID);
        if (charRead != null) {
            mBluetoothGatt.readCharacteristic(charRead);
        }
    }

    // 发送数据到服务端
    private void sendDataToServer(String message) {
        if (mBluetoothGatt == null) {
            updateStatus("未连接");
            return;
        }

        BluetoothGattService service = mBluetoothGatt.getService(SERVICE_UUID);
        if (service == null) return;

        BluetoothGattCharacteristic charWrite =
            service.getCharacteristic(RW_CHARACTERISTIC_UUID);
        if (charWrite != null) {
            charWrite.setValue(message.getBytes());
            mBluetoothGatt.writeCharacteristic(charWrite);
            addMessage("发送: " + message);
        }
    }

    // 断开连接
    private void disconnectDevice() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
            updateStatus("断开中...");
        }
    }

    // 重连
    private void reconnect() {
        if (mDeviceAddress != null) {
            connectToDevice(mDeviceAddress);
        }
    }

    // 清理资源
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
    }

    // UI更新方法
    private void updateStatus(String message) {
        Log.i(TAG, message);
        runOnUiThread(() -> tvStatus.setText(message));
    }

    private void addMessage(String message) {
        Log.i(TAG, message);
        runOnUiThread(() -> tvMessages.append(message + "\n"));
    }
}