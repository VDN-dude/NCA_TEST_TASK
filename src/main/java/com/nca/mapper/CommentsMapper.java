package com.nca.mapper;

import com.nca.dto.request.CommentsCreateRequestDTO;
import com.nca.dto.request.CommentsUpdateRequestDTO;
import com.nca.dto.response.CommentsResponseDTO;
import com.nca.entity.Comments;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * {@code CommentsMapper} for mapping {@link Comments} to DTOs and vice versa.
 */

@Mapper(componentModel = "spring")
public interface CommentsMapper {

    /**
     * {@code toEntity} creating new {@link Comments} and mapping fields from provided {@link CommentsCreateRequestDTO}.
     *
     * @param commentsCreateRequest
     */
    Comments toEntity(CommentsCreateRequestDTO commentsCreateRequest);

    /**
     * {@code updateFromDto} updating provided {@link Comments} by mapping fields from provided {@link CommentsUpdateRequestDTO}.
     *
     * @param commentsUpdateRequestDTO
     * @param comments
     */
    Comments updateFromDto(CommentsUpdateRequestDTO commentsUpdateRequestDTO,
                           @MappingTarget Comments comments);

    /**
     * {@code toEntity} creating new {@link CommentsResponseDTO} and mapping fields from provided {@link Comments}.
     *
     * @param comments
     */
    @Mapping(target = "newsId", source = "news.id")
    CommentsResponseDTO toDto(Comments comments);
}
