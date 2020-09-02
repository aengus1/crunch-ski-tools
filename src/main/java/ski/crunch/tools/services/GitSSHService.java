package ski.crunch.tools.services;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.*;
import org.eclipse.jgit.util.FS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * https://github.com/centic9/jgit-cookbook
 * <p>
 * Clone repository
 * Add files to index
 * Commit
 * Push
 * <p>
 * Auth via ssh private key OR username / password.  un/pw might be preferable from CI
 */
@Service("SshService")
public class GitSSHService implements GitService {


    final SshSessionFactory sshSessionFactory;
    final TransportConfigCallback transportConfigCallback;


    public GitSSHService(@Value("${app.envrepo.pklocation}") String privateKeyLocation) {
        System.out.println("PK location = " + privateKeyLocation);
        sshSessionFactory = new JschConfigSessionFactory() {
            @Override
            protected JSch createDefaultJSch(FS fs) throws JSchException {
                JSch defaultJSch = super.createDefaultJSch(fs);
                defaultJSch.addIdentity(privateKeyLocation);
                return defaultJSch;
            }
        };

        transportConfigCallback = transport -> {
            SshTransport sshTransport = (SshTransport) transport;
            sshTransport.setSshSessionFactory(sshSessionFactory);
        };

    }

    public void cloneRepo(String repositoryUrl, String repositoryDirectory) throws GitAPIException {
        System.out.println("initialized with " + repositoryDirectory + " " + repositoryUrl);

        Git.cloneRepository()
                .setURI(repositoryUrl)
                .setDirectory(new File(repositoryDirectory))
                //.setTransportConfigCallback(transportConfigCallback)
                .setTransportConfigCallback(transportConfigCallback)
                .call();

//        LsRemoteCommand lsRemoteCommand = Git.lsRemoteRepository();
//        lsRemoteCommand.setRemote(repositoryUrl);
//        lsRemoteCommand.setTransportConfigCallback(transportConfigCallback);
//        lsRemoteCommand.call();
    }


}
