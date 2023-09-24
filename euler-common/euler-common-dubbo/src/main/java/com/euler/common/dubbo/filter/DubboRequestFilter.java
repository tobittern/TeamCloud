package com.euler.common.dubbo.filter;

import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.SpringUtils;
import com.euler.common.dubbo.enumd.RequestLogEnum;
import com.euler.common.dubbo.properties.DubboCustomProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.service.GenericService;

/**
 * dubbo日志过滤器
 *
 * @author euler
 */
@Slf4j
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER})
public class DubboRequestFilter implements Filter {

        @Override
        public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

            //1
            Result result = invoker.invoke(invocation);
//            //2
//            if (result.hasException() && GenericService.class != invoker.getInterface()) {
//                Throwable exception = result.getException();
//                throw  new ServiceException(result.getException().getMessage());
//
//            }

            return result;

        }


}
