package com.oschina.zwlzwl.test;

import org.junit.Test;

import net.oschina.zwlzwl376.jfinal.plugin.utils.BindUtils;


public class TableTest {

    @Test
    public void underscoreName(){
        String tableName = BindUtils.underscoreName("TableNameTest");
        System.out.println(tableName);
    }
}
