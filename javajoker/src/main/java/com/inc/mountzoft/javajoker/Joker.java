package com.inc.mountzoft.javajoker;

import java.util.Random;

public class Joker {

        public String tellJoke() {
            final int min = 0;
            final int max = 10;
            final int random = new Random().nextInt((max - min) + 1) + min;

            switch (random){
                case 1: return "Unix is user friendly. It's just very particular about who its friends are.";
                case 2: return "My software never has bugs. It just develops random features.";
                case 3: return "Programmers are tools for converting caffeine into code.";
                case 4: return "My attitude isn't bad. It's in beta.";
                case 5: return "Definition of polymorphism :\n" +
                        "Boys use the word \"friendship\" to start relationship.. Girls use the same word \"friendship\" to end relationship..  word are same, but different behavior.. That's called polymorphism .....";
                case 6: return "Software developers like to solve problems. If there are no problems available, they will create their own problems. It's an addiction.";
                case 7: return "Definition, Algorithm: Word used by programmers when they do not want to explain what they did.";
                case 8: return "Hide and seek champion... ; since 1958";
                case 9: return "Why do Java programmers wear glasses?\n" +
                        "Because they don't C#!";
                case 10: return "HTML is a programming language!";

                default: return "\"Debugging\" is like being the detective in a crime drama where you are also the murderer.";
            }
        }
}
