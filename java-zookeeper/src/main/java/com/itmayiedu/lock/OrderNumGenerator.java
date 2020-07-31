
package com.itmayiedu.lock;

import java.text.SimpleDateFormat;
import java.util.Date;

//生成订单号规则
public class OrderNumGenerator {
	private static int count = 0;

	//生成订单号规则方法
	
	
	public String orderNumber() {
		SimpleDateFormat simpt = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		return simpt.format(new Date()) + "-" + ++count;
	}
	
	
	
	public static void main(String[] args) {
		OrderNumGenerator rn = new OrderNumGenerator();
		System.out.println(rn.orderNumber());
	}

}
