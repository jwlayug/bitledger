package spc;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class InitTests {
    @Test
    public void value() {
        assertThat(new Init().value(), equalTo(42));
    }
}
