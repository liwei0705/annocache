package com.gitee.cs_liwei.annocache.aop;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class AnnoCacheAopConfig {
    @Value("${spring.annocache.enable:true}")
    private boolean cache_enable;//enable annotation cache cache when true.

    @Value("${spring.annocache.cache_null:false}")
    private boolean cache_null;//cache the null value or not.

    @Value("${spring.annocache.cache_backup:true}")
    private boolean cache_backup;//if true, enable level2 cache to backup the last version cached value.

    @Value("${spring.annocache.cache_backup_seconds:259200}")
    private long cache_backup_seconds;//level2 cache durations. default 3 days.
}
