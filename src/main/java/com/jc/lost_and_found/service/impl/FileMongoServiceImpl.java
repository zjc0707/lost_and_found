package com.jc.lost_and_found.service.impl;

import com.jc.lost_and_found.configuration.MongoConfig;
import com.jc.lost_and_found.configuration.MongoConnectParamConfig;
import com.jc.lost_and_found.model.FileMongoDO;
import com.jc.lost_and_found.service.FileMongoService;
import com.jc.lost_and_found.utils.ImageUtil;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * File 服务.
 *
 * @author InitUser
 */
@Slf4j
@Service
public class FileMongoServiceImpl implements FileMongoService {

	@Autowired
	private MongoOperations mongoOperations;

	@Autowired
	private MongoConnectParamConfig mongoConnectParamConfig;

	@Autowired
	MongoConfig mongoConfig;

	@Override
	public String saveFile(MultipartFile file) {
		try {
			byte[] thumbnailBytes = ImageUtil.makeByBufferImage(file.getBytes(), file.getOriginalFilename());
			long index = file.getSize() % mongoConnectParamConfig.getBucketCollectNum();
			FileMongoDO fileMongoDO = new FileMongoDO(file.getOriginalFilename(), file.getContentType(), file.getSize(), new Binary(thumbnailBytes));
			String fileSlot = Long.toString(index);
			mongoOperations.save(fileMongoDO, fileSlot);
			log.debug("collection[" + file.getOriginalFilename() + "], url: " + fileSlot.concat("/").concat(fileMongoDO.getId()));
			return fileSlot.concat("/").concat(fileMongoDO.getId());
//			if (file.getSize() <= mongoConnectParamConfig.getMaxCollectFileSize()) {
//				FileMongoDO fileMongoDO = new FileMongoDO(file.getOriginalFilename(), file.getContentType(), file.getSize(), new Binary(file.getBytes()));
//				String fileSlot = Long.toString(index);
//				mongoOperations.save(fileMongoDO, fileSlot);
//				log.debug("collection[" + file.getOriginalFilename() + "], url: " + fileSlot.concat("/").concat(fileMongoDO.getId()));
//				return fileSlot.concat("/").concat(fileMongoDO.getId());
//			} else {
//				String fileSlot = Long.toString(index + mongoConnectParamConfig.getBucketCollectNum());
//				ObjectId objectId = mongoConfig.getGridFsOperations(fileSlot).store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
//				final String concat = fileSlot.concat("/").concat(objectId.toString());
//				log.debug("bucket[" + file.getOriginalFilename() + "], url: " + concat);
//				return concat;
//			}
		}catch (Exception e) {
			log.error("[" + file.getOriginalFilename() + "] upload fail: " + e.getMessage());
		}
		return "";
	}

	@Override
	public void removeFile(String fileSlot,String id) {
		Integer slot = Integer.parseInt(fileSlot);
        if(slot < mongoConnectParamConfig.getBucketCollectNum()) {
			log.debug(String.format("delete collection: %d, id: %s", slot, id));
			mongoOperations.remove(query(where("_id").is(id)), fileSlot);
        }
        else
        {
			log.debug(String.format("delete bucket: %d, id: %s", slot, id));
			mongoConfig.getGridFsOperations(fileSlot).delete(query(where("_id").is(id)));
        }
	}

	@Override
	public void removeFile(String fileUrl) {
		String[] strs = fileUrl.split("/");
		this.removeFile(strs[0], strs[1]);
	}

	@Override
	public FileMongoDO getFileById(String fileSlot, String id)
	{
		GridFSDownloadStream gridFSDownloadStream = null;
		FileMongoDO fileMongoDO = null;
		try {
			Integer slot = Integer.parseInt(fileSlot);
			if (slot < mongoConnectParamConfig.getBucketCollectNum()) {

				log.debug(String.format("get collection: %d, id: %s", slot, id));
				fileMongoDO = mongoOperations.findById(id, FileMongoDO.class, fileSlot);

			}else {

				log.debug(String.format("get bucket: %d, id: %s", slot, id));
				gridFSDownloadStream = mongoConfig.getGridFSBucket(fileSlot).openDownloadStream(new ObjectId(id));
				if (null != gridFSDownloadStream) {
					GridFSFile gridFSFile = gridFSDownloadStream.getGridFSFile();
					//todo: 这个md5函数 @deprecated，谨慎使用！开始读取数据,后续可以进一步优化输出效率！
					fileMongoDO = new FileMongoDO(gridFSFile.getFilename(),gridFSFile.getMetadata().getString("_contentType"),
							gridFSFile.getLength(),new Binary(gridFSDownloadStream.readAllBytes()));
				}
			}
		}catch (Exception e) {

			log.error("[" + fileSlot.concat("/").concat(id) + "] getFileByteById fail: " + e.getMessage());

		}finally {

			if (null != gridFSDownloadStream) {
				gridFSDownloadStream.close();
			}

		}
		return fileMongoDO;
	}
}
