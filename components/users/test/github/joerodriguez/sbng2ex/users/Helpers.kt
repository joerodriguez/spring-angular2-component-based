package github.joerodriguez.sbng2ex.users

import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing

fun <T> stub(methodCall: T): OngoingStubbing<T> {
    return Mockito.`when`(methodCall)
}

fun <T> OngoingStubbing<T>.with(value: T): OngoingStubbing<T> {
    return this.thenReturn(value)
}

fun <T> OngoingStubbing<T>.withException(value: Throwable): OngoingStubbing<T> {
    return this.thenThrow(value)
}
