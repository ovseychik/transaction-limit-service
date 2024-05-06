# Краткое описание

Репозиторий содержит исходный код тестового задания для Solva Laboratory на позицию Junior Java Developer.

По техническому заданию необходимо было разработать сервис, который сможет получать, хранить и отдавать информацию о транзакциях (операциях) пользователей. При этом, применяются лимиты на операции.

Основная рабочая ветка: `dev` (должна открыться при переходе по ссылке на репо). Ветка `main` нужна для ревью.

# Стек

* Java 17
* Spring Boot, MVC, Data
* Mockito, JUnit
* Persistence: H2 db в режиме имитации PostgreSQL

Приложение упаковано в Docker контейнер. Как запустить, см. ниже.

Дополнительно, сервис был развёрнут на удаленном сервере. 
Можно обратиться с помощью соответствующей коллекции Postman, см. ниже. 

# Необходимое условие для запуска (ключ API)

В рамках сервиса используется стороннее API для получения курсов валют с режимом доступа по персональному API ключу.
*Ключ был передан вместе со ссылкой на репозиторий.*

При запуске приложения ключ необходимо передать как переменную окружения.

## Как использовать

### В IntelliJ (при локальной сборке проекта)

Можно указать [в `application.properties`](https://github.com/ovseychik/transaction-limit-service/blob/dev/src/main/resources/application.properties#L14), заменив `${API_KEY}`на полученный ключ. Либо передать через переменную окружения (для защиты от случайного залития в гит), для этого нужно выполнить следующее:

Выбрать в выпадающем списке конфигураций для запуска "Edit configurations..."
1. В открывшемся окне выбрать конфигурацию Spring boot
2. В меню "Manage options" выбрать "Environment variables"
3. Открыть диалоговое окно и добавить пару ключ-значение API_KEY=полученный_ключ (4)

<img width="1317" alt="Screenshot 2024-04-25 at 23 49 15" src="https://github.com/ovseychik/transaction-limit-service/assets/5370490/3c0dcad1-71b9-4039-97a0-ec17d103f020">

### В Docker

При запуске команды Docker run нужно передать как параметр `-e`:

```shell
docker pull ghcr.io/ovseychik/transaction-limit-service
docker run -d -p 9000:8080 -e API_KEY=полученный_ключ transaction-limit-service
```

# Реализованные API

Доступны в коллекции Postman, скачать [по ссылке](https://drive.google.com/drive/folders/1AOy1BAkU_CGxcK_03bDKPUECtDS-0mnJ?usp=sharing)

Варианты
* Локальный запуск: коллекция с названием local (при обращении к сервису в локалхосте) [Transactions - Local, Sergey Ovseychik.postman_collection.json](https://github.com/ovseychik/transaction-limit-service/files/15125528/Transactions.-.Local.Sergey.Ovseychik.postman_collection.json)
* Обращение к сервису на удаленном сервере (доступно в интернете): коллекция с названием remote (при желании протестировать сервис, запущенный на сервере) [Transactions - Remote, Sergey Ovseychik.postman_collection.json](https://github.com/ovseychik/transaction-limit-service/files/15125562/Transactions.-.Remote.Sergey.Ovseychik.postman_collection.json)

## Транзакции

### POST /api/transactions

Endpoint для передачи сведений о транзакции 

* Метод: `POST`
* Дополнительные параметры стартовой строки: нет
* Тело запроса: должно содержать данные об операции в формате json:

```json
{
    "accountFrom": 123456789,
    "accountTo": 987654321,
    "currencyShortName": "USD",
    "sum": 600,
    "expenseCategory": "SERVICE",
    "dateTime": "2024-04-25T23:45:30.123456+05:00"
}
```

Response при успешной обработке:

* HTTP status: 201 Created
* Тело: объект транзакции

```json
{
    "id": 5,
    "accountFrom": 123456789,
    "accountTo": 987654321,
    "currencyShortName": "USD",
    "sum": 600,
    "expenseCategory": "SERVICE",
    "dateTime": "2024-04-25T18:45:30.123456Z",
    "limitExceeded": true,
    "usdEquivalent": 600,
    "limitSum": null,
    "limitDateTime": null,
    "limitCurrencyShortName": null
}
```

### GET /api/transactions

Endpoint для получения сведений о всех транзакциях. *В ТЗ не было требований по сортировке или фильтрации транзакций, поэтому не реализовано.*

* Метод: `GET`
* Дополнительные параметры стартовой строки: нет
* Тело запроса: нет

Response при успешной обработке:

* HTTP status: 201 Created
* Тело: массив транзакций

```json
[
  {
    "id": 1,
    "accountFrom": 123456789,
    "accountTo": 987654321,
    "currencyShortName": "USD",
    "sum": 600.00,
    "expenseCategory": "SERVICE",
    "dateTime": "2024-04-25T13:45:30.123456Z",
    "limitExceeded": false,
    "usdEquivalent": 600.00,
    "limitSum": null,
    "limitDateTime": null,
    "limitCurrencyShortName": null
  },
  {
    "id": 4,
    "accountFrom": 123456789,
    "accountTo": 987654321,
    "currencyShortName": "USD",
    "sum": 600.00,
    "expenseCategory": "SERVICE",
    "dateTime": "2024-04-25T13:45:30.123456Z",
    "limitExceeded": true,
    "usdEquivalent": 600.00,
    "limitSum": 1000,
    "limitDateTime": "2024-04-01T00:00:00Z",
    "limitCurrencyShortName": "USD"
  }
]
```

### GET /api/transactions/{id}

Endpoint для получения сведений о конкретной транзакции по её id.

* Метод: `GET`
* Дополнительные параметры стартовой строки: нет
* Тело запроса: нет

Response при успешной обработке:

* HTTP status: 200 ok
* Тело: объект транзакции

### GET /api/transactions/exceeded

Endpoint для получения сведений о всех транзакциях, по которым превышен лимит.

* Метод: `POST`
* Дополнительные параметры стартовой строки: нет
* Тело запроса: нет

Response при успешной обработке:

* HTTP status: 201 Created
* Тело: массив транзакций, которые превысили лимит

```json
[
    {
        "id": 4,
        "accountFrom": 123456789,
        "accountTo": 987654321,
        "currencyShortName": "USD",
        "sum": 600.00,
        "expenseCategory": "SERVICE",
        "dateTime": "2024-04-25T13:45:30.123456Z",
        "limitExceeded": true,
        "usdEquivalent": 600.00,
        "limitSum": 1000,
        "limitDateTime": "2024-04-01T00:00:00Z",
        "limitCurrencyShortName": "USD"
    },
    {
        "id": 5,
        "accountFrom": 123456789,
        "accountTo": 987654321,
        "currencyShortName": "USD",
        "sum": 600.00,
        "expenseCategory": "SERVICE",
        "dateTime": "2024-04-25T13:45:30.123456Z",
        "limitExceeded": true,
        "usdEquivalent": 600.00,
        "limitSum": 1000,
        "limitDateTime": "2024-04-01T00:00:00Z",
        "limitCurrencyShortName": "USD"
    }
]
```
## Лимиты

### POST /api/limits

Endpoint для передачи сведений о лимите. *Предполагается, что лимит в USD.*

* Метод: `POST`
* Дополнительные параметры стартовой строки: нет
* Тело запроса: должно содержать данные о лимите в формате json:

```json
{
    "sum": 2000,
    "expenseCategory": "SERVICE"
}
```

Response при успешной обработке:

* HTTP status: 200 ok
* Тело: объект нового установленного лимита

```json
{
    "id": 1,
    "sum": 2000,
    "datetime": "2024-04-25T22:53:37.276872+05:00",
    "currencyShortName": "USD",
    "expenseCategory": "SERVICE"
}
```

### GET /api/limits

Endpoint для получения исторических сведений о лимитах.

* Метод: `GET`
* Дополнительные параметры стартовой строки: нет
* Тело запроса: нет

Response при успешной обработке:

* HTTP status: 200 ok
* Тело: объект нового установленного лимита

```json
[
  {
    "id": 1,
    "sum": 2000.00,
    "datetime": "2024-04-25T14:20:25.923253Z",
    "currencyShortName": "USD",
    "expenseCategory": "SERVICE"
  },
  {
    "id": 2,
    "sum": 2000.00,
    "datetime": "2024-04-25T14:20:26.281832Z",
    "currencyShortName": "USD",
    "expenseCategory": "SERVICE"
  }
]
```

## Курсы валют

### POST /api/forex

Endpoint для получения сведений о курсах валют за заданный период и сохранения в БД.

* Метод: `POST`
* Дополнительные параметры стартовой строки: две даты, интервал от и до включительно
  * from=yyyy-MM-dd
  * to=yyyy-MM-dd
* Тело запроса: нет

Пример полностью заполненной стартовой строки: `{baseUrl}/api/forex?from=2024-04-24&to=2024-04-24`

Response при успешной обработке:

* HTTP status: 200 ok
* Тело: массив полученных курсов валют

```json
[
    {
        "id": 1,
        "currencyCode": "EUR",
        "rate": 0.9344601722,
        "date": "2024-04-24"
    },
    {
        "id": 2,
        "currencyCode": "KZT",
        "rate": 441.9698939804,
        "date": "2024-04-24"
    },
    {
        "id": 3,
        "currencyCode": "RUB",
        "rate": 92.2888099857,
        "date": "2024-04-24"
    },
    {
        "id": 4,
        "currencyCode": "USD",
        "rate": 1.0,
        "date": "2024-04-24"
    }
]
```
### GET /api/forex

Endpoint для получения сведений о всех курсах валют, *сохраненных в БД*.

* Метод: `POST`
* Дополнительные параметры стартовой строки: две даты, интервал от и до включительно
    * from=yyyy-MM-dd
    * to=yyyy-MM-dd
* Тело запроса: нет

Пример полностью заполненной стартовой строки: `{baseUrl}/api/forex?from=2024-04-24&to=2024-04-24`

Response при успешной обработке:

* HTTP status: 200 ok
* Тело: массив курсов валют из БД

```json
[
    {
        "id": 1,
        "currencyCode": "EUR",
        "rate": 0.9344601722,
        "date": "2024-04-24"
    },
    {
        "id": 2,
        "currencyCode": "KZT",
        "rate": 441.9698939804,
        "date": "2024-04-24"
    },
    {
        "id": 3,
        "currencyCode": "RUB",
        "rate": 92.2888099857,
        "date": "2024-04-24"
    },
    {
        "id": 4,
        "currencyCode": "USD",
        "rate": 1.0,
        "date": "2024-04-24"
    }
]
```

