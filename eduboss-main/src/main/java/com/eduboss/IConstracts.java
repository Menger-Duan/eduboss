/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss;

/**
 * 保存系统常量，仅仅用于部分int String等常量的统一存放， 有分类意义的需要使用枚举
 * 
 * @author xiangshaoxu 2016年6月3日下午4:21:15
 * @version 1.0.0
 */
public interface IConstracts {
	/**excel宽度和高度单位不一致，基本上h,w=15则导出为长方形，符合大多情况*/
	final int EXCEL_DEFAULT_ROW_HEIGHT = 15;
	final int EXCEL_DEFAULT_CELL_WIDTH = 15;
}
