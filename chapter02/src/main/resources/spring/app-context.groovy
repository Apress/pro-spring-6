package spring

import com.apress.prospring6.two.decoupled.HelloWorldMessageProvider
import com.apress.prospring6.two.decoupled.StandardOutMessageRenderer

beans {
    provider(HelloWorldMessageProvider) {}
    renderer(StandardOutMessageRenderer) {
        messageProvider = ref('provider')
    }
}