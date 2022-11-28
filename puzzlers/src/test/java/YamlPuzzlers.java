import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

public class YamlPuzzlers {


    /**
     * 1. {countries=[DE, NO, RU, US]}
     * 2. [DE, NO, RU, US]
     * 3. YamlParsingException
     * 4. Somethung else
     * <p>
     * ANSWER: {countries=[DE, false, RU, US]}
     * <p>
     * Yaml pattern for boolean values:
     * y|Y|yes|Yes|YES|n|N|no|No|NO
     * |true|True|TRUE|false|False|FALSE
     * |on|On|ON|off|Off|OFF
     * <p>
     * <p>
     * <p>
     * <p>
     * Правильный ответ: {countries=[DE, false, RU, US]}
     * <p>
     * Проблема в том, что спецификация yaml версии 1.1 тип bool можно выразить 22мя разными способами:
     * <p>
     * y|Y|yes|Yes|YES|n|N|no|No|NO
     * |true|True|TRUE|false|False|FALSE
     * |on|On|ON|off|Off|OFF
     * <p>
     * <p>
     * Из за этого код страны Норвегии парсится как No = false. Эта проблема так и называется The Norway Problem.
     * Чтобы этого избежать нужно всегда в yaml файлах заключать текст в кавычки.
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
}
