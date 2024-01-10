//package com.school.bookstore.integration;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.school.bookstore.models.dtos.AuthorDTO;
//import com.school.bookstore.models.dtos.BookDTO;
//import com.school.bookstore.models.entities.Book;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@AutoConfigureTestDatabase
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//@AutoConfigureMockMvc
//@Transactional
//public class BookController {
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    @WithMockUser(username = "admin", roles = {"STAFF"})
//    void createBookShouldPass() throws Exception {
//
//        BookDTO bookDTO = BookDTO.builder()
//                .title("Book")
//                .authorNameList(List.of("Author"))
//                .genreTagList(List.of("Genre"))
//                .publisher("Publisher")
//                .copiesAvailable(200)
//                .build();
//
//        mockMvc.perform(post("/api/books")
//                        .contentType(APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(bookDTO)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = {"STAFF"})
//    void duplicateBookShouldFail() throws Exception {
//
//        BookDTO bookDTO = BookDTO.builder()
//                .title("Book")
//                .authorNameList(List.of("Author"))
//                .genreTagList(List.of("Genre"))
//                .publisher("Publisher")
//                .copiesAvailable(200)
//                .build();
//
//        mockMvc.perform(post("/api/books")
//                        .contentType(APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(bookDTO)))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(post("/api/books")
//                        .contentType(APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(bookDTO)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = {"STAFF"})
//    void createBookWithoutAuthorShouldFail() throws Exception {
//
//        BookDTO bookDTO = BookDTO.builder()
//                .title("Book")
//                .genreTagList(List.of("Genre"))
//                .publisher("Publisher")
//                .copiesAvailable(200)
//                .build();
//
//        mockMvc.perform(post("/api/books")
//                        .contentType(APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(bookDTO)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = {"STAFF"})
//    void createBookShouldCreateAuthor() throws Exception {
//
//        BookDTO bookDTO = BookDTO.builder()
//                .title("Book")
//                .authorNameList(List.of("Author"))
//                .genreTagList(List.of("Genre"))
//                .publisher("Publisher")
//                .copiesAvailable(200)
//                .build();
//
//        mockMvc.perform(post("/api/books")
//                        .contentType(APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(bookDTO)))
//                        .andExpect(status().isOk());
//
//        MvcResult mvcResult = mockMvc.perform(get("/api/authors"))
//                .andExpect(status().isOk())
//                .andReturn();
//        String resultAsString = mvcResult.getResponse().getContentAsString();
//        List<AuthorDTO> authorList = objectMapper.readValue(resultAsString, new TypeReference<List<AuthorDTO>>() {});
//        assertFalse(authorList.isEmpty());
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = {"STAFF"})
//    void deleteBookShouldDeleteOrphanAuthorShouldPass() throws Exception {
//
//        BookDTO bookDTO = BookDTO.builder()
//                .title("Book")
//                .authorNameList(List.of("Author"))
//                .genreTagList(List.of("Genre"))
//                .publisher("Publisher")
//                .copiesAvailable(200)
//                .build();
//
//        MvcResult mvcResult = mockMvc.perform(post("/api/books")
//                        .contentType(APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(bookDTO)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String resultAsString = mvcResult.getResponse().getContentAsString();
//        BookDTO bookResponseDTO = objectMapper.readValue(resultAsString, new TypeReference<BookDTO>() {});
//
//        mockMvc.perform(delete("/api/books/".concat(bookResponseDTO.getId().toString())))
//                .andExpect(status().isOk());
//
//        MvcResult mvcAuthorResult = mockMvc.perform(get("/api/authors"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String mvcAuthorResultString = mvcAuthorResult.getResponse().getContentAsString();
//        List<AuthorDTO> authorList = objectMapper.readValue(mvcAuthorResultString, new TypeReference<List<AuthorDTO>>() {});
//
//        assertTrue(authorList.isEmpty());
//    }
//}
