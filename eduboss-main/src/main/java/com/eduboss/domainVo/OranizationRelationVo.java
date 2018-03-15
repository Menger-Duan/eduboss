package com.eduboss.domainVo;

import java.util.List;

/**
 * 
 * @author dmr
 *
 */
public class OranizationRelationVo  { 

    private String id;
    private String name;
    private String level;
    
    private List<OranizationRelationVo> children;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public List<OranizationRelationVo> getChildren() {
		return children;
	}

	public void setChildren(List<OranizationRelationVo> children) {
		this.children = children;
	}
    
    

}
