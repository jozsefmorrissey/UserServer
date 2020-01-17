package com.userSrvc.client.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class AccessExceptions {
	@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Url Not Found")
	public static class NotFound extends RuntimeException {
		private static final long serialVersionUID = -6608472693688l;
		private String msg = "Url Not Found";
		public NotFound() {}
		public NotFound(String msg) {this.msg = msg;};
		public String toString() {return msg;}
	}
	
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "UnAuthorized")
	public static class UnAuthorized extends RuntimeException {
		private static final long serialVersionUID = -6608472693688923435L;
		private String msg = "UnAuthorized";
		public UnAuthorized() {}
		public UnAuthorized(String msg) {this.msg = msg;};
		public String toString() {return msg;}
	}
}
