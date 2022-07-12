package com.example.project.mappers;

import com.example.project.domain.Comment;
import com.example.project.model.CommentDTO;
import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(builder = @Builder(disableBuilder = true),
        uses = UserMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class CommentMapper {
    public abstract CommentDTO mapCommentToCommentDTO(Comment comment);

    public abstract Comment mapCommentDTOTOComment(CommentDTO commentDTO);
}
