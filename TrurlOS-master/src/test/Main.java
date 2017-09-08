package test;

import host.Control;
import util.Globals;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Control.hostInit();
		Scanner scan = new Scanner(System.in);
		System.out.println("Options:\n\tstart -- Starts the operating system.");
		System.out.println("\thalt -- Stops the operating system.");
		System.out.println("\texit -- Quits the emulation.");
		String line = scan.nextLine();
		line = line.trim().toLowerCase();
		while(!line.equals("exit")) {
			if(line.equals("start")) {
				if(Globals.host == null) {
					Control.startOS();
				} else
				{
					System.out.println("Operating System is already running.");
				}
			} else if(line.equals("halt")) {
				Control.haltOS();
			} else {
				System.out.println("Unknown Command.");
			}
			line = scan.nextLine();
		}
	}
}
