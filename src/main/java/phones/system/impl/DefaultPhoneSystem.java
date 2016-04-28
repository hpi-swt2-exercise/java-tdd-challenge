package phones.system.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import phones.Phone;
import phones.impl.DefaultPhone;
import phones.system.Call;
import phones.system.CallIncoming;
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
    
    public DefaultPhoneSystem() {
    }

    @Override
    public Phone getPhone(String number) {
        return internalGetPhone(number);
    }
    
    protected ConnectedPhone internalGetPhone(String number) {
        return phones.computeIfAbsent(number, n -> newPhone(n, DefaultPhone::new));
    }
    
    protected ConnectedPhone newPhone(String number, Function<? super PhoneSocket, ? extends ConnectedPhone> factory) {
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
    
    public static class CallRequest implements CallOutgoing, CallIncoming {
        
        private final String number1;
        private final Consumer<RejectReason> onReject1;
        private final Consumer<Call> onAccept1;
        private final Consumer<String> onMessageReceive1;
        private final Runnable onEnd1;
        private ConnectedPhone targetPhone;
        private Connection cnn;
        private boolean cancelled = false;

        public CallRequest(String number, Consumer<RejectReason> onReject, Consumer<Call> onAccept, Consumer<String> onMessageReceive, Runnable onEnd) {
            this.number1 = number;
            this.onReject1 = onReject;
            this.onAccept1 = onAccept;
            this.onMessageReceive1 = onMessageReceive;
            this.onEnd1 = onEnd;
        }
        
        public void sendToPhone(ConnectedPhone targetPhone) {
            this.targetPhone = targetPhone;
            targetPhone.receive(this);
        }
        
        public boolean isUnanswered() {
            return cnn == null && !cancelled;
        }

        @Override
        public void cancel() {
            if (cancelled) {
                throw new IllegalStateException("cancelled");
            }
            targetPhone.cancelled(this);
            cancelled = true;
        }

        @Override
        public Call accept(Consumer<String> onMessageReceive, Runnable onEnd) {
            if (cancelled) {
                throw new IllegalStateException("cancelled");
            }
            cnn = new Connection(number1, onMessageReceive1, onEnd1, 
                    targetPhone.getNumber(), onMessageReceive, onEnd);
            onAccept1.accept(cnn.call1());
            return cnn.call2();
        }

        @Override
        public void reject(RejectReason reason) {
            if (cancelled) {
                throw new IllegalStateException("cancelled");
            }
            cancelled = true;
            onReject1.accept(reason);
        }

        @Override
        public String toString() {
            return (cancelled ? "CANCELLED " : "Connecting ") + 
                    number1 + " -> " + 
                    (targetPhone != null ? targetPhone.getNumber() : "???");
        }
    }
    
    protected static class Connection {
        
        private final String number1;
        private final Consumer<String> onMessageReceive1;
        private final Runnable onEnd1;
        private final String number2;
        private final Consumer<String> onMessageReceive2;
        private final Runnable onEnd2;
        private boolean open = true;

        public Connection(String number1, Consumer<String> onMessageReceive1, Runnable onEnd1, String number2, Consumer<String> onMessageReceive2, Runnable onEnd2) {
            this.number1 = number1;
            this.onMessageReceive1 = onMessageReceive1;
            this.onEnd1 = onEnd1;
            this.number2 = number2;
            this.onMessageReceive2 = onMessageReceive2;
            this.onEnd2 = onEnd2;
        }

        public void cancel() {
            open = false;
            onEnd2.run();
        }

        public Call call1() {
            class Call1 implements Call {
                @Override
                public void send(String message) {
                    if (!open) {
                        throw new IllegalStateException("closed");
                    }
                    onMessageReceive2.accept(message);
                }
                @Override
                public void end() {
                    open = false;
                    onEnd2.run();
                }
                @Override
                public String toString() {
                    return (open ? "Call " : "CLOSED ") +
                            number1 + " --> " + number2;
                }
            }
            return new Call1();
        }

        public Call call2() {
            class Call2 implements Call {
                @Override
                public void send(String message) {
                    if (!open) {
                        throw new IllegalStateException("closed");
                    }
                    onMessageReceive1.accept(message);
                }
                @Override
                public void end() {
                    open = false;
                    onEnd1.run();
                }
                @Override
                public String toString() {
                    return (open ? "Call " : "CLOSED ") +
                            number2 + " --> " + number1;
                }
            }
            return new Call2();
        }

        @Override
        public String toString() {
            return (open ? "Call" : "CLOSED") +
                    number1 + " <-> " + number2;
        }
    }
}
