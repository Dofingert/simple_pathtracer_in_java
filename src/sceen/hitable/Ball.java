package sceen.hitable;
import com.vector.*;
import sceen.aabb.AABB;
import shader.surface.HitRecord;
import shader.surface.SurfaceShader;

public class Ball implements Hitable{
    public Vec3 center;
    public float r;

    public SurfaceShader surface;

    public Ball()
    {
        r = 1;
    }

    public Ball(
            float a,float b,float c,
            float inR,
            SurfaceShader inSurfaceShader)
    {
        center = new Vec3(a,b,c);
        r = inR;
        surface = inSurfaceShader;
    }

    public Ball(
            Vec3 inCenter,float inR,
    SurfaceShader inSurfaceShader)
    {
        center = new Vec3(inCenter);
        r = inR;
        surface = inSurfaceShader;
    }

    public HitReturn hit(Ray inRay,Hitable lastHit)
    {
        if(lastHit == this)
        {
            HitReturn ret = new HitReturn();
            ret.distance = Float.MAX_VALUE;
            ret.hitObject = null;
            return ret;
        }

        Vec3 oc = Vec3.sub(inRay.ori(),center);
        float a = 1;
        float b = Vec3.dot(oc,inRay.dst());
        float c = Vec3.dot(oc,oc) - r * r;
        float delta = b * b - a * c;
        if(delta < 0)
        {
            HitReturn ret = new HitReturn();
            ret.distance = Float.MAX_VALUE;
            ret.hitObject = null;
            return ret;
        }
        else
        {
            delta = (float)Math.sqrt(delta);
            float ret1 = -b - delta;
            float ret2 = -b + delta;

            HitReturn ret = new HitReturn();
            ret.hitObject = this;
            if(ret1 > 0)
            {
                ret.distance = ret1;
                return ret;
            }
            else if(ret2 > 0)
            {
                ret.distance = ret2;
                return ret;
            }
            else
            {
                ret.distance = Float.MAX_VALUE;
                ret.hitObject = null;
                return ret;
            }
        }

    }

    public Vec3 getNormal(Vec3 hitPoint, Ray line) {
        Vec3 normal = Vec3.sub(hitPoint,center).normalize();
        if(normal.dot(line.Dst) < 0)
        {
            return normal;
        }
        return normal.mul(-1);
    }

    public HitRecord getHitRecord(Vec3 hitPoint, Ray line) {
        Vec3 uvw = new Vec3(0,0,0);
        HitRecord ret = new HitRecord();
        ret.hitPoint = hitPoint;
        ret.normal = getNormal(hitPoint,line);
        ret.surfaceDiscribe = surface.getSurfaceDiscribe(uvw);
        ret.hitObject = this;
        return ret;
    }


    @Override
    public AABB generateAABB() {
        Vec3 board = Vec3.instant(1,1,1).mul(r);
        AABB ret = new AABB(
                Vec3.add(center,board),
                Vec3.sub(center,board)
        );

        return ret;
    }

    @Override
    public Vec3 getCenter() {
        return center;
    }
}
