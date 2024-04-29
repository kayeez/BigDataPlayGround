package com.kai.realtime.etl.dao.hbase;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

@Slf4j
public class HBaseDao {

    protected Connection hbaseConnection;

    protected void open() throws IOException {
        System.setProperty("hadoop.home.dir", "/");
        Configuration hbaseConfig = HBaseConfiguration.create();
        hbaseConfig.set("hbase.zookeeper.quorum", "node1:2181,node2:2181,node3:2181");
        hbaseConfig.set("hbase.client.retries.number", "2");  // default 35
        hbaseConfig.set("hbase.rpc.timeout", "10000");  // default 60 secs
        hbaseConfig.set("hbase.rpc.shortoperation.timeout", "5000"); // default 10 secs
        hbaseConnection = ConnectionFactory.createConnection(hbaseConfig);

    }

    protected void close() {
        if (hbaseConnection != null) {
            try {
                hbaseConnection.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }


    }
}
