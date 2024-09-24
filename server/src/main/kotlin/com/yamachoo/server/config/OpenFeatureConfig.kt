package com.yamachoo.server.config

import dev.openfeature.contrib.providers.flagd.FlagdProvider
import dev.openfeature.sdk.OpenFeatureAPI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenFeatureConfig {
    @Bean
    fun openFeatureAPI(): OpenFeatureAPI {
        return OpenFeatureAPI.getInstance().apply {
            setProviderAndWait(FlagdProvider())
        }
    }
}
