package manager;

import com.vector.Vec3;
import sceen.hitable.Ball;
import sceen.hitable.Bvh;
import sceen.hitable.Hitable;
import sceen.hitable.Triangle;
import shader.surface.SurfaceShader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ReadOBJ {
    public static Bvh readAndBuildOBJ(BufferedReader inReader,SurfaceShader defaultShader)
    {
        ArrayList<Hitable> faceList = new ArrayList<Hitable>(2048);
        ArrayList<Vec3> point = new ArrayList<Vec3>(8192);
        ArrayList<Vec3> uvPoint = new ArrayList<Vec3>(8192);

        Scanner sc;
        String line;
        SurfaceShader nowMaterial = defaultShader;
        try {
            while ((line = inReader.readLine()) != null) {
                sc = new Scanner(line);
                try {
                    String cmd = sc.next();

                    if (cmd.compareTo("v") == 0) {
                        float a, b, c;
                        a = sc.nextFloat();
                        b = sc.nextFloat();
                        c = sc.nextFloat();
                        point.add(Vec3.instant(a, b, c));
                        //System.out.print("getV\n");
                    }
                    else if (cmd.compareTo("vt") == 0) {
                        float a, b, c;
                        a = sc.nextFloat();
                        b = sc.nextFloat();
                        c = sc.nextFloat();
                        uvPoint.add(Vec3.instant(a, b, c));
                        //System.out.print("getVt\n");
                    }
                    else if (cmd.compareTo("f") == 0) {
                        int a,b,c,at,bt,ct;
                        sc.useDelimiter(" |/|\\.");
                        a = sc.nextInt() - 1;
                        at = sc.nextInt() - 1;
                        b = sc.nextInt() - 1;
                        bt = sc.nextInt() - 1;
                        c = sc.nextInt() - 1;
                        ct = sc.nextInt() - 1;
                        Triangle tri = new Triangle(point.get(a),
                                        point.get(b),
                                        point.get(c),
                                        uvPoint.get(at),
                                        uvPoint.get(bt),
                                        uvPoint.get(ct));
                        tri.surface = nowMaterial;
                        //System.out.print("getFrag\n");
                        faceList.add(tri);
                    }
                    else if(cmd.compareTo("usemtl") == 0)
                    {
                        String name = sc.next();
                        nowMaterial = ReadMaterial.materialMap.get(name);
                        if(nowMaterial == null)
                        {
                            System.out.printf("Error Find Texture %s, Set to Default",name);
                            nowMaterial = defaultShader;
                        }
                    }
                    else if(cmd.compareTo("b") == 0)
                    {
                        float[] list = new float[4];
                        list[0] = sc.nextFloat();
                        list[1] = sc.nextFloat();
                        list[2] = sc.nextFloat();
                        list[3] = sc.nextFloat();
                        System.out.printf("Got Ball %f %f %f %f\n",list[0],list[1],list[2],list[3]);
                        Ball newBall = new Ball(list[0],list[1],list[2],list[3],nowMaterial);
                        faceList.add(newBall);
                    }

                } catch (NoSuchElementException e) {
                    continue;
                }
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }

        Bvh ret = BVHBuilder.build(faceList);
        return ret;
    }
}
