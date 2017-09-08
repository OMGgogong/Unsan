package com.unsan.example.internalCommunication.client.msg;

import com.unsan.msg.ReqLaserLight;

public class LoveMsgreq extends ReqLaserLight{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LoveMsgreq(String msg) {
		this.setMsg(msg);
	}

}
