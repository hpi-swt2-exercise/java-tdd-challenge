package phones.system.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import phones.Phone;
import phones.impl.DefaultPhone;
import phones.system.Call;
import phones.system.CallOutgoing;
import phones.system.PhoneSocket;
import phones.system.ConnectedPhone;
import phones.system.PhoneSystem;
import phones.system.RejectReason;

/**
 *
 */
public class DefaultPhoneSystem implements PhoneSystem {
    
    private final Map<String, ConnectedPhone> phones = new HashMap<>();
    private final Function<? super PhoneSocket, ? extends ConnectedPhone> factory;
    
    public DefaultPhoneSystem() {
        this(DefaultPhone::new);
    }

    public DefaultPhoneSystem(Function<? super PhoneSocket, ? extends ConnectedPhone> factory) {
        this.factory = factory;
    }

    @Override
    public Phone getPhone(String number) {
        return internalGetPhone(number);
    }
    
    protected ConnectedPhone internalGetPhone(String number) {
        return phones.computeIfAbsent(number, this::newPhone);
    }
    
    protected ConnectedPhone newPhone(String number) {
        return factory.apply(new DefaultSocket(number));
    }
    
    protected class DefaultSocket implements PhoneSocket {
        private final String number;

        public DefaultSocket(String number) {
            this.number = number;
        }
        
        @Override
        public String getNumber() {
            return number;
        }

        @Override
        public CallOutgoing call(String number, Consumer<RejectReason> onReject, Consumer<Call> onAccept, Consumer<String> onMessageReceive, Runnable onEnd) {
            CallRequest request = new CallRequest(getNumber(), onReject, onAccept, onMessageReceive, onEnd);
            request.sendToPhone(internalGetPhone(number));
            return request;
        }

        @Override
        public String toString() {
            return "Socket[" + getNumber() + "]";
        }
    }
}
