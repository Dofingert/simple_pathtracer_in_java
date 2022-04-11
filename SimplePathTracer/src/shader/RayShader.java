package shader;

import com.function.Fresnel;
import com.vector.Ray;
import com.vector.Vec3;
import sceen.Sceen;
import sceen.hitable.Hitable;
import shader.skybox.Sky;
import shader.surface.HitRecord;

public class RayShader {
    static final boolean isNormal = false;
    static final boolean isDistance = false;
    static final boolean isReflectDir = false;
    public int iterLeftDepth;
    public Vec3 effectMultiplyer;
    public Vec3 color;
    public Ray rayLoad;
    public float n;
    public RayShader(Ray ray,int maxDepth)
    {
        color = new Vec3(0,0,0);
        effectMultiplyer = new Vec3(1,1,1);
        rayLoad = new Ray(ray);
        iterLeftDepth = maxDepth;
        n = 1;
    }

    public Vec3 caculateColor(Sceen sceen, Sky missShader)
    {
        //System.out.printf("in\n");
        Hitable lastHit = null;
        for(;;iterLeftDepth--)
        {
            //System.out.printf("iter left %d\nColor",iterLeftDepth);
            //color.print();
            //rayLoad.Dst.print();
            //rayLoad.Ori.print();
            HitRecord hitRecord = sceen.iterInScreen(rayLoad,lastHit);
            if(isNormal)
            {
                //System.out.printf("normal:");
                //hitRecord.normal.print();
                return hitRecord.normal.add(Vec3.instant(1,1,1)).mul((float) 0.5);
            }
            else if(isDistance)
            {
                double distance = Vec3.sub(rayLoad.Ori,hitRecord.hitPoint).getLength();

                return Vec3.instant((float) distance/100,
                        (float)distance/100,
                        (float)distance/100);
            }

            if (hitRecord == null) {
                //System.out.printf("out\n");
                //return Vec3.instant(0,1,1);
                return color.add(Vec3.mul(missShader.getColor(rayLoad),effectMultiplyer));
            } else if (iterLeftDepth <= 1) {
                //System.out.printf("out\n");
                //return Vec3.instant(1,0,0);
                return color.add(Vec3.mul(hitRecord.surfaceDiscribe.textureIllustate,effectMultiplyer));
            } else {
                lastHit = hitRecord.hitObject;
                //System.out.printf("illusion");
                //hitRecord.surfaceDiscribe.textureIllustate.print();
                color.add(Vec3.mul(hitRecord.surfaceDiscribe.textureIllustate,effectMultiplyer));
            }
            if (Math.random() > hitRecord.surfaceDiscribe.diffuseWeight)
            {
                double posibliltyToRefract = 0;
                Ray rayReflect;
                if (hitRecord.surfaceDiscribe.isRefractable)
                {
                    float refraction_ratio;
                    boolean isIn = true;
                    if(n == hitRecord.surfaceDiscribe.n)
                    {
                        refraction_ratio = n;
                        n = 1;
                        isIn = false;
                    }
                    else
                    {
                        refraction_ratio = n / hitRecord.surfaceDiscribe.n;
                        n = hitRecord.surfaceDiscribe.n;
                    }
                    float cos_theta = Math.min(-Vec3.dot(rayLoad.Dst,hitRecord.normal),1.0f);
                    double sin_theta = Math.sqrt(1.0 - cos_theta * cos_theta);

                    boolean can_refract = (refraction_ratio * sin_theta <= 1.0);

                    posibliltyToRefract = Fresnel.simpleCompute(cos_theta,refraction_ratio);
                    if(Math.random() > posibliltyToRefract && can_refract)
                    {
                        effectMultiplyer.mul(hitRecord.surfaceDiscribe.textureRefract);
                        rayLoad = rayLoad.getRefract(hitRecord.normal,hitRecord.hitPoint,refraction_ratio);
                        if(isIn)
                        {
                            lastHit = null;
                        }
                        continue;
                    }
                    else if(isIn == false)
                    {
                        lastHit = null;
                    }
                }
                effectMultiplyer.mul(hitRecord.surfaceDiscribe.textureReflect);
                rayReflect = rayLoad.getReflect(hitRecord.normal, hitRecord.hitPoint);//do refract/reflect
                rayReflect.Dst.add(Vec3.random().mul(1 - hitRecord.surfaceDiscribe.reflectClearness)).normalize();
                rayLoad = rayReflect;
                if(isReflectDir)
                {
                    //return rayReflect.Dst.add(Vec3.instant(1,1,1)).mul((float) 0.5);
                }
                continue;
            } else {
                //System.out.printf("DiffuseMuler&Diffuse");
                effectMultiplyer.mul(hitRecord.surfaceDiscribe.textureDiffuse);
                //effectMultiplyer.print();
                //hitRecord.surfaceDiscribe.textureDiffuse.print();

                //System.out.printf("Color");
                //color.print();
                Vec3 diffuseDst = Vec3.add(Vec3.random(),hitRecord.normal).normalize();
                rayLoad.Dst = diffuseDst;
                rayLoad.Ori = hitRecord.hitPoint;
                if(isReflectDir)
                {
                    return diffuseDst.add(Vec3.instant(1,1,1)).mul((float) 0.5);
                }
                continue;
            }
        }
    }
}
