package os;


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
			if (string.contains("\r")){
				scrollBuffer.addLast("\n");
			}
			scrollBuffer.addLast(string);
			Globals.world.drawText(XPos, YPos, string);
			int offset = Globals.world.measureText(XPos, string);
			XPos += offset;
		}
	}

	@Override
	public void advanceLine() {
		XPos = 0;
		if (getYPos() >= 372){  //Check YPos for scrolling purposes
			resetXY(); //reset xy to start drawing text
			clearScreen(); //clear screen for repaint lines
			scrollText();
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
	   			scrollBuffer.addLast(next);
				Globals.osShell.handleInput(buffer);
				buffer = "";
			}else if(next.equals("8")) { //if backspace is pressed..
                if (XPos > 7) { //keep cursor from going past prompt symbol (>)
					buffer = buffer.substring(0,buffer.length()-1); //remove the last character from the buffer
					XPos = XPos - x; //move the x position backwards 1 character width
                    clearChar(next);
                    scrollBuffer.removeLast();
				}
				else{
                    if(buffer.length() == 1) { //Only 1 character in buffer case
                        buffer = "";
						scrollBuffer.removeLast();
                        XPos = 7;
                    }else if (buffer.length() == 0){ //Empty buffer string case
                        buffer = "";
                        XPos = 7;
                    }
                    else{
                        buffer = buffer.substring(0, buffer.length() - 1);
                        scrollBuffer.removeLast();
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
	 * scrollText will be responsible for removing items from the linked list buffer and
	 * drawing it to the console
	 *
	 */

	public void scrollText(){
		// loop through the buffer until the designated y position is reached..
		while(!scrollBuffer.isEmpty() && getYPos() < 372) {
			String line = scrollBuffer.removeFirst();
			if (line.length() == 1) { //if line contains a single character
				Globals.world.drawText(XPos, YPos, line);
				int offset = Globals.world.measureText(XPos, line);
				XPos += offset;
				if (scrollBuffer.peekFirst() != null) {
					String buff = scrollBuffer.peekFirst();
					if (buff.equals("\n")) { //will check for full word inputs
						XPos = 0;
						YPos += Globals.world.fontHeightMargin() + Globals.world.fontDescent() + Globals.world.fontSize();
					}
				}
			}
			else{//if line contains an entire word
				XPos = 0;
				Globals.world.drawText(XPos, YPos, line);
				YPos += Globals.world.fontHeightMargin() + Globals.world.fontDescent() + Globals.world.fontSize();

			}
		}
	}

    /**
     *
     * Print Center method is used solely for visual effect of a kernel trap error.  Nothing more, nothing less.
     *
     * @param s String - the message you'd like displayed in the middle of the console window
     */

	public void printCenter(String s){
	    XPos = 250;
	    YPos = 200;
	    putText(s.toUpperCase());
    }

}









