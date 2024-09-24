package com.nca.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsCreateRequestDTO {

    @NotNull
    @Size(min = 1, max = 150)
    private String title;

    @NotNull
    @Size(min = 1, max = 2000)
    private String text;
}
