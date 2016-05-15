# shiro-spring-boot-starter



```
shiro:
  realm: #realm class
  loginUrl:  #login url
  successUrl: #success url
  unauthorizedUrl: #403 url
  filterChainDefinitions:	#filter chian
    "/login": authc
    "/logout": logout
    "/static/**": anon
    "/**": authc    
```
