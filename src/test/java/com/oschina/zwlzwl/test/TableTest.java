package com.oschina.zwlzwl.test;

import org.junit.Test;

import net.oschina.zwlzwl376.jfinal.plugin.tablebind.BindUtils;


public class TableTest {

    @Test
    public void underscoreName(){
        String tableName = BindUtils.underscoreName("tableNameTest");
        System.out.println(tableName);
    }
}
