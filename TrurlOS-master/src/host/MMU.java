package host;
import util.Globals;


/**
 * MMMU (Memory Management Unit) class will create and manage memory segments, as well as
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
        this.segNum = segCount;
        segAddress = this.segNum * this.segSize;
        segCount++;
    }


    /**
     *
     * Loads the passed array of integers into the designated segment
     *
     * @param segNum - The segment that is to be written to
     * @param data - The array of integers to write into the segment
     */

    public void loadIntoSegment(int segNum, int[] data) {
        if (segNum < segCount && data.length <= this.segSize) {
            for (int i = 0; i < data.length; i++) {
                Globals.mem.set(segAddress + i, data[i]);
            }
        } else {

            //TODO write OS Trap error
        }
    }

    /**
     *
     * Will clear out the memory located within the specified segment
     *
     * @param segNum - The unique identifier of the segment that is to be cleared
     */

    public void clearSegment(int segNum){

        if (segNum < segCount){
            for (int i = 0; i < this.segSize; i++){
                Globals.mem.set(this.segAddress + i, 0);
            }
        }
    }

    /**
     *
     * Will set the value at the specified segment address and number with the passed value
     *
     * @param address - Address of the memory location
     * @param num - Unique segment identification number
     * @param value - Value stored at specific address
     */

    public void setData(int address, int num, int value){
        if (address < this.segSize && num < segCount){
            int logicalAddress = this.segNum * this.segSize + this.segAddress;
            Globals.mem.set(logicalAddress, value);
        }
        else{
            //TODO: write OS Trap error
        }
    }

    /**
     *
     *
     * Will return the value stored at the specified segment address and number
     *
     * @param address - Address of the
     * @param num - Unique segment identification number
     * @return - The value stored at the passed address and segment number
     */

    public int getData(int address, int num){
        if (address < this.segSize && num < segCount){
            int logicalAddress = this.segNum * this.segSize + this.segAddress;
            return Globals.mem.get(logicalAddress);
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

}
