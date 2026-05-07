# Candle Business

Spring Boot projektas mikro, žvakių ir muilo gamybos bei pardavimo įmonei.

## Projekto paskirtis
Sistema skirta valdyti:
- produktus ir ingredientus;
- tiekėjus, jų adresus ir sutartis;
- klientų užsakymus;
- darbuotojo gamybos veiksmus;
- buhalterio išlaidas;
- vadovo suvestinę.

## Technologijos
- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Thymeleaf
- MySQL
- Maven

## Svarbi pastaba
Šioje sistemoje nėra autentifikacijos ir autorizacijos.
Rolių pasirinkimas atliekamas per pradinį puslapį ir atskiras zonas:
- `vadovas`
- `darbuotojas`
- `buhalteris`
- `klientas`

Tai palikta sąmoningai, nes projektas tik mokymosi tikslams.

## Pagrindiniai puslapiai
- `http://localhost:8080/` - pradinis puslapis
- `http://localhost:8080/vadovas` - vadovo zona
- `http://localhost:8080/darbuotojas` - darbuotojo zona
- `http://localhost:8080/buhalteris` - buhalterio zona
- `http://localhost:8080/klientas` - kliento zona

## Funkcionalumas pagal rolę
### Vadovas
Vadovo puslapyje rodoma suvestinė:
- produktų kiekis;
- ingredientų kiekis;
- klientų kiekis;
- darbuotojų kiekis;
- bendra apyvarta;
- bendros išlaidos;
- likutis.

Papildomai vadovas gali atlikti CRUD veiksmus:
- kurti produktą;
- redaguoti produktą;
- trinti produktą, jei jis dar nenaudotas užsakymuose;
- kurti ingredientą;
- redaguoti ingredientą;
- trinti ingredientą;
- kurti tiekėją;
- redaguoti tiekėją;
- trinti tiekėją, jei jis nenaudojamas ingredientuose;
- kurti adresą;
- redaguoti adresą;
- trinti adresą, jei jis nenaudojamas tiekėjuose;
- kurti sutartį;
- redaguoti sutartį;
- trinti sutartį, jei ji nenaudojama tiekėjuose.

Produkte galima nurodyti:
- pavadinimą;
- aprašymą;
- kainą;
- kiekį sandėlyje;
- gamybos tipą;
- ar gaminama pagal užsakymą;
- priskirtus ingredientus.

### Klientas
Kliento puslapyje galima:
- pasirinkti klientą;
- pasirinkti užsakymo tipą;
- įvesti kiekius prie produktų;
- sukurti naują užsakymą;
- redaguoti užsakymą;
- atšaukti užsakymą;
- ištrinti užsakymą;
- filtruoti užsakymus pagal būseną;
- atsiimti ir apmokėti užsakymą.

Palaikomi užsakymo tipai:
- `PIRKIMAS_IS_SANDELIO`
- `GAMYBA_PAGAL_UZSAKYMA`

Svarbi logika:
- jei užsakymas yra `PIRKIMAS_IS_SANDELIO`, kuriant ir redaguojant tikrinamas produkto likutis sandėlyje;
- sandėlio užsakymas iš karto laikomas paruoštu;
- klientas gali spausti `Atsiimti / apmokėti`, jei užsakymas yra paruoštas arba jei jis yra iš sandėlio;
- apmokėjus sandėlio užsakymą, produkto kiekis sandėlyje sumažinamas.

### Darbuotojas
Darbuotojo puslapyje galima:
- pasirinkti darbuotoją;
- filtruoti užsakymus pagal būseną;
- peržiūrėti užsakymų korteles;
- spausti `Gaminti`;
- spausti `Atšaukti`.

Svarbi logika:
- `Gaminti` veikia tik užsakymams su tipu `GAMYBA_PAGAL_UZSAKYMA`;
- gamyba sukuria gamybos įrašus kiekvienai užsakymo eilutei;
- užsakymas po gamybos pažymimas kaip `PARUOSTAS`;
- darbuotojas gali palikti pastabą gamybos veiksmui.

### Buhalteris
Buhalterio puslapyje galima:
- pasirinkti buhalterį;
- kurti išlaidas;
- redaguoti išlaidas;
- trinti išlaidas;
- matyti visų išlaidų sąrašą.

