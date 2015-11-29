Balance service with Dropwizard
===============================

[![Build Status](https://travis-ci.org/qza/balance-service.png?branch=master)](https://travis-ci.org/qza/balance-service)
[![Coverage Status](https://coveralls.io/repos/qza/balance-service/badge.svg?branch=master&service=github)](https://coveralls.io/github/qza/balance-service?branch=master)

Start application with bundled server
---

```
mvn clean install
cd balance-service-app
java -jar target/balance-service-app-1.0-SNAPSHOT.jar server app.yml
```

Place initial balance
---

```
$ curl -i -X PUT "http://localhost:8080/balances/jack?balance=%2444%2C300%2C200.00"

HTTP/1.1 201 Created
Location: http://localhost:8080/balances/jack
Content-Length: 0
```

Update balance
---

```
$ curl -i -X PUT "http://localhost:8080/balances/jack?balance=%2444%2C300%2C300.00"

HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 16

"/balances/jack"
```

Get balance
---

```
$ curl -i "http://localhost:8080/balances/jack"

HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 94

{"requestId":9,"name":"jack","balance":44300300,"message":"Current balance is $44,300,300.00"}
```

View balance
---

```
$ curl -i -H "Accept: text/html" "http://localhost:8080/balances/jack"

HTTP/1.1 200 OK
Content-Type: text/html
Content-Length: 236

<html>
    <body>
        <h1>Hello, jack!</h1>
        <div>
            Your current balance is: 44,300,300 $
        </div>
        <footer>
            Balance reported for request: 11
        </footer>
    </body>
</html>
```

Sum balances from external services
---

```
$ curl -i http://localhost:8080/balances/total/mark

HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 91

{"requestId":1,"name":"mark","balance":11312965561123711,"message":"Total on all accounts"}

```

Generate sample balance_log with task
---

```
$ curl -X POST http://localhost:8081/tasks/generate?count=10000

generating record 1000
generating record 2000
generating record 3000
generating record 4000
generating record 5000
generating record 6000
generating record 7000
generating record 8000
generating record 9000
generating record 10000

generated balance_log in 3 seconds with size: 0 GB
```

Stream balance_log
---

```
$ curl -i http://localhost:8080/balances/log

HTTP/1.1 200 OK
Content-Type: application/json
Transfer-Encoding: chunked

...
```

Check health
---

```
$ curl -i http://localhost:8081/healthcheck

HTTP/1.1 200 OK
Content-Type: application/json
Cache-Control: must-revalidate,no-cache,no-store
Content-Length: 60

{"deadlocks":{"healthy":true},"repository":{"healthy":true}}
```