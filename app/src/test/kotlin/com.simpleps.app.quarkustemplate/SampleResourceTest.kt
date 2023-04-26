package com.simpleps.app.quarkustemplate

import io.quarkus.test.common.http.TestHTTPEndpoint
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Test
import javax.ws.rs.core.MediaType

@QuarkusTest
@TestHTTPEndpoint(SampleResource::class)
class SampleResourceTest {

    @Test
    fun shouldReturn200() {
        given().header("Content-Type", MediaType.APPLICATION_JSON)
            .post().then().statusCode(200)
    }
}
