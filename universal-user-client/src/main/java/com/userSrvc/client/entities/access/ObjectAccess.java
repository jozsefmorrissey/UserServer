package com.userSrvc.client.entities.access;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.ManagedBean;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.web.context.annotation.ApplicationScope;

import com.userSrvc.client.entities.StringPool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@ManagedBean
@ApplicationScope
@EqualsAndHashCode(callSuper=false)
@Data
@Inheritance
public class ObjectAccess <G> {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name="NICK_NAME_ID")
	private StringPool nickName;
	
	@JoinColumn(name="OBJECT_TYPE_ID")
	private StringPool objectType;
	
	@Column
	private Long accessReferenceId;

	@Column
	private Long objectId;

	@Transient
	private List<G> accessReference = new ArrayList<G>();

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "objectAccessId")
	private Set<Field> fields = new HashSet<Field>();
}
