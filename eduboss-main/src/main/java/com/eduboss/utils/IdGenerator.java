package com.eduboss.utils;

import com.eduboss.exception.ApplicationException;
import org.nutz.dao.entity.annotation.Id;

/**
 * 
 * twitter的uuid算法
 *
 */
public class IdGenerator {

	private static volatile IdGenerator idGenerator = null;
	

	public static IdGenerator idGenerator()
	{
		if(idGenerator == null)
		{
			synchronized(IdGenerator.class)
			{
				if(idGenerator == null){
					idGenerator = new IdGenerator();
				}
				
			}
		}
		return idGenerator;
	}
	
	private long workerId = 1L;
	//默认时间戳是2017-01-01 00:00:00
	private final static long twepoch = 1483200000000L;
	private long sequence = 0L;
    private final static long workerIdBits = 10L;
	public final static long maxWorkerId = -1L ^ -1L << workerIdBits;
	private final static long sequenceBits = 10L;
	private final static long workerIdShift = sequenceBits;
	private final static long timestampLeftShift = sequenceBits + workerIdBits;
	public final static long sequenceMask = -1L ^ -1L << sequenceBits;
	private long lastTimestamp = -1L;
	 
	private IdGenerator() {

	}
	
	public synchronized String nextId() {
		long timestamp = this.timeGen();
		if (this.lastTimestamp == timestamp) 
		{
			this.sequence = (this.sequence + 1) & sequenceMask;
			if (this.sequence == 0) {
				timestamp = this.tilNextMillis(this.lastTimestamp);
	    	}
	    } 
		else 
		{
	    	this.sequence = 0;
	    }
		if (timestamp < this.lastTimestamp) 
		{
			try {
				throw new ApplicationException(String.format("时钟后移，%d毫秒内生成不了id",this.lastTimestamp - timestamp));
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	}
	    }
		this.lastTimestamp = timestamp;
		long nextId = ((timestamp - twepoch << timestampLeftShift))| (workerId << workerIdShift) | (sequence);
		return String.valueOf(nextId);
	}
	
	public synchronized String nextId(String prefix) {
		String nextId = nextId();
		if(StringUtil.isNotBlank(prefix))
		{
			return prefix + nextId;
		}
		return nextId;
	 }

	 private long tilNextMillis(final long lastTimestamp) {

		 long timestamp = this.timeGen();
		 while (timestamp <= lastTimestamp) {
			 timestamp = this.timeGen();
			 }
		 return timestamp;
	 }
	 
	 private long timeGen() {
		 return System.currentTimeMillis();
	 }
	 
	public static void main(String[] args) {
        System.out.println(IdGenerator.idGenerator().nextId());
	}

}
