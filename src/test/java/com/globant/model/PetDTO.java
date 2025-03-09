package com.globant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetDTO {
    private int id;
    private CategoryTagDTO category;
    private String name;
    private List<URL> photoUrls;
    private List<CategoryTagDTO> tags;
    private String status;
}
