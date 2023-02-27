package com.hubino.uploaddownload.fileuploaddownload.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.hubino.uploaddownload.fileuploaddownload.dto.ErrorHandler;

@ControllerAdvice
public class CustomExceptionHandler {
	
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<?> handleMaxSizeError()
	{
		return ErrorHandler.response(ErrorHandler.FILE_SIZE_EXCEEDS, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<?> requestedIdNotFound()
	{
		return ErrorHandler.response(ErrorHandler.FILE_ID_EMPTY, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MissingServletRequestPartException.class)
	public ResponseEntity<?> requestFilenameNotFound()
	{
		return ErrorHandler.response(ErrorHandler.FILE_NAME_EMPTY, HttpStatus.NOT_FOUND);
	}
}
