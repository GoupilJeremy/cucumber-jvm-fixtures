package fixtures.common.database.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.ibm.icu.text.Transliterator;
import cucumber.runtime.converters.LocalizedXStreams;
import cucumber.table.DataTable;
import cucumber.table.TableConverter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class FileToDatatable {
    private static final Transliterator ACCENTS_CONVERTER = Transliterator.getInstance("NFD;[:M:]Remove;NFC;");

    /**
     * Lit un fichier sp�cifiquement format� (cf exemple) et le transforme en datatable.<br/>
     * Tous les cas ne sont pas g�r�s dans cette m�thode. A enrichir <br/>
     * exemple de la forme du tableau � mettre dans le fichier : <br/>
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
        Assert.hasText(api, "pas d'application de d�fini");
        Assert.hasText(data, "pas de fichier de d�fini");
        // on normlise le nom (enl�ve les accents, les quotes et remplace les espaces par des _
        String normalizedData = ACCENTS_CONVERTER.transform(data).replace(" ", "_").replace("'", "_");
        // on cr�e le chemin suppos�
        String path = api + "/insert/" + normalizedData + ".txt";
        ClassPathResource classPathResource = new ClassPathResource(path);
        // on lit le fichier
        List<String> rows = Files.readLines(classPathResource.getFile(), Charsets.UTF_8);
        // on initiliase un split et on trim les r�sultats (on n'exclut pas les champs vide)
        Splitter splitter = Splitter.on("|").trimResults();
        // cr�ation de la liste de liste n�cessaire pour cr�er la datatable
        List<List<String>> rowsForDatatable = new ArrayList<List<String>>();
        for (String row : rows) {
            // si la ligne n'a pas de caract�re, on ne la prend pas en compte
            if (StringUtils.hasText(row)) {
                // on splite sur les | la ligne et on retire le premier et le dernier champs car ceux-ci sont toujours
                // vide � cause du splitte.
                // example : |id|nom| => apr�s le split => "", "id", "nom", ""
                Iterable<String> split = splitter.split(row.trim());
                LinkedList<String> strings = Lists.newLinkedList(split);
                List<String> list = strings.subList(1, strings.size() - 1);
                rowsForDatatable.add(list);
            }
        }
        // on passe par un objet cucumber pour g�n�rer la datable � partir d'une liste de liste
        TableConverter tableConverter = new TableConverter(getXStream(), null);
        return tableConverter.toTable(rowsForDatatable);
    }

    private static LocalizedXStreams.LocalizedXStream getXStream() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        LocalizedXStreams.LocalizedXStream xStream = new LocalizedXStreams(classLoader).get(Locale.getDefault());
        return xStream;
    }
}
