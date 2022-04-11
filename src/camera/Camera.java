package camera;
import com.main.Main;
import com.vector.*;
import sceen.Sceen;
import shader.RayShader;
import shader.skybox.Sky;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class PixelRaySender
{
    static int samplingRate;

    static float LUT[] = new float[256];
    public static int ToneMap(float x)
    {
        int L = 0, R = 255, M = 128;
        while (R - L > 1)
        {
            M = (L + R) / 2;
            if (LUT[M] < x)
            {
                L = M;
            }
            else
            {
                R = M;
            }
        }
        return M;
    }

    public static void build_table()
    {
        for (float i = 0; i < 256; i++)
        {
            if (i / 256 <= 0.8)
            {
                LUT[(int)i] = (i * i) / 65536;
            }
            else
            {
                LUT[(int)i] = (float)((0.128) / (1 - (i / 256)));
            }
        }
    }

    public static Vec3 pixelRandering(Sceen sceen,Sky missShader,Vec3 camPoint, Vec3 beginPoint, Vec3 stepX, Vec3 stepY,float aperture)
    {
        Vec3 result = new Vec3();
        for(int i = 0;i<samplingRate;i++)
        {
            Vec3 rayDst = Vec3.sub(Vec3.mul(stepX,(float)Math.random()),Vec3.mul(stepY,(float)Math.random()));
            rayDst.add(beginPoint);
            rayDst.sub(camPoint);
            Ray ray = new Ray(
                    (Vec3.random().mul(aperture)).add(camPoint),
                    rayDst);
            RayShader rayShader = new RayShader(ray, Main.maxDepth);
            result.add(rayShader.caculateColor(sceen,missShader));
        }
        result.div(samplingRate);
        return result;
    }
    public static void Vec3To8BColor(Vec3 color,int[] result,int x,int y,int widthInPixel)
    {
        result[widthInPixel * y + x] = ToneMap(color.v[2])
                | (ToneMap(color.v[1]) << 8)
                | (ToneMap(color.v[0]) << 16);
    }
}

public class Camera {
    public Vec3 camPoint;
    public Vec3 dst;
    public Vec3 screenCenter;
    public Vec3 dx;
    public Vec3 dy;

    public static final int cpuCore = Runtime.getRuntime().availableProcessors();



    float focusDistance;
    float aperture;
    float fov;

    float halfLength;

    public Camera(Vec3 inCamPoint,Vec3 inDst,float inFov,float inAperture,float inFocusDistance)
    {
        camPoint = new Vec3(inCamPoint);
        dst = new Vec3(inDst);
        fov = (float)Math.toRadians(inFov);
        aperture = inAperture;
        focusDistance = inFocusDistance;
        cameraUpdate();
    }
    public Camera()
    {
        camPoint = new Vec3();
        dst = new Vec3(0,1,0);
        cameraUpdate();
    }

    public void renderCall(int widthInPixel,int lengthInPixel,int[] screen,int samplingNum,Sceen sceen,Sky missShader)
    {
        PixelRaySender.samplingRate = samplingNum;
        PixelRaySender.build_table();
        Vec3 LU_Point = Vec3.add(Vec3.mul(dx,-halfLength),Vec3.mul(dy,halfLength * lengthInPixel / widthInPixel));
        LU_Point.add(screenCenter);
        Vec3 xStep = Vec3.mul(dx,2 * halfLength / widthInPixel);
        Vec3 yStep = Vec3.mul(dy,2 * halfLength / widthInPixel);

        Vec3 beginPointY = new Vec3(LU_Point);

        ExecutorService executorService = Executors.newFixedThreadPool(cpuCore - 1);
        System.out.printf("Render at %d Threads\n",cpuCore - 1);
        Future []runState = new Future[cpuCore - 1];
        for(int y = 0; y < lengthInPixel; y++)
        {
            //System.out.printf("End %d line\n",y);
            Vec3 beginPoint = new Vec3(beginPointY);
            final int innerY = y;
            runState[y % (cpuCore - 1)] = executorService.submit(new Runnable()
            {
                public void run()
                {
                    for(int x = 0; x < widthInPixel ; x++)
                    {
                        //System.out.printf("End %d pixel\n",x);
                        Vec3 color = PixelRaySender.pixelRandering(
                                sceen,
                                missShader,
                                camPoint,
                                beginPoint,
                                xStep,
                                yStep,
                                aperture
                        );
                        //System.out.printf("color");
                        //color.print();
                        PixelRaySender.Vec3To8BColor(color,screen,x,innerY,widthInPixel);
                        //System.out.printf("0x%x %d %d %d\n",screen[widthInPixel * y + x],PixelRaySender.ToneMap(color.v[0]),PixelRaySender.ToneMap(color.v[1]),PixelRaySender.ToneMap(color.v[2]));
                        beginPoint.add(xStep);
                    }
                    System.out.printf("End %d line\n",innerY);
                }
            });
            beginPointY.sub(yStep);
        }

        try {
            for(int i = 0;i < cpuCore - 1; i ++)
            {
                while (runState[i].get() != null)
                {
                    continue;
                }
            }
        }catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.shutdown();


        System.out.print("xy is\n");
        xStep.print();
        yStep.print();
        LU_Point.print();
        beginPointY.print();
        System.out.printf("d,f is %f %f\n",halfLength,focusDistance);
    }

    public void cameraSet(Vec3 inCamPoint,Vec3 inDst)
    {
        camPoint.mul(inCamPoint,1);
        dst.mul(inDst,1);
        cameraUpdate();
    }

    public void cameraUpdate()
    {
        dst.normalize();
        dst.mul(focusDistance);
        screenCenter = Vec3.add(camPoint,dst);
        halfLength = (float)(focusDistance * Math.tan(fov / 2));

        dst.div(focusDistance);
        if(dst.v[2] >= 0.99999f)
        {
            dx = new Vec3(1,0,0);
            dy = new Vec3(0,-1,0);
        }
        else
        {
            dy = new Vec3(0, 0, 1);
            dx = Vec3.cross(dst, dy).normalize();
            dy = Vec3.cross(dx, dst).normalize();
        }
    }
}
