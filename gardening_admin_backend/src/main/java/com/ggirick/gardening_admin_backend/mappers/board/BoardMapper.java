package com.ggirick.gardening_admin_backend.mappers.board;

import com.ggirick.gardening_admin_backend.dto.board.BoardCommentDTO;
import com.ggirick.gardening_admin_backend.dto.board.BoardDTO;
import com.ggirick.gardening_admin_backend.dto.board.BoardFileDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {
    List<BoardDTO> findBoards(int offset, int limit, String sortField, String sortOrder, Map<String, Object> filters);

    int countBoards(Map<String, Object> filters);

    void insertBoard(BoardDTO boardDTO);

    BoardDTO selectBoardById(@Param("id") int id);

    List<BoardCommentDTO> selectCommentsByBoardId(@Param("id") int id);

    List<BoardFileDTO> selectFilesByBoardId(@Param("id") int id);

    void updateBoard(BoardDTO boardDTO);

    BoardDTO deleteBoard(int id);

    List<BoardDTO> findBoardsByIds(@Param("ids") List<Integer> ids);
}
