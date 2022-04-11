package manager;

import camera.Camera;
import com.main.Main;
import com.vector.Vec3;
import sceen.hitable.Bvh;
import shader.skybox.BlackSky;
import shader.skybox.GradientSky;
import shader.skybox.PinkSky;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ReadParameters {

    public static void readFromArg(String[] arg)
    {
        for(int i = 0; i < arg.length; i ++)
        {
            String cmd = arg[i];
            if (cmd.compareTo("-mtl") == 0) {
                String mtlLib = arg[++i];
                System.out.printf("Try to load Material Lib %s...\n",mtlLib);
                try {
                    ReadMaterial.addMaterial(new BufferedReader(new FileReader(mtlLib)));
                    System.out.printf("Successfully Load Material Lib %s\n",mtlLib);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (cmd.compareTo("-obj") == 0)
            {
                String objLib = arg[++i];
                System.out.printf("Try to load Sceen Discription File %s...\n",objLib);
                try {
                    Bvh obj = ReadOBJ.readAndBuildOBJ(new BufferedReader(new FileReader(objLib)), ReadMaterial.materialMap.get("Inner_CheckerTexture"));
                    Main.mainSceen.hitList.add(obj);
                    System.out.printf("Successfully Load Sceen Discription File %s\n",objLib);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (cmd.compareTo("-cam") == 0)
            {
                float[] readList = new float[9];

                for (int j = 0; j < 9 ; j++)
                {
                    Scanner sc = new Scanner(arg[j + i + 1]);
                    if(sc.hasNextFloat())
                    {
                        readList[j] = sc.nextFloat();
                    }
                    else
                    {
                        System.out.printf("Error in reading Cam Discription, At least 9 parameters are needed while we got %d!\n",j);
                        i += j;
                        continue;
                    }
                }
                i += 9;

                Main.cam = new Camera(Vec3.instant(readList[0],readList[1],readList[2]),
                        Vec3.instant(readList[3],readList[4],readList[5]),
                        readList[6],readList[7],readList[8]);
            }
            else if (cmd.compareTo("-res") == 0)
            {
                int[] readList = new int[2];

                for (int j = 0; j < 2 ; j++)
                {
                    Scanner sc = new Scanner(arg[j + i + 1]);
                    if(sc.hasNextInt())
                    {
                        readList[j] = sc.nextInt();
                    }
                    else
                    {
                        System.out.printf("Error in reading Resolution Discription, At least 2 parameters are needed while we got %d!\n",j);
                        i += j;
                        continue;
                    }
                }
                i += 2;

                Main.widthInPixel = readList[0];
                Main.lengthInPixel = readList[1];
            }
            else if (cmd.compareTo("-depth") == 0)
            {
                int depth = 0;
                Scanner sc = new Scanner(arg[i + 1]);
                if(sc.hasNextInt())
                {
                    depth = sc.nextInt();
                    Main.maxDepth = depth;
                }
                else
                {
                    System.out.printf("Error in reading Depth Discription, At least 1 parameters are needed while we got %d!\n",0);
                    continue;
                }
                i++;
            }
            else if (cmd.compareTo("-sr") == 0)
            {
                int sr = 0;
                Scanner sc = new Scanner(arg[i + 1]);
                if(sc.hasNextInt())
                {
                    sr = sc.nextInt();
                    Main.samplingRate = sr;
                }
                else
                {
                    System.out.printf("Error in reading Depth Discription, At least 1 parameters are needed while we got %d!\n",0);
                    continue;
                }
                i++;
            }
            else if (cmd.compareTo("-sky") == 0)
            {
                int type = 0;
                Scanner sc = new Scanner(arg[i + 1]);
                if(sc.hasNextInt())
                {
                    type = sc.nextInt();
                }
                else
                {
                    System.out.printf("Error in reading Sky Discription, At least 1 parameters are needed while we got %d!\n",0);
                    continue;
                }
                switch (type)
                {
                    default:
                        Main.missShader = new BlackSky();
                        break;
                    case 0:
                        Main.missShader = new BlackSky();
                        break;
                    case 1:
                        Main.missShader = new GradientSky();
                        break;
                    case 2:
                        Main.missShader = new PinkSky();
                        break;
                }
                i++;
            }
            else if (cmd.compareTo("-out") == 0)
            {
                String fileName = arg[++i];
                Main.outputFileName = fileName;
            }

        }
    }

}
