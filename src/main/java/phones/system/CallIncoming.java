package phones.system;

import java.util.function.Consumer;

/**
 *
 */
public interface CallIncoming {
    
    Call accept(Consumer<String> onMessageReceive, Runnable onEnd);
    
    void reject(RejectReason reason);
}
