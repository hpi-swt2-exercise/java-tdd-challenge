package phones.system;

import java.util.function.Consumer;

/**
 *
 */
public interface PhoneSocket {
    
    String getNumber();
    
    CallOutgoing call(String number, Consumer<RejectReason> onReject, Consumer<Call> onAccept, Consumer<String> onMessageReceive, Runnable onEnd);
}
