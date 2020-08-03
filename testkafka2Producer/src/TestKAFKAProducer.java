package cn.com.dwsoft.main;


import java.io.IOException;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;


public class TestKAFKAProducer {

    private static final String TOPIC = "testtopic2"; 
    private static final String BROKER_LIST = "192.168.10.190:9192";
    private static final String SERIALIZER_CLASS = "kafka.serializer.StringEncoder"; 
    private static final String ZK_CONNECT = "192.168.10.190:2181";

    private static int Count = 10000;
    private static int number = 10;
    private static Properties props;

    private static Producer<String, String> producer;


    public static void main(String[] args)throws IOException {
              
        props = new Properties();

        props.put("zk.connect", ZK_CONNECT);
        props.put("serializer.class", SERIALIZER_CLASS);
        props.put("bootstrap.servers", BROKER_LIST);

        props.put("request.required.acks", "1");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
       
        producer = new KafkaProducer<String, String>(props);

           ProducerRecord<String, String> data = new ProducerRecord<String, String>(TOPIC, "FromTestKAFKA.JAVA");
      
        System.out.println("start sending ..."); 
          producer.send(data);

        System.out.println("send completed");

	producer.close();
	
/*
		String line;

                boolean firstRunFlag = true;
			try {
			
                        }
			catch(Exception e) {
				e.printStackTrace();
			}
*/

	}
/*
        private static void printWelcome() {
 
		System.out.println("------------------------------------------------");
		System.out.println("|--------   dwsoft cmd cli demo        --------|");
		System.out.println("|--------   common version          -----------|");
		System.out.println("|--------   ver 1.0                 -----------|");
		System.out.println("|--------   jdk 1.6/1.7/1.8             -------|");
		System.out.println("------------------------------------------------");

        }


	private static void printHelp() {
		System.out.println("Usage: [cmd] param1 [value]");
		System.out.println("cmd: get/set/exit/help");
		System.out.println("Examples:");
		System.out.println("\tset param1 value");
		System.out.println("\tget param1 key");
		System.out.println("\thelp");
		System.out.println("\texit");
	}
*/
}



		
