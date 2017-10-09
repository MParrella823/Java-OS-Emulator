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

    /**
     *
     * Creates a segment in memory
     *
     * @param size - The desired size of the segment
     */

    public void createSegment(int size) {
        if (size > Globals.FreeSpace) {
            //TODO write OS Trap error
        } else {
            this.segSize = size;
            Globals.FreeSpace -= size;
            Globals.AllocatedSpace += size;
            this.segNum = segCount;
            segAddress = this.segNum * this.segSize;
            segCount++;
        }
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
     * Provides the size of the memory segment
     *
     * @return int - size of the segment
     */

    public int getSegmentSize(){
        return this.segSize;
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
