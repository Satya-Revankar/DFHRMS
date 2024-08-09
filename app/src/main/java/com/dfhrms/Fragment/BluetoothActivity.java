package com.dfhrms.Fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dfhrms.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {

    private static final String TAG = "BLE";
    public static final int REQUEST_ENABLE_BT = 1;
    public static final int REQUEST_BLUETOOTH_PERMISSIONS = 2;

    private BluetoothLeScanner bluetoothLeScanner;
    private List<String> messageList;
    private ArrayAdapter<String> adapter;
    private ScanCallback scanCallback;
    ListView listView;
    private boolean isScanning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

         listView = findViewById(R.id.listView);
        Button startScanButton = findViewById(R.id.startScanButton);
        Button selectall = findViewById(R.id.selectall);




        messageList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messageList);
        listView.setAdapter(adapter);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.e(TAG, "Device doesn't support Bluetooth");
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
            return;
        }

        if (BluetoothPermissionChecker.hasPermissions(this)) {
            if (!bluetoothAdapter.isEnabled()) {
                BluetoothEnabler.enableBluetooth(this);
            } else {
                startScanning(bluetoothAdapter);
            }
        } else {
            BluetoothPermissionChecker.requestPermissions(this);
        }

        startScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
                    if (isScanning) {
                        stopScanning();
                    } else {
                        startScanning(bluetoothAdapter);
                    }
                } else {
                    Toast.makeText(BluetoothActivity.this, "Bluetooth is not enabled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        selectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllItems();
            }
        });
    }

    private void showAllItems() {
        List<String> allItems = new ArrayList<>();
        for (int i = 0; i < listView.getCount(); i++) {
            allItems.add(adapter.getItem(i));
        }
        String allItemsText = allItems.toString();
        Toast.makeText(BluetoothActivity.this, allItemsText, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter != null) {
                    startScanning(bluetoothAdapter);
                }
            } else {
                Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            boolean allPermissionsGranted = true;
            StringBuilder deniedPermissions = new StringBuilder("Denied permissions:\n");

            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    deniedPermissions.append(permissions[i]).append("\n");
                }
            }

            if (allPermissionsGranted) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
                    startScanning(bluetoothAdapter);
                } else {
                    BluetoothEnabler.enableBluetooth(this);
                }
            } else {
                Toast.makeText(this, deniedPermissions.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startScanning(BluetoothAdapter bluetoothAdapter) {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth adapter is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        if (bluetoothLeScanner == null) {
            Toast.makeText(this, "Bluetooth LE Scanner is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                byte[] serviceData = result.getScanRecord().getServiceData(new ParcelUuid(UUID.fromString("0000180D-0000-1000-8000-00805F9B34FB")));
                if (serviceData != null) {
                    final String message = new String(serviceData);
                    Log.d("Message", "Message received: " + message);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!messageList.contains(message)) {
                                messageList.add(message);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(BluetoothActivity.this, "Message received: " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                boolean hasMessages = false;
                for (ScanResult result : results) {
                    byte[] serviceData = result.getScanRecord().getServiceData(new ParcelUuid(UUID.fromString("0000180D-0000-1000-8000-00805F9B34FB")));
                    if (serviceData != null) {
                        final String message = new String(serviceData);
                        if (!messageList.contains(message)) {
                            messageList.add(message);
                            hasMessages = true;
                        }
                    }
                }
                if (hasMessages) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            Toast.makeText(BluetoothActivity.this, "Messages received", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.e(TAG, "Scan failed with error code: " + errorCode);
                Toast.makeText(BluetoothActivity.this, "Scan failed with error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }
        };

        if (!bluetoothAdapter.isEnabled()) {
            BluetoothEnabler.enableBluetooth(this);
            return;
        }

        bluetoothLeScanner.startScan(scanCallback);
        isScanning = true;
        Toast.makeText(this, "Fatching Attendance", Toast.LENGTH_SHORT).show();
    }

    private void stopScanning() {
        if (bluetoothLeScanner != null && scanCallback != null) {
            bluetoothLeScanner.stopScan(scanCallback);
            isScanning = false;
            Toast.makeText(this, "Fetching stopped", Toast.LENGTH_SHORT).show();
        }
    }
}
