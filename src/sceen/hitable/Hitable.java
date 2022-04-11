package sceen.hitable;
import com.vector.*;
import sceen.aabb.AABB;
import shader.surface.*;

public interface Hitable {
    HitReturn hit(Ray testRay,Hitable lastHit);
    //Vec3 getNormal(Vec3 hitPoint,Ray line);
    HitRecord getHitRecord(Vec3 hitPoint, Ray line);

    AABB generateAABB();
    Vec3 getCenter();
}
