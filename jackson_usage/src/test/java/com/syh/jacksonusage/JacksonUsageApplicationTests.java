package com.syh.jacksonusage;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syh.jacksonusage.controller.JsonViewController;
import com.syh.jacksonusage.model.Message;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JacksonUsageApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Test
	public void serialize() throws Exception {
		String ret = mvc.perform(get("/jsonview/summary"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();
		System.out.println(ret);
	}

	@Test
	public void deserialize() throws Exception {
		String ret = mvc.perform(get("/jsonview/summary"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();
		mvc.perform(post("/jsonview/summary_deserialize")
				.contentType(MediaType.APPLICATION_JSON)
				.content(ret))
				.andExpect(status().isAccepted());
	}

}
