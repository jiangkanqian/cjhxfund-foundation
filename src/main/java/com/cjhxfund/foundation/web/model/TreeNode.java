package com.cjhxfund.foundation.web.model;

import java.util.ArrayList;
import java.util.List;

/**
 * easyui tree 节点
 * @author xiejs
 * @date 2015年7月1日
 * Every node can contains following properties:
id: node id, which is important to load remote data
text: node text to show
state: node state, 'open' or 'closed', default is 'open'. When set to 'closed', the node have children nodes and will load them from remote site
checked: Indicate whether the node is checked selected.
attributes: custom attributes can be added to a node
children: an array nodes defines some children nodes
 */
public class TreeNode {

	private String id;
	private String parentId;
	private String text;
	private String state = "closed";
	private String flag;
	private Object attributes;
	private boolean checked = false;
	private List<TreeNode> children = null;
	
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public TreeNode() {
	}
	
	public TreeNode(String text, String id) {
		this.text = text;
		this.id = id;
	}
	
	public TreeNode getInstance(String text, String id) {
		return new TreeNode(text, id);
	}
	
	public TreeNode AddList(String text, String value) {
		if(children == null){
			children = new ArrayList<TreeNode>();
		}
		this.children.add(new TreeNode(text, value));
		return this;
	}
	
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public Object getAttributes() {
		return attributes;
	}
	public void setAttributes(Object attributes) {
		this.attributes = attributes;
	}
	public List<TreeNode> getChildren() {
		return children;
	}
	
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	
	public void addChildren(TreeNode node) {
		if(children == null){
			children = new ArrayList<TreeNode>();
		}
		this.children.add(node);
	}

	@Override
	public String toString() {
		return "TreeNode [id=" + id + ", parentId=" + parentId + ", text=" + text + ", state=" + state + ", flag=" + flag + ", attributes=" + attributes + ", checked=" + checked + ", children=" + children + "]";
	}
	
	
}
