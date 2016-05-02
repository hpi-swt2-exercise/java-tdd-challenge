package phones.system;

import java.util.function.Consumer;

/**
 * Connects a phone to a phone system.
 */
public interface PhoneSocket {
    
    /**
     * Returns the phone number.
     * @return phone number
     */
    String getNumber();
    
    /**
     * Sends a call request.
     * @param number target phone number
     * @param onReject rejection handler
     * @param onAccept call acceptance handler
     * @param onMessageReceive incoming message handler, if a call is established
     * @param onEnd call cancellation handler, if a call is established
     * @return new outgoing call
     */
    CallOutgoing call(String number, Consumer<RejectReason> onReject, Consumer<Call> onAccept, Consumer<String> onMessageReceive, Runnable onEnd);
}
