package ru.edu.springweb.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.edu.springweb.configuration.WebConfiguration;

import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = WebConfiguration.class)
class InfoControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void itShouldGetAuthor() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/author").contentType("text/html"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(result.contains("<h1>Автор</h1>"));
        assertTrue(result.contains("<h2>Киселев Тимофей</h2>"));
        assertTrue(result.contains("<a href=\"/hobby\">Посмотреть хобби</a>"));
    }

    @Test
    void itShouldGetHobby() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/hobby").contentType("text/html"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(result.contains("<h1>Хобби</h1>"));
        assertTrue(result.contains("<h2>Программирование, инвестиции, спорт</h2>"));
    }

    @Test
    void itShouldFindNothing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/wrongPath").contentType("text/html"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}