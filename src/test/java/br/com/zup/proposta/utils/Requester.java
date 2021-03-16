package br.com.zup.proposta.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Component
public class Requester {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    public ResultActions post(String url, Object obj) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(obj)));
    }

    public ResultActions post(String url, String json) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }

    public ResultActions get(String url, Long id) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url, id));
    }

    public String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }


}
