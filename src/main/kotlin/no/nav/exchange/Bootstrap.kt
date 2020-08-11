package no.nav.exchange

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.github.kittinunf.fuel.Fuel
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import io.ktor.routing.Routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging

private val log = KotlinLogging.logger { }

fun createHttpServer(applicationStatus: ApplicationStatus): NettyApplicationEngine {
    return embeddedServer(Netty, port = 8080, module = {
        setupHttpServer(
            applicationStatus = applicationStatus
        )
    })
}

fun Application.setupHttpServer(applicationStatus: ApplicationStatus) {

    log.info { "Installing routes" }
    install(Routing) {
        selfTest(
            readySelfTestCheck = { applicationStatus.initialized },
            aLiveSelfTestCheck = { applicationStatus.running })
    }
    applicationStatus.initialized = true
    log.info { "Application is up and running" }

    val wellKnownUrl = "https://oidc-ver2.difi.no/idporten-oidc-provider/.well-known/openid-configuration"

    runBlocking {
        log.info("getting OAuth2 server metadata from well-known url=$wellKnownUrl")
        val result = defaultHttpClient.get<String>(wellKnownUrl)
        log.info { result }
    }

    runBlocking {
        val result =
            Fuel.get(wellKnownUrl)
                .response()
        log.info { "Request: ${result.first}" }
        log.info { "Response: ${result.second}" }
        log.info { result.third.component2()?.message ?: "Error empty" }
    }
}

internal val defaultHttpClient = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = JacksonSerializer {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }
    }
}
