package com.elias.GestoBar.dto.categoryDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDTO {

    private Integer categoryId;
    private String name;
}
