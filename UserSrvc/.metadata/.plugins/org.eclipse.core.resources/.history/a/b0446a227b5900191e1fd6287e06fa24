package com.userSrvc.server.entities;

import javax.annotation.ManagedBean;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.web.context.annotation.ApplicationScope;

import com.userSrvc.server.converter.StringByteConverter;

import lombok.Data;

/**
 * TODO: Implement a description(About Me) field
 * @author jozse
 *
 */
@Entity
@Table(name = "PAGE")
@ManagedBean
@ApplicationScope
@Data
public class Page {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAGE_ID_SEQ")
	@SequenceGenerator(name = "PAGE_ID_SEQ", sequenceName = "PAGE_ID_SEQ")
	private long id;
	
	@Column
	private String identifier;

	@Column
	@Convert(converter = StringByteConverter.class)
	private String jsonObj;
}
