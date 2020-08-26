package ski.crunch.tools.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@Getter
@Setter
@NoArgsConstructor
public class ConfigurationModel {

    private String projectName;
    private String profileName;
    private String dataRegion;
    private String domainName;
    private String projectSourceDir;
    private String secondaryRegion;

}
