package com.hlw.library.usb;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * USB Utils
 *
 * @author Von
 */
public class UsbUtils {

    /**
     * @param appContext application
     */
    public static void detectUsbDeviceWithUsbManager(Context appContext) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            HashMap<String, UsbDevice> deviceHashMap = ((UsbManager)
                    appContext.getSystemService(Context.USB_SERVICE)).getDeviceList();
            for (Map.Entry<String, UsbDevice> entry : deviceHashMap.entrySet()) {
                UsbDevice device = entry.getValue();
                Log.d("usb device", String.format("usb device pid:%s vid:%s", device.getProductId(), device.getVendorId()));
            }
        }
    }
}
