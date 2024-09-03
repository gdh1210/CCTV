package com.myapplication;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class Serial {
    public static void main(String[] args) {
        // 포트 목록 가져오기
        SerialPort[] ports = SerialPort.getCommPorts();
        System.out.println("Available Ports:");
        for (SerialPort port : ports) {
            System.out.println(port.getSystemPortName());
        }

        // 사용하려는 포트 선택 (예: COM8)
        SerialPort serialPort = SerialPort.getCommPort("COM8");
        serialPort.setBaudRate(9600);

        // 포트 열기
        if (serialPort.openPort()) {
            System.out.println("Port opened successfully.");
        } else {
            System.out.println("Failed to open the port.");
            return;
        }

        // 데이터 수신
        serialPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;

                byte[] newData = new byte[serialPort.bytesAvailable()];
                serialPort.readBytes(newData, newData.length);
                String receivedString = new String(newData);
                System.out.println("Received from Arduino: " + receivedString);
            }
        });

        // 데이터 전송 예제
        String message = "Hello from Java!";
        serialPort.writeBytes(message.getBytes(), message.length());

        // 일정 시간 대기 후 포트 닫기
        try {
            Thread.sleep(5000); // 5초 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 포트 닫기
        serialPort.closePort();
        System.out.println("Port closed.");
    }
}

