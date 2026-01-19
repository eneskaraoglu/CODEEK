package com.screenengine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pagination information for paginated API responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationInfo {

    private int page;
    private int pageSize;
    private long total;
    private int totalPages;

    /**
     * Calculate total pages from total records and page size
     */
    public static PaginationInfo of(int page, int pageSize, long total) {
        int totalPages = (int) Math.ceil((double) total / pageSize);
        return PaginationInfo.builder()
                .page(page)
                .pageSize(pageSize)
                .total(total)
                .totalPages(totalPages)
                .build();
    }
}
