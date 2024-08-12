package org.codetest.strategy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
@SuppressWarnings("unchecked")
public class SolutionFactoryTest {

    @Before
    public void setUp() {
    }

    @Test
    public void solution() {
        Optional<Object> optional = SolutionFactory.solution("abdc", "Stage1", 3);
        List<String> stringList = (List<String>) optional.get();
        stringList.forEach(xx -> System.out.println("-> " + xx));
        Assert.assertEquals("abdc", stringList.get(stringList.size()-1));
    }

    @Test
    public void solution1() {
        Optional<Object> optional = SolutionFactory.solution("aabcccbbaddd", "Stage1", 3);
        List<String> stringList = (List<String>) optional.get();
        stringList.forEach(xx -> System.out.println("-> " + xx));
        Assert.assertEquals("", stringList.get(stringList.size()-1));
    }

    @Test
    public void solution3() {
        Optional<Object> optional = SolutionFactory.solution("abdc", "Stage2", 3);
        List<String> stringList = (List<String>) optional.get();
        stringList.forEach(xx -> System.out.println("-> " + xx));
        Assert.assertEquals("abdc", stringList.get(stringList.size()-1));
    }

    @Test
    public void solution4() {
        Optional<Object> optional = SolutionFactory.solution("aabcccbbaddd", "Stage2", 3);
        List<String> stringList = (List<String>) optional.get();
        stringList.forEach(xx -> System.out.println("-> " + xx));
        Assert.assertEquals("bac", stringList.get(stringList.size()-1));
    }

    @Test
    public void solution5() {
        Optional<Object> optional = SolutionFactory.solution("ad", "Stage2", 3);
        List<String> stringList = (List<String>) optional.get();
        stringList.forEach(xx -> System.out.println("-> " + xx));
        Assert.assertEquals("ad", stringList.get(stringList.size()-1));
    }

    @Test
    public void solution6() {
        Optional<Object> optional = SolutionFactory.solution("aabcccbbaddd", "Stage3", 3);
        List<String> stringList = (List<String>) optional.get();
        stringList.forEach(xx -> System.out.println("-> " + xx));
        Assert.assertEquals("no such stage", stringList.get(stringList.size()-1));
    }

}