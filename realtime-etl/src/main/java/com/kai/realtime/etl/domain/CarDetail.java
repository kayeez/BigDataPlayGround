package com.kai.realtime.etl.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class CarDetail {
    private String brand;
    private String tag;
    private String name;
    private String id;
    private String score;

    public Put toPut() {
        Put put = new Put(Bytes.toBytes(id + "__" + name));
        if (brand != null) {
            put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("brand"), Bytes.toBytes(brand));
        }
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("tag"), Bytes.toBytes(tag));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("name"), Bytes.toBytes(name));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("id"), Bytes.toBytes(id));
        put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("score"), Bytes.toBytes(score));
        return put;
    }

    public static CarDetail fromJson(String json) {
        try {
            log.info(json);
            return new ObjectMapper().readerFor(CarDetail.class).readValue(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
