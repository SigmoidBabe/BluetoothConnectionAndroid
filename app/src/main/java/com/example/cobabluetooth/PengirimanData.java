package com.example.cobabluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class PengirimanData extends AppCompatActivity {
    private BluetoothAdapter BA;
    String device;
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    Thread thread;
    InputStream inputStream;
    BluetoothSocket btSocket;
    DataInputStream input;
    char[] ptk_array = new char[10];
    char[] bb_array = new char[10];
    Button input_nilai;
    TextView berat_badan, PTK;
    int a, b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengiriman_data2);
        input_nilai = findViewById(R.id.input_bb);
        berat_badan = findViewById(R.id.berat_badan);
        PTK = findViewById(R.id.ptk);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            device = extras.getString("Device");
            //The key argument here must match that used in the other activity
        }
        BA = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice hc05 = BA.getRemoteDevice(device);
        btSocket = null;
        int counter = 0;
        do {
            try {
                btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
                System.out.println(btSocket);
                btSocket.connect();
                System.out.println(btSocket.isConnected());
            } catch (IOException e) {
                e.printStackTrace();
            }
            counter++;
        } while (!btSocket.isConnected() && counter<3);
        inputStream = null;
        input_nilai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request_nilai();
                ambil_nilai();
            }
        });

/*
        try {
            btSocket.close();
            System.out.println("sudah");
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }
    public void request_nilai()
    {
        try {
            OutputStream outputStream = btSocket.getOutputStream();
            outputStream.write(2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void ambil_nilai()
    {
        try{
            inputStream = btSocket.getInputStream();
            inputStream.skip(inputStream.available());
            char karakter;
            for (int i = 0; i < 10; i++) {
                karakter = (char) inputStream.read();
                if(karakter==';')
                {
                    a = i;
                    break;
                }
                ptk_array[i] = karakter;
            }
            for (int i = 0; i < 10; i++) {
                karakter = (char) inputStream.read();
                if(karakter==';')
                {
                    b = i;
                    break;
                }
                bb_array[i] = karakter;
            }
            char[] ptk_array2 = new char[a];
            char[] bb_array2 = new char[b];
            for (int i = 0; i < a; i++)
            {
                ptk_array2[i] = ptk_array[i];
            }
            for (int i = 0; i < b; i++)
            {
                bb_array2[i] = bb_array[i];
            }
            String nilai_ptk= String.valueOf(ptk_array2);
            String nilai_bb = String.valueOf(bb_array2);
            System.out.println(nilai_ptk);
            System.out.println(nilai_bb);
            //float f_ptk = Float.parseFloat(nilai_ptk);
            //float f_bb = Float.parseFloat(nilai_bb);
            //float b = (float)inputStream.read();
            //int a = Byte.toUnsignedInt(b);
            //PTK.setText(nilai_ptk);
            //berat_badan.setText(nilai_bb);
        }
        catch (IOException e) { e.printStackTrace();}
    }
}