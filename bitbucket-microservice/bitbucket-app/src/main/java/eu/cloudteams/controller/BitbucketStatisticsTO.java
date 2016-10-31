package eu.cloudteams.controller;

import eu.cloudteams.util.bitbucket.BitbucketService;
import eu.cloudteams.util.bitbucket.models.Author;
import eu.cloudteams.util.bitbucket.models.BranchResponse;
import eu.cloudteams.util.bitbucket.models.CommitResponse;
import eu.cloudteams.util.bitbucket.models.Repository;
import eu.cloudteams.util.bitbucket.models.Value3;
import eu.cloudteams.util.bitbucket.models.WatchResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public final class BitbucketStatisticsTO {

    private final BitbucketService bitbucketService;
    private final Repository repository;
//    private List<User> collaboratorsList;
//    private List<Label> labelsList;
    private BranchResponse branchesList;
    private WatchResponse watchersList;
    private CommitResponse commitsList;
    private CommitsStats commitsStats;

    public Repository getRepository() {
        return repository;
    }

    public BranchResponse getBranches() {
        return this.branchesList;
    }

    public WatchResponse getWatchers() {
        return this.watchersList;
    }

    public CommitResponse getCommits() {
        return this.commitsList;
    }

    public BitbucketStatisticsTO(BitbucketService githubService, Repository repository) throws IOException {
        this.bitbucketService = githubService;
        this.repository = repository;
        gatherInfo();
    }

    public void gatherInfo() throws IOException {
        branchesList = bitbucketService.getBranches(repository.getLinks().getBranches().getHref()).orElse(new BranchResponse());
        watchersList = bitbucketService.getWatchers(repository.getLinks().getWatchers().getHref()).orElse(new WatchResponse());
        commitsList = bitbucketService.getCommits(repository.getLinks().getCommits().getHref()).orElse(new CommitResponse());

        //labelsList = bitbucketService.getLabelService().getLabels(repository);
        //Code section (info for master branch)
        //      labelsList = bitbucketService.getLabelService().getLabels(repository);
        //    collaboratorsList = bitbucketService.getCollaboratorService().getCollaborators(repository);
        //Pulse section
        setLatMonthCommitsStats();
    }

    private void setLatMonthCommitsStats() {

        Calendar cal = Calendar.getInstance();
        //cal.add(Calendar.DAY_OF_WEEK, -7);
        cal.add(Calendar.YEAR, -4);
        //final List<RepositoryCommit> lastMonthcommits = commits.stream().filter(commit -> commit.getCommit().getCommitter().getDate().getTime() > cal.getTime().getTime())  .collect(Collectors.toList());

        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        List<Value3> commits = commitsList.getValues().stream()
                .filter(commit -> parseTime(commit.getDate(), df) > cal.getTime().getTime())
                .collect(Collectors.toList());

        //Set commitsStats
        this.commitsStats = new CommitsStats(0, 0, commits.size(), commits.stream().map(commit -> commit.getAuthor().getRaw()).distinct().collect(Collectors.toSet()));
    }

    public CommitsStats getCommitsStats() {
        return this.commitsStats;
    }

    private long parseTime(String date, DateFormat df) {
        try {
            return df.parse(date).getTime();
        } catch (ParseException ex) {
            Logger.getLogger(BitbucketService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0L;
    }

}

class CommitsStats {

    private final int totalAdditions;
    private final int totalDeletions;
    private final int totalCommits;
    private final Set<String> contributors;

    public CommitsStats(int totalAdditions, int totalDeletions, int totalCommits, Set<String> contributors) {
        this.totalAdditions = totalAdditions;
        this.totalDeletions = totalDeletions;
        this.totalCommits = totalCommits;
        this.contributors = contributors;

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

    public int getContributors() {
        return this.contributors.size();
    }

    public String getContributorsNames() {
        return this.contributors.stream().collect(Collectors.joining(" "));
    }

}
