package fixtures.common.database.utils;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.ibm.icu.text.Transliterator;
import cucumber.api.DataTable;
import cucumber.runtime.table.TableConverter;
import cucumber.runtime.xstream.LocalizedXStreams;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class FileToDatatable {
    private static final Transliterator ACCENTS_CONVERTER = Transliterator.getInstance("NFD;[:M:]Remove;NFC;");

    private FileToDatatable() {
    }

    /**
     * Lit un fichier spécifiquement formaté (cf exemple) et le transforme en datatable.<br/>
     * Tous les cas ne sont pas gérés dans cette méthode. A enrichir <br/>
     * exemple de la forme du tableau à mettre dans le fichier : <br/>
     * <br/>
     * | id  | indice | ordre | nom du fichier                  | type de l'image |<br/>
     * | 134 | 1      | 3     | lololo-a.jpg                    | PHOTO           |<br/>
     * | 134 | 2      | 2     | lulu-1-a.jpg                    | PHOTO           |<br/>
     * | 134 | 3      | 1     | http://lili.com/1234-01-1-a.jpg | PHOTO           |<br/>
     * | 134 | 4      | 1     | mein-logo.gif-!                 | LOGO            |<br/>
     * | 136 | 1      | 3     | http://photo.fr/1234-01-1-a.jpg | PHOTO           |<br/>
     * <br/>
     *
     * @throws java.io.IOException
     */
    public static DataTable convert(final String api, final String data) throws IOException {
        Assert.hasText(api, "pas d'application de défini");
        Assert.hasText(data, "pas de fichier de défini");
        // on normalise le nom (enlève les accents, les quotes et remplace les espaces par des _
        String normalizedData = ACCENTS_CONVERTER.transform(data).replace(" ", "_").replace("'", "_").toLowerCase();
        // on crée le chemin supposé
        String path = api + "/insert/" + normalizedData + ".txt";
        ClassPathResource classPathResource = new ClassPathResource(path);
        // on lit le fichier
        List<String> rows = Files.readLines(classPathResource.getFile(), Charsets.UTF_8);
        // on initiliase un split et on trim les résultats (on n'exclut pas les champs vide)
        Splitter splitter = Splitter.on("|").trimResults();
        // création de la liste de liste nécessaire pour créer la datatable
        List<List<String>> rowsForDatatable = new ArrayList<List<String>>();
        for (String row : rows) {
            // si la ligne n'a pas de caractère, on ne la prend pas en compte
            if (StringUtils.hasText(row)) {
                // on splite sur les | la ligne et on retire le premier et le dernier champs car ceux-ci sont toujours
                // vide à cause du splitte.
                // example : |id|nom| => après le split => "", "id", "nom", ""
                Iterable<String> split = splitter.split(row.trim());
                LinkedList<String> strings = Lists.newLinkedList(split);
                List<String> list = strings.subList(1, strings.size() - 1);
                rowsForDatatable.add(list);
            }
        }
        // on passe par un objet cucumber pour générer la datatable à partir d'une liste de liste
        TableConverter tableConverter = new TableConverter(getXStream(), null);
        return tableConverter.toTable(rowsForDatatable);
    }

    private static LocalizedXStreams.LocalizedXStream getXStream() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return new LocalizedXStreams(classLoader).get(Locale.getDefault());
    }
}
