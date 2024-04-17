package com.slipper.unieap.demo.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.slipper.service.unieap.base.email.MailBO;
import com.slipper.service.unieap.base.email.MailVO;
import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.db.Criteria;
import com.slipper.unieap.db.Restrictions;
import com.slipper.unieap.demo.pojo.UnieapDemo;
import com.slipper.unieap.demo.repository.UnieapDemoRepository;
import com.slipper.unieap.vo.PaginationSupport;

@Service
public class DemoBO extends BaseBO {

	@Autowired
	public UnieapDemoRepository unieapDemoRepository;
	
	@Resource
	private MailBO mailBO;

	public PaginationSupport getGridList(PaginationSupport page, Object obj) throws Exception {
		UnieapDemo vo = (UnieapDemo) obj;
		Criteria<UnieapDemo> criteria = new Criteria<>();
		criteria.add(Restrictions.eq("id", vo.getId(), true));
		String message = UnieapConstants.getMessage("test.message", Locale.SIMPLIFIED_CHINESE.toLanguageTag(), null);
		System.out.println("message=" + message);
		unieapDemoRepository.getPaginationDataByMysql(page, null, null);
		return page;
	}

	public UnieapDemo getInfo(Long id) {
		return unieapDemoRepository.findById(id).get();
	}

	public void create(UnieapDemo vo) throws Exception {
		vo.setId(UnieapConstants.getSeq(null));
		vo.setCode("code");
		vo.setGroupId(Long.valueOf(1));
		vo.setTenantId(Long.valueOf(1));
		this.save(unieapDemoRepository, vo);
	}

	public void update(UnieapDemo vo) throws Exception {
		vo.setCode("code");
		vo.setGroupId(Long.valueOf(1));
		vo.setTenantId(Long.valueOf(1));
		this.update(unieapDemoRepository, vo);
	}

	public void delete(UnieapDemo vo) throws Exception {
		List<Long> ids = new ArrayList<Long>();
		ids.add(vo.getId());
		this.deleteBatch(unieapDemoRepository, ids);
	}

	public void deleteBatch(List<Long> ids) throws Exception {
		if (ids != null && ids.size() > 0) {
			this.deleteBatch(unieapDemoRepository, ids);
		}
	}

	public void email() throws Exception {

		MailVO mailvo = new MailVO();
		mailvo.setSubject("test");
		mailvo.setContent("test email");
		mailvo.setToAddressList(new String[] {"caiqiufu@hotmail.com"});
		try {
			mailBO.sendMail("744161932@qq.com", mailvo);
		} catch (Exception e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
