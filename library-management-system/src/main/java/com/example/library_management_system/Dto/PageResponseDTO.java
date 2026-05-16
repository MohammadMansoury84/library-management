package com.example.library_management_system.Dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponseDTO<T> {
    private final List<T> content;
    private final int       currentPage;
    private final int       totalPages;
    private final long      totalElements;
    private final int       pageSize;
    private final boolean   first;
    private final boolean   last;
    private final boolean   hasNext;
    private final boolean   hasPrevious;

    public static <T> PageResponseDTO<T> of(Page<T> page) {
        PageResponseDTO<T> pageResponseDTO = new PageResponseDTO<>(page);
        return pageResponseDTO;
    }

    private PageResponseDTO(Page<T> page) {
        this.content       = page.getContent();
        this.currentPage   = page.getNumber();
        this.totalPages    = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.pageSize      = page.getSize();
        this.first         = page.isFirst();
        this.last          = page.isLast();
        this.hasNext       = page.hasNext();
        this.hasPrevious   = page.hasPrevious();
    }
}
