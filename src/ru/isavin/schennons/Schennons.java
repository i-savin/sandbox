package ru.isavin.schennons;

/**
 * Устроена программа так. В ней имеется 5-индексный массив A[0:1, 0:2, 1:1, 0:2, 0:1]  из 72 элементов.
 * Вначале массив очищен нулями, и машина первые три раза печатает двойки.
 * В дальнейшем машина помнит несколько последних ходов своих и человека.
 *
 * Если человек последними написал числа a1, a2, a3  и машина на это отвечала b1, b2, b3,
 * то в ячейку A[a1, b1, a2, b2, a3] добавляется единица, то есть машина запоминает,
 * что после комбинации a1, b1, a2, b2 человек выбрал число a3.
 *
 * Чтобы предсказать, что теперь напишет человек, машина сравнивает числа A[a2, b2, a3, b3, 0] и A[a2, b2, a3, b3, 1].
 * Если первое сильно превосходит второе, то предсказывается число 0, если наоборот, то число 1,
 * а если они отличаются мало, то печатает число 2, то есть отказывается угадывать.
 *
 * Можно усовершенствовать программу, добавляя на ходе i в нужную ячейку не единицу, а число (1.1)i,
 * и тем самым уменьшая вес старых событий, которые человек успевает забыть.
 * @author ilasavin
 * @since 01.06.15
 */
public class Schennons {
    private int[][][][][] pythia = new int[2][3][2][3][2];
    private int[] humanAnswers = new int[3];
    private int[] myAnswers = new int[3];
    private int numberOfQuestions = 0;

    public void addHumanAnswer(int answer) {
//        for (int i = humanAnswers.length - 1; i > 0; i--) {
//            humanAnswers[i] = humanAnswers[i-1];
//        }
//        humanAnswers[0] = answer;
        for (int i = 0; i < humanAnswers.length - 1; i++) {
            humanAnswers[i] = humanAnswers[i+1];
        }
        humanAnswers[humanAnswers.length-1] = answer;
    }

    public void addMyAnswer(int answer) {
//        for (int i = myAnswers.length - 1; i > 0; i--) {
//            myAnswers[i] = myAnswers[i-1];
//        }
//        myAnswers[0] = answer;
        for (int i = 0; i < myAnswers.length - 1; i++) {
            myAnswers[i] = myAnswers[i+1];
        }
        myAnswers[myAnswers.length-1] = answer;
    }

    public void updateData() {
        pythia[humanAnswers[0]][myAnswers[0]][humanAnswers[1]][myAnswers[1]][humanAnswers[2]]++;
        numberOfQuestions++;
    }

    public int guessAnswer() {
        int diff = pythia[humanAnswers[1]][myAnswers[1]][humanAnswers[2]][myAnswers[2]][0]
                - pythia[humanAnswers[1]][myAnswers[1]][humanAnswers[2]][myAnswers[2]][1];

//        System.out.println("diff: " + diff);

        int answer = 2;

        if (diff < 0) {
            answer = 1;
        } else if (diff > 0) {
            answer = 0;
        } else {
            answer = 2;
        }
        addMyAnswer(answer);
        return answer;
    }
}
