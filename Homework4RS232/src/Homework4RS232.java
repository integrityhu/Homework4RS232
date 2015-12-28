import java.io.IOException;
import java.util.Date;

import components.RS232Port;
import components.SerialManager;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;
import tasks.ReadSmsTask;
import tasks.SimpleModemTask;
import utils.SerialRequest;
//http://www.developershome.com/sms/readSmsByAtCommands.asp
//https://code.google.com/p/java-simple-serial-connector/wiki/jSSC_examples  (search: UTF-16BE)

public class Homework4RS232 {

    public static void main(String[] args) {
        try {
            ReadSmsTask.sendATCommands();
            //SimpleModemTask.run();
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

}
