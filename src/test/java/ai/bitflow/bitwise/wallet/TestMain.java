package ai.bitflow.bitwise.wallet;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;

public class TestMain {

    public static void main(String[] args) {
        OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
        MemoryMXBean mem = ManagementFactory.getMemoryMXBean();
        double cpuavr = os.getSystemLoadAverage();
        int proc = os.getAvailableProcessors();
//        double data1 = os.getSystemLoadAverage() / os.getAvailableProcessors();
//        mem.getFreePhysicalMemorySize();
//        mem.getTotalPhysicalMemorySize();
        double memmax = mem.getNonHeapMemoryUsage().getMax();
        double memuse = mem.getNonHeapMemoryUsage().getUsed();
        double heapmax = mem.getHeapMemoryUsage().getMax();
        double heapuse = mem.getHeapMemoryUsage().getUsed();
        File file = new File("c:/");
        double filemax = file.getTotalSpace();
        double fileuse = file.getUsableSpace();
//        double disk = file.getUsableSpace() / file.getTotalSpace();
        System.out.println("cpu avr " + cpuavr);
        System.out.println("proc " + proc);
        System.out.println("memmax " + memmax);
        System.out.println("memuse " + memuse);
        System.out.println("heapmax " + heapmax);
        System.out.println("heapuse " + heapuse);
        System.out.println("filemax " + filemax);
        System.out.println("fileuse " + fileuse);
    }
}
