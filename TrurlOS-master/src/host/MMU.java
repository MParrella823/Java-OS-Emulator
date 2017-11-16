package host;
import jdk.nashorn.internal.objects.Global;
import os.Interrupt;
import util.Globals;
import os.Kernel;


/**
 * MMU (Memory Management Unit) class will create and manage memory segments, as well as
 * make all calls to the Memory class, when data needs to be written
 * or read to/from memory.
 *
 */

public class MMU {

    private static int generatedSegNum = 0; //Used for auto generation unique segment numbers
    private static int segCount = 3; //Total number of segments
    /**
     *
     * Segment subclass will help keep track of vital segment parameters
     *
     */
    public class Segment{
        private int startAddr;
        private int endAddr;
        private int segSize;
        private boolean isFree;
        private int segNum;


        /**
         *
         * Segment Constructor
         *
         * @param startAddr Starting address of Segment in main memory
         */
        public Segment(int startAddr){
            this.startAddr = startAddr;
            this.segNum = generatedSegNum;
            this.endAddr = startAddr + 255;
            this.isFree = true;
            this.segSize = 256;
            generatedSegNum++;
            segCount++;
        }

        public int getSegNum(){
            return this.segNum;
        }

        public int getStartAddr(){
            return this.startAddr;
        }

        public int getEndAddr(){
            return this.endAddr;
        }


    }//End Segment Class


    public static int getNextSegment() {
        for (int i = 0; i < segArray.length; i++) {
            if (segArray[i].isFree) {
                return segArray[i].getSegNum();
            }
        }
        return -1;
    }

    public static Segment[] segArray = new Segment[3];

    public MMU(){

        //Create 3 segments in memory (each 256 bytes in size);
        Segment s1 = new Segment(0);
        Segment s2= new Segment(256);
        Segment s3 = new Segment(512);

        //Create array of segments for easy searching through segment objects

        segArray[0] = s1;
        segArray[1] = s2;
        segArray[2] = s3;

    }

    public void clearmem(){
        for (int i = 0; i < Globals.mem.capacity(); i++){
            Globals.mem.set(i, 0);

        }

        for (int i = 0; i < segArray.length; i ++){
            segArray[i].isFree = true;
        }
    }

    public Segment getSegment(int num){

        if (num < segCount){
            for (int i = 0; i < segArray.length; i++){
                if (segArray[i].getSegNum() == num){
                    return segArray[i];
                }
            }

        }

        return null;

    }




    public int getSegmentStart(int num){
        int value = -1;
        for (int i = 0; i < segArray.length; i++){
            if (num == segArray[i].segNum){
                value =  segArray[i].getStartAddr();
            }
        }
        return value;
    }

    /**
     *
     * Loads the passed array of integers into the designated segment
     *
     * @param segNum Segment Number
     * @param data - The array of integers to write into the segment
     */

    public void loadIntoSegment(int segNum, int[] data) {
        Segment test = getSegment(segNum);
        if (segNum < segCount && data.length <= test.segSize) {

            for (int i = 0; i < data.length; i++) {
                Globals.mem.set(test.startAddr + i, data[i]);

            }
            test.isFree = false;
        }else {

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
            for (int i = getSegment(segNum).getStartAddr(); i < (getSegment(segNum).getEndAddr()); i++) {
                Globals.mem.set(i, 0);
            }
            getSegment(segNum).isFree = true;
        }
        else{
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
        int test = address / getSegment(num).segSize;
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
             int offset = address - (segNum * getSegment(segNum).segSize);
             Globals.mem.set(((segNum*getSegment(segNum).segSize) + offset), value);
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
           int offset = address - (segNum * getSegment(segNum).segSize);
            return Globals.mem.get((segNum * getSegment(segNum).segSize) + offset);
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
            switch (num){
                case 0:
                    return 0;

                case 1:
                    return 256;

                case 2:
                    return 512;

                default:
                    return -1;
            }
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
        int test = segNum * getSegment(segNum).segSize + 255;
        return test;
    }
}