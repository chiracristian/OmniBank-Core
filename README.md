# CHIRA Cristian-Ioan-George, grupa 322CD - Descriere rezolvare proiect etapa 1

## Structură cod

Am început prin a crea clasele `Bank`, `Account`, `Card` și `User`. Pentru simplitatea
accesării datelor, clasa `Bank` păstrează în tabele de dispersie referințe către toți
utilizatorii, toate conturile, toate aliasurile și toate cardurile. Fiecare cont îșî ține 
minte, în `ArrayList`-uri toate cardurile și toate tranzacțiile aferente acestuia. În mod
similar, fiecare utilizator este prevăzut cu `ArrayList`-uri ce păstrează conturile sale,
precum și tranzacțiile ce s-au efectuat asupra lor.

## Modalitate prelucrare comenzi

Pentru a prelucra comenzile am ales să folosesc **Command Pattern**, având o clasa
abstractă `Command`, ce are membrul `timestamp`, deoarece se regăsește la toate
comenzile, un invocator ce la construcție primește comenzile și banca asupra cărora
să aibă loc, prevăzut cu o metodă care execută toate comenzile și pune output-ul lor
într-un ArrayNode. Pentru a crea ușor comenzile concrete, reprezentate fiecare de
câte o clasă package-private, am folosit **Factory Pattern**. Toate acestea le-am
pus în pachetul `commands`

## Implementare schimburi valutare

Pentru a implementa schimburile valutare, am inclus în proiect dependența `JGraphT`, stocând
într-un graf toate monedele existente ca noduri și rata de schimb dintre ele este reprezentată
de muchiile dintre aceste noduri. La construcția instanței de `CurrencyExchanger`, în graf
am adăugat și muchii pentru conversii inverse celor date din fișierul de intrare. Pentru a
efectua o conversie, se găsește drumul cu cele mai puține muchii dintre monedele date și
valoarea de convertit este înmulțită cu valoarea dată fiecărei muchii din drumul găsit.

## Implementare tranzacții
Pentru a păstra tranzacțiile, am creat pachetul `transactions`, în care am pus clasa abstractă
`Transaction` ce are o funcție pentru afișare în format JSON și tranzacțiile concrete
ce extind clasa anterior menționată. Orice tranzacție trebuie adăuga cu metoda
`addTransaction` din `Bank`, care pune tranzacția atât în lista specifică utilizatorului,
cât și în lista corespunzătoare contului asupra căruia s-a efectuat tranzacția.

## Implementare generare de rapoarte
Pentru a genera un raport clasic, `Account` este prevăzut cu metoda `getReport`, ce preia
toate tranzacțiile în intervalul de timp cerut și le pune în format JSON.

Pentru a putea face raportul de cheltuieli, `Account` dispune de un `TreeMap` care are 
chei comercianții (aceștia vor rămâne mereu în ordine alfabetică) și ca valori câte un
vector de instanțe ale clasei interne `CommerciantPaymentData` (având în componență
sumele cheltuite și `timestamp`-ul lor). Raportul de cheltuieli este generat similar
ca și cel clasic, numai că afișează doar tranzacțiile cu cardul și la fiecare comerciant
se iterează prin plățile către acesta pentru a aduna doar plățile din intervalul dat
pentru a le afișa (doar dacă suma lor nu este zero).

## Feedback
Ca dificultate, această temă mi s-a părut mai simplă decât tema 0, nu sunt sigur dacă
datorită faptului că specificația a fost mai simplă decât la prima temă sau doar
datorită experienței.

Pentru voi, ca sugestie pentru viitoarele teme: să faceți testele specifice fiecărei
funcționalități să cuprindă toate edge case-urile corespunzătoare lor, ci nu să fie 
prevăzute doar în testele cu input mare. Spre exemplu, la această temă, puteau fi incluse
comenzi specifice contului de economii aplicate asupra conturilor clasice și în testul 13,
nu doar în testul 18.

Totodată, toată funcționalitatea prevăzută în cerință să fie testată: la această temă nu
am văzut în niciun test apelul corect al comenzii `addInterest` asupra unui cont de 
economii (care în opinia mea, ar fi avut sens să existe măcar o dată în testul 13). De
asemenea, nu am observat nici testarea raportului clasic aplicat asupra unui cont de
economii (voi ați spus în cerință în cazul ăsta să includă doar încasări de dobândă sau
schimbări ale acesteia).
