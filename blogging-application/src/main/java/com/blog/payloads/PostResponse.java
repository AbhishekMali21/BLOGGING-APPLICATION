package com.blog.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostResponse {

    private List<PostDTO> content;
    private Integer pageNumber;
    private Integer pageSize;
    private long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
