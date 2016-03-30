package eu.cloudteams.repository.service;

import eu.cloudteams.repository.dao.ProjectRepository;
import eu.cloudteams.repository.domain.SonarqubeProject;
import eu.cloudteams.repository.domain.SonarqubeUser;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    private static final Logger logger = Logger.getLogger(ProjectService.class.getName());

    public boolean store(SonarqubeProject project) {
        try {
            projectRepository.save(project);
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
            return false;
        }
        return project.getId() > 0;
    }

    public boolean delete(long id) {
        try {
            projectRepository.delete(id);
        } catch (Exception ex) {
            Logger.getLogger(ProjectService.class.getName()).severe(ex.getMessage());
            return false;
        }
        return true;
    }

    public SonarqubeProject findOne(long id) {
        return projectRepository.findOne(id);
    }

    public SonarqubeProject findByProjectIdAndUser(long project_id, SonarqubeUser user) {
        return projectRepository.findByProjectIdAndUser(project_id , user);
    }

}
