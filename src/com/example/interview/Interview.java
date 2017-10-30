package com.example.interview;

import java.util.Scanner;


public class Interview {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input: ");
        String text = scanner.nextLine();
        String longestPalindrome = findPalindrome(text);
        if (longestPalindrome == null) {
            System.out.println("Longest Palindrome: none");
        } else {
            System.out.println("Longest Palindrome: " + longestPalindrome);
        }
    }

    static String findPalindrome(String text) {
        String longestPalindrome = "";
        String latestPalindrome;
        if (text == null || text.length() < 1) {
            return null;
        } else {
            for (int i = 0; i < text.length(); i++) {
                latestPalindrome = findEvenLengthPalindrome(text, i);
                if (latestPalindrome != null) {
                    if (latestPalindrome.length() > longestPalindrome.length()) {
                        longestPalindrome = latestPalindrome;
                    }
                }
                latestPalindrome = findOddLengthPalindrome(text, i);
                if (latestPalindrome != null) {
                    if (latestPalindrome.length() > longestPalindrome.length()) {
                        longestPalindrome = latestPalindrome;
                    }
                }
            }
            if (longestPalindrome.length() > 1) {
                return longestPalindrome;
            } else {
                return null;
            }
        }
    }

    private static String palindromeSpreadingFrom(String text, int leftIndex, int rightIndex) {
        while ((leftIndex >= 0) && (rightIndex < text.length())) {
            if (text.charAt(leftIndex) != text.charAt(rightIndex)) {
                break;
            }
            leftIndex--;
            rightIndex++;
        }
        if (rightIndex - leftIndex <= 2) {
            return null;
        } else {
            return text.substring(leftIndex+1, rightIndex);
        }
    }

    static String findEvenLengthPalindrome(String text, int centerPoint) {
        int leftIndex = centerPoint;
        int rightIndex = centerPoint + 1;
        return palindromeSpreadingFrom(text, leftIndex, rightIndex);
    }

    static String findOddLengthPalindrome(String text, int centerPoint) {
        int leftIndex = centerPoint - 1;
        int rightIndex = centerPoint + 1;
        return palindromeSpreadingFrom(text, leftIndex, rightIndex);
    }

}
