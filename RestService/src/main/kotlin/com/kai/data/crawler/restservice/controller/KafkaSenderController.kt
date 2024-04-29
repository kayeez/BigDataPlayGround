package com.kai.data.crawler.restservice.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("kafka")
class KafkaSenderController {
    @Autowired
    private val kafkaTemplate: KafkaTemplate<Any, Any>? = null

    @PostMapping("send/{topic}")
    fun sendToKafka(@RequestBody data: Map<String, Any>, @PathVariable topic: String): Map<String, Any> {
        print("send to kafka ${topic} ${data}")
        kafkaTemplate!!.send(topic, data)
        return mapOf("success" to true)
    }

    @GetMapping("debug")
    fun debugMsg(): Map<String, Any> {
        return mapOf("success" to true)
    }
}