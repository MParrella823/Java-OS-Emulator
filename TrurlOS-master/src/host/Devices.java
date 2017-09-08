package host;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import os.Interrupt;
import util.Globals;

public class Devices extends java.awt.event.KeyAdapter{
	private int hardwareClockID = Globals.hardwareClockID;
	public static final Devices devices = new Devices();
	
	public void hostClockPulse() {
		Globals.OSclock++;
		Control.kernel.kernelOnCPUClockPulse();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		HashMap<String, String> event = new HashMap<String, String>();
		event.put("key","" +  e.getKeyCode());
		event.put("modifiers","" +  e.getModifiers());
		event.put("when","" + e.getWhen());
		event.put("location","" + e.getKeyLocation());
		event.put("id","" + e.getID());
		event.put("char","" + e.getKeyChar());
		Globals.kernelInterruptQueue.add(new Interrupt(Globals.KEYBOARD_IRQ, event));
	}
	
	public void hostEnableKeyboardInterrupt() {
		Globals.world.addKeyListener(this);
	}
	
	public void hostDisableKeyboardInterrupt() {
		Globals.world.removeKeyListener(this);
	}
}
