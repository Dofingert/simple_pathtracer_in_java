package shader.surface;

import com.vector.Vec3;
import shader.texture.GrayTexture;
import shader.texture.Texture;

public class SurfaceShader {
    public Texture diffuseTexture;
    public Texture reflectTexture;
    public Texture refractTexture;
    public Texture illustrationTexture;
    public GrayTexture diffuseWeight;
    public GrayTexture reflectClearness;
    public boolean isRefractable;
    public float n;
    public SurfaceDiscribe getSurfaceDiscribe(Vec3 uvw)
    {
        SurfaceDiscribe surfaceDiscribe = new SurfaceDiscribe();
        surfaceDiscribe.diffuseWeight = diffuseWeight.getTexture(uvw);
        surfaceDiscribe.reflectClearness = reflectClearness.getTexture(uvw);
        surfaceDiscribe.textureDiffuse = diffuseTexture.getTexture(uvw);
        surfaceDiscribe.textureReflect = reflectTexture.getTexture(uvw);
        surfaceDiscribe.textureRefract = refractTexture.getTexture(uvw);
        surfaceDiscribe.textureIllustate = illustrationTexture.getTexture(uvw);
        surfaceDiscribe.n = n;
        surfaceDiscribe.isRefractable = isRefractable;
        return surfaceDiscribe;
    }
}
