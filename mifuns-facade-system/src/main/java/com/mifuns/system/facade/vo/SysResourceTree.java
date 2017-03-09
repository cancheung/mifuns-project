package com.mifuns.system.facade.vo;

import com.mifuns.common.node.Node;
import com.mifuns.common.node.NodeAttribute;
import com.mifuns.system.facade.entity.SysResource;

import java.util.*;

/**
 * Created by miguangying on 2017/3/9.
 */
public class SysResourceTree {
    private List<Node> nodes = new LinkedList<Node>();
    private Node root = null;//根节点
    public SysResourceTree(List<SysResource> resources){
        for(SysResource resource:resources){
            Node node = new Node(resource.getResourceId(),resource.getParentId(),resource.getResourceName(),"open",
                    new NodeAttribute(null==resource.getUrl()?"":resource.getUrl(),resource.getResourceId()),
                    resource.getSerialNum());
            nodes.add(node);
            if(node.getId()==0){
                root = node;
            }
        }
    }

    public List<Node> build(){
        buildTree(root);
        List<Node> result = new ArrayList<Node>();
        result.add(root);
        return result;
    }
    private void buildTree(Node parent){
        Node node = null;
        for(Iterator<Node> ite = nodes.iterator(); ite.hasNext();){
            node = ite.next();
            if(Objects.equals(node.getParentId(), parent.getId())){
                parent.getChildren().add(node);
                buildTree(node);
            }
        }
    }

}
