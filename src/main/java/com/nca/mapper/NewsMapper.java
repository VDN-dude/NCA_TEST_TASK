package com.nca.mapper;

import com.nca.dto.request.NewsCreateRequestDTO;
import com.nca.dto.request.NewsUpdateRequestDTO;
import com.nca.dto.response.NewsResponseDTO;
import com.nca.dto.response.NewsResponseNoCommentsDTO;
import com.nca.entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * {@code NewsMapper} for mapping {@link News} to DTOs and vice versa.
 */

@Mapper(componentModel = "spring")
public interface NewsMapper {

    /**
     * {@code toEntity} creating new {@link News} and mapping fields from provided {@link NewsCreateRequestDTO}.
     *
     * @param newsCreateRequest
     */
    News toEntity(NewsCreateRequestDTO newsCreateRequest);

    /**
     * {@code updateFromDto} updating provided {@link News} by mapping fields from provided {@link NewsUpdateRequestDTO}.
     *
     * @param newsUpdateRequest
     * @param newsUpdateRequest
     */
    News updateFromDto(NewsUpdateRequestDTO newsUpdateRequest, @MappingTarget News news);

    /**
     * {@code toEntity} creating new {@link NewsResponseNoCommentsDTO} and mapping fields from provided {@link News}.
     *
     * @param news
     */
    NewsResponseNoCommentsDTO toDtoWithoutComments(News news);

    /**
     * {@code toEntity} creating new {@link NewsResponseDTO} and mapping fields from provided {@link News}.
     *
     * @param news
     */
    @Mapping(target = "comments", ignore = true)
    NewsResponseDTO toDtoWithComments(News news);
}
