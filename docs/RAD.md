# Requirement Analysis Document
---
<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Requirement Analysis Document](#requirement-analysis-document)
- [1. Introduzione](#1-introduzione)
	- [1.1 Obiettivo del Sistema](#11-obiettivo-del-sistema)
	- [1.2 Scopo del sistema](#12-scopo-del-sistema)
	- [1.3 Definizioni, acronimi, e abbreviazioni](#13-definizioni-acronimi-e-abbreviazioni)
	- [1.4 Riferimenti](#14-riferimenti)
- [2. Sistema corrente](#2-sistema-corrente)
- [3. Sistema proposto](#3-sistema-proposto)
	- [3.1 Requisiti funzionali](#31-requisiti-funzionali)
	- [3.2 Requisiti non funzionali](#32-requisiti-non-funzionali)
	- [3.3 Modelli del sistema](#33-modelli-del-sistema)
		- [3.3.1 Casi d'uso](#331-casi-duso)
			- [Casi d'uso di alto livello.](#casi-duso-di-alto-livello)
				- [EffettuaAutenticazione](#effettuaautenticazione)
				- [CreaPrenotazione](#creaprenotazione)
				- [ModificaPrenotazione](#modificaprenotazione)
				- [VisualizzaFSE](#visualizzafse)
				- [InserisciDettagliVisita](#inseriscidettaglivisita)
				- [NotificaPrenotazione](#notificaprenotazione)
			- [Effettua Autenticazione](#effettua-autenticazione)
				- [AutenticaPersonale](#autenticapersonale)
				- [AutenticaPaziente](#autenticapaziente)
				- [IndividuaPaziente](#individuapaziente)
				- [RegistraPaziente](#registrapaziente)
			- [Crea Prenotazione](#crea-prenotazione)
				- [InserisciDettagliRicetta](#inseriscidettagliricetta)
				- [ScegliRegimeVisita](#scegliregimevisita)
				- [PrenotaVisitaSSN](#prenotavisitassn)
				- [PrenotaVisitaALPI](#prenotavisitaalpi)
			- [Modifica Prenotazione](#modifica-prenotazione)
				- [ScegliPrenotazione](#scegliprenotazione)
				- [SpostaPrenotazione](#spostaprenotazione)
				- [EliminaPrenotazione](#eliminaprenotazione)
				- [ConfermaModifica](#confermamodifica)
			- [Visualizza FSE](#visualizza-fse)
				- [VisualizzaStoricoVisite](#visualizzastoricovisite)
				- [StampaCartellaClinica](#stampacartellaclinica)
			- [Inserisci Dettagli Visita](#inserisci-dettagli-visita)
				- [InserisciDettagli](#inseriscidettagli)
			- [Notifica Prenotazione](#notifica-prenotazione)
			- [InvioNotifica](#invionotifica)
		- [3.3.2 Modello degli oggetti](#332-modello-degli-oggetti)
			- [Lista delle classi](#lista-delle-classi)
			- [Diagramma delle entity](#diagramma-delle-entity)
			- [Diagramma delle classi](#diagramma-delle-classi)
		- [3.3.3 Modello dinamico](#333-modello-dinamico)
			- [Diagrammi delle sequenze](#diagrammi-delle-sequenze)
				- [Sequenza AutenticaPersonale](#sequenza-autenticapersonale)
				- [Sequenza AutenticaPaziente](#sequenza-autenticapaziente)
				- [Sequenza RegistraPaziente](#sequenza-registrapaziente)
				- [Sequenza IndividuaPaziente](#sequenza-individuapaziente)
				- [Sequenza InserisciDettagliRicetta](#sequenza-inseriscidettagliricetta)
				- [Sequenza ScegliRegimeVisita](#sequenza-scegliregimevisita)
				- [Sequenza PrenotaVisitaSSN](#sequenza-prenotavisitassn)
				- [Sequenza PrenotaVisitaALPI](#sequenza-prenotavisitaalpi)
				- [Sequenza ScegliPrenotazione](#sequenza-scegliprenotazione)
				- [Sequenza SpostaPrenotazione](#sequenza-spostaprenotazione)
				- [Sequenza EliminaPrenotazione](#sequenza-eliminaprenotazione)
				- [Sequenza ConfermaModifica](#sequenza-confermamodifica)
				- [Sequenza VisualizzaStoricoVisite](#sequenza-visualizzastoricovisite)
				- [Sequenza StampaCartellaClinica](#sequenza-stampacartellaclinica)
				- [Sequenza InserisciDettagliVisita](#sequenza-inseriscidettaglivisita)
				- [Sequenza InvioNotifica](#sequenza-invionotifica)
		- [3.3.4 Interfaccia utente: navigazione e mock-up](#334-interfaccia-utente-navigazione-e-mock-up)
			- [Login](#login)
			- [Schermata principale](#schermata-principale)
			- [Creazione di una prenotazione](#creazione-di-una-prenotazione)
			- [Modifica di una prenotazione](#modifica-di-una-prenotazione)
			- [Visualizzazione del fascicolo sanitario elettronico](#visualizzazione-del-fascicolo-sanitario-elettronico)
			- [Inserimento dati sulla visita](#inserimento-dati-sulla-visita)

<!-- /TOC -->

---
# 1. Introduzione
## 1.1 Obiettivo del Sistema
Il sistema si propone di gestire l'intera offerta delle prenotazioni sanitarie con efficienza, strutturando razionalmente le procedure di accesso alle informazioni e supportando modalità di comunicazione con gli utenti.

## 1.2 Scopo del sistema

Lo scopo del sistema è fornire ai cittadini un servizio che migliori drasticamente l'esperienza di interfacciarsi con il sistema sanitario attraverso una reale informatizzazione ed automatizzazione dei processi. Questi ultimi, tradizionalmente eseguiti da operatori, al fine di essere migliorati, necessitano una profonda digitalizzazione.  
La conseguenza di una maggiore efficienza si traduce in significative riduzioni dei costi e nel miglioramento dell'esperienza del cittadino in senso lato.


## 1.3 Definizioni, acronimi, e abbreviazioni

Definizione/Acronimo  | 	Descrizione  
----------------------|------------  
ALPI  | Per _Attività Libero Professionista Intramoenia_ si intende l’attività che il personale aziendale, individualmente o in équipe, esercita fuori dell’impegno di servizio in regime ambulatoriale, ivi comprese le attività di diagnostica strumentale e di laboratorio, in favore e su libera scelta dell’assistito e con oneri a carico dello stesso. Tale attività è finalizzata a garantire l’espressione di una libera scelta dell’utente.  
CUP|  Il servizio del _Centro Unico di Prenotazioni_ effettua prenotazioni di visite ed esami specialistici in regime di Servizio Sanitario Nazionale con l'Impegnativa del Medico, oppure in regime di libera professione.  
FSE  | Il _Fascicolo Sanitario Elettronico_ è lo strumento attraverso il quale il cittadino può tracciare e consultare tutta la storia della propria vita sanitaria, condividendola con i professionisti sanitari per garantire un servizio più efficace ed efficiente.  
Personale Amministrativo	| Personale dell'ospedale impiegato allo sportello del CUP per permettere la fruizione dei servizi anche ai pazienti che non utilizzano direttamente il software.
Personale Medico | Personale dell'ospedale, esperto in medicina, che si occupa della salute dei pazienti prevenendo, diagnosticando e curando le malattie. Può accedere alla loro cartella clinica personale.
Ricetta  | La ricetta medica è un documento, redatto da un medico abilitato, che consente al paziente di prenotare visite specialistiche, esami diagnostici e di poter ritirare o acquistare farmaci che richiedono una prestazione medica.
SSN  | Servizio Sanitario Nazionale  
Ticket  |    Il ticket sanitario è una quota di partecipazione diretta dei cittadini alla spesa pubblica come controprestazione per l'assistenza sanitaria fornita dallo Stato. Esiste inoltre un sistema di esenzioni per reddito, fasce di età e servizi considerati "salvavita".


## 1.4 Riferimenti
- [Sanità digitale - Agenzia per l'Italia digitale](https://www.agid.gov.it/it/piattaforme/sanita-digitale)  
- [Tarifari nazionale delle prestazioni del SSN](http://www.salute.gov.it/portale/temi/p2_6.jsp?id=3662&area=programmazioneSanitariaLea&menu=vuoto)  

# 2. Sistema corrente
Ad oggi il sistema di gestione prenotazioni ospedaliere è soltanto parzialmente supportato dall'utilizzo di tecnologia informatica. Molte operazioni, quali la prenotazione di prestazioni urgenti e, quindi, l'eventuale spostamento di altre prenotazioni (tenendo conto della loro urgenza) avvengono manualmente causando disagi non trascurabili. I pazienti, raggiunta la struttura ospedaliera, subiscono lunghi tempi di attesa consapevoli dell'alta probabilità di non raggiungere i propri obiettivi. La mancanza di un supporto informatico valido e in gran parte autonomo comporta spreco di risorse, denaro e personale. Il paziente risulta essere vincolato da una gestione prettamente amministrativa, spesso subendo complicazioni e ristrettezze. Qualsiasi operazione deve necessariamente essere affrontata a diretto contatto col personale amministrativo che si ritrova a gestire in modo non ottimale una quantità non poco rilevante di materiale.
Fino ad ora il sistema informatico è utilizzato esclusivamente per la memorizzazione delle varie informazioni e per l'invio notifiche al paziente, senza possibilità di risposta.
# 3. Sistema proposto
Il nostro sistema, tenendo conto delle problematiche riscontrate in quello corrente, si propone di eliminare tutti i disagi causati dall'assenza di una completa informatizzazione del sistema sanitario. Le prenotazioni e i loro livelli di urgenza saranno gestiti tramite il nostro software che, grazie ad un algoritmo, riuscirà a gestire automaticamente l'eventuale spostamento di prenotazioni causando meno problemi possibili. Non ne beneficerà soltanto il paziente, il quale sarà in grado di accedere ad un area riservata comodamente da casa (evitando così eventuali ritardi) ma anche il personale medico e amministrativo della struttura ospedaliera. In caso di impossibilità da parte del paziente il personale amministrativo sarà in grado di accedere all'area riservata dell'utente   velocizzando ulteriormente le procedure di gestione delle prenotazioni. Il personale medico sarà invece in grado di accedere e modificare la cartella clinica del paziente in maniera completamente elettronica. Informatizzando quasi totalmente le prestazioni sopra citate, il sistema proposto porterà notevoli miglioramenti sotto tutti i punti di vista raggiungendo obiettivi di efficenza, robustezza, sicurezza e facilità di utilizzo.
## 3.1 Requisiti funzionali
- Il personale amministrativo, il personale medico e il paziente dovranno essere in grado di creare una prenotazione
- Il personale amministrativo, il personale medico ed il paziente dovranno poter visionare il Fascicolo Sanitario Elettronico
- Il paziente potrà visualizzare lo Storico delle sue visite ogni qual volta lo richiederà
- Il personale medico dovrà poter inserire i dettagli della visita effettuata
- Il sistema dovrà offrire funzionalità di reminder agli utenti che dovranno effettuare una o più visite
- Il sistema dovrà gestire prestazioni sanitarie in dipendenza della politica dei differenti reparti
- Il sistema dovrà sempre rispettare i tempi previsti dalla legge in base al codice di urgenza di ogni prenotazione registrata
- Il sistema dovrà tenere informato il paziente sulla documentazione da portare all'eventuale visita
- Il sistema dovrà saper gestire le prenotazioni in base alle scelte del personale medico, del paziente o del personale amministrativo,   modificandole se necessario, per una eventuale soluzione più efficiente
- Il paziente e il personale amministrativo potranno modificare le prenotazioni ed eventualmente cancellarle
- Il sistema dovrà saper fare distinzione tra paziente, personale medico e personale amministrativo
- Il sistema dovrà saper gestire differentemente visite intramoenia e visite con SSN

## 3.2 Requisiti non funzionali
- Per effettuare una prenotazione sarà necessario essere provvisti di ricetta
- Il sistema dovrà impedire di utilizzare più volte la stessa ricetta per la medesima prestazione   
- Il Fascicolo Sanitario Elettronico potrà essere stampato dal paziente solo tramite pagamento di una somma di denaro
- Il sistema dovrà inviare al paziente una mail di reminder almeno 24 ore prima della visita.
- Il sistema dovrà inviare una mail di notifica al Paziente ogni volta che viene creata o modificata una prenotazione a suo nome.

## 3.3 Modelli del sistema
### 3.3.1 Casi d'uso
#### Casi d'uso di alto livello.
![Casi d'uso generali](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Casi%20d'uso/Casi%20d'uso%20generali.png)

---
##### EffettuaAutenticazione  

__Attori:__  _PersonaleMedico, PersonaleAmministrativo, Paziente_, DBMS  

__Breve descrizione:__ Caso d'uso che permette la corretta autenticazione degli utenti che intendono utilizzare il software proposto.

[_Vista dettagliata_](#effettua-autenticazione)

---
##### CreaPrenotazione  

__Attori:__  _PersonaleMedico, PersonaleAmministrativo, Paziente_, DBMS  

__Breve descrizione:__ Caso d'uso che permette la creazione di una prenotazione o di un ricovero.  

[_Vista dettagliata_](#crea-prenotazione)

---
##### ModificaPrenotazione  

__Attori:__  _PersonaleAmministrativo, Paziente_, DBMS

__Breve descrizione:__ Caso d'uso che permette di modificare, ed eventualmente eliminare, una prenotazione o un ricovero.

[_Vista dettagliata_](#modifica-prenotazione)

---
##### VisualizzaFSE  

__Attori:__  _PersonaleMedico, PersonaleAmministrativo, Paziente_, DBMS

__Breve descrizione:__ Caso d'uso che permette la lettura, e l'eventuale stampa, del Fascicolo Sanitario elettronico.

[_Vista dettagliata_](#visualizza-fse)

---
##### InserisciDettagliVisita  

__Attori:__   _PersonaleMedico_, DBMS  

__Breve descrizione:__ Caso d'uso che permette l'inserimento della diagnosi e dei relativi dettagli riguardanti una visita.

[_Vista dettagliata_](#inserisci-dettagli-visita)

---
##### NotificaPrenotazione  

__Attori:__   _Tempo_, DBMS  

__Breve descrizione:__ Caso d'uso che permette di inviare notifiche reminder agli specifici pazienti riguardo le loro prenotazioni.

[_Vista dettagliata_](#notifica-prenotazione)

---
#### Effettua Autenticazione
![Use case "EffettuaAutenticazione"](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Casi%20d'uso/Effettua%20Autenticazione.png)
[_Vista di alto livello_](#effettuaautenticazione)

---
##### AutenticaPersonale
__Attori:__  _PersonaleMedico, PersonaleAmministrativo_, DBMS  
__Precondizioni:__ `L'utente non si è ancora autenticato in questa sessione`  
__Flusso degli eventi:__
```
1. Il caso d'uso inizia quando un utente avvia il software.
	2. Il sistema chiede all'utente di inserire il suo codice
	   identificativo e password.
3. L'utente inserisce il proprio codice identificativo, la sua password
   e conferma.
	4. Il sistema chiede al DBMS informazioni sull'utente individuato
	   univocamente attraverso il codice che abbia password
	   corrispondente a quella inserita.
5. Il DBMS comunica al sistema se l'utente è presente nel database ed
   eventuali informazioni connesse.
	6. Se l'utente non è presente nel database il sistema informa che
       il codice inserito o la password sono errati e chiede di
	   reinserirli.
```  
__Postcondizioni:__  `Il sistema è nella schermata principale`  
[_Diagramma delle sequenze_](#sequenza-AutenticaPersonale)

---
##### AutenticaPaziente
__Attori:__  _Paziente_, DBMS  
__Precondizioni:__ `Paziente non si è ancora autenticato in questa sessione`  
__Flusso degli eventi:__
```
1. Il caso d'uso inizia quando Paziente avvia il software.
	2. Il sistema chiede a Paziente di inserire il suo codice
	   fiscale e la sua password.
3. Paziente inserisce i propri CF e password, e conferma.
	4. Il sistema chiede al DBMS informazioni sul Paziente individuato
	   univocamente attraverso il codice con password corrispondente a
	   quella inserita.
5. Il DBMS comunica al sistema se il Paziente è presente nel database ed
   eventuali informazioni connesse.
	6. Se non viene identificato nessun paziente il sistema chiede
	   nuovamente di autenticarsi.
```  
__Postcondizioni:__  `Il sistema è nella schermata principale`  
[_Diagramma delle sequenze_](#sequenza-autenticapaziente)

---
##### IndividuaPaziente
__Attori:__ _PersonaleAmministrativo_, DBMS  
__Precondizioni:__ `PersonaleAmministrativo si trova nella schermata principale `  
__Flusso degli eventi:__  
```
1.PersonaleAmministrativo sceglie l'opzione "Seleziona il paziente"
	2. Il sistema chiede a PersonaleAmministrativo di inserire il CF
	   del Paziente.
3. Il sistema chiede al DBMS informazioni sul Paziente individuato
   univocamente attraverso il codice.
	4. Il DBMS comunica al sistema i dati del Paziente
5.Il sistema comunica a PersonaleAmministrativo i dati del Paziente

```
__Postcondizioni:__ `PersonaleAmministrativo si trova nella schermata principale e può selezionare le altre opzioni `  
[_Diagramma delle sequenze_](#sequenza-IndividuaPaziente)

---
##### RegistraPaziente
__Attori:__ _PersonaleAmministrativo, Paziente_, DBMS  
__Precondizioni:__ `Il sistema è in una schermata di login per il Paziente o di individuazione paziente`  
__Flusso degli eventi:__
```
1. Il caso d'uso inizia quando, durante un'operazione di autenticazione
   di un paziente, Paziente o PersonaleAmministrativo selezionano la
   opzione "Registra paziente"
	2. Il sistema mostra un form per inserire i dati necessari
	   all'inserimento nel sistema
3. L'utente compila il modulo, eventualmente non riempiendo i campi
   facoltativi e invia i dati.
	4. Il sistema chiede al DBMS se sono già presenti pazienti con
	   dati coincidenti.
5. Il DBMS restituisce i dati richiesti.
	6. Se il Paziente è già presente nel database viene mostrato un
	   messaggio di errore e viene chiesto di inserire dei dati
	   corretti
	   ALTRIMENTI
	   Viene comunicato al DBMS di aggiungere il nuovo paziente.
```
__Postcondizioni:__  `L'utente può proseguire l'operazione che aveva iniziato`  
[_Diagramma delle sequenze_](#sequenza-registrapaziente)

---

#### Crea Prenotazione
![Use case "CreaPrenotazione"](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Casi%20d'uso/CreaPrenotazione.png)  
[_Vista di alto livello_](#creaprenotazione)

---
##### InserisciDettagliRicetta
__Attori:__  _Paziente, PersonaleMedico, PersonaleAmministrativo_, DBMS  
__Precondizioni:__ `L'utente si trova nella schermata principale `  
__Flusso degli eventi:__
```
1. Il caso d'uso inizia quando l'attore principale seleziona
   l'opzione "Crea Prenotazione".
	2. Il sistema chiede all'utente di compilare un form
	   con i dati della ricetta.
3. L'utente inserisce il numero di ricetta, il codice di urgenza e
   la prestazione richiesta e conferma.
	4. Il sistema controlla la correttezza sintattica dei dati
	   inseriti.
	   Se non sono corretti lo notifica all'utente e chiede di
	   immettere dati corretti.
	   ALTRIMENTI
	   Chiede al DBMS il numero di prenotazioni con lo stesso
	   numero di ricetta per la stessa prestazione.
5. Il DBMS restituisce le informazioni richieste.
	6. Se esiste almeno una ricetta che soddisfa i parametri
	   il sistema notifica che quella prenotazione è già stata
	   effettuata e richiede di inserire i dati corretti.
```  
__Postcondizioni:__  `Il sistema ha immagazzinato le informazioni sulla prenotazione `  
[_Diagramma delle sequenze_](#sequenza-inseriscidettagliricetta)  

---
##### ScegliRegimeVisita
__Attori:__ _Paziente, PersonaleAmministrativo_    
__Precondizioni:__ `Il sistema ha immagazzinato i dettagli della ricetta `  
__Flusso degli eventi:__
```
1. Il caso d'uso inizia quando l'utente conferma i dati della ricetta
   senza errori.
	2. Il sistema chiede se si voglia prenotare in convenzione col
 	   SSN o in regime ALPI
3. L'utente seleziona su "Servizio sanitario nazionale" o
   "Attività di libera professione intramoenia"
```  
__Postcondizioni:__  `Il sistema ha registrato la scelta dell'utente `  
[_Diagramma delle sequenze_](#sequenza-scegliregimevisita)  

---
##### PrenotaVisitaSSN
__Attori:__ _Paziente, PersonaleAmministrativo, PersonaleMedico_ DBMS      
__Precondizioni:__ `È in corso una procedura di prenotazione con una ricetta valida`  
__Flusso degli eventi:__
```
1. Il caso d'uso inizia quando l'utente comunica al sistema di volere
   prenotare una visita in convenzione con il sistema sanitario nazionale.
	2. Il sistema, tenendo conto del codice di urgenza, chiede al DBMS
	   quali giorni e orari sono disponibili per la nuova prenotazione  
3. Il DBMS comunica al sistema l'elenco degli orari disponibili.
	4. Il sistema propone al paziente le date disponibili per
	   effettuare la visita.
5. Il paziente sceglie tra le opzioni proposte e conferma la scelta.
	6. Il sistema comunica la nuova prenotazione al DBMS.
7. Il DBMS aggiorna i dati.
	8. Il sistema invia una notifica di avvenuta prenotazione
	   all'utente, riportando il costo del ticket e i documenti
	   da portare.
```  
__Flusso alternativo:__
```
3. Il DBMS comunica che non ci sono orari disponibili
   4. Il sistema cerca un orario in cui è possibile effettuare
      la vista spostandone una meno urgente e lo propone al paziente.
5. Il paziente accetta la proposta del sistema
	6. Il sistema comunica la nuova prenotazione al DBMS e lo
	   spostamento della prenotazioni meno urgente.
7. Il DBMS aggiorna i dati.
	8. Il sistema invia una notifica di avvenuta prenotazione
	   all'utente, riportando il costo del ticket e i documenti
	   da portare.
```
__Postcondizioni:__  `Il sistema torna alla schermata principale`  
[_Diagramma delle sequenze_](#sequenza-prenotavisitassn)  

---
##### PrenotaVisitaALPI
__Attori:__ _Paziente, PersonaleAmministrativo,_ DBMS      
__Precondizioni:__ `È in corso una procedura di prenotazione con una ricetta valida`  
__Flusso degli eventi:__
```
1. Il caso d'uso inizia quando l'utente comunica al sistema di volere
   prenotare una visita in regime intramoenia.
	2. Il sistema chiede al DBMS quali medici effettuano il tipo di
	   visita richiesto e il loro onorario.
3. Il DBMS trasmette al sistema i dati richiesti.
	4. Il sistema mostra la lista di medici all'utente chiedendo di
	   indicarne uno.
5. Il paziente seleziona il medico presso il quale vuole sottoporsi alla
   visita.
	6. Il sistema chiede al DBMS giorni e orari in cui il medico
	   selezionato non può effettuare ulteriori visite.
7. Il DBMS comunica al sistema l'elenco delle prenotazioni relative
   al medico scelto dall'utente.	 
	8. Il sistema chiede all'utente di scegliere giorno e ora tra
           quelli in cui il medico è disponibile.
9. L'utente seleziona e conferma giorno e ora.	   
	10. Il sistema comunica la nuova prenotazione al DBMS.
11. Il DBMS aggiorna i dati.
	12. Il sistema invia una notifica di avvenuta prenotazione
	    all'utente, riportando i documenti
	    da portare.

```  
__Flusso alternativo:__
```
7. Il DBMS comunica che non ci sono orari disponibili
	8. Il sistema avvisa che non è possibile evadere la richiesta.
```
__Postcondizioni:__  `Il sistema torna alla schermata principale`  
[_Diagramma delle sequenze_](#sequenza-prenotavisitaalpi)

---

#### Modifica Prenotazione
![Use case "ModificaPrenotazione"](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Casi%20d'uso/ModificaPrenotazione.png)
[_Vista di alto livello_](#modificaprenotazione)

---
##### ScegliPrenotazione
__Attori:__  _Paziente, PersonaleAmministrativo_, DBMS  
__Precondizioni:__  `L'utente si trova nella schermata prinicpale`  
__Flusso degli eventi:__  
```
1. Il caso d'uso inizia quando l'utente seleziona l'opzione "Modifica
   Prenotazione" dalla schermata principale
	2. Il sistema chiede al DBMS l'elenco di tutte le prenotazioni
	   relative al paziente con data successiva a quella corrente
	   +24 ore e le relative informazioni.
3. Il DBMS comunica al sistema le prenotazioni richieste.
	4. Il sistema mostra all'utente la lista di prenotazioni
	   ottenuta, se la lista è vuota viene mostrato un messaggio
	   di errore.
5. L'attore che ha iniziato il caso d'uso seleziona la prenotazione
   che desidera modificare.
	6. Il sistema mostra i dati relativi alla Prenotazione selezionata
```
__Postcondizioni:__ `Paziente si trova nella schermata di modifica prenotazione`  
[_Diagramma delle sequenze_](#sequenza-scegliprenotazione)

---
##### SpostaPrenotazione  
__Attori:__ _PersonaleAmministrativo, Paziente_, DBMS  
__Precondizioni:__ `L'utente è nella schermata di modifica prenotazione`  
__Flusso degli eventi:__  
```
1. Il caso d'uso inizia quando l'utente seleziona l'opzione "Sposta
prenotazione".
	2. Il sistema chiede al DBMS le informazioni necessarie per
           fornire all'utente i giorni disponibili in cui spostare la
	   prenotazione.
3. Il DBMS restituisce le informazioni richieste.
	4. Il sistema mostra all'utente le opzioni disponibili per la
	   nuova data.
4. L'utente sceglie il giorno e l'ora tra quelli proposti.
	5. Il sistema da inizio al caso d'uso ConfermaModifica.
```
__Postcondizioni:__ `L'utente torna alla schermata principale`  
[_Diagramma delle sequenze_](#sequenza-spostaprenotazione)

---
##### EliminaPrenotazione
__Attori:__ _Paziente, PersonaleAmministrativo_  
__Precondizioni:__ `L'utente è nella schermata di modifica prenotazione`  
__Flusso degli eventi:__  
```
1. Il caso d'uso inizia quando l'utente seleziona l'opzione "Elimina
prenotazione"
	2. Il sistema predispone la cancellazione della prenotazione e
	   dà inizio al caso d'uso ConfermaModifica.
```
__Postcondizioni:__ `L'utente torna alla schermata principale`  
[_Diagramma delle sequenze_](#sequenza-eliminaprenotazione)

---
##### ConfermaModifica
__Attori:__ _PersonaleAmministrativo, Paziente_, DBMS  
__Precondizioni:__ `L'utente ha apportato modifiche ad una prenotazione `  
__Flusso degli eventi:__  
```
	1. Il sistema riassume le modifiche effettuate dall'utente e
	   chiede di confermarle o annullarle.
2. L'utente conferma l'operazione.
	3. Il sistema comunica al DBMS la modifica.
4.Il DBMS attua le modifiche richieste.
	5. Il sistema manda all'utente una notifica per segnalare
	   l'avvenuta modifica.	   

```
__Flusso alternativo:__  
```
3. L'utente annulla l'operazione.
	4. Il sistema mostra all'utente un messaggio di avviso
	   annullamento.
```
__Postcondizioni:__ `L'utente è nuovamente nella schermata principale`  
[_Diagramma delle sequenze_](#sequenza-confermamodifica)

---

#### Visualizza FSE
![Use case "VisualizzaFSE"](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Casi%20d'uso/VisualizzaFSE.png)  
[_Vista di alto livello_](#visualizzafse)

---
##### VisualizzaStoricoVisite
__Attori:__  _Paziente, PersonaleMedico, PersonaleAmministrativo_, DBMS  
__Precondizioni:__ `L'utente si trova nella schermata principale`  
__Flusso degli eventi:__
```
1. Il caso d'uso inizia quando l'utente seleziona l'opzione "Visualizza
   fascicolo sanitario"
	2. Il sistema chiede al DBMS tutte le visite già effettuate dal
	   paziente per cui si sta operando e i dati correlati.
3. Il DBMS restituisce i dati richiesti.
	4. Il sistema mostra in una schermata l'elenco di tutte le
	   visite con rispettive informazioni.
```  
__Postcondizioni:__  `L'utente è in grado di accedere correttamente al suo storico visite`  
[_Diagramma delle sequenze_](#sequenza-visualizzastoricovisite)

---
##### StampaCartellaClinica
__Attori:__  _PersonaleMedico, PersonaleAmministrativo_, DBMS  
__Precondizioni:__ `L'utente ha visualizzato correttamente lo storico visite`  
__Flusso degli eventi:__
```
1. Il caso d'uso inizia quando l'utente seleziona l'opzione "Stampa FSE".
	2. Il sistema produce un file in formato PDF riportante i dati
	   anagrafici del paziente e l'elenco in ordine cronologico di
	   tutte le visite effettuate, specificando per ognuna data,
	   costo, tipo di visita, nome del medico che se ne è occupato
	   e le sue annotazioni.
```  
__Postcondizioni:__  `L'utente è in grado di stampare correttamente il documento FSE`  
[_Diagramma delle sequenze_](#sequenza-stampacartellaclinica)

---

#### Inserisci Dettagli Visita
![Use case "InserisciDettagliVisita"](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Casi%20d'uso/InserisciDettagliVisita.png)
[_Vista di alto livello_](#inseriscidettaglivisita)

---
##### InserisciDettagli
__Attori:__ _PersonaleMedico_, DBMS
__Precondizioni:__ `Il sistema mostra la schermata principale `
__Flusso degli eventi:__
```
1. Il caso d'uso inizia quando l'utente seleziona un paziente dalla
   schermata principale e clicca "Inserisci Dettagli Visita".
	2. Il sistema richiede al DBMS le eventuali informazioni relative
	   alla visita connessa al paziente precedentemente selezionato.
3. Il DBMS restituisce le informazioni richieste
        4. Il sistema mostra una schermata con box di testo modificabili
           contenenti le eventuali informazioni già presenti.
5. PersonaleMedico modifica/aggiunge informazioni e conferma le modifiche.
        6. Il sistema comunica al DBMS le informazioni aggiornate sulla
		   visita.	  
```
__Postcondizioni:__ `Il sistema ritorna alla schermata principale`  
[_Diagramma delle sequenze_](#sequenza-inseriscidettaglivisita)

---

#### Notifica Prenotazione
![Use case "NotificaPrenotazione"](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Casi%20d'uso/NotificaPrenotazione.png)
[_Vista di alto livello_](#notificaprenotazione)

---
#### InvioNotifica
__Attori:__  _Tempo_, DBMS  
__Precondizioni:__ `È trascorsa un'ora dall'ultima volta che si è provato ad inviare le notifiche`  
__Flusso degli eventi:__
```
1. Il caso d'uso inizia quando è passata un'ora dall'ultima volta che
   è iniziato il caso d'uso.
	2. Il sistema chiede al DBMS l'elenco di tutti i pazienti che
	   hanno una visita dopo l'ultima prenotazione già notificata

	4. Il sistema invia ad ogni paziente una notifica contenente le
	   informazioni relative alle sue prenotazioni.
```  
__Postcondizioni:__  `Tutti i pazienti con visite in programma entro 24 ore hanno ricevuto un promemoria`  
[_Diagramma delle sequenze_](#sequenza-invionotifica)

---
### 3.3.2 Modello degli oggetti
#### Lista delle classi

Classe | Descrizione
-|-|
ConfermaDialog | Nelle operazioni di modifica di una prenotazione già effettuata questa schermata riassume le modifiche e consente di confermarle o annullarle.
ConfermaModificaControl | Gestisce le operazioni riguardanti la conferma di una modifica da effettuare.
EffettuaPrenotazioneControl | Gestisce le operazioni riguardanti la prenotazione di una nuova visita da effettuare.
ErroreDialog | Mostra a video all'utente che l'operazione da lui intrapresa non è andata a buon fine.
FormRicetta | Permette l'inserimento dei dati relativi alla ricetta.
FSEControl | Gestisce le operazioni per l'ottenimento del Fascicolo Sanitario Elettronico da parte dell'utente.
FSEDialog | Schermata che presenta lo Storico Visite richiesto precedentemente dal Paziente o il Fascicolo Sanitario richiesto dal       personale.
GeneraRicettaControl | Gestisce le operazioni per la memorizzazione delle informazioni relative ad una ricetta.
IndividuaPazienteForm | Schermata che presenta un box dove scrivere il CF del paziente che PersonaleAmministrativo vuole scegliere per continuare le operazioni desiderate.
InserisciDettagliVisitaControl | Gestisce le operazioni per il corretto inserimento della diagnosi da parte del medico.
InserisciDettagliVisitaDialog | Interfaccia per l'inserimento e l'eventuale conferma della diagnosi della visita precedentemente effettuata.
InviaNotificaControl | Permette la gestione di eventuali notifiche scaturite da operazioni effettuate nel sistema.
LoginControl | Gestisce le operazioni di verifica della corretteza di un eventuale login da parte di un utente.
MainScreen | Schermata principale per l'avvio di varie procedure.
ModificaPrenotazioneControl | Gestisce le operazioni necessarie per la modifica di una prenotazione.
ModificaPrenotazioneDialog | Interfaccia che permette all'utente di modificare la Prenotazione selezionata.
PazienteEntity | All'interno del sistema i dati dei pazienti sono racchiusi in questa classe.
PazienteLoginForm | Il form attraverso cui Paziente può inserire il proprio codice fiscale e proseguire le procedure di autenticazione.
PersonaleLoginForm | Il form attraverso cui PersonaleMedico e PersonaleAmministrativo possono inserire la propria matricola e proseguire le procedure di autenticazione.
PersonaleEntity | All'interno del sistema i dati del personale sono racchiusi in questa classe.
Prenotazione | Questa classe contiene i dati rilevanti della prenotazione mentre viene elaborata dal sistema.
RegistrazioneForm | Il form che permette di immettere i dati personali di un paziente la prima volta che questo utilizza i servizi di SPRINT.
RegistrazioneControl | Gestisce le operazioni necessarie per la registrazione di un nuovo paziente.
Ricetta | Questa classe contiene i dati relativi alle ricetta mentre viene inserita nel DBMS.
ScegliOrarioDialog | Durante le operazioni di prenotazione attraverso questa schermata l'utente può selezionare data e ora della visita tra quelli proposti dal sistema.
ScegliPrenotazioneControl | Gestisce le operazioni per la corretta realizzazione della scelta della visita da modificare da parte dell'utente.
ScegliPrenotazioneDialog | Questa schermata presenta l'elenco delle prenotazioni per visite ancora non effettuate dal paziente, da qui è possibile selezionarne una per modificarla.
ScegliRegimeDialog | Nelle operazioni di prenotazione questa schermata consente di scegliere tra regime ALPI e regime SSN.
SceltaMedicoDialog | Nelle operazioni di prenotazione se si sceglie il regime ALPI questa schermata permette di scegliere il medico dal quale farsi visitare.
SelezionaPazienteControl | Gestisce le operazioni per selezionare un paziente da parte del PersonaleAmministrativo.



#### Diagramma delle entity
![Entity](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Diagrammi%20delle%20classi/Entity.png)
#### Diagramma delle classi
![Classi](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Diagrammi%20delle%20classi/Tutte%20le%20classi.png)
### 3.3.3 Modello dinamico
#### Diagrammi delle sequenze
---
##### Sequenza AutenticaPersonale
 ![AutenticaPersonale](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Diagrammi%20delle%20sequenze/AutenticaPersonale.png)
 [_Caso d'uso_](#autenticapersonale)

 ---
##### Sequenza AutenticaPaziente
 ![AutenticaPaziente](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Diagrammi%20delle%20sequenze/AutenticaPaziente.png)
 [_Caso d'uso_](#autenticapaziente)

 ---
##### Sequenza RegistraPaziente
 ![RegistraPaziente](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Diagrammi%20delle%20sequenze/RegistraPaziente.png)
 [_Caso d'uso_](#registrapaziente)

---
##### Sequenza IndividuaPaziente
![IndividuaPaziente](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Diagrammi%20delle%20sequenze/IndividuaPaziente.png)
[_Caso d'uso_](#individuapaziente)

---

##### Sequenza InserisciDettagliRicetta
![InserisciDettagliRicetta](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Diagrammi%20delle%20sequenze/InserisciDettagliRicetta.png)  
[_Caso d'uso_](#inseriscidettagliricetta)

 ---
##### Sequenza ScegliRegimeVisita
![InserisciDettagliRicetta](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Diagrammi%20delle%20sequenze/ScegliRegimeVisita.png)  
[_Caso d'uso_](#scegliregimevisita)

 ---
##### Sequenza PrenotaVisitaSSN
![InserisciDettagliRicetta](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Diagrammi%20delle%20sequenze/PrenotaVisitaSSN.png)  
[_Caso d'uso_](#prenotavisitassn)

 ---
##### Sequenza PrenotaVisitaALPI
![InserisciDettagliRicetta](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Diagrammi%20delle%20sequenze/PrenotaVisitaALPI.png)  
[_Caso d'uso_](#prenotavisitaalpi)

 ---
##### Sequenza ScegliPrenotazione
 ![ScegliPrenotazione](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Diagrammi%20delle%20sequenze/ScegliPrenotazione.png)
 [_Caso d'uso_](#scegliprenotazione)

 ---
##### Sequenza SpostaPrenotazione
 ![SpostaPrenotazione](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Diagrammi%20delle%20sequenze/SpostaPrenotazione.png)
 [_Caso d'uso_](#spostaprenotazione)

 ---
##### Sequenza EliminaPrenotazione
 ![EliminaPrenotazione](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Diagrammi%20delle%20sequenze/EliminaPrenotazione.png)
 [_Caso d'uso_](#eliminaprenotazione)

 ---
##### Sequenza ConfermaModifica
 ![ConfermaModifica](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Diagrammi%20delle%20sequenze/ConfermaModifica.png)
 [_Caso d'uso_](#confermamodifica)

 ---
##### Sequenza VisualizzaStoricoVisite
 ![VisualizzaStoricoVisite](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Diagrammi%20delle%20sequenze/VisualizzaStoricoVisite.png)
 [_Caso d'uso_](#visualizzastoricovisite)

 ---
##### Sequenza StampaCartellaClinica
 ![StampaCartellaClinica](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Diagrammi%20delle%20sequenze/StampaCartellaClinica.png)
 [_Caso d'uso_](#stampacartellaclinica)

  ---
##### Sequenza InserisciDettagliVisita
 ![InserisciDettagliVisita](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Diagrammi%20delle%20sequenze/InserisciDettagliVisita.png)
 [_Caso d'uso_](#inseriscidettagli)

 ---
##### Sequenza InvioNotifica
 ![NotificaPrenotazione](https://andrea-augello.github.io/SviluppoSW/media/Diagrammi/Diagrammi%20delle%20sequenze/InvioNotifica.png)
 [_Caso d'uso_](#invionotifica)

 ---
### 3.3.4 Interfaccia utente: navigazione e mock-up
#### Login
---
Schermata di login per PersonaleMedico
![Schermata di login per PersonaleMedico](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20Login/Schermata%20Login%20-%20Personale%20Medico.png)

---
Schermata di login per PersonaleAmministrativo
![Schermata di login per PersonaleAmministrativo](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20Login/Schermata%20Login%20-%20Personale%20Amministrativo.png)

---
Schermata di login per Paziente  
![Schermata di login per Paziente](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20Login/Schermata%20Login%20-%20Paziente.png)

---
Schermata di errore nel login per PersonaleMedico
![Schermata di errore nel login per PersonaleMedico](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20Login/Schermata%20Login%20-%20Errore%20(PersonaleMedico).png)

---
Schermata di errore nel login per PersonaleAmministrativo
![Schermata di errore nel login per PersonaleAmministrativo](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20Login/Schermata%20Login%20-%20Errore%20(PersonaleAmministrativo).png)

---
Schermata di errore nel login per Paziente
![Schermata di errore nel login per Paziente](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20Login/Schermata%20Login%20-%20Errore%20(Paziente).png)

---
Schermata di registrazione paziente
![Schermata di registrazione paziente](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20Login/Schermata%20Login%20-%20Registrazione%20paziente.png)

---
Schermata di individua paziente
![Schermata di individua paziente](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20Login/Schermata%20Individua%20Paziente.png)

---
Schermata di errore in individua paziente
![Schermata di errore in individua paziente](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20Login/Schermata%20Individua%20Paziente%20-%20Errore.png)

---
#### Schermata principale
---
Schermata principale Paziente
![Schermata principale Paziente](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20Mainscreen/Schermata%20Iniziale%20-%20Paziente.png)

---
Schermata principale PersonaleAmministrativo
![Schermata principale PersonaleAmministrativo](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20Mainscreen/Schermata%20Iniziale%20-%20Personale%20Amministrativo.png)

---
Schermata principale PersonaleMedico
![Schermata principale PersonaleMedico](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20Mainscreen/Schermata%20Iniziale%20-%20Personale%20Medico.png)

---
#### Creazione di una prenotazione
---
Schermata per l'inserimento dei dettagli della ricetta
![Inserimento dettagli ricetta](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20CreaPrenotazione/Schermata%20Crea%20Prenotazione%20-%20Inserisci%20dettagli%20ricetta.png)

---
Errore nell'inserimento dei dettagli della ricetta
![Errore inserimento dettagli ricetta](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20CreaPrenotazione/Schermata%20Crea%20Prenotazione%20-%20Inserisci%20dettagli%20ricetta%20(errore).png)

---
Schermata di scelta del regime della prenotazione
![Scelta SSN o ALPI](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20CreaPrenotazione/Schermata%20Crea%20Prenotazione%20-%20Scegli%20Regime%20Visita.png)

---
Schermata per la scelta del medico per una visita intramoenia
![Scelta medico](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20CreaPrenotazione/Schermata%20Crea%20Prenotazione%20-%20PrenotaVisitaALPI%20(scelta%20medico).png)

---
Schermata di errore per impossibilità di evadere la prenotazione nei tempi previsti dalla legge per il codice di urgenza.
![Errore mancanza orario](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20CreaPrenotazione/Schermata%20Crea%20Prenotazione%20-%20PrenotazioneVisitaALPI%20(Errore).png)

---
Schermata di scelta per data e ora della visita
![Scelta data](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20CreaPrenotazione/Schermata%20Crea%20Prenotazione%20-%20PrenotaVisitaSSN:ALPI%20(Data).png)

---
#### Modifica di una prenotazione
---
Schermata per la scelta della prenotazione da modificare
![Scelta prenotazione](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20ModificaPrenotazione/Schermata%20Modifica%20Prenotazione%20-%20Scegli%20prenotazione.png)

---
Schermata di errore se non sono presenti prenotazioni modificabili
![Errore nessuna prenotazione modificabile](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20ModificaPrenotazione/Schermata%20Modifica%20Prenotazione%20-%20Errore%20(lista%20vuota).png)

---
Schermata riassuntiva dei dettagli della prenotazione da modificare
![Scelta data](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20ModificaPrenotazione/Schermata%20Modifica%20Prenotazione%20-%20Modifica.png)

---
Schermata di scelta per data e ora della visita
![Scelta data](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20ModificaPrenotazione/Schermata%20Modifica%20Prenotazione%20-%20SpostaPrenotazione%20(NuovoOrarioDialog).png)

---
Schermata per annullare la prenotazione selezionata
![Scelta data](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20ModificaPrenotazione/Schermata%20Modifica%20Prenotazione%20-%20EliminaPrenotazione%20(ConfermaDialog).png)

---
Schermata di conferma modifiche
![Conferma modifiche](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20ModificaPrenotazione/Schermata%20Modifica%20Prenotazione%20-%20SpostaPrenotazione%20(ConfermaDialog).png)

---

#### Visualizzazione del fascicolo sanitario elettronico

---
Visualizzazione fasciolo sanitario (Medico)
![VisualizzaFSE](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20VisualizzaFSE/Schermata%20Visualizza%20FSE%20-%20VisualizzaCartellaClinica.png)

---
Visualizzazione fasciolo sanitario (Amministrazione)
![VisualizzaFSE](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20VisualizzaFSE/Schermata%20Visualizza%20FSE%20-%20VisualizzaStoricoVisite%20(Amministrazione).png)

---
Visualizzazione fasciolo sanitario (Paziente)
![VisualizzaFSE](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20VisualizzaFSE/Schermata%20Visualizza%20FSE%20-%20VisualizzaStoricoVisite%20(paziente).png)

---

#### Inserimento dati sulla visita

![Inserisci dettagli visita](https://andrea-augello.github.io/SviluppoSW/media/mock-up/Mock-ups%20InserisciDettagliVisita/Schermata%20Inserisci%20Dettagli%20Visita%20-%20InserisciDettagli.png)
