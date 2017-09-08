package test;

import host.Control;
import util.Globals;

public class Test1 {
	public static void main(String[] args) throws InterruptedException {
		Control.hostInit();
		Control.startOS();  //start the os...
		Globals.Klapaucius.delay(5000); //wait 5 seconds before the test is started.  Gives you time to refocus on Trurl.
		Globals.Klapaucius.type("help\n");  // forces scrolling...
		Globals.Klapaucius.type("help\n");
		Globals.Klapaucius.type("help\n");
		Globals.Klapaucius.delay(2500);  //wait awhile to test the success of the previous test...
		Globals.Klapaucius.type("ver\n");  //start the next test.
		Globals.Klapaucius.delay(2500);  //wait awhile to test the success of the previous test...
		Globals.Klapaucius.type("date\n");  //start the next test.
		Globals.Klapaucius.delay(2500);  //wait awhile to test the success of the previous test...
		Globals.Klapaucius.type("whereami\n");  //start the next test.
		Globals.Klapaucius.delay(2500);  //wait awhile to test the success of the previous test...
		Globals.Klapaucius.type("status this is a status message!\n");  //start the next test.
		Globals.userProgramInput.setText("13 3 0 9 2 -1 9 4 1 10 14 -1 13 231 13 20 1 13 3 0 15 0\n");  //memory testing program.  No errors should result.
		Globals.Klapaucius.delay(2500);  //wait awhile to test the success of the previous test...
		Globals.Klapaucius.type("load\n");  //start the next test.
		Globals.userProgramInput.setText("Dead beef wait I mean anything but deadbeef.");
		Globals.Klapaucius.delay(2500);  //wait awhile to test the success of the previous test...
		Globals.Klapaucius.type("load\n");  //start the next test.
		Globals.userProgramInput.setText("");
		Globals.Klapaucius.delay(2500);  //wait awhile to test the success of the previous test...
		Globals.Klapaucius.type("load\n");  //start the next test.

	}
}
