package com.userSrvc.client.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.ManagedBean;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.context.annotation.ApplicationScope;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.userSrvc.client.marker.HasToken;

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
public class App implements HasToken<AppAccessToken> {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column
	private String name;
	
	@Column
	private String accessKey;
	
	@Transient
	private AppAccessToken token;

	@Transient
	private Boolean createNewKey = false;
	
	@Column
	private long tokenExpiration;

	@Column
	private Long protectedAppId;
	
	@Column
	private String host;

	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "appId")
	private Set<Rule> enpointRules = new HashSet<Rule>();
	
	@JsonIgnore
	@ManyToMany(cascade = {CascadeType.DETACH})
	@JoinTable(name="AUTHORIZED_APP_USER",
		joinColumns=@JoinColumn(name="APP_ID"),
		inverseJoinColumns = @JoinColumn(name = "USER_ID"))
	private List<UUserAbs> authorizedUsers = new ArrayList<UUserAbs>();
	
	@Transient
	private List<String> rules = new ArrayList<String>();
	
	@Transient
	private List<String> authorizedEmails;
	
	public void setEndpointRules(Set<Rule> rules) {
		this.enpointRules = rules;
		this.rules = new ArrayList<String>();
		for (Rule rule : rules) {
			this.rules.add(rule.getEndpointRegExp());
		}
	}
	
	public void setAuthorizedUsers(List<UUserAbs> users) {
		this.authorizedUsers = users;
		this.authorizedEmails = new ArrayList<String>();
		for (UUserAbs user : users) {
			this.authorizedEmails.add(user.getEmail());
		}
	}

	@Override
	public AppAccessToken emptyToken() {
		return new AppAccessToken();
	}
}
