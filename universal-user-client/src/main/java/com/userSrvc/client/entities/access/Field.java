package com.userSrvc.client.entities.access;

import javax.annotation.ManagedBean;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.springframework.web.context.annotation.ApplicationScope;

import com.userSrvc.client.entities.StringPool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@ManagedBean
@ApplicationScope
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Field {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private Long objectAccessId;

	@JoinColumn(name="FIELD_NAME_ID")
	private StringPool fieldName;
	
	@Column
	private int accessType;
}
