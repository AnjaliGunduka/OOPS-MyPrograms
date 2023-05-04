package com.junodx.api.models.commerce;

import java.util.List;

public class PriceBook {
    private String id;
    private List<PriceBookEntry> entries;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<PriceBookEntry> getEntry() {
        return entries;
    }

    public void setEntry(List<PriceBookEntry> entry) {
        this.entries = entry;
    }
}
