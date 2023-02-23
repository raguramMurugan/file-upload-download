package com.hubino.uploaddownload.fileuploaddownload.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hubino.uploaddownload.fileuploaddownload.controller.FileUploadDownloadController;
import com.hubino.uploaddownload.fileuploaddownload.dto.ErrorHandler;
import com.hubino.uploaddownload.fileuploaddownload.dto.ErrorHandlerResponse;
import com.hubino.uploaddownload.fileuploaddownload.entity.FileAttachment;
import com.hubino.uploaddownload.fileuploaddownload.repository.FileAttachmentRepository;

import jakarta.validation.Valid;

@Service
public class FileServiceImpl implements FileService{
	
	private static final Logger log=LoggerFactory.getLogger(FileServiceImpl.class);
	
	@Autowired
	private FileAttachmentRepository attachmentRepository;
	
	private static final String folderPath="E:\\My PDF_Files\\";
	
	//uploading the File to local Directory
	
	public ResponseEntity<?> fileUpload(@Valid MultipartFile files) 
	{
		String filePath=folderPath+files.getOriginalFilename();
		
		FileAttachment fileAttachment=attachmentRepository.save(
				FileAttachment.builder()
				.filename(files.getOriginalFilename())
				.fileType(files.getContentType())
				.filePath(filePath).build());
		
		//copying the files from db to the specified local path
				
		try {
				files.transferTo(new File(filePath));
		} catch (IllegalStateException | IOException e) {
			return ErrorHandler.response(ErrorHandler.FILE_COPY_FAILED, HttpStatus.NOT_FOUND);
		}
		if(fileAttachment!=null)
		{
			return new ResponseEntity<>(ErrorHandler.FILE_UPLOAD,HttpStatus.ACCEPTED);
		}
		else
		return ErrorHandler.response(ErrorHandler.FILE_UPLOAD_FAILED, HttpStatus.BAD_REQUEST);
	}

	
	public ResponseEntity<?> downloadFile(@Valid String filename)
	{
		
		//getting the filename from the repository as an optional
	Optional<FileAttachment> attachment=attachmentRepository.findByFilename(filename);
	
			//getting the filepath
			String filePath=attachment.get().getFilePath();
			
			//converting it to byte Array using the filepath
			byte[] downloadedFile=null;
			String base64File=null;
			try {
				downloadedFile = Files.readAllBytes(new File(filePath).toPath());
				//converting the ByteArray to Base64 String format
				base64File=Base64.getEncoder().encodeToString(downloadedFile);
			} catch (IOException e) {
				ErrorHandler.response(ErrorHandler.BYTE_CONVERSION_FAILED, HttpStatus.BAD_REQUEST);
			}
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(base64File);

	}

	public ResponseEntity<?> getFilesById(Integer id) {
		FileAttachment attachment=attachmentRepository.findById(id).get();
		
		
			byte[] attachmentFile;
			
			if(attachment.getId().equals(id))
			{
				log.info("id is:"+attachment.getId().equals(id));
			try {
				attachmentFile=Files.readAllBytes(new File(attachment.getFilePath()).toPath());
				String downloadedFile=Base64.getEncoder().encodeToString(attachmentFile);
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(downloadedFile);
			} catch (IOException e) {
			
				e.printStackTrace();
			}
			
			}
			else {
				return ErrorHandler.response(ErrorHandler.FILE_NOT_FOUND, HttpStatus.NOT_FOUND);
			}
		
		return null;
	}
}
