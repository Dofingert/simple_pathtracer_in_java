package shader.texture;

import com.vector.Vec3;

public class ConstGrayTexture implements GrayTexture{
    float gray;

    public ConstGrayTexture(float gray)
    {
        this.gray = gray;
    }

    public float getTexture(Vec3 uvw)
    {
        Vec3 ret = new Vec3(gray,gray,gray);
        return gray;
    }
}
