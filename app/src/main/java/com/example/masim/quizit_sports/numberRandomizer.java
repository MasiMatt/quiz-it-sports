package com.example.masim.quizit_sports;

import java.util.Random;

public class numberRandomizer {

    public int randoBtn()
    {
        Random rand = new Random();

        int  n = rand.nextInt(4) + 1;
        return n-1;
    }

    public int randoImg()
    {
        Random rand = new Random();
        return rand.nextInt(305) + 1;
    }

    public int randoNames()
    {
        Random rand = new Random();
        return rand.nextInt(305) + 1;
    }
}

