package compress;/**
 * Description : 
 * Created by YangZH on 16-8-19
 *  上午12:26
 */

import org.easyproxy.pojo.AccessRecord;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description :
 * Created by YangZH on 16-8-19
 * 上午12:26
 */

public class CompareTest {

    @Test
    public void compareTest(){
        AccessRecord ar1 = new AccessRecord("aaa",1);
        AccessRecord ar2 = new AccessRecord("bbb",3);
        AccessRecord ar3 = new AccessRecord("bbb",2);
        List<AccessRecord> ars = new ArrayList<>();
        ars.add(ar1);
        ars.add(ar2);
        ars.add(ar3);
        System.out.println(Collections.min(ars));
    }
}
