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
    private static int segCount = 3; //Total number of segments
    private int segNum; //Unique identifier for each segment
    private int segAddress; //Starting address of segment
    private int segDefaultSize = 85;
    private int startAddress;

    /**
     *
     * Default Constructor
     *
     * Creates a segment in memory, 85 units in size
     *
     *
     */

    public MMU(){
        this.segSize = segDefaultSize;
        Globals.FreeSpace -= this.segSize;
        Globals.AllocatedSpace += this.segSize;
    }

    /**
     *
     * Loads the passed array of integers into the designated segment
     *
     * @param segNum Segment Number
     * @param data - The array of integers to write into the segment
     */

    public void loadIntoSegment(int segNum, int[] data) {
        if (segNum < segCount && data.length <= this.segSize) {
            for (int i = 0; i < data.length; i++) {
                Globals.mem.set((segNum * this.segSize + i),data[i]);
            }
        } else {

            //TODO write OS Trap error
        }
    }

    /**
     *
     * Will clear out the memory located within the specified segment
     *
     * @param segNum Segment Number
     */

    public void clearSegment(int segNum){
        if (segNum < segCount) {
            for (int i = (segNum * segSize); i < (segNum * segSize + 84); i++) {
                Globals.mem.set(i, 0);
            }
        }
        else {
            //TODO: OS Trap Error (incorrect segment)
        }
    }

    /**
     *
     *
     * Will check to ensure the passed segment number and address are correct.
     * Example: User enters segment 0, but references an address outside of segment 0 -> Error
     *
     * @param num Segment Number
     * @param address Address in memory
     * @return True/False of correct segment number/address combination
     */

    public boolean checkSegment(int num, int address){
        boolean flag = false;
        int test = address / this.segSize;
        if (test == num){
            flag = true;
        }
        else{
            flag = false;
        }

        return flag;
    }

    /**
     *
     * Will set the value at the specified segment address and number with the passed value
     *
     * @param address - Address of the memory location
     * @param segNum - Segment Number
     * @param value - Value stored at specific address
     */

    public void setData(int address, int segNum, int value){
        if (this.checkSegment(segNum, address)){
             int offset = address - (segNum * this.segSize);
             Globals.mem.set(((segNum*this.segSize) + offset), value);
        }
        else{
            //TODO: write OS Trap error
        }
    }

    /**
     *
     * Will return the value stored at the specified segment address and number
     *
     * @param segNum Segment Number
     * @param address - Address of the
     * @return - The value stored at the passed address and segment number
     */

    public int getData(int segNum, int address){
       if (this.checkSegment(segNum, address)){
            int offset = address - (segNum * segSize);
            return Globals.mem.get(segNum * this.segSize + offset);
       }
       else{
           //TODO: write OS Trap error (incorrect segment)
             return -1;
        }
    }

    /**
     *
     * Provides the corresponding segment number of the passed memory address
     *
     * @param address Address location in memory
     * @return int - segment's unique identifier
     */

    public int getSegmentNum(int address) {
        if (address > 255) {
            return -1;
        } else {
            this.segNum = address / this.segSize;
            return this.segNum;
        }
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
     * @param num Segment Number
     * @return int - the starting address of that segment
     */

    public int getSegmentAddress(int num){
        if (num < segCount) {
            return segSize * num;
        }
        else{
            //TODO: OS Trap Error (no such segment)
            return 0;
        }
    }

    /**
     *
     * Provides the upper limit memory location of a given segment number
     *
     * @param segNum Segment Number
     * @return int Upper limit memory location for passed segment number
     */

    public int getSegmentLimit(int segNum) {
        int test = segNum * segSize + 84;
        return test;
    }
}