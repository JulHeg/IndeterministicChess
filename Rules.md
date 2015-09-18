# Regeln

## 1. Figurregeln

1.1. In einem Zug kann ein Teil einer Figur in einer beliebigen Größe (1% - 99%) abgespalten werden.

1.2. Der abgespaltene Teil, soweit existent, muss entsprechend der Zugregeln bewegt werden.

1.3. Derselbe Figurtyp (auch die "Restfigur", bzw. die nicht gespaltenene Figur) darf entsprechend der Zugregeln bewegt werden.


## 2. Zugregeln

2.1. In einem Zug darf maximal ein Figurtyp bewegt werden.

2.2. Es darf maximal 100% Gesamtwahrscheinlichkeit bewegt werden, es gilt **kein** Zugzwang.

2.3. Es dürfen maximal zwei Figurinstanzen bewegt werden.

2.4. Wenn zwei Figuren mit den Wahrscheinlichkeiten _m_ und _n_ des gleichen Types und der gleichen Farbe auf ein Feld rücken so werden diese zu einer Figur mit der Wahrscheinlichkeit `1-(1-m)*(1-n)` kombiniert.

2.5. Es darf auf einem Feld nicht mehr als 100% Figurenwahrscheinlichkeit bestehen (berechet **nach** dem Schlagen, siehe Schlagregeln).

2.6. Die restlichen Bewegungsregeln werden übernommen.

## 3. Schlagregeln

3.1. Wenn eine Figur mit der Wahrscheinlichkeit _n_ auf einem Feld steht und eine andersfarbige Figur mit der Wahrscheinlichkeit _m_ daraufzieht (bzw. sie Schlägt), hat Figur _n_ danach `n*(1-m)` Wahrscheinlichkeit und Wahrscheinlichkeit _m_ bleibt gleich.

3.2. Sollte auf einem Feld Figur _n_, sowie ein oder mehrere andersfarbigen Figuren _m1_, _m2_, ..., _m(n-1)_ auf dem Feld stehen, so ist nach dem zusätzlichen draufziehen der Figur mit der Wahrscheinlichkeit _m(n)_, die Wahrscheinlichkeit _n_ gleich `n*(1-(_m1_ + _m2_ + ... _m(n)_))`.

3.3. Sollten mehrere Figuren einer Farbe von ein oder mehreren Figuren angegriffen werden, so werden für die anzugreifenden Figuren die Regeln 3\.1\. bzw. 3\.2\. getrennt angewand.

