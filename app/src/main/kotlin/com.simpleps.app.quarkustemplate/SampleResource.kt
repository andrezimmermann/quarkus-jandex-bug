package com.simpleps.app.quarkustemplate

import com.simpleps.domain.quarkustemplate.Sample
import com.simpleps.domain.quarkustemplate.SampleRepository
import javax.transaction.Transactional
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/sample")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class SampleResource(
    val sampleRepository: SampleRepository
) {
    @POST
    @Transactional
    fun createSample(): Sample {
        return sampleRepository.createSample(Sample())
    }
}
