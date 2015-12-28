/**
 * 
 */
package tasks;

import java.io.IOException;
import java.util.Date;

import components.RS232Port;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

/**
 * @author pzoli
 *
 */
public class SimpleModemTask {

    public static void read(SerialPort serialPort) throws SerialPortException, IOException {
        int count = 0;
        int waitTime = 10000;
        int totalWaitTime = 0;
        

        while (totalWaitTime < waitTime) {
            count = serialPort.getInputBufferBytesCount();
            byte[] buffer = {};
            Date startTime = new Date();
            try {
                buffer = serialPort.readBytes(count > 0 ? count : 1, 100);
            } catch (SerialPortTimeoutException ex) {

            }
            Date endTime = new Date();
            totalWaitTime += endTime.getTime() - startTime.getTime();
            if (buffer.length > 0) {
                System.out.write(buffer);
            }
        }
    }
    
    public static void run() throws SerialPortException {
        try {
            SerialPort serialPort = new SerialPort("COM31");
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            //serialPort.writeString(RS232Port.SETMEMORYSTORES);
            //read(serialPort);
            String command = RS232Port.GETSMS.replaceAll("-idx-", "5");
            //String command = RS232Port.GETMEMORYSTORES;
            serialPort.writeString(command);
            read(serialPort);
            
            serialPort.closePort();
        } catch (IOException ex) {

        }

    }
}
