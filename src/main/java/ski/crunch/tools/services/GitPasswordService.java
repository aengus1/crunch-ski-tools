package ski.crunch.tools.services;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.File;
@Primary
@Service("PasswordService")
public class GitPasswordService implements GitService {

    private final UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider;

    public GitPasswordService(@Value("${app.envrepo.username}") String userName, @Value("${app.envrepo.password}") String password) {
        System.out.println("password = " + password);
        usernamePasswordCredentialsProvider = new UsernamePasswordCredentialsProvider(userName, password);
    }


    @Override
    public void cloneRepo(String repositoryUrl, String repositoryDirectory) throws GitAPIException {
        Git.cloneRepository()
                .setURI(repositoryUrl)
                .setDirectory(new File(repositoryDirectory))
                .setCredentialsProvider(usernamePasswordCredentialsProvider)
                .call();
    }
}
