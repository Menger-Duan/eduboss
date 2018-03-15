package com.eduboss.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.helpers.Transform;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

/**
 * 
 * @author qin.jingkai
 * @version v1.0
 * 2014-09-16
 * 
 * 因为默认的HTMLLayout时间一列位数值，位了方便显示成标准的时间 格式，故重写了HTMLLayout
 * 
 * 参考http://blog.csdn.net/drift_away/article/details/7410038
 *
 */
public class EduBossHTMLLayout extends HTMLLayout {
	
	private StringBuffer sbuf = new StringBuffer(BUF_SIZE); 
	
	boolean locationInfo = true;   
	
	static String TRACE_PREFIX = "<br>    ";  
	
	 String title;

	@Override
	public String format(LoggingEvent event) {
		// TODO Auto-generated method stub
		if (this.sbuf.capacity() > 1024)
		      this.sbuf = new StringBuffer(256);
		    else {
		      this.sbuf.setLength(0);
		    }

		    this.sbuf.append(Layout.LINE_SEP + "<tr>" + Layout.LINE_SEP);

		    this.sbuf.append("<td>");
		   // this.sbuf.append(event.timeStamp - LoggingEvent.getStartTime());
		    sbuf.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())); 
		    this.sbuf.append("</td>" + Layout.LINE_SEP);

		    String escapedThread = Transform.escapeTags(event.getThreadName());
		    this.sbuf.append("<td title=\"" + escapedThread + " thread\">");
		    this.sbuf.append(escapedThread);
		    this.sbuf.append("</td>" + Layout.LINE_SEP);

		    this.sbuf.append("<td title=\"Level\">");
		    if (event.getLevel().equals(Level.DEBUG)) {
		      this.sbuf.append("<font color=\"#339933\">");
		      this.sbuf.append(Transform.escapeTags(String.valueOf(event.getLevel())));
		      this.sbuf.append("</font>");
		    }
		    else if (event.getLevel().isGreaterOrEqual(Level.WARN)) {
		      this.sbuf.append("<font color=\"#993300\"><strong>");
		      this.sbuf.append(Transform.escapeTags(String.valueOf(event.getLevel())));
		      this.sbuf.append("</strong></font>");
		    } else {
		      this.sbuf.append(Transform.escapeTags(String.valueOf(event.getLevel())));
		    }
		    this.sbuf.append("</td>" + Layout.LINE_SEP);

		    String escapedLogger = Transform.escapeTags(event.getLoggerName());
		    this.sbuf.append("<td title=\"" + escapedLogger + " category\">");
		    this.sbuf.append(escapedLogger);
		    this.sbuf.append("</td>" + Layout.LINE_SEP);

		    if (this.locationInfo) {
		      LocationInfo locInfo = event.getLocationInformation();
		      this.sbuf.append("<td>");
		      this.sbuf.append(Transform.escapeTags(locInfo.getFileName()));
		      this.sbuf.append(':');
		      this.sbuf.append(locInfo.getLineNumber());
		      this.sbuf.append("</td>" + Layout.LINE_SEP);
		    }

		    this.sbuf.append("<td title=\"Message\">");
		    this.sbuf.append(Transform.escapeTags(event.getRenderedMessage()));
		    this.sbuf.append("</td>" + Layout.LINE_SEP);
		    this.sbuf.append("</tr>" + Layout.LINE_SEP);

		    if (event.getNDC() != null) {
		      this.sbuf.append("<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : xx-small;\" colspan=\"6\" title=\"Nested Diagnostic Context\">");
		      this.sbuf.append("NDC: " + Transform.escapeTags(event.getNDC()));
		      this.sbuf.append("</td></tr>" + Layout.LINE_SEP);
		    }

		    String[] s = event.getThrowableStrRep();
		    if (s != null) {
		      this.sbuf.append("<tr><td bgcolor=\"#993300\" style=\"color:White; font-size : xx-small;\" colspan=\"6\">");
		      appendThrowableAsHTML(s, this.sbuf);
		      this.sbuf.append("</td></tr>" + Layout.LINE_SEP);
		    }

		    return this.sbuf.toString();
	}
	
	
	 void appendThrowableAsHTML(String[] s, StringBuffer sbuf) {
		    if (s != null) {
		      int len = s.length;
		      if (len == 0)
		        return;
		      sbuf.append(Transform.escapeTags(s[0]));
		      sbuf.append(Layout.LINE_SEP);
		      for (int i = 1; i < len; ++i) {
		        sbuf.append(TRACE_PREFIX);
		        sbuf.append(Transform.escapeTags(s[i]));
		        sbuf.append(Layout.LINE_SEP);
		      }
		    }
		  }
	 
	 
	 
	 public String getHeader()
	  {
	    StringBuffer sbuf = new StringBuffer();
	    sbuf.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" + Layout.LINE_SEP);
	    sbuf.append("<html>" + Layout.LINE_SEP);
	    sbuf.append("<head>" + Layout.LINE_SEP);
	    sbuf.append("<title>" + this.title + "</title>" + Layout.LINE_SEP);
	    sbuf.append("<style type=\"text/css\">" + Layout.LINE_SEP);
	    sbuf.append("<!--" + Layout.LINE_SEP);
	    sbuf.append("body, table {font-family: arial,sans-serif; font-size: x-small;}" + Layout.LINE_SEP);
	    sbuf.append("th {background: #336699; color: #FFFFFF; text-align: left;}" + Layout.LINE_SEP);
	    sbuf.append("-->" + Layout.LINE_SEP);
	    sbuf.append("</style>" + Layout.LINE_SEP);
	    sbuf.append("</head>" + Layout.LINE_SEP);
	    sbuf.append("<body bgcolor=\"#FFFFFF\" topmargin=\"6\" leftmargin=\"6\">" + Layout.LINE_SEP);
	    sbuf.append("<hr size=\"1\" noshade>" + Layout.LINE_SEP);
	    sbuf.append("Log session start time " + new Date() + "<br>" + Layout.LINE_SEP);
	    sbuf.append("<br>" + Layout.LINE_SEP);
	    sbuf.append("<table cellspacing=\"0\" cellpadding=\"4\" border=\"1\" bordercolor=\"#224466\" width=\"100%\">" + Layout.LINE_SEP);
	    sbuf.append("<tr>" + Layout.LINE_SEP);
	    sbuf.append("<th>Time</th>" + Layout.LINE_SEP);
	    sbuf.append("<th>Thread</th>" + Layout.LINE_SEP);
	    sbuf.append("<th>Level</th>" + Layout.LINE_SEP);
	    sbuf.append("<th>Category</th>" + Layout.LINE_SEP);
	    if (this.locationInfo)
	      sbuf.append("<th>File:Line</th>" + Layout.LINE_SEP);

	    sbuf.append("<th>Message</th>" + Layout.LINE_SEP);
	    sbuf.append("</tr>" + Layout.LINE_SEP);
	    return sbuf.toString();
	  }

	  public String getFooter()
	  {
	    StringBuffer sbuf = new StringBuffer();
	    sbuf.append("</table>" + Layout.LINE_SEP);
	    sbuf.append("<br>" + Layout.LINE_SEP);
	    sbuf.append("</body></html>");
	    return sbuf.toString();
	  }
	
	

}
