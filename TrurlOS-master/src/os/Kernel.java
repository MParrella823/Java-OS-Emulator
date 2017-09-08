package os;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import util.Globals;
import host.Control;
import host.Devices;

public class Kernel {
	
	public void kernelBootstrap()
	{
		Control.hostLog("bootstrap", "host");
		Globals.kernelInputQueue = new LinkedList<String>();
		Globals.kernelInterruptQueue = new LinkedList<Interrupt>();
		Globals.kernelBuffers = new ArrayList<Object>();

		Globals.console = new Console();
		Globals.console.init();
		
		Globals.standardIn = Globals.console;
		Globals.standardOut = Globals.console;
		
		kernelTrace("Loading the keyboard device driver.");
		Globals.kernelKeyboardDriver = new DeviceKeyboardDriver();
		Globals.kernelKeyboardDriver.driverEntry();
		kernelTrace(Globals.kernelKeyboardDriver.getStatus());
		
		kernelTrace("Enabling Interrupts.");
		kernelEnableInterrupts();
		
		kernelTrace("Enabling and Launching Shell.");
				
		Globals.osShell = new Shell();
		Globals.osShell.init();
	}
	
	public void kernelShutdown() {
		kernelTrace("Begin shutdown OS");
		kernelTrace("Disabling Interrupts.");
		kernelDisableInterrupts();
		kernelTrace("End shutdown OS");
	}
	
	public void kernelOnCPUClockPulse() {
		if(! Globals.kernelInterruptQueue.isEmpty()) {
			Interrupt interrupt = Globals.kernelInterruptQueue.removeFirst();
			kernelInterruptHandler(interrupt.irq, interrupt.params);
		} else if(Control.cpu.isExecuting()){
			Control.cpu.cycle();
		} else {
			kernelTrace("idle");
		}
	}
	
	public void kernelEnableInterrupts() {
		Devices.devices.hostEnableKeyboardInterrupt();
	}
	
	public void kernelDisableInterrupts() {
		Devices.devices.hostDisableKeyboardInterrupt();
	}
	
	public void kernelInterruptHandler(int irq, HashMap<String, String> params) {
		kernelTrace("Handling IRQ~" + irq);
		switch(irq) {
			case Globals.TIMER_IRQ :
				kernelTimerISR();
				break;
			case Globals.KEYBOARD_IRQ :
				Globals.kernelKeyboardDriver.isr(params);
				Globals.standardIn.handleInput();
				break;
			default:
				kernelTrapError("Invalid Interrupt Request. irq: " + irq + " params: " + params);
		}
	}

	public void kernelTimerISR() {

	}
	
	public void kernelTrace(String message) {
		if(Globals.trace) {
			if(message.equals("idle")) {
				if(Globals.OSclock % 10 == 0) {
					Control.hostLog(message, "OS");
				}
			} else {
				Control.hostLog(message, "OS");
			}
		}
	}
	
	public void kernelTrapError(String message) {
		Control.hostLog("OS ERROR - TRAP " + message, null);
		kernelShutdown();
	}
}
