# jcode-test

Приложение принимает запросы, вида: 

Создание новой оплаты \
POST api/v1/wallet \
Запрос:
```json
{
    "walletId" : "6f581191-f086-4af8-976a-742475f51306",
    "operationType" : "DEPOSIT",
    "amount" : 1000
} 
```

Получение баланса кошелька \
GET api/v1/wallets/{WALLET_UUID} \
Ответ:
```json
{
    "balance" : 1000000
} 
```

Получение всех оплат по WALLET_UUID \
GET api/v1/wallets/payments/{WALLET_UUID} \
Ответ:
```json
[
    {
        "walletId": "cf9e7d45-151d-45c4-a26b-3c57642d561e",
        "operationType": "DEPOSIT",
        "amount": 1000
    },
    {
        "walletId": "cf9e7d45-151d-45c4-a26b-3c57642d561e",
        "operationType": "DEPOSIT",
        "amount": 500 
    },
    {
        "walletId": "cf9e7d45-151d-45c4-a26b-3c57642d561e",
        "operationType": "DEPOSIT",
        "amount": 1000
    },
    {
        "walletId": "cf9e7d45-151d-45c4-a26b-3c57642d561e",
        "operationType": "DEPOSIT",
        "amount": 1000
    }
]
```
Ошибки обарабатываются и отсылаются в формате: \
```json
{
    "message": "message",
    "timestamp": "timestamp"
}
```

Возникли вопросы по тестам: Mockito странно их обрабатывает