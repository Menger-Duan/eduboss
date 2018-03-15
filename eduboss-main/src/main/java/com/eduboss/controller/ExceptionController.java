package com.eduboss.controller;

import com.eduboss.dto.Response;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value="/ExceptionController")
@Controller
public class ExceptionController {
	private  Logger log = Logger.getLogger(ExceptionController.class);

	/**
	 *请求错误
	 * @return
	 */
	@RequestMapping(value="/returnErrInfo")
	@ResponseBody
	public Response returnErrInfo(int errCode){
		Response res = new Response();
		res.setResultMessage(errCode+"错误");
		res.setResultCode(errCode);
		log.error(errCode+"错误！");
		return res;
	}

}
