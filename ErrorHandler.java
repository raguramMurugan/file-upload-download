package com.hubino.uploaddownload.fileuploaddownload.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;

public class ErrorHandler {
	
	public static final String INVALID_FILE_UPLOAD="Uploaded file is not PDF";
	
	public static final String FILE_SIZE_EXCEEDS="File Size Exceeds, Please Upload file within 40MB";
	
	public static final String FILE_UPLOAD_FAILED="File upload failed, No File Found";
	
	public static final String FILE_COPY_FAILED="Transferring the files failed";
	
	public static final String BYTE_CONVERSION_FAILED="Internal conversion failed";
	
	public static final String FILE_NOT_ATTACHED="No Filename Attached";
	
	public static final String FILE_NOT_FOUND="File not found in Database";
	
	public static final String FILE_ID_EMPTY="File id or Value is Empty else Incorrect, Kindly recheck!";
	
	public static final String FILE_UPLOAD="File Upload Successfully";
	
	public static final String FILE_RETRIVED="File Retrieved Successfully";
	
	public static final String ENCODE_FAILED="failed to encode to Base64 Format";
	
	public static final String FILE_NAME_EMPTY="Required field Key is Empty or Incorrect, Please recheck!";
	
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
		else if(httpStatus.value()==406)
		{
			errorResponse.setError(error);
			return new ResponseEntity<Object>(errorResponse,httpStatus);
		}
		else if(httpStatus.value()==409)
		{
			errorResponse.setError(error);
			return new ResponseEntity<Object>(errorResponse,httpStatus);
		}
		else {
			errorResponse.setError(error);
			return new ResponseEntity<Object>(errorResponse,httpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	public static ResponseEntity<?> successResponse(String status, HttpStatus http)
	{
		SuccessResponse successResponse=new SuccessResponse();
		if(http.value()==200)
		{
			successResponse.setStatus(status);
			return new ResponseEntity<Object>(successResponse,http);
		}
		else 
		if(http.value()==202)
		{
			successResponse.setStatus(status);
			return new ResponseEntity<Object>(successResponse,http);
		}
		return new ResponseEntity<Object>(successResponse,http.ACCEPTED);
		
	}

}