Palaikomos kategorijos:
- `INGREDIENTAI`
- `IRANGA`
- `PAKAVIMAS`
- `TRANSPORTAS`
- `KITA`

## Produktai ir ingredientai
### Ingredientai
Ingredientas turi tipą:
- `NATURALUS`
- `PRAMONINIS`

Papildomai ingredientui galima priskirti tiekėją.

### Produktai
Produktas turi:
- gamybos tipą: `RANKU_DARBO` arba `MASINE_GAMYBA`;
- požymį `customMade`;
- sandėlio kiekį `stockQuantity`.

Produktai ir ingredientai susieti `ManyToMany` ryšiu.

### Tiekėjai
Tiekėjas turi:
- pavadinimą;
- adresą;
- sutartį.

### Adresai
Adresas turi:
- šalį;
- gatvę;
- namo numerį;
- patalpos numerį, jei yra.

### Sutartys
Sutartis turi:
- unikalų numerį;
- tekstą.

Papildomi ryšiai:
- `Ingredient -> Supplier` yra `ManyToOne`;
- `Supplier -> Address` yra `ManyToOne`;
- `Supplier -> Contract` yra `ManyToOne`.

## Užsakymų būsenos
Naudojamos būsenos:
- `NAUJAS`
- `VYKDOMAS`
- `PARUOSTAS`
- `UZBAIGTAS`
- `ATSAUKTAS`

Bendra logika:
- pagal užsakymą gaminami produktai pradžioje turi būseną `NAUJAS`;
- sandėlio užsakymas pradžioje laikomas `PARUOSTAS`;
- darbuotojas gali pagaminti užsakymą ir taip pakeisti jį į `PARUOSTAS`;
- klientas gali užbaigti užsakymą per `Atsiimti / apmokėti`;
- užbaigtas užsakymas gauna būseną `UZBAIGTAS`.

## REST API
### Užsakymai
- `POST /api/orders`
- `GET /api/orders/{orderId}`
- `GET /api/orders?clientId={id}`

### Gamybos įrašai
- `POST /api/production-tasks`
- `GET /api/production-tasks?employeeId={id}`
- `GET /api/production-tasks?clientId={id}`

### Išlaidos
- `POST /api/expenses`
- `GET /api/expenses?financierId={id}`
- `GET /api/expenses?start=YYYY-MM-DD&end=YYYY-MM-DD`

## Demo duomenys
Pirmo paleidimo metu, jei duomenų bazė tuščia, sistema sukuria demo duomenis:
- 1 vadovą;
- 1 darbuotoją;
- 1 buhalterį;
- 2 klientus;
- adresus;
- sutartis;
- tiekėjus;
- ingredientus;
- kelis produktus;
- užsakymus;
- gamybos įrašą;
- išlaidų įrašus.

Demo naudotojų `username`:
- `asta.vadove`
- `mantas.darbuotojas`
- `ruta.finansai`
- `egle.kliente`
- `jonas.klientas`

## Paleidimas
### Reikalavimai
Reikia turėti:
- Java 21
- MySQL
- Maven arba naudoti `mvnw`

### Duomenų bazė
Pagal nutylėjimą naudojama:
- DB: `candle_business`
- URL: `jdbc:mysql://localhost:3306/candle_business`

Galima naudoti aplinkos kintamuosius:
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

Nustatymai yra faile:
`src/main/resources/application.properties`

### Programos paleidimas
```powershell
./mvnw spring-boot:run
```

### Testai
```powershell
./mvnw test
```

## Kas šiuo metu veikia
- pradinis puslapis su rolių pasirinkimu;
- vadovo dashboard;
- produkto CRUD;
- ingrediento CRUD;
- tiekėjo CRUD;
- adreso CRUD;
- sutarties CRUD;
- kliento užsakymo CRUD;
- užsakymų filtravimas pagal būseną;
- darbuotojo kortelių veiksmai `Gaminti` ir `Atšaukti`;
- kliento veiksmas `Atsiimti / apmokėti`;
- buhalterio išlaidų CRUD;
- demo duomenų užpildymas;
- MVC testai pagrindiniams puslapiams.

## Ribojimai
- nėra autentifikacijos ir autorizacijos;
- nėra atskiro naudotojų CRUD;
- nėra sudėtingesnės sandėlio rezervacijos logikos;
