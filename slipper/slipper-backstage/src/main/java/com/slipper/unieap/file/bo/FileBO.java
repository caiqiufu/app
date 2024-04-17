package com.slipper.unieap.file.bo;

import java.io.File;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.file.pojo.UnieapFileArchive;
import com.slipper.unieap.file.repository.FileArchiveRepository;

@Service
public class FileBO extends BaseBO {

	@Autowired
	public FileArchiveRepository fileArchiveRepository;

	public List<UnieapFileArchive> upload(UnieapFileArchive vo, Map<String, MultipartFile> fileMap) throws Exception {
		if (StringUtils.isEmpty(vo.getCategory())) {
			vo.setCategory("common");
		}
		// 物理路径
		String shareFolderPath = UnieapConstants.SHAREFOLDER_PATH;
		// 应用访问路径
		String url = UnieapConstants.APP_BASE_URL + UnieapConstants.APP_PATH + File.separator + "file" + File.separator;
		return this.upload(vo.getCategory(), vo.getExtKey(), fileMap, shareFolderPath, url);
	}

	public List<UnieapFileArchive> upload(String category, String extKey, Map<String, MultipartFile> fileMap,
			String path, String url) throws Exception {
		List<UnieapFileArchive> fileArchiveList = new ArrayList<UnieapFileArchive>();
		if (fileMap != null && fileMap.size() > 0) {
			for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
				MultipartFile fileItem = entity.getValue();
				String originalFilename = fileItem.getOriginalFilename();
				if (StringUtils.isNotEmpty(originalFilename)) {
					String fileType = StringUtils.split(originalFilename, ".")[1];
					String actualFileName = UUID.randomUUID().toString() + "." + fileType;
					if (StringUtils.isNotEmpty(category)) {
						path = path + category;
						url = url + category;
					}
					this.createDir(path);
					File savedFile = new File(path, actualFileName);
					fileItem.transferTo(savedFile);
					UnieapFileArchive fileArchive = new UnieapFileArchive();
					fileArchive.setArchiveDate(UnieapConstants.getDateTime());
					fileArchive.setExtKey(extKey);
					fileArchive.setCategory(category);
					fileArchive.setOriginalName(originalFilename);
					fileArchive.setActualName(actualFileName);
					fileArchive.setPath(path);
					fileArchive.setSize(BigInteger.valueOf(fileItem.getSize()));
					fileArchive.setType(fileType);
					fileArchive.setId(UnieapConstants.getSeq(null));
					url = url + File.separator + actualFileName;
					fileArchive.setUrl(url);

					fileArchive.setActivateFlag(UnieapConstants.YES);
					fileArchive.setTenantId(UnieapConstants.getTenantId());
					fileArchive.setRemark("");
					fileArchiveList.add(fileArchive);
				}
			}
			fileArchiveRepository.saveAll(fileArchiveList);
		}
		return fileArchiveList;
	}

	public String getFinialFileName(String originalFileName) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String dateString = formatter.format(currentTime);
		return originalFileName + "_" + dateString;
	}

	public boolean createDir(String path) {
		File filepath = new File(path);
		if (!filepath.exists()) {
			return filepath.mkdirs();
		}
		return true;
	}

	public void delete(Long fileId) {
		if ( fileArchiveRepository.existsById(fileId)) {
			fileArchiveRepository.deleteById(fileId);
		}
	}
}
