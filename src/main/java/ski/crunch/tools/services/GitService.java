package ski.crunch.tools.services;

import org.eclipse.jgit.api.errors.GitAPIException;

public interface GitService {

    void cloneRepo(String repositoryUrl, String repositoryDirectory) throws GitAPIException;
}
