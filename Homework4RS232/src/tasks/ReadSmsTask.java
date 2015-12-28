/**
 * 
 */
package tasks;

import jssc.SerialPortException;
import utils.SerialRequest;
import components.RS232Port;
import components.SerialManager;

/**
 * @author pzoli
 *
 */
public class ReadSmsTask {

    public static void sendATCommands() throws SerialPortException {
        // RS232PortReader.enumeratePorts();
        RS232Port comPort = new RS232Port("COM31");

        SerialManager sm = new SerialManager(comPort);
        
        SerialRequest request00 = new SerialRequest();
        request00.setReady("OK");
        request00.setCommand(SerialManager.reset());
        sm.addRequest(request00);
        
        SerialRequest request02 = new SerialRequest();
        request02.setReady("OK");
        request02.setCommand(SerialManager.getMessageStores());
        sm.addRequest(request02);

        SerialRequest request03 = new SerialRequest();
        request03.setCommand(SerialManager.setMemStorage("\"ME\",\"ME\",\"ME\""));
        request03.setReady("OK");
        request03.setWaitTime(10000);
        request03.setFail("ERROR");
       sm.addRequest(request03);

        SerialRequest request04 = new SerialRequest();
        request04.setCommand(SerialManager.getDeviceName());
        request04.setReady("OK");
        sm.addRequest(request04);

        SerialRequest request05 = new SerialRequest();
        request05.setCommand(SerialManager.getSMS(1));
        request05.setWaitTime(10000);
        request05.setReady("OK");
        request05.setFail("ERROR");
        sm.addRequest(request05);

        sm.run();
        //sm.process();

    }
}
