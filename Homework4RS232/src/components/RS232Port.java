package components;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import utils.ArrayUtils;
import utils.SerialPortReader;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import jssc.SerialPortTimeoutException;

public class RS232Port {

    private SerialPort serialPort;

    public static final String AT = "AT\r";
    public static final String MANUFACTURER = "AT+CGMM\r";
    public static final String MODELNO = "AT+CGMM\r";
    public static final String IMEI = "AT+CGSN\r";
    public static final String SOFTWARE_VERSION = "AT+CGMR\r";
    public static final String GETMEMORYSTORES = "AT+CPMS=?\r";
    public static final String SETMEMORYSTORES = "AT+CPMS=\"ME\",\"ME\",\"ME\"\r";
    public static final String GETSMS = "AT+CMGR=-idx-\r";
    public static final String LISTSMS = "AT+CMGL=\"ALL\"\r";


    public RS232Port(String portName) throws SerialPortException {
        serialPort = new SerialPort(portName);
        serialPort.openPort();
        serialPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
    }
    
    public SerialPort getSerialPort() {
        return serialPort;
    }
    
    public void addListener(SerialPortReader listener) {
        try {
            serialPort.addEventListener(listener);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    public void addCommand(String command) throws SerialPortException {
        serialPort.writeBytes(command.getBytes());
    }

    public static void enumeratePorts() {
        String[] portNames = SerialPortList.getPortNames();
        for (int i = 0; i < portNames.length; i++) {
            System.out.println(portNames[i]);
        }
    }

    public String readPort(int waitTime) throws IOException, SerialPortException {
        String result = null;
        byte[] buffer = {};
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        try {
            int count = serialPort.getInputBufferBytesCount();
            buffer = serialPort.readBytes(count > 0 ? count : 1, waitTime);
            if (buffer.length>0) {
                bOut.write(buffer);
                result = bOut.toString();
            }
        } catch (SerialPortTimeoutException ex) {
            bOut.write(buffer);
        }
        return result;
    }

    public void close() throws SerialPortException {
        if (serialPort.isOpened()) {
            serialPort.closePort();
        }
    }
}
