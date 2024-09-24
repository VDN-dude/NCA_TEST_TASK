package com.nca.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponseDTO {

    private long id;
    private String title;
    private String text;

    private Page<CommentsResponseDTO> comments;

    public NewsResponseDTO(long id, String title, String text) {
        this.text = text;
        this.title = title;
        this.id = id;
    }
}
