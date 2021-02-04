package com.gitee.cs_liwei.annocache.sample.api.controller;

import com.gitee.cs_liwei.annocache.sample.api.business.TestBusiness;
import com.gitee.cs_liwei.annocache.sample.api.model.TestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    TestBusiness testBusiness;

    /**
     * sample 1：API returns model object.
     * @param param1
     * @param param2
     * @param param3
     * @return
     */
    @RequestMapping("/test1")
    public TestModel test(@RequestParam int param1,
                          @RequestParam Integer param2,
                          @RequestParam String param3){

        //business process method with cache annotation.
        //the dummy process will cost over 1 second if no cache.
        return testBusiness.test(param1, param2, param3);
    }

    /**
     * sample2：API returns list of model objects.
     * @param param1
     * @param param2
     * @param param3
     * @return
     */
    @RequestMapping("/test2")
    public List<TestModel> test2(@RequestParam int param1,
                                 @RequestParam Integer param2,
                                 @RequestParam String param3){

        //business process method with cache annotation.
        //the dummy process will cost over 3 seconds if no cache.
        return testBusiness.test2(param1, param2, param3);
    }

    /**
     * sample3：API returns map of model objects.
     * @param param1
     * @param param2
     * @param param3
     * @return
     */
    @RequestMapping("/test3")
    public Map<String, TestModel> test3(@RequestParam int param1,
                                @RequestParam Integer param2,
                                @RequestParam String param3){

        //business process method with cache annotation.
        //the dummy process will cost over 3 seconds if no cache.
        return testBusiness.test3(param1, param2, param3);
    }
}
