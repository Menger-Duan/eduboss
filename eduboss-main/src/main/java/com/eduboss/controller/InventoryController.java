package com.eduboss.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domain.InventoryManager;
import com.eduboss.domain.InventoryProduct;
import com.eduboss.domain.InventoryRecord;
import com.eduboss.domain.Organization;
import com.eduboss.domainVo.AutoCompleteOptionVo;
import com.eduboss.domainVo.InventoryManagerVo;
import com.eduboss.domainVo.InventoryProductVo;
import com.eduboss.domainVo.InventoryRecordVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.service.InventoryService;


@Controller
@RequestMapping(value = "/Inventory")
public class InventoryController {
	
	@Autowired  
	private  HttpServletRequest request;
	
	@Autowired
	private InventoryService inventoryService;
	
	private DataPackage gridRequest2Datapackage(GridRequest gridRequest) {
		DataPackage dataPackage = new DataPackage();
		int pageNum = gridRequest.getPage() - 1;
		if (pageNum < 0) {
			pageNum = 0;
		}
		if (gridRequest.getRows() == 0) {
			gridRequest.setRows(999);
		}
		dataPackage.setPageNo(pageNum);
		dataPackage.setPageSize(gridRequest.getRows());
		dataPackage.setSidx(gridRequest.getSidx());
		dataPackage.setSord(gridRequest.getSord());
		
		return dataPackage;
	}
	
	/**
	 * 库存管理列表
	 * */
	@RequestMapping(value = "/getInventoryRecordForGrid")
	@ResponseBody
	public DataPackageForJqGrid getInventoryRecordForGrid(InventoryRecordVo inventoryRecordVo, @ModelAttribute GridRequest gridRequest){
		DataPackage dataPackage = gridRequest2Datapackage(gridRequest);
		dataPackage = inventoryService.getInventoryRecordForGrid(inventoryRecordVo, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	/**
	 * 新增库存产品
	 * */
	@RequestMapping(value = "/saveInventoryProduct")
	@ResponseBody
	public Response saveInventoryProduct(InventoryProduct product){
		inventoryService.saveInventoryProduct(product);
		return new Response();
	}
	
	/**
	 * 分页查询库存产品
	 * */
	@RequestMapping(value = "/getInventoryProductForGrid")
	@ResponseBody
	public DataPackageForJqGrid getInventoryProductForGrid(InventoryProductVo inventoryProductVo, @ModelAttribute GridRequest gridRequest){
		DataPackage dp = gridRequest2Datapackage(gridRequest);
		dp = inventoryService.getInventoryProductForGrid(inventoryProductVo, dp);
		return new DataPackageForJqGrid(dp);
	}
	
	/**
	 * 新增库存记录
	 * */
	@RequestMapping(value = "/saveInveotoryRecord")
	@ResponseBody
	public Response saveInveotoryRecord(InventoryRecord inventoryRecord){
		inventoryService.saveInventoryRecord(inventoryRecord);
		return new Response();
	}
	
	/**
	 * 移库
	 * */
	@RequestMapping(value = "/moveInventoryRecord")
	@ResponseBody
	public Response moveInventoryRecord(InventoryManager inventoryManager){
		inventoryService.moveInventoryRecord(inventoryManager);
		return new Response();
	}
	
	/**
	 * 报损
	 * */
	@RequestMapping(value = "/destroyInventoryRecord")
	@ResponseBody
	public Response destroyInventoryRecord(InventoryManager inventoryManager){
		inventoryService.destroyInventoryRecord(inventoryManager);
		return new Response();
	}
	
	/**
	 * 销售
	 * */
	@RequestMapping(value = "/saleInventoryRecord")
	@ResponseBody
	public Response saleInventoryRecord(InventoryManager inventoryManager){
		inventoryService.saleInventoryRecord(inventoryManager);
		return new Response();
	}
	
	/**
	 * 领用
	 * */
	@RequestMapping(value = "/consumeInventoryRecord")
	@ResponseBody
	public Response consumeInventoryRecord(InventoryManager inventoryManager){
		inventoryService.consumeInventoryRecord(inventoryManager);
		return new Response();
	}
	
	/**
	 * 退回
	 * */
	@RequestMapping(value = "/backInventoryRecord")
	@ResponseBody
	public Response backInventoryRecord(InventoryManager inventoryManager){
		inventoryService.backInventoryRecord(inventoryManager);
		return new Response();
	}

	
	
	//操作记录列表
	@RequestMapping(value = "/getInventoryManagerForGrid")
	@ResponseBody
	public DataPackageForJqGrid getInventoryManagerForGrid(InventoryManagerVo inventoryManagerVo, @ModelAttribute GridRequest gridRequest){
		DataPackage dp = gridRequest2Datapackage(gridRequest);
		dp = inventoryService.getInventoryManagerForGrid(inventoryManagerVo, dp);
		return new DataPackageForJqGrid(dp);
	}
	
	@RequestMapping(value = "/getStudentForPerformance")
	@ResponseBody
	public List<AutoCompleteOptionVo> getStudentForPerformance(@RequestParam String term){
		return inventoryService.getStudentForPerformance(term);
	}
	
	@RequestMapping(value = "/getOrganizationForInventory")
	@ResponseBody
	public List<Organization> getOrganizationForInventory(Organization org){
		return inventoryService.getOrganizationForInventory(org);
	}
}
