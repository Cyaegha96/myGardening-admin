package com.ggirick.gardening_admin_backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggirick.gardening_admin_backend.dto.board.BoardCommentDTO;
import com.ggirick.gardening_admin_backend.dto.board.BoardDTO;
import com.ggirick.gardening_admin_backend.dto.board.BoardFileDTO;
import com.ggirick.gardening_admin_backend.mappers.board.BoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper boardMapper;
    private final ObjectMapper mapper;

    public record BoardQueryResult(
            List<BoardDTO> list,
            int offset,
            int limit,
            int total
    ) {}

    public BoardService.BoardQueryResult getBoards(String sort, String range, String filter)
            throws JsonProcessingException {
        int offset = 0;
        int limit = 10;

        if (range != null) {
            int[] r = mapper.readValue(range, int[].class);
            offset = r[0];
            limit = r[1] - r[0] + 1;
        }

        // sort 파싱
        String sortField = "createdAt";
        String sortOrder = "ASC";

        if (sort != null) {
            try {
                Map<String, String> s = mapper.readValue(
                        sort,
                        new TypeReference<Map<String, String>>() {}
                );
                sortField = s.getOrDefault("field", sortField);
                sortOrder = s.getOrDefault("order", sortOrder);
            } catch (Exception e) {
                String[] s = mapper.readValue(sort, String[].class);
                sortField = s[0];
                sortOrder = s[1];
            }
        }

        // filter 파싱
        Map<String, Object> filters = new HashMap<>();
        if (filter != null) {
            filters = mapper.readValue(filter, new TypeReference<>() {});
        }

        List<BoardDTO> list =
                boardMapper.findBoards(offset, limit, sortField, sortOrder, filters);
        int total = boardMapper.countBoards(filters);

        return new BoardService.BoardQueryResult(list, offset, limit, total);
    }

    public BoardDTO createBoard(BoardDTO boardDTO) {
        boardMapper.insertBoard(boardDTO);
        return boardDTO;
    }

    public BoardDTO getBoard(int id) {
        BoardDTO board = boardMapper.selectBoardById(id);
        if (board == null) {
            return null;
        }

        List<BoardCommentDTO> comments = boardMapper.selectCommentsByBoardId(id);
        List<BoardFileDTO> files = boardMapper.selectFilesByBoardId(id);

        board.setComments(comments);
        board.setFiles(files);

        return board;
    }

    public BoardDTO updateBoard(BoardDTO boardDTO) {
        boardDTO.setStatus(boardDTO.getStatus().toLowerCase(Locale.ROOT));
        boardMapper.updateBoard(boardDTO);
        return boardDTO;
    }

    public BoardDTO deleteBoard(int id) {
        return boardMapper.deleteBoard(id);

    }

}
