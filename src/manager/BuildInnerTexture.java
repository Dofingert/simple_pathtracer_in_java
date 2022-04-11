package manager;

import shader.surface.SurfaceShader;
import shader.texture.CheckerTexture;
import shader.texture.ConstGrayTexture;
import shader.texture.ConstTexture;
import shader.texture.Texture;

public class BuildInnerTexture {
    public static void BuildInnerTexture()
    {
        SurfaceShader solidConstTexture = new SurfaceShader();
        Texture checkBoard = new CheckerTexture();
        solidConstTexture.diffuseTexture = checkBoard;
        solidConstTexture.reflectTexture = checkBoard;
        solidConstTexture.refractTexture = new ConstTexture(
                (float)0xBB / 256,
                (float)0xBC / 256,
                (float)0xB1 / 256);
        solidConstTexture.illustrationTexture = new ConstTexture(
                0,
                0,
                0
        );
        solidConstTexture.diffuseWeight = new ConstGrayTexture((float) 0.75);
        solidConstTexture.reflectClearness = new ConstGrayTexture((float) 0.38);
        solidConstTexture.n = 1;
        solidConstTexture.isRefractable = false;
        ReadMaterial.materialMap.put("Inner_CheckerTexture",solidConstTexture);
    }
}
