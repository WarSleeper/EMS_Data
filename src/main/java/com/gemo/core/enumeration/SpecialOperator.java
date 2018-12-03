package com.gemo.core.enumeration;

/**
 * 支持特殊操作类型
 * 
 * @author  
 * @date 2014-4-30
 * @version v1.0
 */
public enum SpecialOperator {

	$lt(" < "), $lte(" <= "), $gt(" > "), $gte(" >= "), $ne(" <> "), 
	$like(" like "), $in(" in "), $nin(" not in "), $exists(" is "), 
	@Deprecated $null(" is null "), @Deprecated $not_null(" is not null ");

	private String operator;

	SpecialOperator(String operator) {
		this.operator = operator;
	}

	public String getOperator() {
		return operator;
	}
}
