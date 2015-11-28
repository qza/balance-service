<#-- @ftlvariable name="" type="org.koko.balance.service.api.BalanceService" -->
<html>
    <body>
        <h1>Hello, ${model.name?html}!</h1>
        <div>
            Your current balance is: ${model.balance} $
        </div>
        <footer>
            Balance reported for request: ${model.requestId}
        </footer>
    </body>
</html>