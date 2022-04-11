package sceen.hitable;

import com.vector.Ray;
import com.vector.Vec3;
import sceen.aabb.AABB;
import shader.surface.HitRecord;

public class Bvh implements Hitable{
    static final float minimalDistance = (float) 0.00;
    public AABB aabb;

    public Hitable left;
    public Hitable right;

    public HitReturn hit(Ray testRay,Hitable lastHit)
    {
        HitReturn ret = new HitReturn();
        if(aabb.hit(testRay))
        {
            HitReturn leftHit = left.hit(testRay,lastHit);
            if(right == null)
            {
                return leftHit;
            }
            HitReturn rightHit = right.hit(testRay,lastHit);
            if(leftHit.distance < rightHit.distance && minimalDistance < leftHit.distance)
            {
                return leftHit;
            }
            else if(rightHit.distance > minimalDistance)
            {
                return rightHit;
            }
            else
            {
                ret.distance = Float.MAX_VALUE;
                ret.hitObject = null;
                return ret;
            }
        }
        else
        {
            ret.distance = Float.MAX_VALUE;
            ret.hitObject = null;
            return ret;
        }
    }

    public HitRecord getHitRecord(Vec3 hitPoint, Ray line)
    {
        System.out.println("Error Call\n");
        return null;
    }

    @Override
    public AABB generateAABB() {
        return aabb;
    }

    @Override
    public Vec3 getCenter() {
        return Vec3.add(aabb.maximum,aabb.minimum).div(2);
    }
}
