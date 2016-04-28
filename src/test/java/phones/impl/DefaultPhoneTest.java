package phones.impl;

import org.junit.Test;
import phones.Phone;
import phones.system.PhoneSystem;
import phones.system.impl.DefaultPhoneSystem;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.CoreMatchers.*;
import static phones.PhoneStatus.*;


public class DefaultPhoneTest {
    
    private static final String ALICE = "555-ALICE";
    private static final String BOB = "555-THE-BOBSTER";
    private static final String CAROL = "555-CAROL";
    
    private final PhoneSystem system = new DefaultPhoneSystem();
    private final Phone alice = system.getPhone(ALICE);
    private final Phone bob = system.getPhone(BOB);
    private final Phone carol = system.getPhone(CAROL);
    
    public DefaultPhoneTest() {
    }

    private void given_call_between(Phone p1, Phone p2) {
        p1.dial(p2.getNumber());
        p1.pushGreen();
        p2.pushGreen();
        assumeThat(p1.getStatus(), is(IN_CALL));
        assumeThat(p2.getStatus(), is(IN_CALL));
    }
    
    @Test
    public void new_phone_is_idle() {
        assertThat(alice.getStatus(), is(IDLE));
    }
    
    @Test
    public void after_dial_push_green_to_call() {
        alice.dial(BOB);
        alice.pushGreen();
        assertThat(alice.getStatus(), is(CALLING));
    }
    
    @Test
    public void with_incoming_is_ringing() {
        alice.dial(BOB);
        alice.pushGreen();
        assertThat(bob.getStatus(), is(RINGING));
    }
    
    @Test
    public void in_call_getLastMessage_returns_last_message() {
        given_call_between(alice, bob);
        alice.send("Hello");
        assertThat(bob.getLastMessage(), is("Hello"));
        assertThat(bob.getLastMessage(), is(nullValue()));
    }
}
