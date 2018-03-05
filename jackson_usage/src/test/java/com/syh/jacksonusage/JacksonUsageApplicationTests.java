package com.syh.jacksonusage;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syh.jacksonusage.model.ValidMessage;
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


	@Test
    public void testValid() throws Exception {
	    ObjectMapper objectMapper = new ObjectMapper();
	    String json = objectMapper.writeValueAsString(new ValidMessage(1L, "123", "234"));

        mvc.perform(post("/jsonview/valid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isAccepted());

    }

    @Test
    public void testValid_error() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(new ValidMessage(1L, "123", "234dsfodsfodsbfodsbfos"));

        mvc.perform(post("/jsonview/valid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

}
