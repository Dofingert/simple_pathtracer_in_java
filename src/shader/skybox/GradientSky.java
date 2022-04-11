package shader.skybox;
import com.vector.Ray;
import com.vector.Vec3;

public class GradientSky implements Sky{
    public Vec3 getColor(Ray inRay) {
        //Vec3 returnColor = new Vec3((float) 0.5,(float) 0.7,(float) 1);
        Vec3 returnColor = new Vec3((float)(0.75 - (0.25 * inRay.dst().v[2])), (float)(0.85 - (0.15 * inRay.dst().v[2])), (float)1.0);
        return returnColor;
    }
}
