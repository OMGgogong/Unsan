package com.unsan.example.internalCommunication.client.msg;

import com.unsan.msg.ReqLaserLight;
import com.unsan.msg.RespLaserLight;

public class LoveMsgresp extends RespLaserLight{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LoveMsgresp(LoveMsgreq req) {
		super(req);
	}

}
