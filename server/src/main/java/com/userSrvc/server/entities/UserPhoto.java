package com.userSrvc.server.entities;

import javax.annotation.ManagedBean;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.web.context.annotation.ApplicationScope;

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
public class UserPhoto implements Comparable {
	@Id
	private long id;
	
	@Column
	private String ext;

	@Column
	private Long userId;

	@Column
	private Long appUserId;

	@Column
	private Short position;

	@Column
	private Byte[] photo;

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		if (!(o instanceof UserPhoto)) {
			return -1;
		}
		
		return this.position - ((UserPhoto)o).position;
	}
}
