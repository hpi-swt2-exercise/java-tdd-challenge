package phones.system;

import phones.Phone;

/**
 * A phone that can receive call requests from a phone system.
 */
public interface ConnectedPhone extends Phone {
    
    void receive(CallIncoming request);
    
    void canceled(CallIncoming request);
}
