package com.userSrvc.client.entities;

import java.util.Collections;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.Transient;

import org.springframework.web.context.annotation.ApplicationScope;

import com.userSrvc.client.aop.AopSecure;

import lombok.Data;

/**
 * TODO: Implement a description(About Me) field
 * @author jozse
 *
 */

@Entity
@ManagedBean
@ApplicationScope
@Data
@Inheritance
public class UUserAbs extends AopSecure implements Comparable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String fullname;

	@Column
	private String email;
	
	@Column
	private String token;

	@Column
	private String password;
	
	@Transient
	private List<String> imageUrls;
	
	@Transient
	private List<String> permissionTypes;
	
	public UUserAbs() {
		super();
	}
	public UUserAbs(UUserAbs user) {
		super(user);
		this.setId(user.getId());
		this.setFullname(user.getFullname());
		this.setEmail(user.getEmail());
		this.setToken(user.getToken());
		this.setPassword(user.getPassword());
		this.setImageUrls(user.getImageUrls());
		this.setPermissionTypes(user.getPermissionTypes());
	}
	
	public UUserAbs(long id, String name, String email, String password, byte[] photo) {
		super();
		this.setId(id);
		this.setFullname(name);
		this.setEmail(email);
		this.setPassword(password);
	}

	public void merge(UUserAbs dbUser) {
		this.setId(dbUser.getId());
		this.setFullname(dbUser.getFullname());
		this.setToken(dbUser.getToken());
		this.setPassword(dbUser.getPassword());
		this.setEmail(dbUser.getEmail());
		this.setPermissionTypes(dbUser.getPermissionTypes());
		this.setImageUrls(dbUser.getImageUrls());
	}
	
	public static <U extends UUserAbs> void merg(List<U> localUsers, List<U> srvcUsers) {
		Collections.sort(localUsers);
		Collections.sort(srvcUsers);
		int size = localUsers.size();
		int srvcIndex = 0;
		for (int index = 0; index < size; index += 1) {
			int next = index > size - 1 ? index : size - 1;
			UUserAbs local = localUsers.get(index);
			UUserAbs nextLocal = localUsers.get(next);
			UUserAbs srvc = srvcUsers.get(srvcIndex);
			while (!srvc.getId().equals(local.getId()) && local.getId() < srvc.getId()) {
				srvcIndex += 1;
				srvc = srvcUsers.get(srvcIndex);
			}
			
			if (srvc.getId().equals(local.getId())) {
				local.merge(srvc);
			}
		}
	}
	
	public String getName(int i) {
		if (fullname == null) {
			return null;
		}
		String[] names = fullname.split(" ");
		if (i >= names.length - 1) {
			return names[names.length - 1];
		}
		if (i <= 0) {
			return names[0];
		}
		return names[i];
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}
	
	public List<String> getImageUrls() {
		return this.imageUrls;
	}

	@Override
	public int compareTo(Object o) {
		if (!(o instanceof UUserAbs)) {
			return -1;
		}
		
		UUserAbs u = ((UUserAbs)o);
		Long uId = u.getId();
		Long id = this.id;
		
		if (uId == null) {
			return uId == id ? 0 : -1;
		}
		
		if (id == null) {
			return 1;
		}
		
		return uId.equals(id) ? 0 : uId > id ? -1 : 1;
	}

	@Override
	public String getObjectType() {
		return "User";
	}
	
}
