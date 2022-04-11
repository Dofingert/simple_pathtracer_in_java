package shader.texture;

import com.vector.Vec3;

public class ConstTexture implements Texture{
    public Vec3 constTexture;
    public ConstTexture(float a,float b,float c)
    {
        constTexture = new Vec3(a,b,c);
    }

    public ConstTexture(Vec3 color)
    {
        constTexture = new Vec3(color);
    }
    public Vec3 getTexture(Vec3 uvw)
    {return constTexture;}
}
