package com.scalable.capital.demo;


import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;

public class InverterTest {

    @Test
    public void whenStringEmpty_returnEmpty() {
        String inverted = Inverter.invert("");
        Assert.assertEquals("", inverted);
    }

    @Test
    public void whenStringNull_returnEmpty() {
        String inverted = Inverter.invert(null);
        Assert.assertEquals("", inverted);
    }

    @Test
    public void whenStringHasOnlyOneCharacter_returnOneCharacter() {
        String inverted = Inverter.invert("a");
        Assert.assertEquals("a", inverted);
    }

    @Test
    public void whenStringHasMoreThanOneCharacter_returnInvertedString() {
        String inverted = Inverter.invert("abcd");
        Assert.assertEquals("dcba", inverted);
    }


}
