package os;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.lang.StringBuilder;

import util.Globals;

public class DeviceKeyboardDriver extends DeviceDriver {
	
	public DeviceKeyboardDriver() {
		super();
		DeviceDriver x = this;
		driverEntry = new DriverEntry() {
			public void enter() {
				x.setStatus("loaded");
			}
		};
		isr = new EventDispatch() {
			public void dispatch(HashMap<String, String> params) {
				int keyCode = Integer.parseInt(params.get("key"));
				int modifiers = Integer.parseInt(params.get("modifiers"));
				
				String character = null;
				if(keyCode == KeyEvent.VK_UP) {
					character = params.get("key");

				}else if(keyCode == KeyEvent.VK_DOWN){
					character = params.get("key");

				} else if(keyCode >= 32 && keyCode <= 255) {
					character = params.get("char");
				} else if(keyCode == 10) { //this is the enter key on my mac...
					character = "" + ((char)keyCode);
				} else if(keyCode == KeyEvent.VK_BACK_SPACE){
					character = params.get("key");
				}

				else {
					character = keyCode+":"+modifiers;
				}
				
				//and add to the input queue.
				//Globals.standardOut.putText("char:" + character);
				Globals.kernelInputQueue.add(character);
			}
		};
	}
	
	public void driverEntry() {
		driverEntry.enter();
	}
	
	public void isr(HashMap<String, String> params) {
		isr.dispatch(params);
	}
}
