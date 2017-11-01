package com.example.interview;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;


public class InterviewTest {

    private void check_input_output_main(String input, String output) throws Exception {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        String expected_output = "Input: " +
                "Longest Palindrome: " + output + System.lineSeparator();
        ByteArrayInputStream inContent = new ByteArrayInputStream((input + System.lineSeparator()).getBytes());
        System.setIn(inContent);
        Interview.main(null);
        assertEquals(expected_output, outContent.toString());
        assertEquals("", errContent.toString());
        System.setOut(null);
        System.setErr(null);
        System.setIn(null);
    }

    @Test
    public void main() throws Exception {
        check_input_output_main("racecars", "racecar");
        check_input_output_main("racecarssss", "racecar");
        check_input_output_main("test", "none");
        check_input_output_main("", "none");
    }

    @Test
    public void findPalindrome() throws Exception {
        assertEquals(null, Interview.findPalindrome(null));
        assertEquals(null, Interview.findPalindrome("a"));
        assertEquals(null, Interview.findPalindrome("ab"));
        assertEquals(null, Interview.findPalindrome("abc"));
        assertEquals(null, Interview.findPalindrome("abcd"));
        assertEquals("aa", Interview.findPalindrome("aa"));
        assertEquals("aba", Interview.findPalindrome("aba"));
        assertEquals("abba", Interview.findPalindrome("abba"));
        assertEquals("abcba", Interview.findPalindrome("abcba"));
        assertEquals("aa", Interview.findPalindrome("zxaa"));
        assertEquals("aba", Interview.findPalindrome("zaba"));
        assertEquals("abba", Interview.findPalindrome("zzabbax"));
        assertEquals("abcba", Interview.findPalindrome("zabcbaxx"));
        assertEquals("abcba", Interview.findPalindrome("abcbaxx"));
    }

    @Test
    public void findEvenLengthPalindrome() throws Exception {
        assertEquals(null,Interview.findEvenLengthPalindrome("ab", 0));
        assertEquals("aa",Interview.findEvenLengthPalindrome("aa", 0));
        assertEquals("abccba",Interview.findEvenLengthPalindrome("abccba", 2));
        assertEquals("abccba",Interview.findEvenLengthPalindrome("zzabccba", 4));
        assertEquals(null,Interview.findEvenLengthPalindrome("zzabccba", 5));
        assertEquals("abccba",Interview.findEvenLengthPalindrome("zzabccbaxx", 4));
        assertEquals(null,Interview.findEvenLengthPalindrome("zzabc cbaxx", 5));
    }

    @Test
    public void findOddLengthPalindrome() throws Exception {
        assertEquals(null,Interview.findOddLengthPalindrome("ab", 0));
        assertEquals("aba",Interview.findOddLengthPalindrome("aba", 1));
        assertEquals("abcba",Interview.findOddLengthPalindrome("abcba", 2));
        assertEquals("abcba",Interview.findOddLengthPalindrome("zzabcba", 4));
        assertEquals("abcba",Interview.findOddLengthPalindrome("zzabcbaxx", 4));
        assertEquals(null,Interview.findOddLengthPalindrome("zzabccbaxx", 5));
    }

}
