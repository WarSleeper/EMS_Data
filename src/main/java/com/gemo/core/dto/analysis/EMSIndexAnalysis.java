package com.gemo.core.dto.analysis;

import java.util.List;

/**
 * EMS ORM解析Index
 * @author  
 * @date 2015年12月16日
 * @version v1.0
 */
public class EMSIndexAnalysis {
	
	//是否唯一索引
	private Boolean unique;
	//数据库字段
	private List<String> columnList;
	//是否已创建
	private Boolean created;

	public List<String> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<String> columnList) {
		this.columnList = columnList;
	}

	public Boolean getCreated() {
		return created;
	}

	public void setCreated(Boolean created) {
		this.created = created;
	}

	public Boolean getUnique() {
		return unique;
	}

	public void setUnique(Boolean unique) {
		this.unique = unique;
	}

	@Override
	public String toString() {
		return "EMSIndexAnalysis [unique=" + unique + ", columnList=" + columnList + ", created=" + created + "]";
	}

}
