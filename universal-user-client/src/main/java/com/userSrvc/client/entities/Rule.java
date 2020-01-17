package com.userSrvc.client.entities;

import java.util.List;

import javax.annotation.ManagedBean;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.web.context.annotation.ApplicationScope;

import com.userSrvc.client.converter.IntBoolConverter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table
@ManagedBean
@ApplicationScope
@AllArgsConstructor
@Data
public class Rule {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private Long appId;
	
	@Column
	private String endpointRegExp;
	
	@Column
	@Convert(converter = IntBoolConverter.class)
	private Boolean appExclusion = true;

	@Column
	@Convert(converter = IntBoolConverter.class)
	private Boolean userExclusion = true;

	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinTable(name="USER_RULE",
		joinColumns=@JoinColumn(name="RULE_ID"),
		inverseJoinColumns = @JoinColumn(name = "USER_ID"))
	private List <UUserAbs> users;

	@OneToMany(fetch=FetchType.LAZY)
	@JoinTable(name="USER_RULE",
		joinColumns=@JoinColumn(name="RULE_ID"),
		inverseJoinColumns = @JoinColumn(name = "USER_ID"))
	private List <UUserAbs> apps;
}
