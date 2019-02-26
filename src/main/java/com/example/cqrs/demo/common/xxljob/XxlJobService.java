package com.example.cqrs.demo.common.xxljob;

import com.alibaba.fastjson.JSONObject;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.Map;

public interface XxlJobService {

    @RequestLine("GET /xxl-job-admin/jobinfo/pageList?jobGroup={jobGroup}")
    JSONObject pageList(@Param(value = "jobGroup") int jobGroup);

    @RequestLine("POST /xxl-job-admin/jobinfo/add")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    JSONObject add(Map<String, ?> form);

    @RequestLine("GET /xxl-job-admin/jobinfo/start?id={id}")
    JSONObject start(@Param(value = "id") int id);

    @RequestLine("GET /xxl-job-admin/jobinfo/stop?id={id}")
    JSONObject stop(@Param(value = "id") int id);
}
