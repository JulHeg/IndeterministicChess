# Regeln


## Präambel

Beim normalen Schach steht immer eine Figur alleine auf einem Feld. Beim nicht deterministischen Schach kann sich eine Figur auf mehre Felder verteilen, die sie sich auch mit anderen Figuren teilen kann. Diese Verteilung ist dabei eine Wahrscheinlichkeitsverteilung, eine Dame kann also etwa mit einer `10%`-Wahrscheinlichkeit auf dem Feld A4, mit einer `60%`-Wahrscheinlichkeit auf B5 und mit einer `30%`-Wahrscheinlichkeit schon geschlagen sein.
Das wird dann etwa unter anderem so ausgedrückt, dass sich auf A4 eine `10%`-Dame befindet.

## Regeln

### 1. Figurregeln

1.1. Statt einem Zug kann ein Teil eine Figur halbiert werden, beide Teile können dann noch auseinanderbewegt werden.

1.2. Der abgespaltene Teil, soweit existent, muss entsprechend der Zugregeln bewegt werden.

1.3. Auch andere Instanzen des desselben Figurtyps (auch die "Restfigur", bzw. die nicht gespaltene Figur) dürfen entsprechend der Zugregeln bewegt werden.


### 2. Zugregeln

2.1. In einem Zug darf maximal ein Figurtyp bewegt werden.

2.2. Es dürfen maximal zwei Figurinstanzen bewegt werden.

2.3. Eine Figur mit der Wahrscheinlichkeit `p` darf ein Feld nur überqueren, wenn die Wahrscheinlichkeit, dass dort irgendeine Figur steht (Die ist gleich der Summe der Wahrscheinlichkeiten aller Figuren auf dem Feld) kleiner als `100%-p` ist.

2.4. Gleiche Figuren können ihre Teile wieder zusammensetzen, indem sie auf dasselbe Feld ziehen. Haben die beiden Teile die Wahrscheinlichkeiten `p` und `q`, so hat die entstehende Figur die Wahrscheinlichkeit `p+q` zu existieren. Auch mehr als zwei Figuren können sich über eine solche Addition kombinieren.

2.5. Die Summe aller Wahrscheinlichkeiten von Figuren einer Farbe auf einem Feld darf 100% nicht übersteigen.

2.6. Sollte ein Bauer mit Wahrscheinlichkeit `p` die gegnerische Grundlinie erreichen, so darf sich in eine beliebige Figur (außer einem König) mit der Existenzwahrscheinlichkeit `p` verwandeln.

2.7. Die restlichen Bewegungsregeln werden vom normalen Schach übernommen.

### 3. Schlagregeln

3.1. Sollte auf einem Feld eine Figur mit der Wahrscheinlichkeit `p`, sowie ein oder mehrere andersfarbigen Figuren mit Wahscheinlichkeiten `q(1)`, `q(2)`, ..., `q(n-1)` stehen, so ist nach einem zusätzlichen Draufziehen der Figur mit der anderen Farbe und Wahrscheinlichkeit `q(n)`, die Wahrscheinlichkeitder ersten Figur gleich `p'=p*(100%-q(n)/(100%-q(1) - q(2) - ... - q(n-1)))`.

3.2. Sollten mehrere Figuren einer Farbe von einer oder mehreren Figuren angegriffen werden, so werden für die anzugreifenden Figuren die Regeln 3\.1\. bzw. 3\.2\. getrennt angewandt.

3.3. Sollte eine Figur oder ein Figurteil unter `10%` Existenzwahrscheinlichkeit fallen, so wird die Figur bzw. dieser Figurteil aus dem Spiel entfernt.

3.4. Es gibt kein en-passant und keine Rochade.

### 4. Spielende

4.1. Es gibt weder Schach noch Schachmatt, der König kann entsprechend den Schlagregeln geschlagen werden.

4.2. Sollte der König (bzw. alle Königsinstanzen zusammen) die Existenzwahrscheinlichkeit `q ≤ 100%` haben, so ist die Wahrscheinlichkeit in diesem Zug automatisch zu verlieren `100%-q`.
