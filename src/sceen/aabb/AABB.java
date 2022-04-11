package sceen.aabb;

import com.vector.Ray;
import com.vector.Vec3;

public class AABB {
    public Vec3 minimum;
    public Vec3 maximum;

    public AABB(Vec3 max,Vec3 min)
    {
        maximum = new Vec3(max);
        minimum = new Vec3(min);
    }

    public boolean hit(Ray r)
    {
        float t_min,t_max;
        float invD = 1.0f / r.dst().v[0];
        float t0 = (minimum.v[0] - r.ori().v[0]) * invD;
        float t1 = (maximum.v[0] - r.ori().v[0]) * invD;
        if (invD < 0.0f)
        {
            t_min = t1;
            t_max = t0;
        }
        else
        {
            t_min = t0;
            t_max = t1;
        }
        for (int a = 1; a < 3; a++) {
            invD = 1.0f / r.dst().v[a];
            t0 = (minimum.v[a] - r.ori().v[a]) * invD;
            t1 = (maximum.v[a] - r.ori().v[a]) * invD;
            if (invD < 0.0f)
            {
                float temp = t0;
                t0 = t1;
                t1 = temp;
            }
            t_min = Math.max(t0, t_min);
            t_max = Math.min(t1, t_max);
            if (t_max <= t_min)
                return false;
        }
        return true;
    }

    static public AABB merge(AABB a,AABB b)
    {

        //System.out.printf("Before");
        //a.maximum.print();
        //a.minimum.print();
        //b.maximum.print();
        //b.minimum.print();

        AABB ret = new AABB(
                Vec3.getMax(a.maximum,b.maximum),
                Vec3.getMin(a.minimum,b.minimum)
        );

        //System.out.printf("After");
        //ret.maximum.print();
        //ret.minimum.print();

        return ret;
    }
}
