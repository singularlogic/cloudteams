//package eu.cloudteams.controller;
//
//import eu.cloudteams.util.bitbucket.BitbucketService;
//import java.io.IOException;
//import java.util.Calendar;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import java.util.stream.Collectors;
//import org.eclipse.egit.github.core.Label;
//import org.eclipse.egit.github.core.Repository;
//import org.eclipse.egit.github.core.RepositoryBranch;
//import org.eclipse.egit.github.core.RepositoryCommit;
//import org.eclipse.egit.github.core.User;
//
///**
// *
// * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
// */
//public final class BitbucketStatisticsTO {
//
//    private final BitbucketService bitbucketService;
//    private final Repository repository;
//    private List<User> collaboratorsList;
//    private List<Label> labelsList;
//    private List<RepositoryBranch> branchesList;
//    private List<RepositoryCommit> commits;
//    private CommitsStats commitsStats;
//
//    public Repository getRepository() {
//        return repository;
//    }
//
//    public List<User> getCollaborators() {
//        return collaboratorsList;
//    }
//
//    public List<RepositoryBranch> getBranches() {
//        return branchesList;
//    }
//
//    public List<RepositoryCommit> getCommits() {
//        return this.commits;
//    }
//
//    public List<Label> getLabels() {
//        return this.labelsList;
//    }
//
//    public BitbucketStatisticsTO(BitbucketService githubService, Repository repository) throws IOException {
//        this.bitbucketService = githubService;
//        this.repository = repository;
//        gatherInfo();
//    }
//
//    public void gatherInfo() throws IOException {
//        
//        branchesList = bitbucketService.getBitbucketRepositoryService().getBranches(repository);
//        labelsList = bitbucketService.getLabelService().getLabels(repository);
//        commits = bitbucketService.getCommitService().getCommits(repository);
//
//        //Code section (info for master branch)
//
//        labelsList = bitbucketService.getLabelService().getLabels(repository);
//        collaboratorsList = bitbucketService.getCollaboratorService().getCollaborators(repository);
//
//        //Pulse section
//        setLatMonthCommitsStats();
//
//    }
//
//    private void setLatMonthCommitsStats() {
//
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DAY_OF_WEEK, -7);
//        //final List<RepositoryCommit> lastMonthcommits = commits.stream().filter(commit -> commit.getCommit().getCommitter().getDate().getTime() > cal.getTime().getTime())  .collect(Collectors.toList());
//
//        List<String> commitsSHA = commits.stream().filter(commit -> commit.getCommit().getCommitter().getDate().getTime() > cal.getTime().getTime()).map(commit -> commit.getSha()).collect(Collectors.toList());
//
//        Set<String> contributors = new HashSet<>();
//
//        int totalAdditions = 0;
//        int totalDeletions = 0;
//
//        //Get latest commits
//        for (String commitSHA : commitsSHA) {
//            try {
//
//                RepositoryCommit tmpRepositoryCommit = bitbucketService.getCommitService().getCommit(repository, commitSHA);
//                contributors.add(tmpRepositoryCommit.getCommitter().getLogin());
//                totalAdditions += tmpRepositoryCommit.getStats().getAdditions();
//                totalDeletions += tmpRepositoryCommit.getStats().getDeletions();
//            } catch (IOException ex) {
//                Logger.getLogger(BitbucketStatisticsTO.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
////Set commitsStats
//        this.commitsStats = new CommitsStats(totalAdditions, totalDeletions, commitsSHA.size(), contributors);
//    }
//
//    public CommitsStats getCommitsStats() {
//        return this.commitsStats;
//    }
//
//}
//
//class CommitsStats {
//
//    private final int totalAdditions;
//    private final int totalDeletions;
//    private final int totalCommits;
//    private final Set<String> contributors;
//
//    public CommitsStats(int totalAdditions, int totalDeletions, int totalCommits, Set<String> contributors) {
//        this.totalAdditions = totalAdditions;
//        this.totalDeletions = totalDeletions;
//        this.totalCommits = totalCommits;
//        this.contributors = contributors;
//
//    }
//
//    public int getTotalAdditions() {
//        return this.totalAdditions;
//    }
//
//    public int getTotalDeletions() {
//        return this.totalDeletions;
//    }
//
//    public int getTotalCommits() {
//        return this.totalCommits;
//    }
//
//    public int getTotalChanges() {
//        return this.totalAdditions + this.totalDeletions;
//    }
//
//    public int getContributors() {
//        return this.contributors.size();
//    }
//
//    public String getContributorsNames() {
//        return this.contributors.stream().collect(Collectors.joining(" "));
//    }
//}
