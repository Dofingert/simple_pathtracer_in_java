package manager;

import com.vector.Vec3;
import shader.surface.SurfaceShader;
import shader.texture.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class ReadMaterial {

    public static HashMap<String, SurfaceShader> materialMap = new HashMap<String,SurfaceShader>();

    public static void addMaterial(BufferedReader inReader)
    {
        try {
            String nowLine;

            SurfaceShader solidConstTexture = new SurfaceShader();
            String name = null;
            int NewTexState = 0;
            while ((nowLine = inReader.readLine()) != null)
            {
                Scanner sc = new Scanner(nowLine);
                String cmd;
                try
                {
                    cmd = sc.next();
                    if(cmd.compareTo("newmtl") == 0)
                    {
                        String oldName = name;
                        name = sc.next();
                        if(NewTexState == 1)
                        {
                            System.out.printf("Save Texture %s\n",oldName);
                            materialMap.put(oldName,solidConstTexture);
                        }
                        solidConstTexture = new SurfaceShader();
                        solidConstTexture.diffuseTexture = new ConstTexture(
                                (float)0x9F / 256,
                                (float)0x8F / 256,
                                (float)0x7C / 256);
                        solidConstTexture.reflectTexture = new ConstTexture(
                                (float)0xaF / 256,
                                (float)0xbF / 256,
                                (float)0xcF / 256);
                        solidConstTexture.refractTexture = new ConstTexture(
                                (float)0xBB / 256,
                                (float)0xBC / 256,
                                (float)0xB1 / 256);
                        solidConstTexture.illustrationTexture = new ConstTexture(
                                0,
                                0,
                                0
                        );
                        solidConstTexture.diffuseWeight = new ConstGrayTexture((float) 0.85);
                        solidConstTexture.reflectClearness = new ConstGrayTexture((float) 0.38);
                        solidConstTexture.n = 1;
                        solidConstTexture.isRefractable = false;
                        NewTexState = 1;
                        continue;
                    }
                    if(NewTexState == 1)
                    {
                        if(cmd.compareTo("map_Kd") == 0)
                        {
                            String filePath = sc.next();
                            RGBTexture newTexture = new RGBTexture(filePath);
                            solidConstTexture.diffuseTexture = newTexture;
                            solidConstTexture.reflectTexture = newTexture;
                        }
                        else if(cmd.compareTo("Kd") == 0)
                        {
                            float a,b,c;
                            a = sc.nextFloat();
                            b = sc.nextFloat();
                            c = sc.nextFloat();
                            Texture newTexture = new ConstTexture(Vec3.instant(a,b,c));
                            solidConstTexture.diffuseTexture = newTexture;
                            solidConstTexture.reflectTexture = newTexture;
                        }
                        else if(cmd.compareTo("map_Kl") == 0)
                        {
                            String filePath = sc.next();
                            RGBTexture newTexture = new RGBTexture(filePath);
                            solidConstTexture.illustrationTexture = newTexture;
                        }
                        else if(cmd.compareTo("Kl") == 0)
                        {
                            float a,b,c;
                            a = sc.nextFloat();
                            b = sc.nextFloat();
                            c = sc.nextFloat();
                            Texture newTexture = new ConstTexture(Vec3.instant(a,b,c));
                            solidConstTexture.illustrationTexture = newTexture;
                        }
                        else if(cmd.compareTo("map_Kr") == 0)
                        {
                            String filePath = sc.next();
                            RGBTexture newTexture = new RGBTexture(filePath);
                            solidConstTexture.reflectTexture = newTexture;
                        }
                        else if(cmd.compareTo("Kr") == 0)
                        {
                            float a,b,c;
                            a = sc.nextFloat();
                            b = sc.nextFloat();
                            c = sc.nextFloat();
                            Texture newTexture = new ConstTexture(Vec3.instant(a,b,c));
                            solidConstTexture.reflectTexture = newTexture;
                        }
                        else if(cmd.compareTo("Kt") == 0)
                        {
                            float a,b,c;
                            a = sc.nextFloat();
                            b = sc.nextFloat();
                            c = sc.nextFloat();
                            Texture newTexture = new ConstTexture(Vec3.instant(a,b,c));
                            solidConstTexture.refractTexture = newTexture;
                        }
                        else if(cmd.compareTo("Roughness") == 0)
                        {
                            float a;
                            a = sc.nextFloat();
                            System.out.printf("Roughness Is %f\n",a);
                            GrayTexture newTexture = new ConstGrayTexture(a);
                            solidConstTexture.diffuseWeight = newTexture;
                        }
                        else if(cmd.compareTo("Fuzz") == 0)
                        {
                            float a;
                            a = sc.nextFloat();
                            GrayTexture newTexture = new ConstGrayTexture(1 - a);
                            solidConstTexture.reflectClearness = newTexture;
                        }
                        else if(cmd.compareTo("Refractable") == 0)
                        {
                            solidConstTexture.isRefractable = true;
                        }
                        else if(cmd.compareTo("Kn") == 0)
                        {
                            solidConstTexture.n = sc.nextFloat();
                        }
                        else
                        {
                            continue;
                        }
                    }
                }
                catch (NoSuchElementException e)
                {
                    continue;
                }
            }
            System.out.printf("Save Texture %s\n",name);
            materialMap.put(name,solidConstTexture);
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
