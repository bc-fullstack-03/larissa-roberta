package com.sysmap.parrot.exception;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionDetails {

	protected String title;
	protected int status;
	protected String message;
	protected LocalDateTime timestamp;
}
