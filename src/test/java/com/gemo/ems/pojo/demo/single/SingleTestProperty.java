package com.gemo.ems.pojo.demo.single;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gemo.core.annotation.Column;
import com.gemo.core.annotation.Dimension;
import com.gemo.core.annotation.Entity;
import com.gemo.core.annotation.ForeignKey;
import com.gemo.core.annotation.Index;
import com.gemo.core.annotation.Redundant;
import com.gemo.core.annotation.Table;
import com.gemo.core.constant.SchemaConstant.Schema;
import com.gemo.core.mvc.pojo.BaseProperty;


@Dimension
@Entity(name="testProperty")
@Table(
		name="z_test_property",
		comment="测试",
		schema=Schema.UDM,
		indexes={
				@Index(columns={"c_property_name"})
		}
)
public class SingleTestProperty extends BaseProperty {

	private static final long serialVersionUID = -3839087325508172489L;

	@ForeignKey(mainClass=SingleTest.class)
	@Column(order=1,name="c_test_id",length=36,nullable=false,comment="主表id")
	@JsonProperty("testId")
	private String testId;
	
	@JsonProperty("name")
	@Redundant(cls=SingleTest.class,valueField="name",field="testId")
	private String name;

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
