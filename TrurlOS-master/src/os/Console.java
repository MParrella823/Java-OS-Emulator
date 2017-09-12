package os;

import com.sun.prism.Graphics;
import host.TurtleWorld;
import javafx.scene.Cursor;
import sun.awt.Graphics2Delegate;
import util.Globals;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.Key;
import java.lang.*;




public class Console implements Input, Output{
	private String buffer = "";
	private int XPos, YPos;
	public Console() {

	}

	public void init() {
		// TODO Auto-generated method stub
		clearScreen();
		resetXY();
	}



	@Override
	//Writes character input into console
	public void putText(String string) {
		if(!string.equals("")) {
			//            _DrawingContext.drawText(this.currentFont, this.currentFontSize, this.currentXPosition, this.currentYPosition, text);
			Globals.world.drawText(XPos, YPos, string);
			int offset = Globals.world.measureText(XPos, string);
			XPos += offset;
		}
	}

	@Override
	public void advanceLine() {
		XPos = 0;
		YPos += Globals.world.fontHeightMargin() + Globals.world.fontDescent() + Globals.world.fontSize();
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

		    if(next.length() > 1) continue; //TODO: handle special key strokes...
			if(next.equals("\n") || next.equals("\r") || next.equals("" + ((char)10))){
				Globals.osShell.handleInput(buffer);
				buffer = "";
			}else if(next.equals("8")) { //if backspace is pressed..
                Globals.world.setBackground(Color.green);

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
				//Globals.standardOut.putText("Buffer: " + buffer);

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

	public void clearChar(String s){

	    int x = Globals.world.measureText(XPos, s);

       // Globals.standardOut.putText("" + getYPos());
        Globals.world.setBackground(Globals.world.getBackground());
        Globals.world.setColor(0,0,0);
        Globals.world.getPage().fillRect(getXPos(),getYPos()-12, x, 13);
        Globals.world.setColor(255,255,255);
        Globals.world.repaint();
    }



}
