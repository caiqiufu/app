package com.slipper.unieap.file.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.slipper.common.utils.R;
import com.slipper.unieap.controller.CommonController;
import com.slipper.unieap.file.bo.FileBO;
import com.slipper.unieap.file.pojo.UnieapFileArchive;

@RestController
@RequestMapping("/backstage/unieap/file")
public class UFileController extends CommonController {

	@Autowired
	FileBO fileBO;

	@RequestMapping("/upload")
	public R upload(UnieapFileArchive vo, MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, MultipartFile> fileMap = request.getFileMap();
		List<UnieapFileArchive> files = fileBO.upload(vo, fileMap);
		return R.success(files);
	}
	@RequestMapping("/delete")
	public R delete(Long fileId) throws Exception {
		fileBO.delete(fileId);
		return R.success();
	}
}
