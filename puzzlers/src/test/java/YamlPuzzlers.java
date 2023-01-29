import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

public class YamlPuzzlers {


    /**
     * 1. {countries=[DE, NO, RU, US]}
     * 2. [DE, NO, RU, US]
     * 3. YamlParsingException
     * 4. Something else
     * <p>
     * ANSWER: {countries=[DE, false, RU, US]}
     * <p>
     * Yaml pattern for boolean values:
     * y|Y|yes|Yes|YES|n|N|no|No|NO
     * |true|True|TRUE|false|False|FALSE
     * |on|On|ON|off|Off|OFF
     * <p>
     * That problem called "The Norway Problem"
     * <p>
     */
    @Test
    public void countries() {
        var yaml = """
                countries:
                  - DE
                  - NO
                  - RU
                  - US
                """;

        var countries = new Yaml().load(yaml);
        System.out.println(countries);
    }

    /**
     * 1. {port_mapping=[1342, 80:80, 9000:3000]}
     * 2. {port_mapping=[1, 1, 3]}
     * 3. {port_mapping=[22:22, 80:80, 9000:3000]}
     * 4. Something else
     * <p>
     * ANSWER:
     * <p>
     * if your string literal consists of a series of one or two-digit numbers delimited by colons and all numbers
     * but the first are between 0 and 59, it would be interpreted as a sexagesimal number
     */
    @Test
    public void portMappers() {
        var yaml = """
                port_mapping:
                  - 22:22
                  - 80:80
                  - 9000:3000
                """;

        var ports = new Yaml().load(yaml);
        System.out.println(ports);
    }
}
