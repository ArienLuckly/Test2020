package top.arien;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("top.arien.dao")
public class SpringbootMybatisplusvueApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootMybatisplusvueApplication.class, args);
    }

}
