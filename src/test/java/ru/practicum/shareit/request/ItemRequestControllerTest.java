package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemRequestService itemRequestService;

    private static ItemRequestDto itemRequestDto;

    private final ItemRequestDto itemRequestDtoEmpty = new ItemRequestDto();

    @BeforeAll
    static void setUp() {
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("description");
        itemRequestDto.setCreated(LocalDateTime.MIN);
    }

    @Test
    void createNew() throws Exception {
        when(itemRequestService.create(anyLong(), any())).thenReturn(itemRequestDto);
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));
    }

    @Test
    void getBadRequestForCreateWhenHasNoHeader() throws Exception {
        when(itemRequestService.create(anyLong(), any())).thenReturn(itemRequestDto);
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBadRequestForCreateWhenItemDtoIsIncorrect() throws Exception {
        when(itemRequestService.create(anyLong(), any())).thenReturn(itemRequestDto);
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDtoEmpty))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAllByOwner() throws Exception {
        List<ItemRequestDtoWithReply> itemRequestDtos = new ArrayList<>();
        when(itemRequestService.findAllByOwner(anyLong())).thenReturn(itemRequestDtos);
        mvc.perform(get("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getBadRequestForFindAllByOwnerWhenHasNoHeader() throws Exception {
        List<ItemRequestDtoWithReply> itemRequestDtos = new ArrayList<>();
        when(itemRequestService.findAllByOwner(anyLong())).thenReturn(itemRequestDtos);
        mvc.perform(get("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBadRequestForFindAllForOneUserWhenHasNoHeader() throws Exception {
        List<ItemRequestDtoWithReply> itemRequestDtos = new ArrayList<>();
        when(itemRequestService.findAllForOneUser(anyLong(), any())).thenReturn(itemRequestDtos);
        mvc.perform(get("/requests/all")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAllForOneUser() throws Exception {
        List<ItemRequestDtoWithReply> itemRequestDtos = new ArrayList<>();
        when(itemRequestService.findAllForOneUser(anyLong(), any())).thenReturn(itemRequestDtos);
        mvc.perform(get("/requests/all")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findById() throws Exception {
        ItemRequestDtoWithReply itemRequestDtoWithReply = new ItemRequestDtoWithReply();
        when(itemRequestService.findById(anyLong(), anyLong())).thenReturn(itemRequestDtoWithReply);
        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getBadRequestForFindByIdWithoutHeader() throws Exception {
        ItemRequestDtoWithReply itemRequestDtoWithReply = new ItemRequestDtoWithReply();
        when(itemRequestService.findById(anyLong(), anyLong())).thenReturn(itemRequestDtoWithReply);
        mvc.perform(get("/requests/1"))
                .andExpect(status().isBadRequest());
    }
}