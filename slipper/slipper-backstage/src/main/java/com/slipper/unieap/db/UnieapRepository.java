package com.slipper.unieap.db;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.utils.DBUtils;
import com.slipper.unieap.vo.PaginationSupport;

@NoRepositoryBean
public interface UnieapRepository<T> extends JpaSpecificationExecutor<T>, JpaRepository<T, Long> {
	
	/**
	 * 
	 * @param page
	 * @param criteria
	 * @throws Exception
	 */
	default void getPaginationDataByMysql(PaginationSupport page, Criteria<T> criteria) throws Exception {
		this.getPaginationDataByMysql(page, criteria, null);
	}

	/**
	 * 
	 * @param page
	 * @param criteria
	 * @param vo
	 * @throws Exception
	 */
	default void getPaginationDataByMysql(PaginationSupport page, Criteria<T> criteria, Object vo) throws Exception {
		Sort sort = null;
		Pageable pageable = null;
		if (StringUtils.isNotEmpty(page.getDir()) && StringUtils.isNotEmpty(page.getSort())) {
			if (page.ASC.equals(page.getDir().toUpperCase())) {
				sort = Sort.by(Direction.ASC, page.getSort());
			} else {
				sort = Sort.by(Sort.Direction.DESC, page.getSort());
			}
			pageable = PageRequest.of(page.getCurrentPage() - 1, page.getPageSize(), sort);
		} else {
			pageable = PageRequest.of(page.getCurrentPage() - 1, page.getPageSize());
		}
		if (criteria == null) {
			criteria = new Criteria<>();
		}
		if (!UnieapConstants.isAdminWithActivate()) {
			criteria.add(Restrictions.eq("activateFlag", UnieapConstants.YES, true));
		}
		if (vo != null) {
			DBUtils.setCriteria(criteria, vo);
		}
		Page<T> datas = this.findAll(criteria, pageable);
		page.setTotalCount((int) datas.getTotalElements());
		page.setItems(datas.getContent());
	}

	default Long count(Criteria<T> criteria) {
		List<T> datas = this.findAll(criteria);
		if (datas != null) {
			return Long.valueOf(datas.size());
		} else {
			return Long.valueOf(0);
		}
	}
}
