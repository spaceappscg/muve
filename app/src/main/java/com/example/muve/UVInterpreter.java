package com.example.muve;

import java.time.Instant;

public class UVInterpreter
{
    public static void notify(MainActivity mainActivity)
    {
            long mins = (MainActivity.lastUVMaxTime.toUnixTimestamp()-(System.currentTimeMillis()/1000L))/60;
            if(mins > 0 && mins < 60) {
                mainActivity.sendNotification("M!UVE - Cuidado", "Ápice UV de "+ MainActivity.lastUVMax + " ("+UVInterpreter.getUVLevel(MainActivity.lastUVMax)+") em "+mins+" minutos.");
            }
            else if(MainActivity.degreeVar > 0.001)
            {
                mainActivity.sendNotification("M!UVE - Informação", "O UV atual da sua região é de " + MainActivity.lastUV + " ("+UVInterpreter.getUVLevel(MainActivity.lastUV)+").");
                if(MainActivity.lastUV >= 6) {
                    mainActivity.sendNotification("M!UVE - Dica", "Se for ficar muito tempo na rua não esqueça o protetor solar.");
                }
                else
                {
                    mainActivity.sendNotification("M!UVE - Dica", "Aproveite o baixo Índice UV para fazer uma caminhada.");
                }
            }

            MainActivity.degreeVar = 0.0;
    }

    public static String getUVLevel(double UV)
    {
        if(UV < 3)
        {
            return "Baixo";
        }
        else if(UV < 6)
        {
            return "Moderado";
        }
        else if(UV < 8)
        {
            return "Alto";
        }
        else if(UV < 11)
        {
            return "Muito Alto";
        }
        else
        {
            return "Extremo";
        }
    }
}
