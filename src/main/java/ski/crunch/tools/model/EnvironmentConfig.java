package ski.crunch.tools.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class EnvironmentConfig {

    private String project_name;
    private String primary_region;
    private String secondary_region;

    private Map<String, Environment> environments;




    @Getter
    @Setter
     public static class Environment {
        private String domain_name;
        private String default_branch;
        private String profile;
        private boolean isCI;
        private boolean isProd;
        private String servicesVersion;
        private String infraVersion;
        private String infraBranch;
        private int user_table_read_capacity;
        private int user_table_write_capacity;
        private boolean encrypt_user_table;
        private boolean user_table_point_in_time_recovery;
        private String user_table_billing_mode;
        private int activity_table_read_capacity;
        private int activity_table_write_capacity;
        private boolean encrypt_activity_table;
        private String activity_table_billing_mode;
        private boolean activity_table_point_in_time_recovery;
        private String app_alias;
        private String cognito_sub_domain;
        private String ws_sub_domain;
        private String api_sub_domain;

    }
}
