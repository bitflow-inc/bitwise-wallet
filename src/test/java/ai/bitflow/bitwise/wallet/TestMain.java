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
    }
}
