package eu.hbp.mip;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.GenericXmlContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericXmlContextLoader.class)
public class MIPApplicationTests {

    @Test
    public void testHello()
    {
        /*
        * NO UNIT TEST NEEDED FOR THIS COMPONENT
        */

        Assert.assertNotNull("test");
    }
}
