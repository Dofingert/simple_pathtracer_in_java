package com.function;

public class Fresnel {
    public TriSynth P;
    public TriSynth S;
    public static Fresnel compute(float n1,float n2,float cos1,float cos2)
    {
        double k1 = (double)n1 / (double)n2;
        Fresnel ret = new Fresnel();
        ret.S = new TriSynth((float)(Math.pow((k1 * cos1 - cos2) / (k1 * cos1 + cos2),2)),(k1 < 1)?(float)Math.PI : 0);
        ret.P = new TriSynth((float)(Math.pow((k1 * cos2 - cos1) / (k1 * cos2 + cos1),2)),(k1 < 1)?(float)Math.PI : 0);
        return ret;
    }
    public static double simpleCompute(float cosine, float ref_idx)
    {
        // Use Schlick's approximation for reflectance.
        double r0 = (1-ref_idx) / (1+ref_idx);
        r0 = r0*r0;
        return r0 + (1-r0)*Math.pow((1 - cosine),5);
    }
}
