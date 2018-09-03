/*package com.bolt.proxy;

import java.util.Properties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import redis.clients.jedis.Jedis;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;


@RestController
@RequestMapping("/proxy")
public class Proxy {

	
	@RequestMapping(value="/receive",method=RequestMethod.POST)
	public void receiveRecords(@RequestBody String jsonStr) throws Exception {
		System.out.println("@@ Received map "+jsonStr);
	
	}
	
}
*/