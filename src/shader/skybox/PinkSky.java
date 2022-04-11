package shader.skybox;

import com.vector.Ray;
import com.vector.Vec3;

public class PinkSky implements Sky{
    public Vec3 getColor(Ray inRay) {
        //Vec3 returnColor = new Vec3((float) 0.5,(float) 0.7,(float) 1);
        Vec3 returnColor = new Vec3((float)1, (float)0.431, (float)0.888);
        return returnColor;
    }

}
