package com.hubino.uploaddownload.fileuploaddownload.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hubino.uploaddownload.fileuploaddownload.entity.FileAttachment;

@Repository
public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Integer>{

	Optional<FileAttachment> findByFilename(String filename);

}
