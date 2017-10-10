package host;
import util.Globals;


/**
 * MMU (Memory Management Unit) class will create and manage memory segments, as well as
 * make all calls to the Memory class, when data needs to be written
 * or read to/from memory.
 *
 */

public class MMU {

    private int segSize; //Size of segment
    private static int segCount = 0; //Total number of segments
    private int segNum; //Unique identifier for each segment
    private int segAddress; //Starting address of segment
    private int segDefaultSize = 85;

    /**
     *
     * Default Constructor
     *
     * Creates a segment in memory, 100 units in size
     *
     *
     */

    public MMU(){
        this.segSize = segDefaultSize;
        Globals.FreeSpace -= this.segSize;
        Globals.AllocatedSpace += this.segSize;
        segCount++;

    }

    /**
     *
     * Loads the passed array of integers into the designated segment
     *
     *
     * @param data - The array of integers to write into the segment
     */

    public void loadIntoSegment(int[] data) {
        if (data.length <= this.segSize) {
            for (int i = 0; i < data.length; i++) {
                Globals.mem.set(i,data[i]);
            }
        } else {

            //TODO write OS Trap error
        }
    }

    /**
     *
     * Will clear out the memory located within the specified segment
     *
     */

    public void clearSegment(){
        for (int i = 0; i < this.segSize; i++){
                Globals.mem.set(i, 0);
            }

    }

    /**
     *
     * Will set the value at the specified segment address and number with the passed value
     *
     * @param address - Address of the memory location
     * @param value - Value stored at specific address
     */

    public void setData(int address, int value){
        if (address < this.segSize){
            Globals.mem.set(address, value);
        }
        else{
            //TODO: write OS Trap error
        }
    }

    /**
     *
     * Will return the value stored at the specified segment address and number
     *
     * @param address - Address of the
     * @return - The value stored at the passed address and segment number
     */

    public int getData(int address){
        if (address < this.segSize){
            return Globals.mem.get(address);
        }
        else{
            //TODO: write OS Trap error
            return -1;
        }
    }

    /**
     *
     * Provides the unique identifier of a specific segment
     *
     * @return int - segment's unique identifier
     */

    public int getSegmentNum(){
        return this.segNum;
    }

    /**
     *
     * Provides the total number of segments currently in memory
     *
     * @return int - current number of segments
     */

    public int getSegmentCount(){
        return segCount;
    }

    /**
     *
     * Provides starting address of memory segment
     *
     * @return int - the starting address of that segment
     */

    public int getSegmentAddress(){
        return this.segAddress;
    }

    public int getSegmentSize() { return this.segSize; }
}