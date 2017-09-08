package os;

public interface Output {

	public void putText(String string);
	public void advanceLine();
	public void clearScreen();
	public void resetXY();
	public int getXPos();

}
