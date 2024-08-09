package com.dfhrms.Fragment;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class BluetoothEnabler {

    public static void enableBluetooth(AppCompatActivity activity) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, BluetoothActivity.REQUEST_ENABLE_BT);
        }
    }
}
