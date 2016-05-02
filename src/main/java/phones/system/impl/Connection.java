package phones.system.impl;

import java.util.function.Consumer;
import phones.system.Call;

class Connection {
    
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

    private void assertOpen() {
        if (!open) {
            throw new IllegalStateException("closed");
        }
    }

    private Call call(String name, Consumer<String> onMessageReceive, Runnable onEnd) {
        return new Call() {
            @Override
            public void send(String message) {
                assertOpen();
                onMessageReceive.accept(message);
            }

            @Override
            public void end() {
                assertOpen();
                open = false;
                onEnd.run();
            }

            @Override
            public String toString() {
                return (open ? "Call " : "CLOSED ") + name;
            }
        };
    }

    public Call call1() {
        return call(number1 + " --> " + number2, onMessageReceive2, onEnd2);
    }

    public Call call2() {
        return call(number2 + " --> " + number1, onMessageReceive1, onEnd1);
    }

    @Override
    public String toString() {
        return (open ? "Call" : "CLOSED") + number1 + " <-> " + number2;
    }
}
