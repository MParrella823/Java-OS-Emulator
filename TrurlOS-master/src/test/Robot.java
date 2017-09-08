package test;

import java.awt.*;
import java.awt.event.KeyEvent;

//import com.sun.glass.events.KeyEvent;

public class Robot extends java.awt.Robot{

	private boolean control;
	private boolean alternate;

	public Robot() throws AWTException {
		super();
	}

	private static final String lower = "`1234567890-=[]\\;',./";
	private static final String upper = "~!@#$%^&*()_+{}|:\"<>?";
	
	public void ctrl() {
		control = true;
		keyPress(KeyEvent.VK_CONTROL);
	}
	
	public void alt() {
		alternate = true;
		keyPress(KeyEvent.VK_ALT);
	}
	
	public void type(String s) {
		for(char c : s.toCharArray()) {
			type(c);
		}
	}
	
	public void type(char key) {
		if(key >= 'a' && key <= 'z') {
			keyPress(key - 'a' + 'A');
			this.delay(50);
			keyRelease(key - 'a' + 'A');
			this.delay(200);
		} else if(key >= 'A' && key <= 'Z') {
			keyPress(KeyEvent.VK_SHIFT);
			this.delay(50);
			keyPress(key);
			this.delay(50);
			keyRelease(key);
			this.delay(50);
			keyRelease(KeyEvent.VK_SHIFT);
			this.delay(100);
		} else if(key == ' ') {
			keyPress(KeyEvent.VK_SPACE);
			this.delay(50);
			keyRelease(KeyEvent.VK_SPACE);
			this.delay(200);
		} else if(lower.contains("" + key)) {
			keyPress(key);
			this.delay(50);
			keyRelease(key);
			this.delay(200);
		} else if(upper.contains("" + key)) {
			key = lower.charAt(upper.indexOf(key));  //make it lower case, and then shift it.
			keyPress(KeyEvent.VK_SHIFT);
			this.delay(50);
			keyPress(key);
			this.delay(50);
			keyRelease(key);
			this.delay(50);
			keyRelease(KeyEvent.VK_SHIFT);
			this.delay(100);
		} else if(key == '\n') {
			keyPress(KeyEvent.VK_ENTER);
			this.delay(50);
			keyRelease(KeyEvent.VK_ENTER);
			this.delay(200);
		}
		if(control) {
			keyRelease(KeyEvent.VK_CONTROL);
			delay(50);
		}
		if(alternate) {
			keyRelease(KeyEvent.VK_CONTROL);
			delay(50);
		}
	}
	
	public void backspace(int times) {
		for(int i = 0; i < times; i++) {
			keyPress(KeyEvent.VK_BACK_SPACE);
			this.delay(50);
			keyRelease(KeyEvent.VK_BACK_SPACE);
			this.delay(200);
		}
	}
	
	public void up() {
		keyPress(KeyEvent.VK_UP);
		this.delay(50);
		keyRelease(KeyEvent.VK_UP);
		this.delay(200);
	}
	
	public void down() {
		keyPress(KeyEvent.VK_DOWN);
		this.delay(50);
		keyRelease(KeyEvent.VK_DOWN);
		this.delay(200);
	}
	
	public void left() {
		keyPress(KeyEvent.VK_LEFT);
		this.delay(50);
		keyRelease(KeyEvent.VK_LEFT);
		this.delay(200);
	}
	
	public void right() {
		keyPress(KeyEvent.VK_RIGHT);
		this.delay(50);
		keyRelease(KeyEvent.VK_RIGHT);
		this.delay(200);
	}
	
	
	
}
