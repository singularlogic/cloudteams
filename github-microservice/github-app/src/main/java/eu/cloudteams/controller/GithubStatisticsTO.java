package eu.cloudteams.controller;

import eu.cloudteams.util.github.GithubService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javassist.compiler.ast.Pair;
import org.eclipse.egit.github.core.CommitStats;
import org.eclipse.egit.github.core.Label;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryBranch;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.User;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public final class GithubStatisticsTO {

    private final GithubService githubService;
    private final Repository repository;
    private List<User> collaboratorsList;
    private List<Label> labelsList;
    private List<RepositoryBranch> branchesList;
    private List<RepositoryCommit> commits;
    private CommitsStats commitsStats;

    public Repository getRepository() {
        return repository;
    }

    public List<User> getCollaborators() {
        return collaboratorsList;
    }

    public List<RepositoryBranch> getBranches() {
        return branchesList;
    }

    public List<RepositoryCommit> getCommits() {
        return this.commits;
    }

    public List<Label> getLabels() {
        return this.labelsList;
    }

    public GithubStatisticsTO(GithubService githubService, Repository repository) throws IOException {
        this.githubService = githubService;
        this.repository = repository;
        gatherInfo();
    }

    public void gatherInfo() throws IOException {

        branchesList = githubService.getGithubRepositoryService().getBranches(repository);
        labelsList = githubService.getLabelService().getLabels(repository);
        commits = githubService.getCommitService().getCommits(repository);

        //Code section (info for master branch)
        RepositoryBranch masterBranch = branchesList.stream().filter(GithubStatisticsTO::isMasterBranch).findFirst().get();

        labelsList = githubService.getLabelService().getLabels(repository);
        collaboratorsList = githubService.getCollaboratorService().getCollaborators(repository);

        //Pulse section
        setLatMonthCommitsStats();
    }

    private void setLatMonthCommitsStats() {

//        if (null == commitsStats) {
//
//            Calendar cal = Calendar.getInstance();
//            cal.add(Calendar.MONTH, -1);
//            //final List<RepositoryCommit> lastMonthcommits = commits.stream().filter(commit -> commit.getCommit().getCommitter().getDate().getTime() > cal.getTime().getTime())  .collect(Collectors.toList());
//
//            List<String> commitsSHA = commits.stream().filter(commit -> commit.getCommit().getCommitter().getDate().getTime() > cal.getTime().getTime()).map(commit -> commit.getSha()).collect(Collectors.toList());
//
//            final List<RepositoryCommit> lastMonthcommits = new ArrayList<>();
//
//            //Get latest commits
//            for (String commitSHA : commitsSHA) {
//                try {
//                    lastMonthcommits.add(githubService.getCommitService().getCommit(repository, commitSHA));
//                } catch (IOException ex) {
//                    Logger.getLogger(GithubStatisticsTO.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//            }
//
//            lastMonthcommits.parallelStream().map(repoCommit -> new Object() {
//                int additions = repoCommit.getStats().getAdditions();
//                int deletions = repoCommit.getStats().getDeletions();
//                int total = repoCommit.getStats().getTotal();
//
//            }).collect(Collectors.collectingAndThen(Collectors.groupingBy(obj -> obj.additions, Collectors.groupingBy(obj -> obj.deletions, Collectors.groupingBy(obj -> obj.total))), null));
//
//        }

//Set commitsStats
        this.commitsStats = new CommitsStats(0, 0, 0);
    }

    private static boolean isMasterBranch(RepositoryBranch repository) {
        return repository.getName().equalsIgnoreCase("master");
    }

    public CommitsStats getCommitsStats() {
        return this.commitsStats;
    }

}

class CommitsStats {

    private final int totalAdditions;
    private final int totalDeletions;
    private final int totalCommits;

    public CommitsStats(int totalAdditions, int totalDeletions, int totalCommits) {
        this.totalAdditions = totalAdditions;
        this.totalDeletions = totalDeletions;
        this.totalCommits = totalCommits;
    }

    public int getTotalAdditions() {
        return this.totalAdditions;
    }

    public int getTotalDeletions() {
        return this.totalDeletions;
    }

    public int getTotalCommits() {
        return this.totalCommits;
    }

    public int getTotalChanges() {
        return this.totalAdditions + this.totalDeletions;
    }
}
