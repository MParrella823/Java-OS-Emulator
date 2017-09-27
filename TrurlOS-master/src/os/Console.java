package os;


import host.TurtleWorld;
import util.Globals;


import java.lang.*;
import java.util.LinkedList;




public class Console implements Input, Output{
	private String buffer = "";
	private static LinkedList<String> scrollBuffer  = new LinkedList();
	private int XPos, YPos;
	public Console() {

	}

	public void init() {

		clearScreen();
		resetXY();
	}



	@Override
	//Writes character input into console
	public void putText(String string) {
		if(!string.equals("")) {
			Globals.world.drawText(XPos, YPos, string);
			int offset = Globals.world.measureText(XPos, string);
			XPos += offset;
		}
	}

	@Override
	public void advanceLine() {
		XPos = 0;
		if (getYPos() >= 372){  //Check YPos for scrolling purposes
			Globals.world.scrollText();
		}
		else{
			YPos += Globals.world.fontHeightMargin() + Globals.world.fontDescent() + Globals.world.fontSize();
		}
	}

	@Override
	public void clearScreen() {
		Globals.world.clearRect(0, 0, Globals.world.width(), Globals.world.height());
	}

	@Override
	public void resetXY() {
		XPos = 0;
		YPos = Globals.world.startYPos();
	}

	@Override
	public void handleInput() {
		while(! Globals.kernelInputQueue.isEmpty()) {
			String next = Globals.kernelInputQueue.removeFirst();
			int x = Globals.world.measureText(XPos, next);
		    if(next.length() > 1) continue;
			if(next.equals("\n") || next.equals("\r") || next.equals("" + ((char)10))){
	   			Globals.osShell.handleInput(buffer);
				buffer = "";
			}else if(next.equals("8")) { //if backspace is pressed..
                if (XPos > 7) { //keep cursor from going past prompt symbol (>)
					buffer = buffer.substring(0,buffer.length()-1); //remove the last character from the buffer
					XPos = XPos - x; //move the x position backwards 1 character width
                    clearChar(next);
                }
				else{
                    if(buffer.length() == 1) { //Only 1 character in buffer case
                        buffer = "";
						XPos = 7;
                    }else if (buffer.length() == 0){ //Empty buffer string case
                        buffer = "";
                        XPos = 7;
                    }
                    else{
                        buffer = buffer.substring(0, buffer.length() - 1);
                        XPos = 7;
                    }
				}
			}
			else {
				putText("" + next);
				buffer += next;
			}
		}
	}

	@Override
	public int getXPos() {
		return XPos;
	}

	public int getYPos(){
		return YPos;
	}

	/**
	 *
	 * @function clearChar(String) will remove the character  from the console window itself by repainting a rectangle with the same color as the background
	 *
	 * @param s String type - the character that is to be measured and removed from the screen
	 */

	public void clearChar(String s){

	    int x = Globals.world.measureText(XPos, s);
		// Need to save r,g,b values of text color so it can be reset after changing color for backspace..
		int r = Globals.world.getPage().getColor().getRed();
		int g = Globals.world.getPage().getColor().getGreen();
		int b = Globals.world.getPage().getColor().getBlue();

        Globals.world.setBackground(Globals.world.getBackground());
        Globals.world.setColor(0,0,0); //Set color to background color for repainting over characters
        Globals.world.getPage().fillRect(getXPos(),getYPos()-12, x, 14);//Repaint over character(s)
        Globals.world.setColor(r,g,b);//Return text color to original color
        Globals.world.repaint();
    }

    /**
     *
     * Print Center method is used solely for visual effect of a kernel trap error.  Nothing more, nothing less.
     *
     * @param s String - the message you'd like displayed in the middle of the console window
	 *
     */

	public void printCenter(String s){
	    XPos = 250;
	    YPos = 200;
	    putText(s.toUpperCase());
    }

}









