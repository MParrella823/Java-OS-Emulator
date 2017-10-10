package host;

import java.util.Arrays;

public class Memory {
    public int[] memory;

     
     

    //Constructor to initialize memory size to 256 for Project 2
    public Memory(){

       this.memory = new int[256];

    }

    //Constructor to initialize memory size to an pre-determined size
    public Memory(int capacity) {
        memory = new int[capacity]; // Could we just have this set to a default capacity of 256 for this project?
    }


    public void set(int address, int value) {
        check(address);
        memory[address] = value;
    }

    public int get(int address) {
        check(address);
        return memory[address];
    }

    // Check should be handled by MMU class..
    public void check(int address) {
        if (address < 0 || address >= memory.length) {
            //TODO add trap error here..
            System.out.println("Illegal Memory Access: " + address + " for memory of capacity " + memory.length);
           // System.exit(0); don't think we need this
        }
    }

    public int capacity() {
        return memory.length;
    }

    public int[] from(int start) {
        int count=0;
        return Arrays.copyOfRange(memory, start, memory.length);
    }
}
