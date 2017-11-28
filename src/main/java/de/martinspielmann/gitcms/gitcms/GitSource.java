package de.martinspielmann.gitcms.gitcms;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("source.git")
@EnableScheduling
public class GitSource implements InitializingBean {

	private static final Logger LOG = LoggerFactory.getLogger(GitSource.class);

	private String absolutePath;
	private String url;
	private Git git;
	private Path checkoutDir;

	@Override
	public void afterPropertiesSet() throws InvalidRemoteException, TransportException, GitAPIException, IOException {
		Path path;
		if (checkoutDir != null) {
			path = Files.createTempDirectory(checkoutDir, "gitcms");
		}else {
			path = Files.createTempDirectory("gitcms");
		}
		absolutePath = path.toAbsolutePath().toString();
		git = Git.cloneRepository().setURI(url).setDirectory(path.toFile()).call();
	}

	/**
	 * Refresh git backend by pulling. <strong>default:</strong> every minute
	 */
	@Scheduled(cron = "${source.git.cron}")
	protected void refresh() {
		try {
			LOG.debug("Refresh git");
			git.pull().call();
		} catch (GitAPIException e) {
			LOG.error("Error refreshing ", e);
		}
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	protected Path getCheckoutDir() {
		return checkoutDir;
	}

	protected void setCheckoutDir(Path checkoutDir) {
		this.checkoutDir = checkoutDir;
	}

}
