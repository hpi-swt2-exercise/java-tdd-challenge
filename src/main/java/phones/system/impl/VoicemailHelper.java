package phones.system.impl;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class VoicemailHelper {
    
    private static List<Runnable> actions = new ArrayList<>();
    
    /**
     * Executes an action after the voicemail timeout passed.
     * @param action to be executed
     */
    public static void onTimeoutDo(Runnable action) {
        actions.add(action);
    }
    
    /**
     * Executes all on-timeout actions.
     */
    public static void waitUntilTimeout() {
        List<Runnable> next = actions;
        reset();
        next.forEach(Runnable::run);
    }
    
    public static void reset() {
        actions = new ArrayList<>();
    }
}
