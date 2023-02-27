package com.hubino.uploaddownload.fileuploaddownload.controller;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.hubino.uploaddownload.fileuploaddownload.dto.ErrorHandler;
import com.hubino.uploaddownload.fileuploaddownload.entity.FileAttachment;
import com.hubino.uploaddownload.fileuploaddownload.repository.FileAttachmentRepository;
import com.hubino.uploaddownload.fileuploaddownload.service.FileServiceImpl;

import jakarta.validation.Valid;

@RestController
@ControllerAdvice
public class FileUploadDownloadController {
	
	private static final Logger log=LoggerFactory.getLogger(FileServiceImpl.class);
		
	@Autowired
	private FileServiceImpl fileService;
	
	@Autowired
	private FileAttachmentRepository repo;
	/**
	 * 
	 * @param files
	 * @return
	 */
	@PostMapping("/fileUpload")
	public ResponseEntity<?> fileUpload(@Valid @RequestParam("file") MultipartFile files)
	{
		System.out.println(files.getContentType());
		String extension=StringUtils.getFilenameExtension(files.getOriginalFilename());
		System.out.println("extension:"+" "+extension);
		if(!files.isEmpty() && files!=null)
		{
			 if(files.getContentType()==extension)
			{
				 log.info(extension, "Checking the extension");
				 System.out.println(files.getContentType()!=extension);
				 return ErrorHandler.response(ErrorHandler.INVALID_FILE_UPLOAD, HttpStatus.NOT_ACCEPTABLE);
			}else
			{
			return fileService.fileUpload(files);
			}
		}
		else
		return ErrorHandler.response(ErrorHandler.FILE_UPLOAD_FAILED,HttpStatus.NOT_FOUND); 
	}
	/**
	 * 
	 * @param filename
	 * @return
	 */
	@GetMapping("/downloadFile/")
	public ResponseEntity<?> downloadFile(@Valid @RequestParam("filename") String filename)
	{
		String extension=StringUtils.getFilenameExtension(filename);
		System.out.println(extension);
		if(!filename.isEmpty() && filename!=null)
		{
		if(filename.contains(extension))
		{
			return fileService.downloadFile(filename);
		}
		else
		{
			return ErrorHandler.response(ErrorHandler.FILE_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
	}
		return ErrorHandler.response(ErrorHandler.FILE_NOT_ATTACHED, HttpStatus.UNAUTHORIZED);
}
	
		/**
		 * 
		 * @param id
		 * @return
		 */
	@GetMapping("/getFileById/")
	public ResponseEntity<?> getFilesById(@Valid @RequestParam("id") Integer id)
	{
		Optional<FileAttachment> fileId=repo.findById(id);
		if(fileId.isPresent())
		{
		if(id!=null)
		{
			log.info("id id null"+" "+id);
			return fileService.getFilesById(id);
		}
		else
			return ErrorHandler.response(ErrorHandler.FILE_ID_EMPTY, HttpStatus.BAD_REQUEST);
		}
		return ErrorHandler.response(ErrorHandler.FILE_NOT_FOUND,HttpStatus.NOT_FOUND);
	}
	
}

