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
package com.generallycloud.baseio.codec.protobase.future;

import com.generallycloud.baseio.buffer.ByteBuf;
import com.generallycloud.baseio.component.SocketChannelContext;
import com.generallycloud.baseio.component.SocketSession;

/**
 *
 */
public class HashedProtobaseReadFutureImpl extends SessionIdProtobaseReadFutureImpl
		implements HashedProtobaseReadFuture {

	public HashedProtobaseReadFutureImpl(SocketChannelContext context) {
		super(context);
	}
	
	public HashedProtobaseReadFutureImpl(SocketChannelContext context, int futureID, String futureName) {
		super(context, futureID, futureName);
	}

	public HashedProtobaseReadFutureImpl(SocketChannelContext context, String futureName) {
		this(context, 0, futureName);
	}

	public HashedProtobaseReadFutureImpl(SocketSession session, ByteBuf buf, boolean isBroadcast) {
		super(session, buf, isBroadcast);
	}

	private int		hashCode;

	@Override
	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}

	@Override
	public int getHashCode() {
		return hashCode;
	}

	@Override
	protected void generateHeaderExtend(ByteBuf buf) {
		super.generateHeaderExtend(buf);
		this.hashCode = buf.getInt();
	}

}
