package phones;

/**
 * The visible state of a phone.
 */
public enum PhoneStatus {
    
    /**
     * The phone is ready to call.
     */
    IDLE,
    
    /**
     * The phone is calling, but the partner has not responded yet.
     */
    CALLING,
    
    /**
     * The phone is being called.
     */
    RINGING,
    
    /**
     * The phone is in a call and can send and receive messages.
     */
    IN_CALL,
    
    /**
     * The call request was rejected because the partner is already in a call.
     */
    BUSY,
    
    /**
     * The partner rejected or terminated the call.
     */
    CANCELED;
}
