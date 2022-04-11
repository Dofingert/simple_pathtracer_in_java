package shader.skybox;
import com.vector.*;

public interface Sky {
    Vec3 getColor(Ray inRay);
}
