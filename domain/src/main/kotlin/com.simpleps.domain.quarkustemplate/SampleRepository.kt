package com.simpleps.domain.quarkustemplate

interface SampleRepository {
    fun createSample(sample: Sample): Sample
}
