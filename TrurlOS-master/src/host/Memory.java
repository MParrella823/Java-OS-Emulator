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
        memory[address] = value;
    }

    public int get(int address) {
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
