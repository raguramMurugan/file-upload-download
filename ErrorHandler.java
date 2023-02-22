package com.hubino.uploaddownload.fileuploaddownload.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;

public class ErrorHandler {
	
	public static final String INVALID_FILE_UPLOAD="Uploaded file is not PDF";
	
	public static final String FILE_UPLOAD="File upload Sucessfully";
	
	public static final String FILE_UPLOAD_FAILED="File upload failed";
	
	public static final String FILE_COPY_FAILED="Transferring the files failed";
	
	public static final String BYTE_CONVERSION_FAILED="Internal conversion failed";
	
	public static final String FILE_NOT_ATTACHED="No Filename Attached";
	
	public static final String FILE_NOT_FOUND="File not found in Database";
	
	public static final String FILE_ID_EMPTY="File id is Null";
	
	public static final String FILE_RETRIEVED="File retrieved Successfully";
	
	
	public static ResponseEntity<?> response(String error, HttpStatus httpStatus)
	{
		ErrorHandlerResponse errorResponse=new ErrorHandlerResponse();
		if(httpStatus.value()==400)
		{
			errorResponse.setError(error);
			return new ResponseEntity<Object>(errorResponse,httpStatus);
		}
		else if(httpStatus.value()==401)
		{
			errorResponse.setError(error);
			return new ResponseEntity<Object>(errorResponse,httpStatus);
		}
		else if(httpStatus.value()==404)
		{
			errorResponse.setError(error);
			return new ResponseEntity<Object>(errorResponse,httpStatus);
		}
		else {
			errorResponse.setError(error);
			return new ResponseEntity<Object>(errorResponse,httpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

}
