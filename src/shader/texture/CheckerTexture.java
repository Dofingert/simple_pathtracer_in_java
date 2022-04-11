package shader.texture;

import com.vector.Vec3;

public class CheckerTexture implements Texture{
    public Vec3 HighLight = Vec3.instant(1,1,1);
    public Vec3 DarkLight = Vec3.instant(0.25f,0.75f,0.25f);
    public Vec3 getTexture(Vec3 uvw) {
        int sines = (int)(1000 * uvw.v[0] * uvw.v[1]);
        if (sines % 2 == 1)
            return HighLight;
        else
            return DarkLight;
    }
}
