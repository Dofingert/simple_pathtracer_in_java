package manager;

import com.vector.Vec3;
import sceen.aabb.AABB;
import sceen.hitable.Bvh;
import sceen.hitable.Hitable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

class MortonCode implements Comparator {
    public int MortonCode;
    public int ID;

    static public MortonCode getMortonCode(Vec3 point,Vec3 Minimal,Vec3 Range,int ID)
    {
        int ret = 0;
        double []normalize = new double[3];
        int []normalizeInt = new int[3];
        for(int i = 0; i < 3 ; i ++)
        {
            normalize[i] = ((double) point.v[i] - (double) Minimal.v[i]);
            normalize[i] = normalize[i] * 1023/ Range.v[i];
            normalizeInt[i] = (int) normalize[i];
        }

        for(int i = 0; i < 9 ; i++)
        {
            ret |= (((normalizeInt[0] & 0x1) << 2)|
                    ((normalizeInt[1] & 0x1) << 1)|
                    (normalizeInt[2] & 0x1)) << 27;
            ret = ret >> 3;
            normalizeInt[0] >>= 1;
            normalizeInt[1] >>= 1;
            normalizeInt[2] >>= 1;
        }
        ret |= ((normalizeInt[0] << 2)|
                (normalizeInt[1] << 1)|
                 normalizeInt[2]) << 27;

        MortonCode mortonRecord = new MortonCode();

        mortonRecord.MortonCode = ret;
        mortonRecord.ID = ID;

        return mortonRecord;
    }

    public int compare(Object oa,Object ob)
    {
        MortonCode a = (MortonCode) oa;
        MortonCode b = (MortonCode) ob;
        int ret = a.MortonCode - b.MortonCode;
        if(ret!=0)
        {
            return ret;
        }
        else
        {
            return a.ID - b.ID;
        }
    }

}

public class BVHBuilder {

    public static Bvh build(ArrayList<Hitable> hitList)
    {
        int sizeOfObjectQueue =hitList.size();

        MortonCode[] mortanList = new MortonCode[sizeOfObjectQueue];
        Hitable[] hitables = new Hitable[sizeOfObjectQueue];
        AABB[] aabbList = new AABB[sizeOfObjectQueue];
        Bvh[] bvhList = new Bvh[sizeOfObjectQueue];

        for(int i =0 ; i < sizeOfObjectQueue;i++)
        {
            hitables[i] = hitList.get(i);
        }

        Vec3 max = new Vec3(-Float.MAX_VALUE,-Float.MAX_VALUE,-Float.MAX_VALUE);
        Vec3 min = new Vec3(Float.MAX_VALUE,Float.MAX_VALUE,Float.MAX_VALUE);
        for(int i = 0 ; i < sizeOfObjectQueue; i++)
        {
            AABB singleBox = hitables[i].generateAABB();
            aabbList[i] = singleBox;
            max = Vec3.getMax(max,singleBox.maximum);
            min = Vec3.getMin(min,singleBox.minimum);
        }
        Vec3 range = Vec3.sub(max,min);
        for(int i = 0 ; i < sizeOfObjectQueue; i++)
        {
            mortanList[i] = MortonCode.getMortonCode(hitables[i].getCenter(),min,range,i);
        }
        Arrays.sort(mortanList,new MortonCode());

        int levelSize = sizeOfObjectQueue;
        int level = 0;
        while(levelSize >= 2 || level == 0)
        {
            for(int i = 0; i< levelSize - 1; i+=2)
            {
                int index1,index2;
                AABB box;
                Bvh bvh = new Bvh();
                if(level == 0)
                {
                    index1 = mortanList[i].ID;
                    index2 = mortanList[i+1].ID;
                    box = AABB.merge(aabbList[index1],aabbList[index2]);
                    bvh.left = hitables[index1];
                    bvh.right = hitables[index2];
                    bvh.aabb = box;
                }
                else
                {
                    index1 = i;
                    index2 = i + 1;
                    box = AABB.merge(bvhList[index1].aabb,bvhList[index2].aabb);
                    bvh.left = bvhList[index1];
                    bvh.right = bvhList[index2];
                    bvh.aabb = box;
                }
                bvhList[i / 2] = bvh;
            }
            if(levelSize % 2 == 1)
            {
                Bvh bvh = new Bvh();
                bvh.right = null;
                if(level == 0)
                {
                    bvh.aabb = aabbList[mortanList[levelSize - 1].ID];
                    bvh.left = hitables[mortanList[levelSize - 1].ID];
                }
                else
                {
                    bvh = bvhList[levelSize - 1];
                }
                bvhList[levelSize / 2] = bvh;
            }
            //System.out.printf("%dlevel: %dBvh\n",level,levelSize);
            levelSize = (levelSize + 1) / 2;
            level++;
            //System.out.printf("%dAflevel: %dBvh\n",level,levelSize);
        }

        return bvhList[0];
    }
}
