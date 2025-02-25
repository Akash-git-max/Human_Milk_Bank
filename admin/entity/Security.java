package com.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="security")
public class Security {
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	@Column(name="sid")
	private int sid;
	
	@Column(name="question")
	private String question;

	public int getSid() {
		return sid;
	}

	public String getQuestion() {
		return question;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	
	

}
