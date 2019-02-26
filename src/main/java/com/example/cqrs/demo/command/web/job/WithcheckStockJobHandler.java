package com.example.cqrs.demo.command.web.job;

import com.alibaba.fastjson.JSONObject;
import com.example.cqrs.demo.command.commands.WithcheckStockCommand;
import com.example.cqrs.demo.command.commands.WithdrawMoneyCommand;
import com.example.cqrs.demo.common.domain.AccountId;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@JobHandler(value = "WithcheckStockJobHandler")
@Component
public class WithcheckStockJobHandler extends IJobHandler {

    @Autowired
    public CommandGateway commandGateway;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("Start to Check product stock");
        try {
            commandGateway.sendAndWait(new WithcheckStockCommand(s));
        } catch (Exception e) {
            XxlJobLogger.log("Check product {} stock occur exception ", e);
            return FAIL;
        }

        return SUCCESS;
    }
}
