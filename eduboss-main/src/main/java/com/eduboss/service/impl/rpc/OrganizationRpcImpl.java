package com.eduboss.service.impl.rpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.boss.rpc.base.dto.Node;
import org.boss.rpc.eduboss.service.OrganizationRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.domain.Organization;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.UserService;

@Service("organizationRpcImpl")
public class OrganizationRpcImpl implements OrganizationRpc {

	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 获取集团和所有分公司
	 */
	@Override
	public List<Map<String, String>> getGroupAndAllBrench() {
		List<Organization> list = organizationService.getGroupAndAllBrench();
		List<Map<String, String>> retList = new ArrayList<Map<String,String>>();
		for (Organization org : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", org.getId());
			map.put("name", org.getName());
			map.put("parentId", org.getParentId());
			retList.add(map);
		}
		return retList;
	}

	@Override
	public Node getOrganizationTree() {
		List<Organization> list = userService.getOrganizationTree("GROUNP,BRENCH,CAMPUS");
		List<Node> retList = new ArrayList<Node>();
		Node root = null;
		for (Organization org : list) {
			String parentId = org.getParentId() != null ? org.getParentId() : "0";
			Node node = new Node(org.getId(), parentId, org.getName());
			if (parentId.equals("0")) {
				root = node;
			} else {
				retList.add(node);
			}
		}
		return this.buildListToTree(retList, root);
	}
	
    private Node buildListToTree(List<Node> dirs, Node root) {
        root.setChildren(findChildren(root, dirs));
        return root;
    }
	
    private List<Node> findChildren(Node root, List<Node> allNodes) {
        List<Node> children = new ArrayList<Node>();
        for (Node comparedOne : allNodes) {
            if (comparedOne.getParentId().equals(root.getId())) {
                comparedOne.setLevel(root.getLevel() + 1);
                children.add(comparedOne);
            }
        }
        allNodes.removeAll(children);
        for (Node child : children) {
            List<Node> tmpChildren = findChildren(child, allNodes);
            if (tmpChildren == null || tmpChildren.size() < 1) {
                child.setLeaf(true);
            } else {
                child.setLeaf(false);
            }
            child.setChildren(tmpChildren);
        }
        return children;
    }
	
}
