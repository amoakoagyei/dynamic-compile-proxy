package io.richard.event.processor.app

import io.richard.event.processor.EventProcessorNotFoundException
import io.richard.event.processor.EventRecord
import io.richard.event.processor.ProxyProcessorChain

class ProcessorChainWiredSuccessfully extends AbstractApplicationContextSpec {

    ProxyProcessorChain proxyProcessorChain;

    void setup() {
        proxyProcessorChain = applicationContext.getBean(ProxyProcessorChain)
    }

    void "Process a bean with a processor wired"() {
        expect:
        proxyProcessorChain != null

        when:
        proxyProcessorChain.process(new EventRecord<ProductCreatedEvent>(
                new ProductCreatedEvent(UUID.randomUUID(), "Product 1"),
                UUID.randomUUID(),
                "partition-1"
        ))

        then:
        noExceptionThrown()
    }


    void "event with no processor will throw an exception"() {

        when:
        proxyProcessorChain.process(new EventRecord<TestEventWithNoProcessor>(
                new TestEventWithNoProcessor(),
                UUID.randomUUID(),
                "fake-partitionKey"
        ))

        then:
        thrown(EventProcessorNotFoundException)
    }

}