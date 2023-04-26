package com.simpleps.infrastructure.quarkustemplate

import com.simpleps.domain.quarkustemplate.Sample
import com.simpleps.domain.quarkustemplate.SampleRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class SampleRepositoryImpl : SampleRepository {
    override fun createSample(sample: Sample): Sample {
        return sample.copy(id = "test")
    }
}
