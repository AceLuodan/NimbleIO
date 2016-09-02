package com.generallycloud.nio.connector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.locks.ReentrantLock;

import com.generallycloud.nio.common.LifeCycleUtil;
import com.generallycloud.nio.component.AbstractIOService;
import com.generallycloud.nio.component.NIOContext;
import com.generallycloud.nio.component.Session;
import com.generallycloud.nio.component.concurrent.UniqueThread;
import com.generallycloud.nio.configuration.ServerConfiguration;

public abstract class AbstractIOConnector extends AbstractIOService implements IOConnector {

	protected boolean			active		= false;
	protected ReentrantLock		activeLock	= new ReentrantLock();
	protected InetSocketAddress	serverAddress	;
	protected Session			session		;

	protected abstract UniqueThread getSelectorLoopThread();

	public void close() throws IOException {

		Thread thread = Thread.currentThread();

		UniqueThread loopThread = getSelectorLoopThread();
		
		if (loopThread != null && loopThread.isMonitor(thread)) {
			throw new IllegalStateException("not allow to close on future callback");
		}
		
		ReentrantLock lock = this.activeLock;

		lock.lock();

		try {

			if (!active) {
				return;
			}

			close(context);

		} finally {

			active = false;

			LifeCycleUtil.stop(context);

			lock.unlock();
		}
	}
	
	protected abstract void close(NIOContext context);
	
	public void connect() throws IOException {
		
		ReentrantLock lock = this.activeLock;

		lock.lock();

		try {

			if (active) {
				return;
			}

			if (context == null) {
				throw new IllegalArgumentException("null nio context");
			}

			LifeCycleUtil.start(context);
			
			ServerConfiguration configuration = context.getServerConfiguration();

			String SERVER_HOST = getSERVER_HOST(configuration);

			int SERVER_PORT = getSERVER_PORT(configuration);

			this.serverAddress = new InetSocketAddress(SERVER_HOST, SERVER_PORT);
			
			this.setIOService(context);

			this.connect(context,serverAddress);

			active = true;

		} finally {

			lock.unlock();
		}
	}
	
	protected abstract void connect(NIOContext context,InetSocketAddress socketAddress) throws IOException;

	public Session getSession() {
		return session;
	}

	public boolean isConnected() {
		return session != null && session.isOpened();
	}
	
	public boolean isActive() {
		return isConnected();
	}
}
