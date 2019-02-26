# Java Task Challenge

## Zu lösendes Problem

Bitte erstellen Sie eine variable Datenauswertung, die mit einer Daten- und einer Anweisungsdatei beliefert werden und eine Ausgabedatei erzeugen soll. Alle Dateien liegen in einem XML-Format vor. Der Aufbau der Dateien wird nicht durch ein Schema vorgegeben und ist nur anhand der Beispieldateien dokumentiert.

Die Eingabedaten sind in einem einfachen Format gehalten: Jede Datei enthält ein Top-Level-Element namens „data“, darunter befinden sich Einträge namens „city“, die eine variable Anzahl von Attributen und Kinder-Knoten enthalten können. In unserem Beispiel hat jedes „city“-Element zwei Attribute und einen Kinder-Knoten. Ein Attribut ist stetsvorhanden, dieses heißt „name“ und wird zur Filterung benutzt.

Die Anweisungen sind ähnlich aufgebaut, ein Top-Level-Element namens „operations“, darunter einzelne „operation“-Elemente. Die Operation-Elemente enthalten fünf Attribute:

- name - Der für die Ausgabe zu benutzende Name.
- attrib – Name des auszuwertenden Attributs oder Kinder-Knotens.
- type - Ob das gewählte Attribut ein Kinder-Knoten oder ein Attribut ist.
- func - Die Funktion, die ausgewertet werden soll, dies kann „min“, „max“, „sum“ oder „average“ sein.
- filter - Ein regulärer Ausdruck, der auf das „name“-Element der „city“ angewandt werden soll. Nur solche „city“s, die dem regulären Ausdruck entsprechen sollen in die Auswertung aufgenommen werden.

Die Ausgabe besteht ebenfalls aus einem Top-Level-Element „results“ und darunter einzelnen „result“-Einträgen. Jedes „result“ hat ein Attribut „name“, das dem Namen der entsprechenden „operation“ entnommen ist. Der Text-Inhalt jedes „result“- Elementes ist das Ergebnis der Berechnung.

Angehängt finden Sie drei Dateien, die eine solche Auswertung vornehmen. 

Programmiert werden soll eine Implementierung auf der Java VM; es dürfen alle Standard-Bibliotheken der von Ihnen verwendeten Sprache benutzt werden, aber keine weiteren. Die Dateinamen dürfen fest in das Programm eingebaut werden, ein Auslesen ist nicht erforderlich. Gleitkommazahlen sollen mit genau zwei Nachkommastellen ausgegeben werden.

## Mitgelieferte Dateien

Zum Lösen des Problems werden folgende Dateien geliefert:

- Aufgabe.pdf Dieses Dokument
- data.xml Beispielhafte Eingabe-Daten
- operations.xml Beispiel für Operationen, die berechnet werden sollen.
- output.xml Die Ergebnisse für die mitgelieferten Beispiel-Daten.

## Hinweise zur Abgabe

Das Ergebnis checken Sie dann bitte einfach wieder in diesem Repository ein. Sobald Sie Ihre finale Lösung hochgeladen haben, geben Sie bitte einen kurzen Hinweis an jobs@itdesign.de