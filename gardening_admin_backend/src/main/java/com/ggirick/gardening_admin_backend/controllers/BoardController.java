package com.ggirick.gardening_admin_backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ggirick.gardening_admin_backend.dto.board.BoardDTO;
import com.ggirick.gardening_admin_backend.services.BoardService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

  private final BoardService boardService;

    @GetMapping()
    public List<BoardDTO>  getBoards(    @RequestParam(required = false) String sort,
                                         @RequestParam(required = false) String range,
                                         @RequestParam(required = false) String filter,
                                         HttpServletResponse response)
            throws JsonProcessingException {

        BoardService.BoardQueryResult result =  boardService.getBoards(sort, range, filter);


        response.setHeader(
                "Content-Range",
                String.format("users %d-%d/%d",
                        result.offset(),
                        result.offset() + result.list().size() - 1,
                        result.total())
        );
        response.setHeader("Access-Control-Expose-Headers", "Content-Range");

        return result.list();

    }

    @PostMapping
    public BoardDTO createBoard(@RequestBody BoardDTO boardDTO) {
        return boardService.createBoard(boardDTO);
    }

    @GetMapping("/{id}")
    public BoardDTO getBoard(@PathVariable("id") int id) throws  JsonProcessingException {
        return boardService.getBoard(id);
    }

    @PutMapping("/{id}")
    public BoardDTO updateBoard(@PathVariable("id") int id, @RequestBody BoardDTO boardDTO) throws  JsonProcessingException {
        boardDTO.setId(id);
        return boardService.updateBoard(boardDTO);
    }

    @DeleteMapping("/{id}")
    public BoardDTO deleteBoard(@PathVariable("id") int id) throws  JsonProcessingException {
        return boardService.deleteBoard(id);
    }

}
