package com.gitee.cs_liwei.annocache.sample.api.business;

import com.gitee.cs_liwei.annocache.annotation.AnnoCache;
import com.gitee.cs_liwei.annocache.sample.api.model.TestModel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * dummy business
 */
@Component
public class TestBusiness {

    @AnnoCache(seconds = 45)
    public TestModel test(int param1, Integer param2, String param3){
        TestModel model = new TestModel();

        //int field
        model.setTestField1(String.format("param:{} {} {}",  param1 , param2 , param3));
        model.setTestField2(666l);

        //String list field
        List<String> list1 = new ArrayList<>();
        list1.add("listitem1 data");
        list1.add("listitem2 data");
        model.setTestField3(list1);

        //date time field
        LocalDateTime now = LocalDateTime.now();
        model.setTestField4(now);

        //Object list field
        List<TestModel.InnerModel> list2 = new ArrayList<>();
        TestModel.InnerModel innerModel = new TestModel.InnerModel();
        innerModel.setInnerField("valueInner1");
        list2.add(innerModel);
        innerModel = new TestModel.InnerModel();
        innerModel.setInnerField("valueInner2");
        list2.add(innerModel);
        model.setTestField5(list2);

        //dummy process time cost（1000 milliseconds）
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return model;
    }

    @AnnoCache(seconds = 45)
    public List<TestModel>  test2(int param1, Integer param2, String param3){
        List<TestModel> list = new ArrayList<>();

        list.add(test(param1, param2, param3));
        list.add(test(param1, param2, param3));
        list.add(test(param1, param2, param3));

        return list;
    }

    @AnnoCache(seconds = 45)
    public Map<String, TestModel> test3(int param1, Integer param2, String param3){
        Map<String, TestModel> map = new HashMap<>();

        map.put("mapkey1", test(param1, param2, param3));
        map.put("mapkey2", test(param1, param2, param3));
        map.put("mapkey3", test(param1, param2, param3));

        return map;
    }
}
