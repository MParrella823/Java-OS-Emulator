package test;

import host.Control;
import util.Globals;

public class Test2 {
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
        Globals.userProgramInput.setText("Dead beef wait I mean anything but deadbeef.");
        Globals.Klapaucius.delay(2500);  //wait awhile to test the success of the previous test...
        Globals.Klapaucius.type("load\n");  //start the next test.
        Globals.Klapaucius.delay(2500);  //wait awhile to test the success of the previous test...
        Globals.userProgramInput.setText(" \n");
        Globals.Klapaucius.type("load\n");  //start the next test.
        Globals.Klapaucius.delay(500);  //wait awhile to test the success of the previous test...
        Globals.Klapaucius.type("run 0\n");  //start the next test.
        while (Control.cpu.isExecuting()) {
            Globals.Klapaucius.delay(500);
        }
        Globals.userProgramInput.setText("13 3 0 9 2 -1 9 4 1 10 14 -1 13 231 13 20 1 13 3 0 15 0");
        Globals.Klapaucius.delay(500);
        Globals.Klapaucius.type("load\n");

    }
}
