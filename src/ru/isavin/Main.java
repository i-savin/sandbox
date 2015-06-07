package ru.isavin;

import ru.isavin.schennons.Schennons;

import java.io.BufferedReader;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Schennons schennons = new Schennons();

        int i = 0;
        int correct = 0;
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.print("Загадайте 0 или 1: ");
            int humanAnswer = in.nextInt();
            int answer = 2;
            if (i < 3) {
                schennons.addMyAnswer(answer);
            } else {
                answer = schennons.guessAnswer();
            }
            if (humanAnswer == answer) {
                correct++;
            }
            System.out.println("Мой ответ: " + answer + ", процент правильных: " + ((float) correct / (float) i));
//            System.out.println("А что загадали вы?");
            schennons.addHumanAnswer(humanAnswer);
            schennons.updateData();
            i++;
        }
    }
}
