package ai.bitflow.bitwise.wallet.utils;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.RpcError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class Logger {

    public void info(String method, String res) {
        log.info("[" + method + "] " + res);
    }

    public void success(String method, String res) {
        log.info("[" + method + "] " + res);
    }

    public void debug(String method, String res) {
        log.debug("[" + method + "] " + res);
    }

    public void warn(String method, String res) {
        log.warn("[" + method + "] " + res);
    }

    public void error(String method, Exception e) {
        log.error("[" + method + "] " + e.getMessage());
//        e.printStackTrace();
    }

    public void error(String method, RpcError e) {
        log.error("[" + method + "] [" + e.getCode() + "] "
                + e.getMessage());
    }

    public void error(String method, String msg) {
        log.error("[" + method + "] " + msg);
    }

}
