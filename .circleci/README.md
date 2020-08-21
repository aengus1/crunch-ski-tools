# Continuous Integration Setup

## Actors
Ensure that accounts are set up and configured with the following providers


### Image Registry - DockerHub
- Create an account on dockerhub.  Go to `Account-settings/Security` and add an access token.

- Note your account username and this access token as they will need to be added to CircleCI's tool-context.
 

### CI Server - CircleCI
- The CI workflow is defined in .circleci/config.yml.  

#### Context Setup
- Create a context called `tools-context` under the CircleCI pipeline and add the following variables:
`DOCKER_USER: ` username of dockerhub account
`DOCKER_PASS:`  access key of dockerhub account 

#### Configuring Push Access to Bitbucket
Not at all straighforward.  TLDR -> create a user key from project settings in CircleCI, 
hack Chrome devtools to grab the ssh key and then upload this to bitbucket under this repo.

https://circleci.com/docs/2.0/gh-bb-integration/#creating-a-bitbucket-user-key

https://support.circleci.com/hc/en-us/articles/360018860473-How-to-push-a-commit-back-to-the-same-repository-as-part-of-the-CircleCI-job

[^2 "An attempt was made to use gitlab but ran 
into a lot of problems with allowing
gitlab CI to push tags back into the source repository" ].

### VCS - Bitbucket
The only special config is described above
[^2 "attempted to use Gitlab but ran into the aforementioned issue" ].

### Package Registry - Gitlab
Using the free Gitlab package registry.

- Make sure that repo pointed to in `build.gradle` points to your package repo
`publishing / repositories / maven / url`

- Create a personal access token in gitlab: https://gitlab.com/profile/personal_access_tokens

- Add this token to CircleCI tools context as `GITLAB_ACCESS_TOKEN`


 