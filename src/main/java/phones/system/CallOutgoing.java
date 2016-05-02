package phones.system;

/**
 * Allows to manage a call request that was send to another phone.
 */
public interface CallOutgoing {

    /**
     * Cancels the call request.
     */
    void cancel();
}
