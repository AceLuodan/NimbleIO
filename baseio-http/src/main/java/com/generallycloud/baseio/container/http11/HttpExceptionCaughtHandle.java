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
package com.generallycloud.baseio.container.http11;

import com.generallycloud.baseio.codec.http11.future.HttpStatus;
import com.generallycloud.baseio.codec.http11.future.ServerHttpFuture;
import com.generallycloud.baseio.component.ExceptionCaughtHandle;
import com.generallycloud.baseio.component.SocketSession;
import com.generallycloud.baseio.log.DebugUtil;
import com.generallycloud.baseio.log.Logger;
import com.generallycloud.baseio.log.LoggerFactory;
import com.generallycloud.baseio.protocol.Future;

/**
 * @author wangkai
 *
 */
public class HttpExceptionCaughtHandle implements ExceptionCaughtHandle {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void exceptionCaught(SocketSession session, Future future, Exception ex) {
        logger.error(ex.getMessage(), ex);
        ServerHttpFuture f = new ServerHttpFuture(session.getContext());
        f.write("oops, server threw an inner exception, the stack trace is :\n");
        f.write("-------------------------------------------------------\n");
        f.write(DebugUtil.exception2string(ex));
        f.setStatus(HttpStatus.C500);
        session.flush(f);
    }

}
