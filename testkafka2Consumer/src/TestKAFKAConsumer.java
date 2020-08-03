package cn.com.dwsoft.main;


import java.io.IOException;


//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.Producer;
//import org.apache.kafka.clients.producer.ProducerRecord;


import kafka.consumer.ConsumerConfig;  
import kafka.consumer.ConsumerIterator;  
import kafka.javaapi.consumer.ConsumerConnector;  

import kafka.consumer.KafkaStream;

import kafka.serializer.StringDecoder;  

import kafka.utils.VerifiableProperties;


import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  
import java.util.Properties; 

//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.Date;
//import java.util.Properties;


public class TestKAFKAConsumer {

    private final ConsumerConnector consumer; 

    private TestKAFKAConsumer() {  
        Properties props = new Properties();  
 
        props.put("zookeeper.connect", "192.168.10.190:2181");  
    
        props.put("zookeeper.session.timeout.ms", "4000");  
        props.put("zookeeper.sync.time.ms", "200");  
        props.put("auto.commit.interval.ms", "1000");  
        props.put("auto.offset.reset", "smallest");  
 
        props.put("group.id", "58332"); 

	      props.put("serializer.class", "kafka.serializer.StringEncoder");  
  
        ConsumerConfig config = new ConsumerConfig(props);  
  
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);  
    }  

    void consume() {  
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();  
        topicCountMap.put("testtopic2", new Integer(1));  
  
        StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());  
        StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());  
  
        Map<String, List<KafkaStream<String, String>>> consumerMap =  
                consumer.createMessageStreams(topicCountMap,keyDecoder,valueDecoder);  
        
        KafkaStream<String, String> stream = consumerMap.get("testtopic2").get(0);  
        
        ConsumerIterator<String, String> it = stream.iterator();  
        while (it.hasNext())  
        {  
            System.out.println(it.next().message());  
        }  
        System.out.println("finish");  
  
  
    } 

       public static void main(String[] args) {  
        new TestKAFKAConsumer().consume();  
    }




}



		
