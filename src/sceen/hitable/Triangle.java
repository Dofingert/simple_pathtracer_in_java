package sceen.hitable;

import com.vector.Ray;
import com.vector.Vec3;
import sceen.aabb.AABB;
import shader.surface.HitRecord;
import shader.surface.SurfaceShader;

public class Triangle implements Hitable{
    protected Vec3[] point= new Vec3[3];
    protected Vec3 normal;
    protected Vec3 s;
    Vec3 v0;
    Vec3 v1;
    Vec3 p0TextureUVW;
    Vec3 p1ReletiveTextureUVW;
    Vec3 p2ReletiveTextureUVW;

    public SurfaceShader surface;

    public void initial()
    {
        v0 = Vec3.sub(point[1],point[0]);
        v1 = Vec3.sub(point[2],point[0]);
        if(p0TextureUVW == null)
        {
            p0TextureUVW = new Vec3(0, 0, 0);
            p1ReletiveTextureUVW = new Vec3(0, 0, 0);
            p2ReletiveTextureUVW = new Vec3(0, 0, 0);
        }
    }
    public void updateNormal()
    {
        normal = Vec3.cross(v0,v1);
        normal = normal.normalize();
        //normal.print();
        s = (Vec3.add(Vec3.add(point[0],point[1]),point[2])).div(3);
        //System.out.printf("s : %f %f %f\n",s.v[0],s.v[1],s.v[2]);
    }

    public Triangle(Vec3[] p)
    {
        point[0] = new Vec3(p[0]);
        point[1] = new Vec3(p[1]);
        point[2] = new Vec3(p[2]);
        initial();
        updateNormal();
    }

    public Triangle(Vec3 point0,Vec3 point1,Vec3 point2,Vec3 texture1,Vec3 texture2,Vec3 texture3)
    {
        point[0] = new Vec3(point0);
        point[1] = new Vec3(point1);
        point[2] = new Vec3(point2);
        p0TextureUVW = new Vec3(texture1);
        //p0TextureUVW.print();
        p1ReletiveTextureUVW = Vec3.sub(texture2,p0TextureUVW);
        p2ReletiveTextureUVW = Vec3.sub(texture3,p0TextureUVW);

        //p1ReletiveTextureUVW.print();
        //p2ReletiveTextureUVW.print();

        initial();
        updateNormal();
    }

    public boolean isInTri(Vec3 p)
    {
        Vec3 v2 = Vec3.sub(p,point[0]);
        float dot00 = Vec3.dot(v0, v0);
        float dot01 = Vec3.dot(v0, v1);
        float dot02 = Vec3.dot(v0, v2);
        float dot11 = Vec3.dot(v1, v1);
        float dot12 = Vec3.dot(v1, v2);
        float inverDeno = 1 / (dot00 * dot11 - dot01 * dot01);

        float u = (dot11 * dot02 - dot01 * dot12) * inverDeno;
        if (u < 0 || u > 1)
        {
            return false;
        }

        float v = (dot00 * dot12 - dot01 * dot02) * inverDeno;
        if (v < 0 || v > 1)
        {
            return false;
        }

        return u + v <= 1;
    }
    public HitReturn hit(Ray line,Hitable lastHit)
    {
        if(lastHit == this)
        {
            HitReturn ret = new HitReturn();
            ret.distance = Float.MAX_VALUE;
            ret.hitObject = null;
            return ret;
        }
        Vec3 posibleHitPoint;

        float distance = Vec3.dot(Vec3.sub(line.ori(),s), normal);
        float dotnline =  Vec3.dot(normal, line.dst());
        if (distance * dotnline > 0)
        {
            HitReturn ret = new HitReturn();
            ret.distance = Float.MAX_VALUE;
            ret.hitObject = null;
            return ret;
        }
        distance /= -dotnline;
        posibleHitPoint = line.at(Math.abs(distance));
        if(isInTri(posibleHitPoint)) {
            HitReturn ret = new HitReturn();
            ret.distance = distance;
            ret.hitObject = this;
            return ret;
        }
        else
        {
            HitReturn ret = new HitReturn();
            ret.distance = Float.MAX_VALUE;
            ret.hitObject = null;
            return ret;
        }
    }

    public Vec3 getUVW(Vec3 hitPoint)
    {
        Vec3 v = Vec3.sub(hitPoint,point[0]);
        float a = Vec3.dot(v0,v0);
        float b = Vec3.dot(v0,v1);
        float c = b;
        float d = Vec3.dot(v1,v1);
        float m = Vec3.dot(v0,v);
        float n = Vec3.dot(v1,v);

        float s = (b * n - d * m)/ (b * c - d * a);
        float t = (a * n - c * m)/ (a * d - c * b);
        //System.out.printf("%f %f is\n",s,t);
        Vec3 ret = Vec3.mul(p1ReletiveTextureUVW,s);
        //ret.print();
        ret.add(Vec3.mul(p2ReletiveTextureUVW,t));
        //ret.print();
        ret.add(p0TextureUVW);
        //ret.print();

        return ret;
        //return new Vec3(p0TextureUVW);
    }
    public Vec3 getNormal(Vec3 hitPoint, Ray line)
    {
        if(Vec3.dot(Vec3.sub(hitPoint,line.ori()),normal) < 0)
        {
            //normal.print();
            return new Vec3(normal);
        }
        else
        {
            Vec3 ret = Vec3.mul(normal,-1);
            //ret.print();
            return ret;
            //return Vec3.mul(normal,-1);
        }
    }
    public HitRecord getHitRecord(Vec3 hitPoint, Ray line)
    {
        HitRecord ret = new HitRecord();
        ret.hitPoint = hitPoint;
        ret.normal = getNormal(hitPoint,line);
        Vec3 uvw = getUVW(hitPoint);
        ret.surfaceDiscribe = surface.getSurfaceDiscribe(uvw);
        ret.hitObject = this;
        return ret;
    }

    @Override
    public AABB generateAABB() {
        Vec3 max,min;
        max = Vec3.getMax(point[0],point[1]);
        max = Vec3.getMax(max,point[2]);
        min = Vec3.getMin(point[0],point[1]);
        min = Vec3.getMin(min,point[2]);

        AABB ret = new AABB(
            max, min
        );

        return ret;
    }

    @Override
    public Vec3 getCenter()
    {
        return new Vec3(s);
    }
}
