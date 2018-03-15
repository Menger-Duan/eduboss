package com.eduboss.servlet;

import com.eduboss.exception.ApplicationException;
import com.eduboss.service.ProductService;
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.JedisUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Timer;

/**
 * Created by Administrator on 2017/5/22.
 */
public class LiveProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    protected static ProductService productService =   ApplicationContextUtil.getContext().getBean(ProductService .class);
    private static org.apache.log4j.Logger log= org.apache.log4j.Logger.getLogger(LiveProductServlet.class);

    public LiveProductServlet()
    {
        super();
    }

    public void init() throws ServletException
    {
        Timer timer = new Timer();
        timer.schedule(new NormalTask(), 1000, 2000);// 在1秒后执行此任务,每次间隔2秒,如果传递一个Data参数,就可以在某个固定的时间执行这个任务.
        timer.schedule(new ErrorTask(), 2000, 10000);
    }

    //具体执行的任务
    class NormalTask extends java.util.TimerTask
    {
        public void run()
        {
            String productId = JedisUtil.rpop("liveProduct");
            if (productId != null) {
                transferProductToLivePlatform(productId);
            }
        }
    }

    //具体执行的任务
    class ErrorTask extends java.util.TimerTask
    {
        public void run()
        {
            String productId = JedisUtil.rpop("liveProductErro");
            if (productId != null) {
                transferProductToLivePlatform(productId);
            }
        }
    }
    public void transferProductToLivePlatform(String productId){
        try {
            productService.transferProductToLivePlatform(productId);
        }catch (ApplicationException e){
            log.error(productId+"直播产品报班失败:"+e.getErrorMsg());
        }catch (Exception e){
            log.error(productId+"直播产品报班失败:"+e.getMessage());
        }
    }

    public void destroy()
    {
        super.destroy();
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {

    }


    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {

    }
}
