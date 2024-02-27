package com.beyond.infinity.demo.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class JiraEpicResponse {

    @JsonProperty("issues")
    private List<Epic> epicList;

    public List<Epic> getEpicList() {
        return epicList;
    }

    public void setEpicList(List<Epic> epicList) {
        this.epicList = epicList;
    }
}
