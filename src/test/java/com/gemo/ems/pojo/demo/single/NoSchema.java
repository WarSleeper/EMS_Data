package com.gemo.ems.pojo.demo.single;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gemo.core.annotation.Column;
import com.gemo.core.annotation.Dimension;
import com.gemo.core.annotation.Entity;
import com.gemo.core.annotation.Index;
import com.gemo.core.annotation.Table;
import com.gemo.core.constant.SchemaConstant.Schema;
import com.gemo.core.mvc.pojo.BaseId;

@Dimension
@Entity(name="NoSchema")
@Table(
		name="z_no_schema",
		comment="测试",
		schema=Schema.NONE,
		indexes={
				@Index(columns={"c_name"}),
				@Index(columns={"c_age","c_height"})
		}
)
public class NoSchema extends BaseId {

	private static final long serialVersionUID = 8614664461486005329L;

	@Column(order=1,name="c_name",length=50,nullable=false,comment="名称")
	@JsonProperty("name")
	private String name;
	
	@Column(order=2,name="c_age",length=3,nullable=false,comment="年龄")
	@JsonProperty("age")
	private Integer age;
	
	@Column(order=3,name="c_height",length=3,scale=2,nullable=false,comment="身高")
	@JsonProperty("height")
	private Double height;
	
	@Column(order=4,name="c_male",length=1,nullable=false,comment="男/女")
	@JsonProperty("male")
	private Boolean male;
	
	@Column(order=5,name="c_birthday",length=0,nullable=false,comment="生日")
	@JsonProperty("birthday")
	private Date birthday;
	
	@Column(order=6,name="c_remark",length=Integer.MAX_VALUE,comment="备注")
	@JsonProperty("remark")
	private String remark;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Boolean getMale() {
		return male;
	}

	public void setMale(Boolean male) {
		this.male = male;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
