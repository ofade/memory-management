package management.memory.memory_management.memory_type;

import management.memory.memory_management.ProcessTable;
import management.memory.memory_management.ProcessTableEntry;
import management.memory.memory_management.memory_control.MemoryException;
import management.memory.memory_management.process.Process;

public class Memory {

	private byte[] memoryArray;

	private int availableSpace;

	private int size;

	private int index;

	public static final Integer DEFAULT_SIZE = 1024;

	public Memory(int capacity) {
		memoryArray = new byte[capacity];
		for (int i = 0; i < capacity; i++)
			memoryArray[i] = 0;
		size = capacity;
		availableSpace = capacity;
		index = 0;
	}

	public Memory() {
		this(DEFAULT_SIZE);
	}
	

	public byte[] getMemoryArray() {
		return memoryArray;
	}

	public void setMemoryArray(byte[] memoryArray) {
		this.memoryArray = memoryArray;
		this.size = memoryArray.length;
		this.availableSpace = size;
		index = 0;
	}

	public int getCapacity() {
		return size;
	}

	public int getAvailableSpace() {
		return availableSpace;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public byte getDataAtIndex(int index) {
		return memoryArray[index];
	}

	public byte readMemory() throws MemoryException {
		if (index < memoryArray.length && index >= 0) {
			byte b = memoryArray[index % memoryArray.length];
			index++;
			return b;
			
		} else {
			throw new MemoryException("Index is out of bounds");
		}

	}

	public void write(byte[] data) throws MemoryException {
		for (byte b : data) {
			if (index < this.size) {
				memoryArray[index++] = b;
				availableSpace--;
			} else {
				throw new MemoryException("Index is out of bounds");
			}
		}
	}
	
	public void write(Process process, ProcessTable processTable) {
		ProcessTableEntry entry = new ProcessTableEntry(process);
		setIndex(getEmptyCellIndex());
		entry.setBaseRegister(getIndex());
		try {
			write(process.getData());
			entry.setLimitRegister(getIndex()-1);
			processTable.addEntry(entry);
		} catch (MemoryException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteDataAtIndex(int dataIndex) {
		if (dataIndex >= 0 && dataIndex < size) {
			memoryArray[dataIndex] = 0;
			availableSpace++;
		}
	}
	
	private int getEmptyCellIndex() {
		int i = 0;
		while ( memoryArray[i] !=0 && i < size){
			i++;
		}
		return i;
	}
}
