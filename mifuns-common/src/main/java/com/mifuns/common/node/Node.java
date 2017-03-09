package com.mifuns.common.node;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by miguangying on 2017/3/9.
 */
public class Node implements Comparable<Node>{
    private Long id;
    private Long parentId;
    private String text;
    private String state;
    private NodeAttribute attributes;
    private List<Node> children = new LinkedList<Node>();
    private Integer order;//节点的状态

    public Node(Long id, Long parentId, String text, String state, NodeAttribute attributes,Integer order) {
        this.id = id;
        this.parentId = parentId;
        this.text = text;
        this.state = state;
        this.attributes = attributes;
        this.order = order;
    }
    //省略get/set方法
    public int compareTo(Node o) {
        if(order>o.order){
            return 1;
        }
        if(order<o.order){
            return -1;
        }
        return 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public NodeAttribute getAttributes() {
        return attributes;
    }

    public void setAttributes(NodeAttribute attributes) {
        this.attributes = attributes;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
