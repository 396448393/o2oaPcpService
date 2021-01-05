package com.x.pcpcustom.core.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.x.base.core.entity.annotation.*;
import org.apache.openjpa.persistence.jdbc.Index;

import com.x.base.core.entity.JpaObject;
import com.x.base.core.entity.SliceJpaObject;
import com.x.base.core.project.annotation.FieldDescribe;

@ContainerEntity
@Entity
@Table(name = PersistenceProperties.SampleEntityClassName.table, uniqueConstraints = {
		@UniqueConstraint(name = PersistenceProperties.SampleEntityClassName.table + JpaObject.IndexNameMiddle
				+ JpaObject.DefaultUniqueConstraintSuffix, columnNames = { JpaObject.IDCOLUMN,
						JpaObject.CREATETIMECOLUMN, JpaObject.UPDATETIMECOLUMN, JpaObject.SEQUENCECOLUMN }) })
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class SampleEntityClassName extends SliceJpaObject {

	private static final long serialVersionUID = 1325197931747463979L;
	private static final String TABLE = PersistenceProperties.SampleEntityClassName.table;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@FieldDescribe("数据库主键,自动生成.")
	@Id
	@Column(length = length_id, name = ColumnNamePrefix + id_FIELDNAME)
	private String id = createId();

	public void onPersist() throws Exception {
	}
	/*
	 * =============================================================================
	 * ===== 以上为 JpaObject 默认字段
	 * =============================================================================
	 * =====
	 */

	/*
	 * =============================================================================
	 * ===== 以下为具体不同的业务及数据表字段要求
	 * =============================================================================
	 * =====
	 */
	public static final String name_FIELDNAME = "name";
	@FieldDescribe("名称")
	@RestrictFlag
	@Index(name = TABLE + IndexNameMiddle + name_FIELDNAME)
	@Column(length = length_255B, name = ColumnNamePrefix + name_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private String name;

	public static final String alias_FIELDNAME = "alias";
	@RestrictFlag
	@FieldDescribe("别名.")
	@Column(length = length_255B, name = ColumnNamePrefix + alias_FIELDNAME)
	@Index(name = TABLE + IndexNameMiddle + alias_FIELDNAME)
	@CheckPersist(allowEmpty = true, simplyString = true)
	private String alias;

	public static final String date_FIELDNAME = "date";
	@FieldDescribe("时间")
	@Temporal(TemporalType.DATE)
	@Column(name = ColumnNamePrefix + date_FIELDNAME)
	private Date date;

	public static final String orderNumber_FIELDNAME = "orderNumber";
	@FieldDescribe("排序号")
	@Column(name = ColumnNamePrefix + orderNumber_FIELDNAME)
	@CheckPersist(allowEmpty = false)
	private Integer orderNumber;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}
}
