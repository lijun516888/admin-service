package com.knowledge;



import com.acooly.core.common.BootApp;
import com.acooly.core.common.boot.Apps;
import org.springframework.boot.SpringApplication;


/**
 * @author qiubo
 */
@BootApp(sysName = "knowledge", httpPort = 8080)
public class Main {
    public static void main(String[] args) {
        Apps.setProfileIfNotExists("dev");
        Apps.setLogPath("/Users/lijun/Work Root/logs/");
        new SpringApplication(Main.class).run(args);
    }
}