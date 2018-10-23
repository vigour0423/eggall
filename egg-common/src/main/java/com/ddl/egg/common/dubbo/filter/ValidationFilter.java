package com.ddl.egg.common.dubbo.filter;


import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;


/**
 * Created by mark.huang on 2017-03-28.
 */
public class ValidationFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext context = RpcContext.getContext();
        if (context.isProviderSide()) {
            Object[] objects = context.getArguments();
        }

        return invoker.invoke(invocation);
    }
}

