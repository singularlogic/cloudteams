package eu.cloudteams.repository.service;

import eu.cloudteams.main.Application;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Christos Paraskeva
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
public class CloudteamsServiceTest {

    @Autowired
    ProjectService projectService;

    @Test
    @Ignore
    public void testFindByID() {
        projectService.findOne(1);
    }

}
