package com.hubino.uploaddownload.fileuploaddownload.controller;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.function.ServerRequest.Headers;

import com.hubino.uploaddownload.fileuploaddownload.dto.ErrorHandler;
import com.hubino.uploaddownload.fileuploaddownload.entity.FileAttachment;
import com.hubino.uploaddownload.fileuploaddownload.repository.FileAttachmentRepository;
import com.hubino.uploaddownload.fileuploaddownload.service.FileServiceImpl;

import jakarta.validation.Valid;

@RestController
public class FileUploadDownloadController {
	
	private final long FILE_SIZE=500000;
	
	@Autowired
	private FileServiceImpl fileService;
	
	@Autowired
	private FileAttachmentRepository repo;
	
	private static final Logger log=LoggerFactory.getLogger(FileUploadDownloadController.class);
	
	/**
	 * 
	 * @param files
	 * @return
	 */
	@PostMapping("/fileUpload")
	public ResponseEntity<?> fileUpload(@Valid @RequestParam("file") MultipartFile files)
	{
		if(!files.isEmpty() && files!=null)
		{
			if(files.getSize() < FILE_SIZE)
			{
				log.info("file Size:"+" "+files.getSize());
			if(files.getOriginalFilename().endsWith(".pdf"))
			{
			fileService.fileUpload(files);
			return ErrorHandler.successResponse(ErrorHandler.FILE_UPLOAD, HttpStatus.ACCEPTED);
		}
			else {
				return ErrorHandler.response(ErrorHandler.INVALID_FILE_UPLOAD, HttpStatus.NOT_ACCEPTABLE);
			}
		}
			else {
				return ErrorHandler.response(ErrorHandler.FILE_SIZE_EXCEEDS, HttpStatus.NOT_ACCEPTABLE);
			}
		}
		return ErrorHandler.response(ErrorHandler.FILE_UPLOAD_FAILED,HttpStatus.NOT_FOUND); 
	}
	
	/**
	 * 
	 * @param filename
	 * @return
	 */
	@GetMapping("/downloadFile/{filename}")
	public ResponseEntity<?> downloadFile(@Valid @PathVariable("filename") String filename)
	{
		if(!filename.isEmpty() && filename!=null)
		{
		if(filename.endsWith(".pdf"))
		{
			ResponseEntity<?> downloadedFile=fileService.downloadFile(filename);
			return ResponseEntity.status(HttpStatus.OK)
					.body(downloadedFile);
		}
		}
		return ErrorHandler.response(ErrorHandler.INVALID_FILE_UPLOAD, HttpStatus.UNAUTHORIZED);
	}
		/**
		 * 
		 * @param id
		 * @return
		 */
	@GetMapping("/getFileById/{id}")
	public ResponseEntity<?> getFilesById(@Valid @PathVariable("id") Integer id)
	{
		Optional<FileAttachment> fileId=repo.findById(id);
		if(fileId.isPresent())
		{
			ResponseEntity<?> files=fileService.getFilesById(id);
			log.info("file id is:"+" "+files.equals(fileService.getFilesById(id)));	
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(files);
		}
		else
		return ErrorHandler.response(ErrorHandler.FILE_NOT_FOUND,HttpStatus.NOT_FOUND);
		}
}

