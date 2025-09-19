package com.intraron.model.common;

// Archivo: com/intraron/model/common/DomainPageable.java
public class DomainPageable {
    private final int page;
    private final int size;
    private final String sortBy;

    public DomainPageable(int page, int size, String sortBy) {
        this.page = page;
        this.size = size;
        this.sortBy = sortBy;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public String getSortBy() {
        return sortBy;
    }
}
