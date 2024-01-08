package com.example.cobabluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Button tombol_on, tombol_pair;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    ListView lv;
    TextView perangkat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tombol_on = findViewById(R.id.Button_On);
        tombol_pair = findViewById(R.id.Button_Pair);
        perangkat = findViewById(R.id.Perangkat);
        BA = BluetoothAdapter.getDefaultAdapter();
        lv = (ListView)findViewById(R.id.listView);
        System.out.println("berhasil");
        tombol_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                on_bluetooth();
            }
        });
        tombol_pair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilkan();
            }
        });
    }

    public void on_bluetooth()
    {
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turned on",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Already on", Toast.LENGTH_LONG).show();
        }
    }

    public void tampilkan()
    {
        pairedDevices = BA.getBondedDevices();

        ArrayList list = new ArrayList();
        ArrayList alamat = new ArrayList();

        for(BluetoothDevice bt : pairedDevices)
        {
            list.add(bt.getName());
            alamat.add(bt.getAddress());
        }
        Toast.makeText(getApplicationContext(), "Showing Paired Devices",Toast.LENGTH_SHORT).show();

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDevice = (String) alamat.get(position);
                perangkat.setText(selectedDevice);
                Intent i = new Intent(MainActivity.this, PengirimanData.class);
                i.putExtra("Device",selectedDevice);
                startActivity(i);
            }
        });
    }
}