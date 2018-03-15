package com.eduboss.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckPhoneNumber {

	/**
	 * 手机号验证
	 * 
	 * @param  str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) { 
		Pattern p = null;
		Matcher m = null;
		boolean b = false; 
		p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches(); 
		return b;
	}
	/**
	 * 电话号码验证
	 * 
	 * @param  str
	 * @return 验证通过返回true
	 */
	public static boolean isPhone(String str) { 
		Pattern p1 = null,p2 = null;
		Matcher m = null;
		boolean b = false;  
		p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
		if(str.length() >9)
		{	
			m = p1.matcher(str);
 		    b = m.matches();  
		}else{
			m = p2.matcher(str);
 			b = m.matches(); 
		}  
		return b;
	}
	
	//判断，返回布尔值
	private static boolean isPhoneNumber(String input){
		String regex="1([\\d]{10})|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}";
		Pattern p = Pattern.compile(regex);
		return p.matches(regex,input);
	}
	
	public static boolean checkPhoneNum(String input){
//		if(11==input.length()){
//			if(isTelephone1(input) || isMobileNO(input)||isTelephone(input)) return true;
//			else return false;
//		}else{
//			if (input.length()>=10 && input.length()<=13){
//				if(isTelephone1(input)) {
//					return true;
//				}
//				else if (isTelephone(input)){
//					return true;
//				} else{
//					return false;
//				}
//			}else if (input.length()>=7 && input.length()<=8){
//				if(isTelephoneNoCodeOfTel(input)) return true;
//				else return false;
//			}else {
//				return false;
//			}
//		}
		return checkContact(input);
	}
	
	public static boolean checkContact(String phone){
		//String regex="^(010\\d{8})|(010-\\d{8})|(02[0-9]\\d{8})|(02[0-9]-\\d{8})|(0[3-9]\\d{2}-\\d{7})|([2-9]\\d{6,7})|(13\\d{9})|(14[57]\\d{8})|(15\\d{9})|(17[0678]\\d{8})|(18\\d{9})$";
		//#742807  增加以下号码段：141、144、146、148、166、174、198、199
		String regex ="(^010\\d{8}$)|(^010-\\d{8}$)|(^02[0-9]\\d{8}$)|(^02[0-9]-\\d{8}$)|(^0[3-9]\\d{2}-\\d{7,8}$)|((^0[3-9]\\d{9,10}$))|(^[2-9]\\d{6,7}$)|(^13\\d{9}$)|(^14[145678]\\d{8}$)|(^15\\d{9}$)|(^166\\d{8}$)|(^17[01345678]\\d{8}$)|(^18\\d{9}$)|(^19[89]\\d{8}$)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(phone);
		return m.matches();
	}
	
	
	public static boolean isTelephone(String phonenumber) {
        String phone = "0\\d{2,3}-\\d{7,8}";
//        String phone = "^0?(10|(2|3[1,5,7]|4[1,5,7]|5[1,3,5,7]|7[1,3,5,7,9]|8[1,3,7,9])[0-9]|91[0-7,9]|(43|59|85)[1-9]|39[1-8]|54[3,6]|(701|580|349|335)|54[3,6]|69[1-2]|44[0,8]|48[2,3]|46[4,7,8,9]|52[0,3,7]|42[1,7,9]|56[1-6]|63[1-5]|66[0-3,8]|72[2,4,8]|74[3-6]|76[0,2,3,5,6,8,9]|82[5-7]|88[1,3,6-8]|90[1-3,6,8,9])(-)\\d{7,8}$";
        Pattern p = Pattern.compile(phone);
        Matcher m = p.matcher(phonenumber);
        return m.matches();
    }

	public static boolean isTelephoneNoCodeOfTel(String phonenumber){
        String phone = "\\d{7,10}";  //\\d{7,8}
        Pattern p = Pattern.compile(phone);
        Matcher m = p.matcher(phonenumber);
        return m.matches();
	}
	
	public static boolean isTelephone1(String phonenumber) {
        String phone = "0\\d{2,3}\\d{7,8}";
//        String phone = "^0?(10|(2|3[1,5,7]|4[1,5,7]|5[1,3,5,7]|7[1,3,5,7,9]|8[1,3,7,9])[0-9]|91[0-7,9]|(43|59|85)[1-9]|39[1-8]|54[3,6]|(701|580|349|335)|54[3,6]|69[1-2]|44[0,8]|48[2,3]|46[4,7,8,9]|52[0,3,7]|42[1,7,9]|56[1-6]|63[1-5]|66[0-3,8]|72[2,4,8]|74[3-6]|76[0,2,3,5,6,8,9]|82[5-7]|88[1,3,6-8]|90[1-3,6,8,9])(-)\\d{7,8}$";
        Pattern p = Pattern.compile(phone);
        Matcher m = p.matcher(phonenumber);
        return m.matches();
    }
	
    // 判断手机号
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("(^13\\d{9}$)|(^14[145678]\\d{8}$)|(^15\\d{9}$)|(^166\\d{8}$)|(^17[01345678]\\d{8}$)|(^18\\d{9}$)|(^19[89]\\d{8}$)");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
	
	public static void main(String[] args){
		String str="0668-7232865";
		if(isTelephone(str)){
			System.out.println("是电话号码");
		}else{
			System.out.println("不是电话号码");
		}
		if(isTelephone1(str)){
			System.out.println("是电话号码1");
		}else{
			System.out.println("不是电话号码1");
		}
		if(isMobileNO(str)){
			System.out.println("是手机号码");
		}else{
			System.out.println("不是手机号码");
		}
		if(isTelephoneNoCodeOfTel(str)){
			System.out.println("是电话号码2");
		}else{
			System.out.println("不是电话号码2");
		}
	}
	
}
