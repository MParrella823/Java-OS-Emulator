package util;

import host.TurtleWorld;
import os.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import host.Memory;
import host.MMU;
import host.PCB;
import host.ResidentList;

public class Globals {
	
	/**
	 * Constants
	 */
	public static final double version = 2.3;
	public static final String name = "Aperture Labs Testing Console";
	public static final int CPU_CLOCK_INTERVAL = 100;
	public static final int TIMER_IRQ = 0;
	public static final int KEYBOARD_IRQ = 1;
    public static Thread host;
	
	/**
	 * Variables.
	 */
	public static int OSclock = 0;
	public static int mode = 0;
	public static int prg_count = 0;
	
	//we need variables that have to do with graphics.
	
	public static boolean trace = true;
	
	public static LinkedList<Interrupt> kernelInterruptQueue = null;  //don't really know what the type of this should be....
	public static LinkedList<String> kernelInputQueue = null; //same.  What type?
	public static ArrayList<?> kernelBuffers = null;

	/**
	 * Memory Variables
	 */

	public static Memory mem = new Memory();

	public static int FreeSpace = mem.capacity();
	public static int AllocatedSpace = 0;
	public static MMU mmu = new MMU();

	/**
	 *
	 * PCB Vars
	 *
	 */

	public static PCB pcb = new PCB();
	public static ResidentList residentList = new ResidentList();


	
	public static Input standardIn;
	public static Output standardOut;
	
	public static Console console;
	public static Shell osShell;
	
	public static int hardwareClockID;
	
	public static DeviceKeyboardDriver kernelKeyboardDriver;
	
	public static TurtleWorld world;
	public static JTextArea userProgramInput;

	public static test.Robot Klapaucius;
    static {
		try {
			Klapaucius = new test.Robot();
		} catch (AWTException e) {
			Klapaucius = null;
		}
	}
	
}
