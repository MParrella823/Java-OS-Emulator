package host;

import java.util.Arrays;
import host.TurtleWorld;
import util.Globals;

public class Memory {
    public int[] memory;
    public boolean seg_used;
     

    //Constructor to initialize memory size to 256 for Project 2
    public Memory(){
       this.memory = new int[256];
    }

    //Constructor to initialize memory size to an pre-determined size
    public Memory(int capacity) {
        memory = new int[capacity]; // Could we just have this set to a default capacity of 256 for this project?
    }


    public void set(int address, int value) {
        Globals.world.memWrite = true;
        memory[address] = value;
    }

    public int get(int address) {
        Globals.world.memWrite = false;
        return memory[address];
    }

    public int capacity() {
        return memory.length;
    }

    public int[] from(int start) {
        int count=0;
        return Arrays.copyOfRange(memory, start, memory.length);
    }
}
