# Continuous Integration Setup

## Actors

### CI Server - CircleCI
CircleCI is used here.  The CI workflow is defined in .circleci/config.yml.  An attempt was made to use gitlab but ran 
into a lot of problems with allowing
gitlab CI to push tags back into the source repository.

- VCS
- Package Registry
- Image Registry

 Create a CircleCI account