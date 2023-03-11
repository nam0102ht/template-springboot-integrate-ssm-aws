package component.com.ntnn.controller;

import component.com.ntnn.ComponentTest;
import com.ntnn.builder.RequestHeaderBuilder;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ComponentTest
@AutoConfigureMockMvc
public class AccountController {

    @Autowired
    private MockMvc mockMvc;

    public static HttpHeaders requestHeader;

    @BeforeEach
    public void setUp() {
        requestHeader = RequestHeaderBuilder.defaultBuilder().build();
    }

    @Test
    public void callAccount() throws Exception {
        MvcResult result = mockMvc
                .perform(post("/api/v2/account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(requestHeader)
                        .content("{" +
                                "    \"username\": \"nam0102ht\"," +
                                "    \"fullName\" : \"Radan Nguyen\"," +
                                "    \"email\" : \"radan.nguyen@gmail.com\"," +
                                "    \"phone\": \"+84919123456\"," +
                                "    \"datOfBirth\": \"1998-04-25T00:00:00.0\"" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON)).andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is("Success")));
    }
}
