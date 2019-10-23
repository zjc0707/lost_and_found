package com.jc.lost_and_found.service;

import com.jc.lost_and_found.model.FileMongoDO;
import org.springframework.web.multipart.MultipartFile;

/**
 * File 服务接口.
 * @author InitUser
 */
public interface FileMongoService {
	/**
	 * 保存文件
	 * @param   file
	 * @return  文件下载url
	 */
	String saveFile(MultipartFile file);

	/**
	 * 删除文件
	 * @param fileSlot
     * @param id
	 */
	void removeFile(String fileSlot, String id);
	void removeFile(String fileUrl);

	/**
	 * 根据id获取文件
	 * @param fileSlot
	 * @param id
	 * @return
	 */
	FileMongoDO getFileById(String fileSlot, String id);

}
