package com.slipper.unieap.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.pojo.UnieapOrder;
import com.slipper.unieap.repository.UnieapOrderRepository;
import com.slipper.unieap.vo.OrderVO;

@Service
public class OrderBO extends BaseBO {

	@Autowired
	public UnieapOrderRepository orderRepository;

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void addOrder(OrderVO vo) throws Exception {
		UnieapOrder pojo = new UnieapOrder();
		if (vo.getId() != null) {
			pojo.setId(vo.getId());
		}
		pojo.setExecuteClass(vo.getExecuteClass());
		pojo.setExecuteSchedule(vo.getExecuteSchedule());
		pojo.setInputParameters(vo.getInputParameters());
		pojo.setOrderStatus(vo.getOrderStatus());
		pojo.setOrderType(vo.getOrderType());
		pojo.setPendingOrderId(vo.getPendingOrderId());
		pojo.setInputParameters(vo.getInputParameters());
		pojo.setRemark(vo.getRemark());
		pojo.setActivateFlag(UnieapConstants.YES);
		pojo.setTenantId(UnieapConstants.getTenantId());
		orderRepository.save(pojo);
	}

}
