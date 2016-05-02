package phones.system;

import java.util.function.Consumer;

/**
 * An incoming call request.
 */
public interface CallIncoming {
    
    /**
     * Accepts the request and creates a call.
     * @param onMessageReceive incoming message handler
     * @param onEnd call cancellation handler
     * @return new call.
     */
    Call accept(Consumer<String> onMessageReceive, Runnable onEnd);
    
    /**
     * Rejects the call for a given reason.
     * @param reason the reason
     */
    void reject(RejectReason reason);
}
