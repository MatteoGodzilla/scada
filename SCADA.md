# SCADA
L'obiettivo del progetto è realizzare un sistema per gestire impianti di energia rinnovabile con annesso sistema SCADA (Supervisory Control And Data Acquisition) per il monitoraggio dei parametri riportati dagli impianti.

Questo sistema deve poter gestire impianti in tutta Italia. Ogni impianto è localizzato all'interno di una provincia e possiede un codice identificativo valido al suo interno.
Ogni impianto è limitato alla produzione di energia attraverso una singola fonte rinnovabile (eolica, fotovoltaica oppure biogas) e all'interno contiene più macchinari, coerenti con la tipologia di fonte rinnovabile. Gli impianti eolici e fotovoltaici possiedono una stazione meteo locale all'impianto

Il sistema deve comprendere tre tipologie di utenti:
- Tecnico: si occupa di attuare interventi su impianti, all'interno della provincia di riferimento
- Addetto SCADA: si occupa del monitoraggio di tutti gli impianti di una regione, con conseguente redazione della reportistica sulla produzione complessiva
- Responsabile Generale: si occupa della gestione della sua regione di riferimento, effettuando le richieste di intervento agli impianti e/o richieste di generazione della reportistica

Ogni utente si autentica al sistema attraverso nome utente e password (si assume che l'utente sia già registrato).

Ci sono tre tipologie di interventi che si possono attuare all'interno di un impianto:
- Intervento di controllo preventivo di un macchinario
- Intervento di sostituzione parti di un macchinario
- Dismissione di un impianto

Per ogni macchinario si deve memorizzare:
- Modello del macchinario e azienda produttrice
- Status del macchinario (attivo, inattivo, in manutenzione)
- Durata della garanzia, in anni di operazione, data dal contratto del macchinario (per semplicità il contratto in sè non sarà memorizzato nel database)
- Condizioni di operatività del macchinario, che variano in base a tipologia e condizioni metereologiche nei pressi dell'impianto
- Elenco di macro componenti del macchinario, che possono essere richiesti e sostituiti dai tecnici. Questi contengono l'azienda produttrice e un codice che identifica la tipologia del componente, non un codice serializzato per il singolo componente.

I tecnici possono esegure le seguenti operazioni nel sistema:
- Visualizzazione degli interventi richiesti e non già assegnati ad altri, con la possibilità di prendersene carico
- Visualizzazione degli interventi di cui si è già incaricati
- Una volta in sito, possibilità di segnalare agli addetti SCADA che tecnici sono presenti all'interno dell'impianto
- Possibilità di poter fermare/riavviare un macchinario specifico, per effettuare l'intervento in sicurezza
- Segnalazione agli addetti SCADA di fine intervento
- Invio della conferma al responsabile di regione dopo aver concluso un intervento, con la possibilità di scrivere note aggiuntive

Gli addetti SCADA possono eseguire le seguenti operazioni nel sistema:
- Visualizzazione dei parametri di tutti i macchinari presenti nella regione
- Visualizzazione della eventuale presenza di tecnici in un impianto
- Visualizzazione delle condizioni metereologiche
- Possibilità di fermare/riavviare un macchinario
- Generazione della reportistica sotto richiesta del responsabile regionale

I responsabili di regione possono eseguire le seguenti operazioni nel sistema:
- Visualizzazione dello storico degli interventi conclusi in passato
- Visualizzazione delle conferme di fine intervento inviate dai tecnici
- Possibilità di richiedere interventi ai tecnici
- Visualizzazione di tutti gli interventi futuri, con la possibilità di filtrare per quelli non ancora assegnati a un tecnico
- Possibilità di aggiungere macchinari e/o impianti nella sua regione di competenza
