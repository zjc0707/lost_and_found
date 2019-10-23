package com.jc.lost_and_found.controller;

import com.jc.lost_and_found.component.ResponseData;
import com.jc.lost_and_found.component.ResponseDataUtil;
import com.jc.lost_and_found.constant.ResultEnums;
import com.jc.lost_and_found.model.FileMongoDO;
import com.jc.lost_and_found.service.FileMongoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

/**
 * @author InitUser
 * @CrossOrigin(origins = "*", maxAge = 3600)
 */
@Slf4j
@RestController
@RequestMapping("/fileServer")
public class FileMongoController {

	@Autowired
	private FileMongoService fileMongoService;

//	@Autowired
//	private FileTransitDomainService fileTransitDomainService;

	/**
	 * 下载文件(Base64编码)
	 * @param bucketOrCollection
	 * @param id
	 * @return
	 */
	@GetMapping("getBase64File/{bucketOrCollection}/{id}")
	public ResponseData<FileMongoDO> getBase64File(@PathVariable String bucketOrCollection, @PathVariable String id) {

		FileMongoDO resultEntity = fileMongoService.getFileById(bucketOrCollection,id);
		if ((null != resultEntity) && (resultEntity.getSize() > 0)) {
			return ResponseDataUtil.buildSuccess(resultEntity);

		}
		return ResponseDataUtil.buildSend(ResultEnums.FILE_NO_FIND);
	}

	/**
	 * HTTP形式文件下载
	 * @param bucketOrCollection
	 * @param id
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@GetMapping("/downLoadFile/{bucketOrCollection}/{id}")
	public ResponseEntity<Object> downLoadFile(@PathVariable String bucketOrCollection, @PathVariable String id) throws UnsupportedEncodingException {

		FileMongoDO resultEntity = fileMongoService.getFileById(bucketOrCollection,id);
		if ((null != resultEntity) && (resultEntity.getSize() > 0)) {
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=".concat(new String(resultEntity.getName().getBytes("utf-8"),"ISO-8859-1")))
					.header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
					.header(HttpHeaders.CONTENT_LENGTH, resultEntity.getSize().toString())
					.header("Connection", "close")
					.body(resultEntity.getContent().getData());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not fount");
		}

	}

	/**
	 * Web形式文件展示(用于图像及视频)
	 * @param bucketOrCollection
	 * @param id
	 * @return
	 */
	@GetMapping("/viewFile/{bucketOrCollection}/{id}")
	public ResponseEntity<Object> viewFile(@PathVariable String bucketOrCollection, @PathVariable String id) {

		FileMongoDO resultEntity = fileMongoService.getFileById(bucketOrCollection,id);

		if ((null != resultEntity) && (resultEntity.getSize() > 0)) {
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "fileName=\"" + resultEntity.getName() + "\"")
					.header(HttpHeaders.CONTENT_TYPE, resultEntity.getContentType() + ";charset=UTF-8")
					.header(HttpHeaders.CONTENT_LENGTH, resultEntity.getSize().toString())
					.header("Connection", "close")
					.body(resultEntity.getContent().getData());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not fount");
		}

	}

	@DeleteMapping("deleteFile/{bucketOrCollection}/{id}")
	public ResponseData<String> deleteFile(@PathVariable String bucketOrCollection, @PathVariable String id) {
		try {
			fileMongoService.removeFile(bucketOrCollection,id);
			return ResponseDataUtil.buildSuccess();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseDataUtil.buildError(e.getMessage());
		}
	}
}
