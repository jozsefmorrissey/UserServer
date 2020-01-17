package com.userSrvc.server.entities;

import javax.annotation.ManagedBean;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.web.context.annotation.ApplicationScope;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER_PHOTO")
@ManagedBean
@ApplicationScope
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Photo implements Comparable<Photo> {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column
	private Long userId;

	@Column
	private Long appUserId;

	@Column
	private Short position;

	@Column
	private String url;

	@Override
	public int compareTo(Photo o) {
		if (!(o instanceof Photo)) {
			return -1;
		}
		
		return this.position - o.position;
	}
}
