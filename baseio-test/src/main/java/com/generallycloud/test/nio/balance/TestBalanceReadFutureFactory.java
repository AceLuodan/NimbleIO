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
package com.generallycloud.test.nio.balance;

import com.generallycloud.baseio.balance.BalanceFacadeSocketSession;
import com.generallycloud.baseio.balance.BalanceReadFutureFactory;
import com.generallycloud.baseio.codec.protobase.future.ProtobaseReadFuture;
import com.generallycloud.baseio.codec.protobase.future.ProtobaseReadFutureImpl;
import com.generallycloud.baseio.protocol.ReadFuture;

/**
 * @author wangkai
 *
 */
public class TestBalanceReadFutureFactory implements BalanceReadFutureFactory{

	@Override
	public ReadFuture createChannelLostPacket(BalanceFacadeSocketSession session) {
		return null;
	}

	@Override
	public ReadFuture createTokenPacket(BalanceFacadeSocketSession session) {
		ProtobaseReadFuture f = new ProtobaseReadFutureImpl(session.getContext(), "getToken");
		f.setToken(session.getToken());
		return f;
	}

	
	
	
}
