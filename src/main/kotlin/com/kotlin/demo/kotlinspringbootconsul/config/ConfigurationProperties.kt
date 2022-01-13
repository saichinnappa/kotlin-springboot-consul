package com.kotlin.demo.kotlinspringbootconsul.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.context.annotation.Configuration


@RefreshScope
@Configuration
@ConfigurationProperties("app")
class ConfigurationProperties {
    var myValue: Int = 12


}