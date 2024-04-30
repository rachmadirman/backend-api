package com.izeno.backendapi;

import com.izeno.backendapi.controller.FileController;
import com.izeno.backendapi.utils.CommonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendApiApplicationTests {

	@Test
	void contextLoads() throws Exception {
	System.out.println("Current Date : " + CommonUtils.getCurrentDate());
	//	System.out.println(CommonUtils.stringToBase64("test"));

	//System.out.println(FileController.uploadFiles());
		System.out.println(CommonUtils.formatDate(CommonUtils.getCurrentDate()));
	}

}
