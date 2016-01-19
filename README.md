Balance service with Dropwizard
===============================

[![Build Status](https://travis-ci.org/qza/balance-service.png?branch=master)](https://travis-ci.org/qza/balance-service)
[![Coverage Status](https://coveralls.io/repos/qza/balance-service/badge.svg?branch=master&service=github)](https://coveralls.io/github/qza/balance-service?branch=master)

Start application with bundled jetty
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

Sum balances with rx jersey client
---

```
$ curl -i http://localhost:8080/balances/total/mark

HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 91

{"requestId":1,"name":"mark","balance":3,"message":"total on all accounts"}

```

Sum balances with akka actors and ok http
---

```
$ curl -i "http://localhost:8080/balances/total/akka/mark"

HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 67

{"requestId":0,"name":"mark","balance":3,"message":"total balance"}

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

Grab some metrics
---

```
$ curl http://localhost:8081/metrics | jq -r '.timers["balance-total-rx-jersey"]'

{
  "count": 4,
  "max": 13.274905018,
  "mean": 11.48465354252596,
  "min": 10.270721734,
  "p50": 11.212324703,
  "p75": 11.555697009000001,
  "p95": 13.274905018,
  "p98": 13.274905018,
  "p99": 13.274905018,
  "p999": 13.274905018,
  "stddev": 1.076564766109133,
  "m15_rate": 0.004281574782607961,
  "m1_rate": 0.039395830443575905,
  "m5_rate": 0.011932396810670546,
  "mean_rate": 0.005601875789945678,
  "duration_units": "seconds",
  "rate_units": "calls/second"
}

$ curl http://localhost:8081/metrics | jq -r '.timers["balance-total-akka-okhttp"]'

{
  "count": 4,
  "max": 60.025915427,
  "mean": 13.333207568045573,
  "min": 3.156668368,
  "p50": 3.156668368,
  "p75": 4.463677273,
  "p95": 60.025915427,
  "p98": 60.025915427,
  "p99": 60.025915427,
  "p999": 60.025915427,
  "stddev": 21.34829488392641,
  "m15_rate": 0.0039709127367839065,
  "m1_rate": 0.0218780142482527,
  "m5_rate": 0.00966579473263782,
  "mean_rate": 0.005647938172126785,
  "duration_units": "seconds",
  "rate_units": "calls/second"
}
```
