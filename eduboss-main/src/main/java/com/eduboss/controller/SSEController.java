package com.eduboss.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.common.Constants;
import com.eduboss.domainVo.OSSCallbackFileData;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.ProtostuffUtils;
import com.eduboss.utils.StringUtil;

/**
 * Created by Administrator on 2017/5/20.
 */
@Controller
@RequestMapping("/sse")
public class SSEController {

    private static final Logger logger = Logger.getLogger(SSEController.class);

    @RequestMapping(value = "/push", produces = "text/event-stream")
    @ResponseBody
    public void push(HttpServletRequest request, HttpServletResponse response, String id, String type) throws InterruptedException, IOException {
        String retry = "10000\n";
        // 设置报头为事件流
        response.setContentType("text/event-stream;charset=UTF-8");
        OutputStream bos = new BufferedOutputStream(response.getOutputStream());
        
        // 获取redis key
        String key = Constants.REDIS_PRE_OSS_UPLOAD_ID + id;
        String event = "event: " + id + type + "\n";
        String data = null;
        // 设置一秒查询一次
            // 查询redis中缓存的数据并进行推送处理
            byte[] result = JedisUtil.get(key.getBytes());
            if (result != null && result.length > 0) {
                OSSCallbackFileData fileData = ProtostuffUtils.deserializer(result,
                        OSSCallbackFileData.class);
                if (fileData != null) {
                    logger.info(type+"端SSE连接获取redis数据为："+JSONObject.fromObject(fileData).toString());
                    if(("PC").equalsIgnoreCase(type))
                    {
                        // 若PC端数据未被检查 返回给PC端监听事件
                        if(!fileData.isCheckedPC())
                        {
                            JSONArray json = JSONArray.fromObject(fileData);
                            data = "data:" + json.toString() + "\n\n";
                            fileData.setCheckedPC(true);
                            // 缓存数据
                            byte[] bytes = ProtostuffUtils.serializer(fileData);
                            JedisUtil.set(key.getBytes(), bytes, 60 * 10);
                        }
                    }
                    
                    if(("H5").equalsIgnoreCase(type))
                    {
                        // 若H5端数据未被检查 返回给H5端监听事件
                        if(!fileData.isCheckedH5())
                        {
                            JSONArray json = JSONArray.fromObject(fileData);
                            data = "data:" + json.toString() + "\n\n";
                            fileData.setCheckedH5(true);
                            // 缓存数据
                            byte[] bytes = ProtostuffUtils.serializer(fileData);
                            JedisUtil.set(key.getBytes(), bytes, 60 * 10);
                        }
                    }
                }
            }

        bos.write(retry.getBytes());
        // 若有数据更新，则进行推送
        if(StringUtil.isNotBlank(data))
        {
            logger.info("检查到更新，对"+type+"端进行推送");
            bos.write(event.getBytes());
            bos.write(data.getBytes());
        }
        bos.flush();
        bos.close();

    }

}
