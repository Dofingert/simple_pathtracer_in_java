package shader.surface;

import com.vector.Vec3;
import sceen.hitable.Hitable;

public class HitRecord {
    public Vec3 hitPoint;
    public Vec3 normal;
    public SurfaceDiscribe surfaceDiscribe;
    public Hitable hitObject;
}
