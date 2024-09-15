package com.example.customersupport.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.example.customersupport.config.TestContainersConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.example.customersupport.dto.CorporationRegistrationDto;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(classes = TestContainersConfiguration.class)
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
public class OpenAiChatControllerTest {
    static {
        System.setProperty("testcontainers.ryuk.disabled", "true");
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testRegistrationController() throws Exception {
        CorporationRegistrationDto registrationDto = new CorporationRegistrationDto();
        registrationDto.setCorpName("TestCorp");
        registrationDto.setEmail("test@example.com");
        registrationDto.setPassword("secureTestPassword");

        String registrationDtoJson = objectMapper.writeValueAsString(registrationDto);

        this.mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationDtoJson)) // Add the JSON Request body
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L)) //1 is the preauthorized admin user
                .andExpect(jsonPath("$.corpName").value(registrationDto.getCorpName()))
                .andExpect(jsonPath("$.email").value(registrationDto.getEmail()));
    }
}
