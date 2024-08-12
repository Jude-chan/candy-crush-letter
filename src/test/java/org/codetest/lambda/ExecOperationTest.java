package org.codetest.lambda;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
@SuppressWarnings("unchecked")
public class ExecOperationTest {

    private ExecOperation operation;
    @Before
    public void setUp() {
        operation = new ExecOperation();
    }

    @Test
    public void getResult() {
        Optional<Object> optional = operation.getResult("Stage1", "aabcccbbaddd", 3);
        List<String> stringList = (List<String>) optional.get();
        stringList.forEach(xx -> System.out.println("-> " + xx));
        Assert.assertEquals("", stringList.get(stringList.size()-1));
    }

    @Test
    public void getResult1() {
        Optional<Object> optional = operation.getResult("Stage2", "aabcccbbaddd", 3);
        List<String> stringList = (List<String>) optional.get();
        stringList.forEach(xx -> System.out.println("-> " + xx));
        Assert.assertEquals("bac", stringList.get(stringList.size()-1));
    }

    @Test
    public void getResult2() {
        Optional<Object> optional = operation.getResult("Stage3", "aabcccbbaddd", 3);
        List<String> stringList = (List<String>) optional.get();
        stringList.forEach(xx -> System.out.println("-> " + xx));
        Assert.assertEquals("no such stage", stringList.get(stringList.size()-1));
    }
}