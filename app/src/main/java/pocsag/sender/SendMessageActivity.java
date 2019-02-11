package pocsag.sender;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

public class SendMessageActivity extends AppCompatActivity {


    private static final String deviceName = "pocsag_converter";
    private static BluetoothDevice device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String number = Objects.requireNonNull(getIntent().getExtras()).getString("number");
        final String message = Objects.requireNonNull(getIntent().getExtras()).getString("message");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String repeatVal = sharedPreferences.getString("repeat_val", "3");
        String sourceVal = sharedPreferences.getString("source_list", "0");
        Intent myIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.getLong("id", Objects.requireNonNull(getIntent().getExtras()).getLong("id"));
        bundle.putString("message", message);
        myIntent.putExtras(bundle);
        device = findDevice();
        if (device == null) {
            setResult(RESULT_CANCELED, myIntent);
            finish();
            return;
        }
        ParcelUuid[] uuids = device.getUuids();
        if (uuids.length < 1) {
            setResult(RESULT_CANCELED, myIntent);
            finish();
            return;
        }
        try {
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
            socket.connect();
            socket.getOutputStream().write(String.format("%s %s %s %s", number, sourceVal, repeatVal, message).getBytes());
            setResult(RESULT_OK, myIntent);
            finish();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    public BluetoothDevice findDevice() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Device doesnt Support Bluetooth", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        BluetoothDevice device = null;
        if (bondedDevices.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Pair the Device first", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            for (BluetoothDevice iterator : bondedDevices) {
                if (iterator.getName().equals(deviceName)) {
                    return device;
                }
            }
        }
        Toast.makeText(getApplicationContext(), String.format("Device with name %s not found", deviceName), Toast.LENGTH_SHORT).show();
        return null;
    }

}
