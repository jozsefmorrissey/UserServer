package com.userSrvc.server.entities;

import java.time.LocalDateTime;

import javax.annotation.ManagedBean;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter;
import org.springframework.web.context.annotation.ApplicationScope;

import lombok.Data;

@Entity
@ManagedBean
@ApplicationScope
@Data
public class Remark {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ID_SEQ")
	@SequenceGenerator(name = "USER_ID_SEQ", sequenceName = "USER_ID_SEQ")
	private long id;
	
	@OneToOne
	@JoinColumn(name = "POSTER_ID")
	private UUser poster;
	
	@Column
	private long conversationId;
	
	@Column(name = "TIME_STAMP")
	@Convert(converter = LocalDateTimeConverter.class)
	LocalDateTime timeStamp;
}
