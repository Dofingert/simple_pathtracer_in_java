package com.vector;

import com.main.Main;

public class Ray{
    public Vec3 Ori;
    public Vec3 Dst;

    public Ray(Vec3 Ori,Vec3 Dst)
    {
        this.Ori = new Vec3(Ori);
        this.Dst = new Vec3(Dst);
        this.Dst.normalize();
    }
    public Ray()
    {
        Ori = new Vec3(0,0,0);
        Dst = new Vec3(0,0,-1);
    }
    public Ray(Ray in)
    {
        Ori = new Vec3(in.ori());
        Dst = new Vec3(in.dst());
    }

    public void standerize(){
        Dst.normalize();
    }

    public void dst(Vec3 in)
    {
        Dst = in;
        this.standerize();
    }

    public Vec3 dst()
    {
        return Dst;
    }

    public void ori(Vec3 in)
    {
        Ori = in;
    }

    public Vec3 ori()
    {
        return Ori;
    }

    public Vec3 at(float distance,Vec3 r)
    {
        r = Vec3.mul(Dst,r,distance);
        r.add(Ori);
        return r;
    }

    public Vec3 at(float distance)
    {
        Vec3 r = Vec3.add(Ori,Vec3.mul(Dst,distance));
        return r;
    }

    public Ray getReflect(Vec3 normal,Vec3 hitPoint)
    {
        Vec3 reflectDst = Vec3.sub(Dst,Vec3.mul(normal,2 * Vec3.dot(Dst,normal)));
        Ray ret = new Ray(hitPoint,reflectDst);
        return ret;
    }

    public Ray getRefract(Vec3 n,Vec3 hitPoint,float etai_over_etat)
    {
        Vec3 uv = Dst;
        float cos_theta = Math.min(Vec3.dot(uv, n) * -1, 1.0f);

        Vec3 scaller = Vec3.mul(n,cos_theta);
        Vec3 r_out_perp =  Vec3.mul(Vec3.add(uv,scaller),etai_over_etat);
        Vec3 r_out_parallel = Vec3.mul(n,(float) -Math.sqrt(Math.abs(1.0 - r_out_perp.dot(r_out_perp))));
        return new Ray(Vec3.add(hitPoint,Vec3.mul(n,-0.01f)),Vec3.add(r_out_perp , r_out_parallel));
    }

}
