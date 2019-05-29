//package com.studerw.config;
//
//import com.google.common.collect.Lists;
//import redis.embedded.Redis;
//import redis.embedded.RedisServer;
//
//import java.io.IOException;
//import java.util.List;
//
///**
// * Created by studerw on 8/27/2015.
// */
//public class HeaplessRedisServer extends RedisServer implements Redis {
//
//    public HeaplessRedisServer(Integer port) throws IOException {
//        super(port);
//        List<String> temp = Lists.newArrayList();
//        temp.addAll(this.args);
//        temp.add("--maxheap");
//        temp.add("1G");
//        this.args = temp;
//    }
//}
