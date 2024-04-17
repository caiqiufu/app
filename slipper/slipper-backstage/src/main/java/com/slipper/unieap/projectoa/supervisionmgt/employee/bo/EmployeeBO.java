package com.slipper.unieap.projectoa.supervisionmgt.employee.bo;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.slipper.service.modules.administrator.service.AdministratorService;
import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.projectoa.supervisionmgt.repository.EmployeeInfoRepository;
import com.slipper.unieap.projectoa.supervisionmgt.vo.EmployeeInfoVO;
import com.slipper.unieap.vo.PaginationSupport;

@Service
public class EmployeeBO extends BaseBO {

	@Autowired
	public EmployeeInfoRepository employeeInfoRepository;

	@Resource
	private AdministratorService administratorService;

	public PaginationSupport getGridList(PaginationSupport page, EmployeeInfoVO vo) throws Exception {
		Page<EmployeeInfoVO> datas = employeeInfoRepository.getEmployeeInfoPage(vo.getName(), vo.getCompanyName(),
				this.getPageable(page));
		page.items = datas.getContent();
		page.setTotalCount((int) datas.getTotalElements());
		return page;
	}
}
