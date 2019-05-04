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

import lombok.Data;

@Entity
@Table
@ManagedBean
@ApplicationScope
@Data
public class UserPhoto {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHOTO_ID_SEQ")
	@SequenceGenerator(name = "PHOTO_ID_SEQ", sequenceName = "PHOTO_ID_SEQ")
	private long id;
	
	@Column
	private long userId;
	
	@Column
	private Byte[] photo;
}
