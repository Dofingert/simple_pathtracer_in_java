package shader.skybox;

import com.vector.Ray;
import com.vector.Vec3;

public class BlackSky implements Sky{
    public Vec3 getColor(Ray inRay) {
    //Vec3 returnColor = new Vec3((float) 0.5,(float) 0.7,(float) 1);
    Vec3 returnColor = new Vec3((float)0.3, (float)0.6, (float)0.8);
    return returnColor;
}
}
