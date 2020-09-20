# Continuous Integration Setup

## Actors
Ensure that accounts are set up and configured with the following providers

### Image Registry - DockerHub
-  Create an account on dockerhub.  Go to `Account-settings/Security` and add an access token.

-  Note your account username and this access token as they will need to be added to CircleCI's tool-context.
 
### CI Server - CircleCI
-  The CI workflow is defined in .circleci/config.yml.  

#### Context Setup
-  Create a context called `tools-context` under the CircleCI pipeline and add the following variables:
`DOCKER_USER: ` username of dockerhub account
`DOCKER_PASS:`  access key of dockerhub account 
`BB_PASS`:      password of bitbucket user account (aengus_mccullough) that is used for CI robot

#### Configuring Push Access to Bitbucket
Not at all straighforward.  TLDR -> create a user key from project settings in CircleCI, 
hack Chrome devtools to grab the ssh key and then upload this to bitbucket under this repo.

<https://circleci.com/docs/2.0/gh-bb-integration/#creating-a-bitbucket-user-key>

<https://support.circleci.com/hc/en-us/articles/360018860473-How-to-push-a-commit-back-to-the-same-repository-as-part-of-the-CircleCI-job>


[^2 "An attempt was made to use gitlab but ran 
into a lot of problems with allowing
gitlab CI to push tags back into the source repository" ].


### VCS - Bitbucket
1.  Special config is described above to allow CI push access to this repo
[^2 "attempted to use Gitlab but ran into the aforementioned issue" ].

2.  Additionally - read/write access to the `crunch-ski-environments` repository is required.  Access
method in this case is username / password.  Create a user on bitbucket and ensure the username is entered in 
`app.envrepo.username`.  Copy the password to CI's tools-context as `BB_PASS`

3.  Additionally - read access to the `crunch-ski-infrastructure` repository is required.  Access method in this case
is SSH.  
- Create a new key on your client:  ` ssh-keygen -m PEM -t rsa -C "aengusmccullough@hotmail.com"` and follow the prompts.
  Note that PEM format is required by circleCI.  Leave pw blank
- In Bitbucket navigate to the `infrastructure` project -> repository settings -> access keys
- paste the public key of the new key you just created:  `cat bb_inf_ro.pub` and label it `Infra Read Key` in the UI
  
 *Note that I had to configure my client to ensure that it used the correct key when connecting to bitbucket
 - `vi ~/.ssh/config` add the following entry:
 
    ```bash
     Host bitbucket.org
     User git
     IdentityFile /home/aengus/.ssh/bb_inf_ro
   ```

- `ssh-add ~/.ssh/bb_inf_ro`
- `ssh-add -l`


In order to use this SSH key from circleCI (i.e. when using in services CD PIPEline) will need to:
- add this key to circleCI https://circleci.com/docs/2.0/add-ssh-key/:
  - `project settings -> ssh keys -> additional ssh keys -> add`
  - paste private key in here. use bitbucket.org as hostname
  - add a step to the CD script (using the fingerprint shown in CI console)
   
     ```bash
      - add_ssh_keys:
             fingerprints:
               - "SO:ME:FIN:G:ER:PR:IN:T"
     ```


### Package Registry - Gitlab
Using the free Gitlab package registry.

- Make sure that repo pointed to in `build.gradle` points to your package repo
`publishing / repositories / maven / url`

- Create a personal access token in gitlab: <https://gitlab.com/profile/personal_access_tokens>

- Add this token to CircleCI tools context as `GITLAB_ACCESS_TOKEN`

 ### Static Code Analysis - Codacy
 
- Go to repository in Codacy and `settings/ integrations/ add integration`
- copy the Project API token into CIRCLE_CI `tools-context` as `CODACY_PROJECT_TOKEN`