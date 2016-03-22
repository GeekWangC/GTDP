package com.glodon.easymedicalapptosvn.bean;

/**
 * 服務器基类Bean
 * 
 * @author shirr
 * 
 */
public class BaseBean {

	/** 服务器返回的code **/
	private String retcode;

	/** 服务器返回的信息 **/
    private String retmsg;

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public String getRetmsg() {
        return retmsg;
    }

    public void setRetmsg(String retmsg) {
        this.retmsg = retmsg;
    }
}
