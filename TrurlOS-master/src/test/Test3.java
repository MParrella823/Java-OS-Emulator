package test;

import host.Control;
import util.Globals;

/**
 * Created by po917265 on 7/20/17.
 */
public class Test3 {
    public static void main(String[] args) throws InterruptedException {
        Control.hostInit();
        Control.startOS();  //start the os...
        Globals.Klapaucius.delay(5000); //wait 5 seconds before the test is started.  Gives you time to refocus on Trurl.

        Globals.Klapaucius.type("help\n");  // forces scrolling...
        Globals.Klapaucius.type("help\n");
        Globals.Klapaucius.type("help\n");
        Globals.Klapaucius.delay(2500);  //wait a while to test the success of the previous test...
        Globals.Klapaucius.type("ver\n");  //start the next test.
        Globals.Klapaucius.delay(2500);  //wait a while to test the success of the previous test...
        Globals.Klapaucius.type("date\n");  //start the next test.
        Globals.Klapaucius.delay(2500);  //wait a while to test the success of the previous test...
        Globals.Klapaucius.type("whereami\n");  //start the next test.
        Globals.Klapaucius.delay(2500);  //wait a while to test the success of the previous test...
        Globals.Klapaucius.type("status this is a status message!\n");  //start the next test.
        Globals.userProgramInput.setText("Dead beef wait I mean anything but deadbeef.");
        Globals.Klapaucius.delay(2500);  //wait a while to test the success of the previous test...
        Globals.Klapaucius.type("load\n");  //start the next test.
        Globals.Klapaucius.delay(2500);  //wait a while to test the success of the previous test...
        Globals.userProgramInput.setText("8 9 10 14 29 4 1 10 3 1 3 3 14 30 2 29 2 30 2 29 13 144 13 28 1 13 2 0 15 0 0\n");
        Globals.Klapaucius.type("load\n");  //start the next test.
        Globals.Klapaucius.delay(500);  //wait a while
        Globals.userProgramInput.setText("13 3 0 9 2 -1 9 4 1 10 14 -1 13 231 13 20 1 13 3 0 15 0");
        Globals.Klapaucius.delay(500);
        Globals.Klapaucius.type("load\n");
        Globals.Klapaucius.delay(500);  //wait a while
        Globals.Klapaucius.type("quantum 15\n");
        Globals.Klapaucius.delay(500);  //wait a while
        Globals.userProgramInput.setText("13 3 0 13 31 0 11 2 10 14 -2 11 1 10 14 -1 11 1 4 4 2 -1 4 3 2 -2 4 2 11 1 0 13 23 13 73 13 40 13 6 0 3 1 3 3 15 0 0");
        Globals.Klapaucius.delay(500);
        Globals.Klapaucius.type("load\n");
        Globals.Klapaucius.delay(500);
        Globals.Klapaucius.type("runall\n");
        Globals.Klapaucius.delay(10000);
        Globals.Klapaucius.type("kill 0\n"); //we want to test the kill command too!

    }
}
