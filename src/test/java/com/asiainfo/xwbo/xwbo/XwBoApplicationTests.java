package com.asiainfo.xwbo.xwbo;

//import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class XwBoApplicationTests {

//    @Test
//    void contextLoads() {
//    }

    public static void main(String[] args) {
        Map<Long, String> m = new HashMap<>();
        m.put(new Long(300000L), "1");
        m.put(new Long(300000L), "1");
        System.out.println(m);
    }
}
