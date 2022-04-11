package sceen;
import com.vector.*;
import sceen.hitable.HitReturn;
import sceen.hitable.Hitable;
import shader.surface.*;

import java.util.ArrayList;

public class Sceen {
    static final float minimalDistance = (float) 0.000;

    public ArrayList<Hitable> hitList;
    public Sceen()
    {
        hitList = new ArrayList<Hitable>(128);
    }

    public HitRecord iterInScreen(Ray ray,Hitable lastHit)
    {
        float minDistance = Float.MAX_VALUE;
        Hitable minObject = null;
        for(int i = hitList.size() - 1;i >= 0 ;i--)
        {
            Hitable hitObject = hitList.get(i);
            HitReturn hitReturn = hitObject.hit(ray,lastHit);
            float distance = hitReturn.distance;
            if(distance < minDistance && distance > minimalDistance)
            {
                //System.out.printf("Hit Ball\n");
                minObject = hitReturn.hitObject;
                minDistance = distance;
            }
        }

        if(minObject == null)
        {
            //System.out.printf("Distance Is %f\n",minDistance);
            return null;
        }
        else
        {
            return minObject.getHitRecord(ray.at(minDistance),ray);
        }
    }
}
