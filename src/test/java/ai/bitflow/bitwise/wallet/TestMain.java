package ai.bitflow.bitwise.wallet;

import ai.bitflow.bitwise.wallet.services.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;

@Slf4j
@TestPropertySource(properties = "scheduling.enabled=false")
@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = BitwiseWalletApplication.class)
public class TestMain {

    @Autowired
    private SettingService settingService;

    @Test
    public void test() {
        settingService.debug();
//        OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
//        MemoryMXBean mem = ManagementFactory.getMemoryMXBean();
//        double cpuavr = os.getSystemLoadAverage();
//        int proc = os.getAvailableProcessors();
//        double memmax = mem.getNonHeapMemoryUsage().getMax();
//        double memuse = mem.getNonHeapMemoryUsage().getUsed();
//        double heapmax = mem.getHeapMemoryUsage().getMax();
//        double heapuse = mem.getHeapMemoryUsage().getUsed();
//        File file = new File("c:/");
//        double filemax = file.getTotalSpace();
//        double fileuse = file.getUsableSpace();
//        System.out.println("cpu avr " + cpuavr);
//        System.out.println("proc " + proc);
//        System.out.println("memmax " + memmax);
//        System.out.println("memuse " + memuse);
//        System.out.println("heapmax " + heapmax);
//        System.out.println("heapuse " + heapuse);
//        System.out.println("filemax " + filemax);
//        System.out.println("fileuse " + fileuse);
    }
}
