package com.carritoService.model;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorInfo {

	@JsonProperty("HttpStatus")
	private HttpStatus status;
	@JsonProperty("message")
	private String message;
	@JsonProperty("code")
	private int code;
	@JsonProperty("BackMenssage")
	private String backMenssage;

	public ErrorInfo(HttpStatus status, String message, int code, String backMenssage) {
		this.status = status;
		this.message = message;
		this.code = code;
		this.backMenssage = backMenssage;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getBackMenssage() {
		return backMenssage;
	}

	public void setBackMenssage(String backMenssage) {
		this.backMenssage = backMenssage;
	}

}