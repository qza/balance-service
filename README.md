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