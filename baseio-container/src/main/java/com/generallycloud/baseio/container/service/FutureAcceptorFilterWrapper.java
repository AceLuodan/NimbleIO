/*
 * Copyright 2015-2017 GenerallyCloud.com
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package com.generallycloud.baseio.container.service;

import com.generallycloud.baseio.component.SocketSession;
import com.generallycloud.baseio.concurrent.Linkable;
import com.generallycloud.baseio.container.ApplicationContext;
import com.generallycloud.baseio.container.configuration.Configuration;
import com.generallycloud.baseio.protocol.NamedReadFuture;
import com.generallycloud.baseio.protocol.ReadFuture;

public class FutureAcceptorFilterWrapper extends FutureAcceptorFilter implements Linkable<FutureAcceptorFilter> {

	private FutureAcceptorFilter			filter;
	private boolean isValidate;
	private FutureAcceptorFilterWrapper	nextFilter;

	public FutureAcceptorFilterWrapper(ApplicationContext context, FutureAcceptorFilter filter, Configuration config) {
		this.filter = filter;
		this.setConfig(config);
	}
	
	@Override
	protected void accept(SocketSession session, NamedReadFuture future) throws Exception {
		getValue().accept(session, future);
	}

	@Override
	public void accept(SocketSession session, ReadFuture future) throws Exception {
		
		FutureAcceptorFilter filter = getValue();
		
		future.setIoEventHandle(filter);
	
		filter.accept(session, future);
		
		if (future.flushed()) {
			return;
		}
		
		nextAccept(session, future);
	}
	
	@Override
	public void destroy(ApplicationContext context, Configuration config) throws Exception {
		getValue().destroy(context, config);
	}

	@Override
	public void exceptionCaught(SocketSession session, ReadFuture future, Exception cause, IoEventState state) {
		getValue().exceptionCaught(session, future, cause, state);
	}

	@Override
	public void futureSent(SocketSession session, ReadFuture future) {
		getValue().futureSent(session, future);
	}

	@Override
	public FutureAcceptorFilterWrapper getNext() {
		return nextFilter;
	}

	@Override
	public FutureAcceptorFilter getValue() {
		return filter;
	}

	@Override
	public void initialize(ApplicationContext context, Configuration config) throws Exception {
		getValue().initialize(context, config);
	}

	@Override
	public boolean isValidate() {
		return isValidate;
	}

	private void nextAccept(SocketSession session, ReadFuture future) throws Exception{
		
		FutureAcceptorFilterWrapper next = getNext();
		
		if (next == null) {
			return;
		}
		
		next.accept(session, future);
	}

	@Override
	public void setNext(Linkable<FutureAcceptorFilter> next) {
		this.nextFilter = (FutureAcceptorFilterWrapper) next;
	}

	@Override
	public void setValidate(boolean validate) {
		this.isValidate = validate;
	}

	@Override
	public String toString() {
		return "Warpper(" + getValue().toString() + ")";
	}

}
