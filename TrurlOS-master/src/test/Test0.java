package test;

import host.Control;
import util.Globals;

public class Test0 {
	public static void main(String[] args) throws InterruptedException {
		Control.hostInit();
		Control.startOS();  //start the os...
		Globals.Klapaucius.delay(5000); //wait 5 seconds before the test is started.  Gives you time to refocus on Trurl.
		Globals.Klapaucius.type("help\n");
		Globals.Klapaucius.delay(2500);  //wait awhile to test the success of the previous test...
		Globals.Klapaucius.type("ver\n");  //start the next test.
	}
}
