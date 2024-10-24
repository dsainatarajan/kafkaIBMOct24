package com.kafka.tutorial;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import java.util.Arrays;
import java.util.Properties;

public class StreamsTransformApplication {

   public static void main(final String[] args) throws Exception {
	   Properties props = new Properties();
       props.put(StreamsConfig.APPLICATION_ID_CONFIG, "transform-application");
       props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9092,kafka2:9092");
       props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
       props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

       StreamsBuilder builder = new StreamsBuilder();
       KStream<String, String> streamInput = builder.stream("streaminput2");
       streamInput.filter((k,v) -> Double.parseDouble(v.toString().trim()) >=200.0).
       map((k,v) -> KeyValue.pair(k,String.valueOf(Double.parseDouble(v.toString().trim())
    		   +Double.parseDouble(v.toString().trim())*0.1)))      
       .to("transformoutput1", Produced.with(Serdes.String(), Serdes.String()));
       KafkaStreams streams = new KafkaStreams(builder.build(), props);
       streams.start();
   }

}