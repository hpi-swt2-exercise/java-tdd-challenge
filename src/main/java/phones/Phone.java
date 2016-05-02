package phones;

public interface Phone {
    
    String getNumber();
    
    void dial(String number);
    
    void pushGreen();
    
    void pushRed();
    
    void send(String message);
    
    PhoneStatus getStatus();
    
    String getLastMessage();
}
