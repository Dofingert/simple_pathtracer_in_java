package shader.texture;

import com.vector.Vec3;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RGBTexture implements Texture{

    BufferedImage buffImg;
    int width;
    int higth;

    public RGBTexture(BufferedImage inBuf)
    {
        buffImg = inBuf;
        width = buffImg.getWidth();
        higth = buffImg.getWidth();
    }

    public RGBTexture(String fileName)
    {
        BufferedImage buf;
        try {
            File file= new File(fileName);
            buf = ImageIO.read(file);
            buffImg = buf;
            width = buffImg.getWidth();
            higth = buffImg.getWidth();
            System.out.printf("Read Tex File %s  %d x %d\n",fileName,width,higth);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Vec3 getTexture(Vec3 uvw) {
        int x = (((int)(uvw.v[0] * width) % width) + width) % width;
        int y = (((int)(uvw.v[1] * higth) % higth) + higth) % higth;
        //x = width - 1 - x;
        y = higth - 1 - y;
        int packedRGB = buffImg.getRGB(x,y);

        int r = (packedRGB >> 16)& 0xff;
        int g = (packedRGB >> 8 )& 0xff;
        int b = packedRGB & 0xff;
        Vec3 ret = new Vec3((float) r / 255,(float)g / 255,(float)b/255);
        return ret;
    }
}
