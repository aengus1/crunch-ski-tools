package ski.crunch.tools.services;

import org.springframework.stereotype.Service;
import ski.crunch.tools.model.AddEnvironmentOptions;
import ski.crunch.tools.model.EnvironmentConfig;

@Service
public class TerraformProvisioningService {
//todo -> go through entire infrastructure project and make the following changes
// 1. add individual 'sidecar' admin modules
// 2. update all stacks and modules to include the 'env' variable
// 3. properly parameterize the admin stack
// need to think about this a bit more. environments used to be organized by stage - but should now be organized by
// environment-name.  This has implications for remote-state more than anything else.  Whenever we are provisioning
// an entirely new environment is being created (regardless or not whether an env already exists for this stage) so
// should always create a new admin sidecar.

    public boolean provision(EnvironmentConfig config, AddEnvironmentOptions options) {

        // 1. Is there a NEW stage or does it exist already?  Check EnvironmentConfig.state to find out
        //  If existing, and no baseDataPath provided in Options then take a data backup from the existing environment

        //2. create a new admin module for the soon to be created environment
        // 3. switch to this stage's directory in the terraformSource dir and provision
        // 4. load data
        //5. update the env repo

        return true;
    }
}
