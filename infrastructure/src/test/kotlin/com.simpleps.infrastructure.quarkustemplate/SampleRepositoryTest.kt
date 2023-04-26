package com.simpleps.infrastructure.quarkustemplate

import com.simpleps.domain.quarkustemplate.Sample
import com.simpleps.domain.quarkustemplate.SampleRepository
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import javax.inject.Inject

@QuarkusTest
class SampleRepositoryTest {

    @Inject
    lateinit var sampleRepository: SampleRepository

    @Test
    fun test() {
        val sample = sampleRepository.createSample(Sample())
        assertNotNull(sample.id)
    }
}
