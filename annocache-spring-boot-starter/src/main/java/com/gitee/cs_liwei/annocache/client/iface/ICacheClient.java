package com.gitee.cs_liwei.annocache.client.iface;

import java.time.Duration;

public interface ICacheClient {

    public String get(String key);

    public void set(String key, String value, Duration ofSeconds);
}
