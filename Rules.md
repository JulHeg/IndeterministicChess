# Präambel

Beim normalen Schach steht immer eine Figur alleine auf einem Feld. Beim nicht deterministischen Schach kann sich eine Figur auf mehre Felder verteilen, die sie sich auch mit anderen Figuren teilen kann. Diese Verteilung ist dabei eine Wahrscheinlichkeitsverteilung, eine Dame kann also etwa mit einer 10°-Wahrscheinlichkeit auf dem Feld A4, mit einer 60%-Wahrscheinlichkeit auf B5 und mit einer 30%-Wahrscheinlichkeit schon geschlagen sein.
Das wird dann etwa unter anderem so ausgedrückt, dass sich auf A4 eine 10%-Dame befindet.

# Regeln

## 1. Figurregeln

1.1. In einem Zug kann ein Teil einer Figur in einer beliebigen Größe (1% - 99%) abgespalten werden.

1.2. Der abgespaltene Teil, soweit existent, muss entsprechend der Zugregeln bewegt werden.

1.3. Auch andere Instanzen des desselben Figurtyps (auch die "Restfigur", bzw. die nicht gespaltene Figur) dürfen entsprechend der Zugregeln bewegt werden.


## 2. Zugregeln

2.1. In einem Zug darf maximal ein Figurtyp bewegt werden.

2.2. Es dürfen nur Figuren mit maximal 100% Gesamtwahrscheinlichkeit bewegt werden, es gilt **kein** Zugzwang.

2.3. Es dürfen maximal zwei Figurinstanzen bewegt werden.

2.4. Wenn sich eine Figur teilt können sich ihre Teile wieder zusammensetzen, indem sie auf dasselbe Feld ziehen. Haben die beiden Teile die Wahrscheinlichkeiten _m_ und _n_, so hat die entstehende Figur die Wahrscheinlicheit _m+n_ zu existieren. Auch mehr als zwei Figuren können isch über eine solche Addition kombinieren.

2.5. Wenn zwei Figuren mit den Wahrscheinlichkeiten _m_ und _n_ des gleichen Types und der gleichen Farbe auf ein Feld rücken, aber nicht von derselben Anfangsfigur stammen, so werden diese zu einer Figur mit der Wahrscheinlichkeit `100%-(100%-m)*(100%-n)` kombiniert.

2.6. Die Summe aller Wahrscheinlichkeiten von Figuren einer Farbe auf einem Feld darf 100% nicht übersteigen.

2.7. Sollte ein Bauer mit Wahrscheinlichkeit _n_ die gegnerische Grundlinie erreichen, so darf sich in eine beliebige Figur (außer einem König) mit der Existenzwahrscheinlichkeit _n_ verwandeln.

2.8. Die restlichen Bewegungsregeln werden vom normalen Schach übernommen.

## 3. Schlagregeln

3.1. Wenn eine Figur mit der Wahrscheinlichkeit _n_ auf einem Feld steht und eine andersfarbige Figur mit der Wahrscheinlichkeit _m_ daraufzieht (bzw. sie schlägt), hat Figur _n_ danach die Wahrscheinlichkeit `n*(1-m)` zu existieren, die Wahrscheinlichkeit _m_ bleibt gleich.

3.2. Sollte auf einem Feld Figur _n_, sowie ein oder mehrere andersfarbigen Figuren _m1_, _m2_, ..., _m(n-1)_ auf dem Feld stehen, so ist nach dem zusätzlichen Draufziehen der Figur mit der Wahrscheinlichkeit _m(n)_, die Wahrscheinlichkeit _n_ gleich `n*(100%-(m1 + m2 + ... + m(n)))/(100%-(m1 + m2 + ... + m(n-1)))`.

3.3. Sollten mehrere Figuren einer Farbe von einer oder mehreren Figuren angegriffen werden, so werden für die anzugreifenden Figuren die Regeln 3\.1\. bzw. 3\.2\. getrennt angewandt.

3.4. Sollte eine Figur oder ein Figurteil unter 10% Existenzwahrscheinlichkeit fallen, so wird die Figur bzw. dieser Figurteil aus dem Spiel entfernt.

3.5. Wir entschuldigen uns bei den Angehörigen der Opfer des "En passent"-Mordes wegen unserer unsensiblen Wortwahl. Dieses wird deshalb abgeschafft. Wir werden ihr Opfer nicht vergessen, dass die feindlichen Bauern, wenn schon nicht aufhalten, doch wenigstens ein Feld zur Seite verschieben konnte.

## 4. Spielende

4.1. Es gibt weder Schach noch Schachmatt.

4.2. Der König kann entsprechend den Schlagregeln geschlagen werden.

4.3. Sollte der König (bzw. alle Königsinstanzen zusammen) die Existenzwahrscheinlichkeit `n <= 100%` haben, so ist die Wahrscheinlichkeit in diesem Zug automatisch zu verlieren `100%-n`.
