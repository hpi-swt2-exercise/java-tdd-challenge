package phones.system;

import phones.Phone;

public interface ConnectedPhone extends Phone {
    
    void receive(CallIncoming request);
    
    void cancelled(CallIncoming request);
}
