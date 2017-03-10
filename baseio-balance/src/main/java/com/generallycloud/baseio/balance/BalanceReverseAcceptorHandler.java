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
package com.generallycloud.baseio.balance;

import com.generallycloud.baseio.balance.router.BalanceRouter;
import com.generallycloud.baseio.common.Logger;
import com.generallycloud.baseio.common.LoggerFactory;
import com.generallycloud.baseio.component.IoEventHandleAdaptor;
import com.generallycloud.baseio.component.SocketSession;
import com.generallycloud.baseio.protocol.ReadFuture;

public class BalanceReverseAcceptorHandler extends IoEventHandleAdaptor {

	private Logger				logger	= LoggerFactory.getLogger(getClass());
	private BalanceRouter		balanceRouter;
	private BalanceFacadeAcceptor	balanceFacadeAcceptor;
	private ExceptionCaughtHandle	exceptionCaughtHandle;
	private BalanceReverseLogger	balanceReverseLogger;

	public BalanceReverseAcceptorHandler(BalanceContext context) {
		this.balanceRouter = context.getBalanceRouter();
		this.balanceReverseLogger = context.getBalanceReverseLogger();
		this.balanceFacadeAcceptor = context.getBalanceFacadeAcceptor();
		this.exceptionCaughtHandle = context.getReverseExceptionCaughtHandle();
	}

	@Override
	public void accept(SocketSession session, ReadFuture future) throws Exception {

		BalanceReadFuture f = (BalanceReadFuture) future;

		if (f.isBroadcast()) {

			balanceFacadeAcceptor.getAcceptor().broadcast(f.translate());

			balanceReverseLogger.logBroadcast(session, future, logger);

			return;
		}

		SocketSession response = balanceRouter.getClientSession(f.getToken());

		if (response == null || response.isClosed()) {

			balanceReverseLogger.logPushLost(session, future, logger);

			return;
		}

		f.setIOEventHandle(response.getContext().getIoEventHandleAdaptor());

		response.flush(f.translate());

		balanceReverseLogger.logPush(session, response, future, logger);
	}

	@Override
	public void exceptionCaught(SocketSession session, ReadFuture future, Exception cause,
			IoEventState state) {
		exceptionCaughtHandle.exceptionCaught(session, future, cause, state);
	}

}
