
package com.kotlin.demo.kotlinspringbootconsul.controller

import com.kotlin.demo.kotlinspringbootconsul.config.ConfigurationProperties
import com.kotlin.demo.kotlinspringbootconsul.domain.Greeting
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import javax.naming.ServiceUnavailableException


@RestController
//@EnableDiscoveryClient
class GreetingController {


    @Value("\${app.myValue}")
    val value:Int = 0

    @Autowired
    lateinit var configurationProperties: ConfigurationProperties

    @Autowired
    lateinit var discoveryClient: DiscoveryClient

    var restTemplate: RestTemplate = RestTemplate()

    val counter = AtomicLong()

    //discoveryClient must get the instance of service ID which is application id, refer to application.yml
    fun serviceUrl(): Optional<URI> {
        return discoveryClient.getInstances("ThirdParty")
                .stream()
                .map({ si-> si.getUri() })
                .findFirst()
    }

    @GetMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String) =
            Greeting(counter.incrementAndGet(), "Hello, $name")

    @GetMapping("/discoveryClient")
    @Throws(RestClientException::class, ServiceUnavailableException::class)
    fun discoveryPing(): String? {
        val service = serviceUrl()
                .map { s -> s.resolve("/ping") }
                .orElseThrow(({ ServiceUnavailableException() }))
        return restTemplate?.getForEntity(service, String::class.java)
                .getBody()
    }

    @GetMapping("/ping")
    fun ping(): String {
        return "pong"
    }

    @GetMapping("/getConfigFromValue")
    fun getConfigFromValue():Int {
        return value
    }
    @GetMapping("/getConfigFromProperty")
    fun getConfigFromProperty():Int? {
        return configurationProperties.myValue
    }

    @RequestMapping("/tpa/v1")
    fun replyBack(): String {
        return "Value = " + configurationProperties.myValue + "<br/>Third Party Card -- You are receiving this response from node 2"
    }

    @RequestMapping("/test")
    fun replySlow(): String {
    println("--- in replyslow ---")
//        try {
////            Thread.sleep(2000)
//        } catch (e: InterruptedException) {
//            // TODO Auto-generated catch block
//            e.printStackTrace()
//        }

        return "Empty response"
    }

}