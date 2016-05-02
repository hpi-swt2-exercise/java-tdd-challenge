package phones.system.impl;

import java.util.function.Consumer;
import phones.system.Call;
import phones.system.CallIncoming;
import phones.system.CallOutgoing;
import phones.system.ConnectedPhone;
import phones.system.RejectReason;

class CallRequest implements CallOutgoing, CallIncoming {
    
    private final String number1;
    private final Consumer<RejectReason> onReject1;
    private final Consumer<Call> onAccept1;
    private final Consumer<String> onMessageReceive1;
    private final Runnable onEnd1;
    private ConnectedPhone targetPhone;
    private Connection cnn;
    private boolean canceled = false;

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

    private void assertUnanswered() {
        if (canceled) {
            throw new IllegalStateException("canceled");
        }
        if (cnn != null) {
            throw new IllegalStateException("connected");
        }
    }

    @Override
    public void cancel() {
        assertUnanswered();
        targetPhone.canceled(this);
        canceled = true;
    }

    @Override
    public Call accept(Consumer<String> onMessageReceive, Runnable onEnd) {
        assertUnanswered();
        cnn = new Connection(number1, onMessageReceive1, onEnd1, targetPhone.getNumber(), onMessageReceive, onEnd);
        onAccept1.accept(cnn.call1());
        return cnn.call2();
    }

    @Override
    public void reject(RejectReason reason) {
        assertUnanswered();
        canceled = true;
        onReject1.accept(reason);
    }

    @Override
    public String toString() {
        return (canceled ? "CANCELLED " : "Connecting ") + 
                number1 + " -> " + 
                (targetPhone != null ? targetPhone.getNumber() : "???");
    }
}
