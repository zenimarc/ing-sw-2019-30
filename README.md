# ing-sw-2019-30
## Funzioanlità sviluppate
- Regole Complete + CLI + RMI + 1 FA (Partite multiple)

Abbiamo predisposto il progetto per poter integrare facilmente una connessione tramite Socket, essendo il nostro protocollo di comunicazione basato sull'invio e la ricezione di comandi.
E' inoltre predisposta un'interfaccia generica di View implementata da CLI ed eventualmente implementabile da GUI, la quale è stata parzialmente sviluppata, ma per questioni di tempo non è stata ultimata.

## Utilizzo server
Per lanciare il server è sufficiente avviare adrenaline-server.jar
```java -jar adrenaline-server.jar```, 
verrà richiesto l'ip sul quale il server resterà in ascolto.
In caso di utilizzo locale è possibile inserire "localhost".
## Utilizzo client
Per lanciare il client è sufficiente avviare adrenaline.jar
```java -jar adrenaline.jar```

verrà richiesto l'ip del server, in caso di server locale è possibile inserire "localhost".

successivamente bisogna impostare un nickname di almeno 3 caratteri, non sono ammessi caratteri speciali.

infine viene richiesto se ci si vuole connettere a una nuova partita o riconnettere ad una partita in corso.
nel primo caso bisogna scrivere "connect" altrimenti "reconnect".
