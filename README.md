# gitcms
A simple git based html hosting with crowd based authentication
## Usage
1. Configure your git backend. Files of the git repo will be served via HTTP.
Your application.yml might look something like this:

```
source:
  git:
    url: YOUR_GIT_URL
#   Optionally: define a cron expression to specify how often the local git repository is refreshed 
#    cron: "0 * * * * *"
#   Optionally: define a checkout directory. Default: java.io.tmpdir
#   checkoutDir: /foo/bar
```

2. Configure your crowd authentication connection.
Your crowd.properties might look something like this:

```
application.name=gitcms
application.password=gitcms
crowd.server.url=https://localhost:8095/crowd
```
3. Create a new application in crowd matching the above properties. Access restrictions can simply be configured in crowd then.
