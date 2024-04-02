package com.izeno.backendapi;

import com.izeno.backendapi.util.CommonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendApiApplicationTests {

	@Test
	void contextLoads() {
		System.out.println(CommonUtils.getCurrentDate());
	}

}
