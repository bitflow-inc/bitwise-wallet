package ai.bitflow.bitwise.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * 스프링 부트 응용프로그램 진입점
 * 애플리케이션 글로벌 설정
 */
@SpringBootApplication
@EnableScheduling
public class BitwiseWalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(BitwiseWalletApplication.class
                , args);
    }
    
}
