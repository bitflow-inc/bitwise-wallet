package ai.bitflow.bitwise.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @author 
 * @since 
 */
@SpringBootApplication
@EnableScheduling
public class BitwiseWalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(BitwiseWalletApplication.class
                , args);
    }
    
}
