package components;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;


import utils.SerialPortReader;
import utils.SerialRequest;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

public class SerialManager {

    private static final int PARTTIME = 1500;

    private LinkedList<SerialRequest> requests = new LinkedList<SerialRequest>();
    private LinkedList<SerialRequest> respones = new LinkedList<SerialRequest>();

    private RS232Port comPort;
    private SerialRequest request;

    public SerialManager(RS232Port comPort) {
        this.comPort = comPort;
    }

    public void addRequest(SerialRequest request) {
        requests.push(request);
    }

    public static String reset() {
        return new String("ATZ\r");
    }

    public static String getSMS(int idx) {
        return new String("AT+CMGR=" + idx + "\r");
    }

    public static String setMemStorage(String storage) {
        return new String("AT+CPMS=" + storage + "\r");
    }

    public static String getDeviceName() {
        return new String("AT+GMM\r");
    }

    public static String setTextMode(int mode) {
        return new String("AT+CMGL=" + mode + "\r");
    }

    public static String getMessageStores() {
        return new String("AT+CPMS=?\r");
    }

    private boolean checkResult(String value) {
        boolean result = "*".equals(request.getReady());
        if (!result && (value != null) && (!value.isEmpty())) {
            value = value.toUpperCase();
            if (request.getReady() != null) {
                result = value.startsWith(request.getReady());
            }
            if (!result && (request.getFail() != null)) {
                result = value.startsWith(request.getFail());
            }
        }
        return result;
    }

    private boolean checkResponse(SerialRequest request) {
        boolean found = false;
        Iterator<String> iter = request.getResponses().iterator();
        while (iter.hasNext()) {
            found = checkResult(iter.next());
            if (found) {
                break;
            }
        }
        return found;
    }

    public void process() {
        request = requests.pollLast();
        String command = request.getCommand();
        System.out.println("[Command]: " + command);
        try {
            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;
            SerialPortReader listener = new SerialPortReader(comPort.getSerialPort(), mask);
            comPort.getSerialPort().addEventListener(listener);
            do {
                command = request.getCommand();
                comPort.addCommand(command);
                synchronized (this) {
                    try {
                        wait(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }    
                }
                request = requests.pollLast();
            } while (request != null);
        } catch (SerialPortException e) {
            e.printStackTrace();
        } finally {
            
        }
    }

    public void run() {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            String result = null;
            request = requests.pollLast();
            String command = request.getCommand();
            System.out.print("[Command]: " + command+">");
            comPort.addCommand(command);
            do {
                int waitTime = 0;
                do {
                    Date startTime = new Date();
                    result = comPort.readPort(PARTTIME);
                    Date endTime = new Date();
                    waitTime += endTime.getTime() - startTime.getTime();
                    if ((result != null)) {
                        stringBuffer.append(result);
                    }
                } while ((waitTime < request.getWaitTime()));

                StringReader stringReader = new StringReader(stringBuffer.toString());
                LineNumberReader lineNumberReader = new LineNumberReader(stringReader);
                String line = null;
                do {
                    line = lineNumberReader.readLine();
                    if (line != null) {
                        System.out.println(line);
                        request.getResponses().add(line);
                    }
                } while (line != null);

                respones.add(request);
                stringBuffer = new StringBuffer();

                request = requests.pollLast();
                if (request != null) {
                    System.out.println("[Command]: " + request.getCommand());
                    comPort.addCommand(request.getCommand());
                }

            } while (request != null);
        } catch (SerialPortException | IOException ex) {
            System.out.println(ex);
        } finally {
            try {
                comPort.close();
            } catch (SerialPortException e) {
            }
        }

    }

}
