package com.userSrvc.client.entities.access;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.springframework.web.context.annotation.ApplicationScope;

import com.userSrvc.client.entities.StringPool;
import com.userSrvc.client.entities.UUserAbs;

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
public class UserGroup {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name="NICK_NAME_ID")
	private StringPool nickName;
	
	@Column
	private Long referenceId;

	@ManyToMany(cascade = {CascadeType.DETACH})
	@JoinTable(name="USER_GROUP_MEMEBERS",
		joinColumns=@JoinColumn(name="USER_GROUP_ID"),
		inverseJoinColumns = @JoinColumn(name = "LINKED_USER_ID"))
	private List<UUserAbs> linkedUsers = new ArrayList<UUserAbs>();

	@ManyToMany(cascade = {CascadeType.DETACH})
	@JoinTable(name="USER_GROUP_GROUPS",
		joinColumns=@JoinColumn(name="USER_GROUP_ID"),
		inverseJoinColumns = @JoinColumn(name = "LINKED_USER_GROUP_ID"))
	private List<UserGroup> linkedGroups = new ArrayList<UserGroup>();

}
