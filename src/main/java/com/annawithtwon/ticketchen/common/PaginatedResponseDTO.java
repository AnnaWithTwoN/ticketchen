package com.annawithtwon.ticketchen.common;

import com.annawithtwon.ticketchen.artist.Artist;

import java.util.List;

public class PaginatedResponseDTO<T> {

    private List<T> items;
    private int page;
    private int size;
    private int totalPages;

    public PaginatedResponseDTO() {
    }

    public PaginatedResponseDTO(List<T> items, int page, int size, int totalPages) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.totalPages = totalPages;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

}
