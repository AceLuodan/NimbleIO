package com.generallycloud.nio.component.protocol;

import java.io.InputStream;

import com.generallycloud.nio.component.Session;
import com.generallycloud.nio.component.TCPEndPoint;

public abstract class AbstractIOReadFuture extends AbstractReadFuture implements IOReadFuture {

	public AbstractIOReadFuture(Session session) {
		super(session);
	}
	
	protected boolean isBeatPacket;
	
	public void flush() {
		flushed = true;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setHasOutputStream(boolean hasOutputStream) {
		this.hasOutputStream = hasOutputStream;
	}
	
	public boolean isBeatPacket() {
		return isBeatPacket;
	}
	
	public TCPEndPoint getTCPEndPoint() {
		return endPoint;
	}
}
