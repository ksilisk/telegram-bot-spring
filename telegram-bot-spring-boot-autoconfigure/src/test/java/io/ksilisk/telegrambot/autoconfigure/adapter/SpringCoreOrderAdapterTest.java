package io.ksilisk.telegrambot.autoconfigure.adapter;

import io.ksilisk.telegrambot.core.order.CoreOrdered;
import org.junit.jupiter.api.Test;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import static org.junit.jupiter.api.Assertions.*;

class SpringCoreOrderAdapterTest {
    interface TestComponent extends CoreOrdered {
        String id();

        default String name() {
            return getClass().getSimpleName();
        }
    }

    static class NoSpringMeta implements TestComponent {
        @Override
        public String id() {
            return "no-spring";
        }
    }

    @Order(10)
    static class WithOrderAnnotation implements TestComponent {
        @Override
        public String id() {
            return "with-order-annotation";
        }
    }

    static class WithOrderedInterface implements TestComponent, Ordered {

        @Override
        public int getOrder() {
            return 42;
        }

        @Override
        public String id() {
            return "with-ordered-interface";
        }
    }

    @Order(99)
    static class WithOrderAndOverride implements TestComponent {

        @Override
        public int getOrder() {
            return -5;
        }

        @Override
        public String id() {
            return "with-order-and-override";
        }

        @Override
        public String name() {
            return "test-name";
        }
    }

    @Test
    void adaptIfNecessary_shouldReturnSameInstance_whenNoSpringMetadata() {
        TestComponent original = new NoSpringMeta();

        TestComponent adapted = SpringCoreOrderAdapter.adaptIfNecessary(original);

        assertSame(original, adapted);
        assertEquals(original.name(), adapted.name());
        assertEquals(0, adapted.getOrder());
        assertEquals("no-spring", adapted.id());
    }

    @Test
    void adaptIfNecessary_shouldUseOrderAnnotation_whenPresentAndNoOverride() {
        TestComponent original = new WithOrderAnnotation();

        TestComponent adapted = SpringCoreOrderAdapter.adaptIfNecessary(original);

        assertNotSame(original, adapted);
        assertEquals(original.name(), adapted.name());
        assertEquals(10, adapted.getOrder());
        assertEquals("with-order-annotation", adapted.id());
    }

    @Test
    void adaptIfNecessary_shouldUseOrderedInterface_whenPresentAndNoOverride() {
        TestComponent original = new WithOrderedInterface();

        TestComponent adapted = SpringCoreOrderAdapter.adaptIfNecessary(original);

        assertEquals(original.name(), adapted.name());
        assertEquals(42, adapted.getOrder());
        assertEquals("with-ordered-interface", adapted.id());
    }

    @Test
    void adaptIfNecessary_shouldNotOverrideExplicitOrder_evenWithSpringMetadata() {
        TestComponent original = new WithOrderAndOverride();

        TestComponent adapted = SpringCoreOrderAdapter.adaptIfNecessary(original);

        assertEquals(original.name(), adapted.name());
        assertEquals(-5, adapted.getOrder());
        assertEquals("with-order-and-override", adapted.id());
    }
}
