package com.function;

public class TriSynth {
    public float A;
    public float shift;

    TriSynth()  
    {
        A = 0;
        shift = 0;
    }
    TriSynth(float strength,float shiftInRad)
    {
        A = strength;
        shift = shiftInRad;
    }

    public static TriSynth synth(float strengthA,float shiftA,float strengthB,float shiftB)
    {
        float k1 = (float)(strengthA * Math.cos(shiftA) + strengthB * Math.cos(shiftB));
        float k2 = (float)(strengthA * Math.sin(shiftA) + strengthB * Math.sin(shiftB));
        float k = (float)Math.sqrt(k1*k1 + k2*k2);
        float shifter = (float)Math.atan(k2/k1);
        TriSynth ret = new TriSynth(k,shifter);
        return ret;
    }
}
