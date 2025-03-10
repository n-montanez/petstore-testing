package com.globant.model;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PetDTO {
    private long id;
    private CategoryTagDTO category;
    private String name;
    private List<String> photoUrls;
    private List<CategoryTagDTO> tags;
    private String status;
}
