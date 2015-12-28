/**
 * 
 */
package utils;

import java.util.LinkedList;

/**
 * @author pzoli
 *
 */
public class SerialRequest {

    private String command;
    
    private String ready;
    
    private String faild;
    
    private int waitTime = 5000;
    
    private LinkedList<String> responses = new LinkedList<String>();
    
    public String getCommand() {
        return command;
    }
    
    public void setCommand(String command) {
        this.command = command;
    }
    
    public String getReady() {
        return ready;
    }
    
    public void setReady(String ready) {
        this.ready = ready;
    }

    public String getFail() {
        return faild;
    }

    public void setFail(String fail) {
        this.faild = fail;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public LinkedList<String> getResponses() {
        return responses;
    }

    public void setResponses(LinkedList<String> responses) {
        this.responses = responses;
    }

}
