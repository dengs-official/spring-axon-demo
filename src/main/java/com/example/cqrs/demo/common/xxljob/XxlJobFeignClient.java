package com.example.cqrs.demo.common.xxljob;

import com.alibaba.fastjson.JSONObject;
import feign.Headers;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "xxljob", configuration = XxlJobFeignConfig.class)
public interface XxlJobFeignClient {
    @GetMapping("/xxl-job-admin/jobinfo/pageList")
    JSONObject pageList(@RequestParam(value = "jobGroup") int jobGroup);

    @PostMapping(value = "/xxl-job-admin/jobinfo/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @Headers("Content-Type: application/x-www-form-urlencoded")
    JSONObject add(@RequestBody Map<String, ?> form);

    @GetMapping("/xxl-job-admin/jobinfo/start")
    JSONObject start(@RequestParam(value = "id") int id);

    @GetMapping("/xxl-job-admin/jobinfo/stop")
    JSONObject stop(@RequestParam(value = "id") int id);
}
