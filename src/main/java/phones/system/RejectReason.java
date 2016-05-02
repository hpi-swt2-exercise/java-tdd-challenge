package phones.system;

/**
 * Reasons for rejecting a call.
 */
public enum RejectReason {

    /**
     * The phone line is busy.
     */
    BUSY, 
    
    /**
     * The recipient declined the call.
     */
    DECLINED;
}
