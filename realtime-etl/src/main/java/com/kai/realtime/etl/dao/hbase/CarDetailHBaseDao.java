package com.kai.realtime.etl.dao.hbase;

import com.kai.realtime.etl.domain.CarDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

@Slf4j
public class CarDetailHBaseDao extends HBaseDao {
    private static final TableName TABLE_NAME = TableName.valueOf("car_detail");
    private Table hbaseTable;

    public CarDetailHBaseDao() {
        try {
            open();
            Admin admin = hbaseConnection.getAdmin();
            if (!admin.tableExists(TABLE_NAME)) {
                admin.createTable(TableDescriptorBuilder.newBuilder(TABLE_NAME)
                        .setColumnFamily(ColumnFamilyDescriptorBuilder.of("cf"))
                        .build());

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    public void put(CarDetail carDetail) {
        try {
            open();
            hbaseTable = hbaseConnection.getTable(TABLE_NAME);
            hbaseTable.put(carDetail.toPut());

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            close();
            if (hbaseTable != null) {
                try {
                    hbaseTable.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }

    }

}
