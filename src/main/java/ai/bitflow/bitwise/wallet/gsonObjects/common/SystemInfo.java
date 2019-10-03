package ai.bitflow.bitwise.wallet.gsonObjects.common;

import lombok.Data;

@Data
public class SystemInfo {

    private String osName;
    private double cpuPercent;
    private double memoryPercent;
    private double diskPercent;

    private long memoryMax;
    private long memoryUsage;
    private long diskMax;
    private long diskUsage;

    public SystemInfo(String osName, double cpuPercent, double memoryPercent, double diskPercent,
              long memoryMax, long memoryUsage, long diskMax, long diskUsage) {

        this.osName = osName;
        this.cpuPercent = cpuPercent;
        this.memoryPercent = memoryPercent;
        this.diskPercent = diskPercent;
        this.memoryMax = memoryMax;
        this.memoryUsage = memoryUsage;
        this.diskMax = diskMax;
        this.diskUsage = diskUsage;
    }

}
