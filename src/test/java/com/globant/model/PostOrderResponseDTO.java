package com.globant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostOrderResponseDTO {
    private long id;
    private long petId;
    private int quantity;
    private String shipDate;
    private boolean complete;
}
