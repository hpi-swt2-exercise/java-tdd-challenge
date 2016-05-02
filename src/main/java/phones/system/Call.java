package phones.system;

/**
 * A connection between to phones.
 */
public interface Call {

    /**
     * Sends a message.
     * @param message text
     */
    void send(String message);
    
    /**
     * Cancels the call.
     */
    void end();
}
