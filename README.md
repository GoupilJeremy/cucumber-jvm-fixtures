cucumber-jvm-fixtures
=====================


Cette librairie ajoute des fonctionalités à Cucumber-JVM, d'intégration avec des librairies tierces, comme elasticSearch, base de données, mail etc....

# transformers vers une Datatable Cucumber-JVM

## Intégration avec une base de données

pour transformer le contenu d'une table en une datatable:
List<Map<String, Object>> query
Datatable datatable
 Class<? extends IBaseColumnToTable> baseColumnToTable

DatatableFromDataBaseComparator.from(datatable,query,baseColumnToTable).toDataTable();

pour comparer une datatable attendue, avec le contenu d'une table de base de données:

DatatableFromDataBaseComparator.from(table, query, FromXToDatatableEnum.class).compare();

Comme pour tout datatable, quand une différence sera présente, une exception sera levée; le runner Cucumber affichera les différences
en inspectant l'exception.

## Intégration avec des mails


## Intégration avec des fichiers texte


# utilitaires pour un scénario

## Communication inter-steps dans un contexte spring

## Intégrer des variables dans les datatables cucumber

## Transformation d'objets en datatables pour comparaison (diff)
# Intégration avec ElasticSearch

