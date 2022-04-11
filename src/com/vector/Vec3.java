package com.vector;

public class Vec3 {
    public float[] v;
    public Vec3(float a,float b,float c)
    {
        v = new float[3];
        v[0] = a;
        v[1] = b;
        v[2] = c;
    }

    public Vec3()
    {
        v = new float[3];
        v[0] = 0;
        v[1] = 0;
        v[2] = 0;
    }

    public void print()
    {
        System.out.printf("%f %f %f\n",v[0],v[1],v[2]);
    }

    static public Vec3 instant(float[]in)
    {
        return new Vec3(in[0],in[1],in[2]);
    }

    static public Vec3 instant(float a,float b,float c)
    {
        return new Vec3(a,b,c);
    }

    public void copy(Vec3 in)
    {
        v[0] = in.v[0];
        v[1] = in.v[1];
        v[2] = in.v[2];
    }

    public Vec3(Vec3 in)
    {
        v = new float[3];
        v[0] = in.v[0];
        v[1] = in.v[1];
        v[2] = in.v[2];
    }

    public static Vec3 add(Vec3 a,Vec3 b,Vec3 r)
    {
        r.v[0] = a.v[0] + b.v[0];
        r.v[1] = a.v[1] + b.v[1];
        r.v[2] = a.v[2] + b.v[2];
        return r;
    }

    public static Vec3 add(Vec3 a,Vec3 b)
    {
        Vec3 r = new Vec3();
        return add(a,b,r);
    }

    public Vec3 add(Vec3 adder)
    {
        return add(this,adder,this);
    }

    public static Vec3 sub(Vec3 a,Vec3 b,Vec3 r)
    {
        r.v[0] = a.v[0] - b.v[0];
        r.v[1] = a.v[1] - b.v[1];
        r.v[2] = a.v[2] - b.v[2];
        return r;
    }

    public static Vec3 sub(Vec3 a,Vec3 b)
    {
        Vec3 r = new Vec3();
        return sub(a,b,r);
    }

    public Vec3 sub(Vec3 adder)
    {
        return sub(this,adder,this);
    }

    public static float dot(Vec3 v1,Vec3 v2)
    {
        return v1.v[0] * v2.v[0] + v1.v[1] * v2.v[1] + v1.v[2] * v2.v[2];
    }

    public float dot(Vec3 v2)
    {
        return dot(this,v2);
    }

    public Vec3 set(float a,float b,float c)
    {
        v[0] = a;
        v[1] = b;
        v[2] = c;
        return this;
    }

    static public Vec3 getMax(Vec3 v1,Vec3 v2)
    {
        Vec3 ret = new Vec3(
                Math.max(v1.v[0],v2.v[0]),
                Math.max(v1.v[1],v2.v[1]),
                Math.max(v1.v[2],v2.v[2])
        );
        return ret;
    }

    static public Vec3 getMin(Vec3 v1,Vec3 v2)
    {
        Vec3 ret = new Vec3(
                Math.min(v1.v[0],v2.v[0]),
                Math.min(v1.v[1],v2.v[1]),
                Math.min(v1.v[2],v2.v[2])
        );
        return ret;
    }

    public Vec3 set(Vec3 v1)
    {
        v[0] = v1.v[0];
        v[1] = v1.v[1];
        v[2] = v1.v[2];
        return this;
    }

    public static Vec3 mul(Vec3 v1,Vec3 v2,Vec3 r)
    {
        r.v[0] = v1.v[0] * v2.v[0];
        r.v[1] = v1.v[1] * v2.v[1];
        r.v[2] = v1.v[2] * v2.v[2];
        return r;
    }

    public static Vec3 mul(Vec3 v1,Vec3 v2)
    {
        Vec3 r = new Vec3();
        r = mul(v1,v2,r);
        return r;
    }

    public Vec3 mul(Vec3 v2)
    {
        mul(this,v2,this);
        return this;
    }

    public double getLength()
    {
        return Math.sqrt(dot(this,this));
    }

    public static Vec3 mul(Vec3 vector,Vec3 r,float mul)
    {
        r.v[0] = vector.v[0] * mul;
        r.v[1] = vector.v[1] * mul;
        r.v[2] = vector.v[2] * mul;
        return r;
    }

    public static Vec3 mul(Vec3 vector,float mul)
    {
        Vec3 r = new Vec3();
        return mul(vector,r,mul);
    }

    public Vec3 mul(float mul)
    {
        mul(this,this,mul);
        return this;
    }

    public static Vec3 div(Vec3 vector,Vec3 r,float div)
    {
        r.v[0] = vector.v[0] / div;
        r.v[1] = vector.v[1] / div;
        r.v[2] = vector.v[2] / div;
        return r;
    }

    public static Vec3 div(Vec3 vector,float divder)
    {
        Vec3 r = new Vec3();
        return div(vector,r,divder);
    }

    public Vec3 div(float divder)
    {
        return div(this,this,divder);
    }

    public Vec3 normalize()
    {
        //System.out.printf("%f %f %f\n",this.v[0],this.v[1],this.v[2]);
        this.div((float)this.getLength());
        //.out.printf("%f %f %f\n",this.v[0],this.v[1],this.v[2]);
        return this;
    }

    public static Vec3 cross(Vec3 v1,Vec3 v2,Vec3 r)
    {
        r.v[0] = v1.v[1] * v2.v[2] - v1.v[2] * v2.v[1];
        r.v[1] = v1.v[2] * v2.v[0] - v1.v[0] * v2.v[2];
        r.v[2] = v1.v[0] * v2.v[1] - v1.v[1] * v2.v[0];
        return r;
    }

    public static Vec3 cross(Vec3 v1,Vec3 v2)
    {
        Vec3 r = new Vec3();
        return cross(v1,v2,r);
    }

    public static Vec3 random()
    {
        Vec3 ret = new Vec3(
                (float)Math.random() - (float) 0.5,
                (float)Math.random() - (float) 0.5,
                (float)Math.random() - (float) 0.5);
        ret.normalize();
        return ret;
    }

}
