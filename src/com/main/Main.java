package com.main;

import com.vector.Vec3;
import manager.*;
import sceen.Sceen;
import camera.Camera;
import sceen.hitable.Ball;
import sceen.hitable.Bvh;
import sceen.hitable.Triangle;
import shader.skybox.BlackSky;
import shader.skybox.PinkSky;
import shader.skybox.Sky;
import shader.surface.SurfaceShader;
import shader.texture.ConstGrayTexture;
import shader.texture.ConstTexture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static int widthInPixel = 500;
    public static int lengthInPixel = 500;
    public static int samplingRate = 16;
    public static int maxDepth = 6;


    public static Sceen mainSceen = null;
    public static Camera cam = new Camera(
            //Vec3.instant((float) 3,(float)3,(float) 3),
            //Vec3.instant((float) -0.2524,(float) -27.21,(float)16.4511), //inCamPoint
            //Vec3.instant((float) -2.5403,(float) -2.0356,(float)17.7068), //inCamPoint
            Vec3.instant((float) 4,(float) -55,(float)16), //inCamPoint
            Vec3.instant((float) 0,(float) 1,(float)0),//Dst
            (float) 38,//fov
            (float)0.001,//aperture
            (float)27);//focusDistance

    public static Sky missShader = new BlackSky();
    public static String outputFileName = "Output.png";

    public static void main(String[] args) {

        mainSceen = new Sceen();

        BuildInnerTexture.BuildInnerTexture();

        ReadParameters.readFromArg(args);

        System.out.print("End Parameters Sets.\n");

        System.out.print("Start To build Top Level BVH....\n");

        if(mainSceen.hitList.size() > 2)
        {
            Bvh bvh = BVHBuilder.build(mainSceen.hitList);
            mainSceen.hitList.clear();
            mainSceen.hitList.add(bvh);
        }

        System.out.print("BVH Ready\n");

        int[] screen = new int[widthInPixel*lengthInPixel];

        cam.renderCall(widthInPixel,lengthInPixel,screen,samplingRate,mainSceen,missShader);

        BufferedImage outPut = new BufferedImage(widthInPixel,lengthInPixel,BufferedImage.TYPE_3BYTE_BGR);

        for(int y = 0; y < lengthInPixel ; y++)
            for(int x = 0; x <widthInPixel;x++)
                outPut.setRGB(x,y,screen[widthInPixel * y + x]);
        try {
            File file= new File(outputFileName);
            ImageIO.write((RenderedImage) outPut, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
