package io.richard.event.processor.app

import io.micronaut.context.ApplicationContext
import spock.lang.AutoCleanup
import spock.lang.Specification


class AbstractApplicationContextSpec extends Specification implements ContextFixture {

    @AutoCleanup
    ApplicationContext applicationContext = ApplicationContext.run(configuration)

    @Override
    Map<String, Object> getConfiguration() {
        return [:]
    }

}