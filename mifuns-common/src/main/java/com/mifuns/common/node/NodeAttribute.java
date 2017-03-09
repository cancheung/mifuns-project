package com.mifuns.common.node;

/**
 * Created by miguangying on 2017/3/9.
 */
public class NodeAttribute {
    private String url;
    private Long id;
    public NodeAttribute(String url, Long id) {
        this.url = url;
        this.id = id;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
