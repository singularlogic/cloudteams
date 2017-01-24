package eu.cloudteams.repository.service;

import eu.cloudteams.repository.dao.ProjectRepository;
import java.util.logging.Logger;

import eu.cloudteams.repository.domain.PaaSportProject;
import eu.cloudteams.repository.domain.PaaSportUser;
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

    public boolean store(PaaSportProject project) {
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

    public PaaSportProject findOne(long id) {
        return projectRepository.findOne(id);
    }

    public PaaSportProject findByProjectIdAndUser(long project_id, PaaSportUser user) {
        return projectRepository.findByProjectIdAndUser(project_id, user);
    }

    public long deleteByProjectIdAndUser(long project_id, PaaSportUser user) {
        return projectRepository.deleteByProjectIdAndUser(project_id, user);
    }

}
