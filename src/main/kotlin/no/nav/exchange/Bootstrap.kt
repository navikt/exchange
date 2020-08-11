package no.nav.exchange

import com.github.kittinunf.fuel.Fuel
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.routing.Routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
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

    kotlinx.coroutines.runBlocking {
        val result =
            Fuel.get("https://oidc-ver2.difi.no/idporten-oidc-provider/.well-known/openid-configuration")
                .response()
        log.info { "Request: ${result.first}" }
        log.info { "Response: ${result.second}" }
        log.info { result.third.component2() }
    }
}
