Postup nasazeni do GlassFish 3.1.2.2 pres admin konzoli:
--------------------------------------------------------
1. Applications -> Deployement .earu
   - context root waru je dán hodnotou v application.xml, resp. v glassfish-web.xml
2. Resources -> Connectors -> Connector Connection Pools -> Novy connection pool, napr. 'toupper-pool'
   - v Additional Properties (2. krok) definovat port a host property
3. Resources -> Connectors -> Connector Resources -> Novy resource s abs. JNDI jmenem, napr. "eis/toupper-tcp-host"
   - stejne JNDI jmeno musi byt uvedeno v:
     a) web.xml -> <resource-ref>/<mapped-name>
     b) glassfish-web.xml -> <resource-ref>/<jndi-name>
   - v obou dvou pripadech je <res-ref-name> logicke jmeno (v kontextu java:comp/env/) napr. "eis/toupper-host"
   - v aplikaci je to pak vyhledavano pod jmenem "java:comp/env/eis/toupper-host"
Neni treba zadny restart GlassFishe.

Postup nasazeni do WildFly 8.2.0.Final pres admin konzoli:
----------------------------------------------------------
1. Configuration -> Connector -> Resource Adapters -> Add, novy resource adapter
   - Name libovolne, napr. ToUpperRAR
   - Archive ve tvaru [ear-module]#[rar-module], napr. toupper-ear.ear#toupper-rar.rar
   - Module prazdny
   - TX, napr. NoTransaction
2. V detailu noveho Resource Adapteru vytvorit novy Connection Definition:
   - Name libovolne, napr. ToUpperConn
   - JNDI Name: java:/eis/toupper-tcp-host
      - musi zacinat java:
      - i kdyz to pak jde zeditovat a prefix "java:" odstranit, stale jako by tam byl
   - Connection Class: factory of managed connections,
     napr. cz.jaros.playground.jee.jca.toupper.rar.spi.ToUpperManagedConnectionFactory
3. V properties connection definition nutno doplnit rucne @ConfigProperty, tj. napr. "host" a "port".
4. Enablovat connection definition, ve vychozim stavu je disabled.
5. Nutny restart WildFly.
6. Deploy aplikace (.ear) pres Deployments -> Add

Az po deployi je connection factory zaregistrovana a je videt v konzoli (Runtime -> JNDI View)


Konfigurace pouzitelna pro GlassFish i WildFly zaroven:
-------------------------------------------------------
Ve web.xml:
    <resource-ref>
        <res-ref-name>eis/toupper-host</res-ref-name>
        <res-type>cz.jaros.playground.jee.jca.toupper.api.ToUpperConnectionFactory</res-type>
        <res-auth>Container</res-auth>
        <res-sharing-scope>Shareable</res-sharing-scope>
        <!-- Absolutni JNDI jmeno pro WildFly -->
        <mapped-name>java:/eis/toupper-tcp-host</mapped-name>
    </resource-ref>
V glassfish-web.xml:
    <resource-ref>
        <res-ref-name>eis/toupper-host</res-ref-name>
        <!-- Absolutni JNDI jmeno pro GlassFish -->
        <jndi-name>eis/toupper-tcp-host</jndi-name>
    </resource-ref>
Lookup je pak vzdy pres "java:comp/env/eis/toupper-host".
