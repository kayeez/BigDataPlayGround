package com.kai.realtime.etl;

import com.kai.realtime.etl.dao.hbase.CarDetailHBaseDao;
import com.kai.realtime.etl.domain.CarDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.util.logging.LogManager;

@Slf4j
public class DimTableEtlJob {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.enableCheckpointing(60000L, CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setCheckpointTimeout(120000L);
        env.getCheckpointConfig().setExternalizedCheckpointCleanup(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(30000L);
        env.setRestartStrategy(RestartStrategies.failureRateRestart(3, Time.days(1), Time.seconds(3)));
        env.setStateBackend(new HashMapStateBackend());
        env.getCheckpointConfig().setCheckpointStorage("file:///opt/data/flink-state-backend2");
        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers("node1:9092,node2:9092,node3:9092")
                .setTopics("car_detail")
                .setGroupId("flink_app")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema()).build();
        DataStream<String> kafkaStream = env.fromSource(source, WatermarkStrategy.noWatermarks(), "car_detail_source");
        DataStream<CarDetail> transformedStream = kafkaStream.map((MapFunction<String, CarDetail>) CarDetail::fromJson);
        transformedStream.addSink(new HBaseSink());
        env.execute("Flink Kafka HBase Example");
    }


    public static class HBaseSink implements SinkFunction<CarDetail> {
        @Override
        public void invoke(CarDetail value, Context context) throws Exception {
            new CarDetailHBaseDao().put(value);
            log.info(value.toString());
            System.out.println("--"+value);
        }
    }
}
