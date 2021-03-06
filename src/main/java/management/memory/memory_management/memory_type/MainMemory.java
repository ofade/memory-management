package management.memory.memory_management.memory_type;

import management.memory.memory_management.OperatingSystem;
import management.memory.memory_management.ProcessTable;
import management.memory.memory_management.ProcessTableEntry;
import management.memory.memory_management.memory_control.MemoryException;
import management.memory.memory_management.process.Process;
import management.memory.memory_management.process.ProcessState;

public class MainMemory extends Memory{
	
	private OperatingSystem operatingSystem;
	
	private ProcessTable processTable;

	public MainMemory(int capacity, ProcessTable table, OperatingSystem operatingSystem) throws MemoryException {
		super(capacity);
		this.processTable = table;
		this.operatingSystem = operatingSystem;
		write(this.operatingSystem.getData());
	}
	
	public MainMemory(ProcessTable table, OperatingSystem operatingSystem) throws MemoryException {
		this(DEFAULT_SIZE, table, operatingSystem);
	}
	
	public MainMemory() {
		this.processTable = new ProcessTable();
		this.operatingSystem = new OperatingSystem();
	}

	public ProcessTable getProcessTable() {
		return processTable;
	}

	public void setProcessTable(ProcessTable processTable) {
		this.processTable = processTable;
	}
	
	public OperatingSystem getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(OperatingSystem operatingSystem) {
		this.operatingSystem = operatingSystem;
	}
	
	
	public void moveProcessToDisk(HardDisk hd,  Process process) {
		ProcessTableEntry processTableEntry = processTable.findProcessEntry(process);
		int baseReg = processTableEntry.getBaseRegister();
		int limitReg = processTableEntry.getLimitRegister();
		process.setProcessState(ProcessState.IDLE);
		
		for (int i = baseReg; i<=limitReg; i++){
			deleteDataAtIndex(i);
		}
		processTable.removeEntry(processTableEntry);
		
		//if process is terminated, then we don't want to move it to disk. 
		if (!process.getProcessState().equals(ProcessState.TERMINATED)) {
			hd.write(process, hd.getProcessTable());
		}
	}
}
