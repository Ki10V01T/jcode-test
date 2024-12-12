# jcode-test

Приложение принимает запросы, вида: \
POST api/v1/wallet 
```json
{
 "walletId" : "6f581191-f086-4af8-976a-742475f51306,
 "operationType" : "DEPOSIT,
 "amount" : 1000
} 
```

GET api/v1/wallets/{WALLET_UUID} 

Из того, что не успел сделать: \
    - Тесты
    - Протестировать контейнеризацию docker (сами конфиги написаны)